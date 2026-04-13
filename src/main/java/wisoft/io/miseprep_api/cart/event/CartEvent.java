package wisoft.io.miseprep_api.cart.event;

public record CartEvent (
        Long cartId,
        CartEventType eventType,
        Object data
) {
}
