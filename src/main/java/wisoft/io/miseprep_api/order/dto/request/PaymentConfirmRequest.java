package wisoft.io.miseprep_api.order.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentConfirmRequest(
        @JsonProperty("payment_key") @NotBlank String paymentKey,
        @JsonProperty("order_id") @NotNull Long orderId,
        @NotNull Integer amount
) {}
