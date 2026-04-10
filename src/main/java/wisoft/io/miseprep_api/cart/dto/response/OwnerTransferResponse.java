package wisoft.io.miseprep_api.cart.dto.response;

public record OwnerTransferResponse(
        Long prevOwnerId,
        Long newOwnerId,
        String newOwnerName
) {
}
