package wisoft.io.miseprep_api.cart.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartEventPayload(
        @JsonProperty("event_type")
        CartEventType eventType,
        Object data
) {
}
