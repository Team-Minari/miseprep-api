package wisoft.io.miseprep_api.cart.dto.response;

public record CartItemBroadcast(
        String action,
        CartItemResponse item
) {
    public static CartItemBroadcast added(CartItemResponse item) {
        return new CartItemBroadcast("ADDED", item);
    }

    public static CartItemBroadcast updated(CartItemResponse item) {
        return new CartItemBroadcast("UPDATED", item);
    }

    public static CartItemBroadcast deleted(CartItemResponse item) {
        return new CartItemBroadcast("DELETED", item);
    }

    public static CartItemBroadcast checked(CartItemResponse item) {
        return new CartItemBroadcast("CHECKED", item);
    }

    public static CartItemBroadcast unchecked(CartItemResponse item) {
        return new CartItemBroadcast("UNCHECKED", item);
    }
}
