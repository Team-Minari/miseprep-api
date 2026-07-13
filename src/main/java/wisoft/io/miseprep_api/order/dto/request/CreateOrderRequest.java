package wisoft.io.miseprep_api.order.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("cart_item_ids") @NotEmpty List<Long> cartItemIds,
        @JsonProperty("shipping_address") @NotBlank String shippingAddress
) {}
