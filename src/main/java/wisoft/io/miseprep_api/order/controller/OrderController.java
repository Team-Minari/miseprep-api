package wisoft.io.miseprep_api.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wisoft.io.miseprep_api.global.dto.ApiResponse;
import wisoft.io.miseprep_api.order.dto.request.CreateOrderRequest;
import wisoft.io.miseprep_api.order.dto.request.PaymentConfirmRequest;
import wisoft.io.miseprep_api.order.dto.response.OrderResponse;
import wisoft.io.miseprep_api.order.service.OrderService;

import java.util.List;

@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid CreateOrderRequest request) {
        OrderResponse data = orderService.createOrder(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(data, "주문이 생성되었습니다."));
    }

    @Operation(summary = "결제 승인")
    @PostMapping("/payment/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmPayment(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid PaymentConfirmRequest request) {
        OrderResponse data = orderService.confirmPayment(memberId, request);
        return ResponseEntity.ok(ApiResponse.of(data, "결제가 완료되었습니다."));
    }

    @Operation(summary = "내 주문 목록 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal Long memberId) {
        List<OrderResponse> data = orderService.getMyOrders(memberId);
        return ResponseEntity.ok(ApiResponse.of(data, "주문 목록을 조회했습니다."));
    }

    @Operation(summary = "주문 상세 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long orderId) {
        OrderResponse data = orderService.getOrder(memberId, orderId);
        return ResponseEntity.ok(ApiResponse.of(data, "주문을 조회했습니다."));
    }
}
