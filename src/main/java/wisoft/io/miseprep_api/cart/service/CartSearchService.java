package wisoft.io.miseprep_api.cart.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import wisoft.io.miseprep_api.cart.dto.response.CartResponse;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.cart.repository.CartLikeRepository;
import wisoft.io.miseprep_api.cart.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartSearchService {

    private final CartRepository cartRepository;
    private final CartLikeRepository cartLikeRepository;
    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    @Value("${openai.api-key}")
    private String apiKey;

    public List<CartResponse> search(String query) {
        List<Cart> carts = cartRepository.findAllByIsPublicTrue().stream()
                .filter(cart -> cart.getPurpose() != null)
                .toList();

        if (carts.isEmpty()) return List.of();

        Map<Long, Cart> cartMap = carts.stream()
                .collect(Collectors.toMap(Cart::getId, c -> c));

        List<Long> relevantIds = callOpenAi(carts, query);

        return relevantIds.stream()
                .filter(cartMap::containsKey)
                .map(id -> {
                    Cart c = cartMap.get(id);
                    return CartResponse.from(c, cartLikeRepository.countByCartId(c.getId()), false);
                })
                .toList();
    }

    private List<Long> callOpenAi(List<Cart> carts, String query) {
        String cartsInfo = carts.stream()
                .map(c -> String.format("{\"id\":%d,\"name\":\"%s\",\"purpose\":\"%s\",\"category\":\"%s\"}",
                        c.getId(), c.getName(), c.getPurpose(), c.getCategory()))
                .collect(Collectors.joining(",", "[", "]"));

        String prompt = String.format("""
                다음 공개 장바구니 목록에서 사용자 쿼리와 의미적으로 관련있는 장바구니의 id만 골라서 JSON으로 반환해줘.
                관련없으면 빈 배열로.

                장바구니 목록: %s

                사용자 쿼리: %s

                응답 형식: {"cart_ids": [1, 2, 3]}
                """, cartsInfo, query);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "response_format", Map.of("type", "json_object")
        );

        try {
            String response = restClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            JsonNode result = objectMapper.readTree(content);

            List<Long> ids = new ArrayList<>();
            result.path("cart_ids").forEach(node -> ids.add(node.asLong()));
            return ids;
        } catch (Exception e) {
            return List.of();
        }
    }
}
