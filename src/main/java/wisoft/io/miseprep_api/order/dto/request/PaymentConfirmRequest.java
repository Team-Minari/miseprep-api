package wisoft.io.miseprep_api.order.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentConfirmRequest(
        @JsonProperty("payment_key") @NotBlank String paymentKey,
        @JsonProperty("toss_order_id") @NotBlank String tossOrderId,
        @NotNull Integer amount
) {
    public Long orderId() {
        return Long.parseLong(tossOrderId.replace("ORDER-", ""));
    }
}
