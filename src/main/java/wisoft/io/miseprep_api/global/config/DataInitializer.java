package wisoft.io.miseprep_api.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.cart.entity.CartItem;
import wisoft.io.miseprep_api.cart.entity.CartParticipant;
import wisoft.io.miseprep_api.cart.entity.enums.CartType;
import wisoft.io.miseprep_api.cart.repository.CartItemRepository;
import wisoft.io.miseprep_api.cart.repository.CartParticipantRepository;
import wisoft.io.miseprep_api.cart.repository.CartRepository;
import wisoft.io.miseprep_api.global.enums.Category;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.member.repository.MemberRepository;
import wisoft.io.miseprep_api.product.entity.Product;
import wisoft.io.miseprep_api.product.repository.ProductRepository;

import java.util.List;

@Profile({"local", "prod"})
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartParticipantRepository cartParticipantRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (productRepository.count() > 0) return;

        // Products - LIVING
        Product vacuum = productRepository.save(Product.create(
                "다이슨 무선 청소기 V15", "강력한 흡입력의 무선 청소기",
                649000, "https://picsum.photos/seed/vacuum/300/300", Category.LIVING));
        Product airPurifier = productRepository.save(Product.create(
                "LG 공기청정기 360", "360도 청정 헤파필터 탑재",
                450000, "https://picsum.photos/seed/airpurifier/300/300", Category.LIVING));
        Product humidifier = productRepository.save(Product.create(
                "샤오미 가습기 3L", "초음파 가습, 조용한 작동",
                59000, "https://picsum.photos/seed/humidifier/300/300", Category.LIVING));
        Product shampoo = productRepository.save(Product.create(
                "판테닌 샴푸 400ml", "손상 모발 집중 케어",
                8900, "https://picsum.photos/seed/shampoo/300/300", Category.LIVING));
        Product fabric = productRepository.save(Product.create(
                "피죤 섬유유연제 2L", "은은한 향 대용량",
                11500, "https://picsum.photos/seed/fabric/300/300", Category.LIVING));
        Product tissue = productRepository.save(Product.create(
                "크리넥스 미용티슈 6입", "부드러운 미용티슈 대용량",
                12000, "https://picsum.photos/seed/tissue/300/300", Category.LIVING));

        // Products - INGREDIENTS
        Product rice = productRepository.save(Product.create(
                "햇반 즉석밥 210g", "간편하게 먹을 수 있는 즉석밥",
                1200, "https://picsum.photos/seed/rice/300/300", Category.INGREDIENTS));
        Product ramyeon = productRepository.save(Product.create(
                "신라면 멀티팩 5개입", "얼큰한 신라면 대용량",
                4500, "https://picsum.photos/seed/ramyeon/300/300", Category.INGREDIENTS));
        Product milk = productRepository.save(Product.create(
                "서울우유 1L", "신선한 국내산 흰 우유",
                2600, "https://picsum.photos/seed/milk/300/300", Category.INGREDIENTS));
        Product egg = productRepository.save(Product.create(
                "계란 30구", "국내산 신선란 대용량",
                7500, "https://picsum.photos/seed/egg/300/300", Category.INGREDIENTS));
        Product oil = productRepository.save(Product.create(
                "백설 식용유 1.8L", "튀김 요리에 적합한 식용유",
                8000, "https://picsum.photos/seed/oil/300/300", Category.INGREDIENTS));

        // Products - OFFICE
        Product pen = productRepository.save(Product.create(
                "모나미 볼펜 12자루", "검정 볼펜 사무용 세트",
                5000, "https://picsum.photos/seed/pen/300/300", Category.OFFICE));
        Product paper = productRepository.save(Product.create(
                "A4 복사용지 500매", "80g 복사용지 1묶음",
                6000, "https://picsum.photos/seed/paper/300/300", Category.OFFICE));
        Product postit = productRepository.save(Product.create(
                "3M 포스트잇 100매", "접착력 강한 메모지",
                3500, "https://picsum.photos/seed/postit/300/300", Category.OFFICE));

        // Products - CAMPING
        Product burner = productRepository.save(Product.create(
                "코베아 캠핑 버너", "휴대용 가스 버너 경량",
                35000, "https://picsum.photos/seed/burner/300/300", Category.CAMPING));
        Product mat = productRepository.save(Product.create(
                "캠핑 폴딩 매트 2인용", "휴대용 접이식 캠핑 매트",
                35000, "https://picsum.photos/seed/mat/300/300", Category.CAMPING));
        Product lantern = productRepository.save(Product.create(
                "LED 캠핑 랜턴", "충전식 고밝기 캠핑용 랜턴",
                28000, "https://picsum.photos/seed/lantern/300/300", Category.CAMPING));

        // 멤버가 있으면 장바구니 목데이터도 생성
        Member owner = memberRepository.findById(6L).orElse(null);
        if (owner == null) return;

        Cart livingCart = cartRepository.save(Cart.create(
                owner, "우리집 생활용품 공동구매", Category.LIVING,
                "매달 함께 구매해요", true, 100000, CartType.SHARED));
        Cart campingCart = cartRepository.save(Cart.create(
                owner, "여름 캠핑 준비물", Category.CAMPING,
                "7월 캠핑 준비", true, 200000, CartType.SHARED));
        Cart ingredientsCart = cartRepository.save(Cart.create(
                owner, "주간 식재료 장보기", Category.INGREDIENTS,
                null, false, 50000, CartType.PERSONAL));
        Cart officeCart = cartRepository.save(Cart.create(
                owner, "사무용품 정기 구매", Category.OFFICE,
                "팀 사무용품", true, 30000, CartType.SHARED));

        cartParticipantRepository.save(CartParticipant.create(livingCart, owner));
        cartParticipantRepository.save(CartParticipant.create(campingCart, owner));
        cartParticipantRepository.save(CartParticipant.create(officeCart, owner));

        cartItemRepository.saveAll(List.of(
                CartItem.create(livingCart, vacuum, 1),
                CartItem.create(livingCart, shampoo, 2),
                CartItem.create(livingCart, fabric, 1),
                CartItem.create(livingCart, tissue, 3),
                CartItem.create(campingCart, burner, 1),
                CartItem.create(campingCart, mat, 2),
                CartItem.create(campingCart, lantern, 1),
                CartItem.create(ingredientsCart, rice, 5),
                CartItem.create(ingredientsCart, egg, 1),
                CartItem.create(ingredientsCart, milk, 2),
                CartItem.create(officeCart, pen, 3),
                CartItem.create(officeCart, paper, 2),
                CartItem.create(officeCart, postit, 5)
        ));
    }
}
