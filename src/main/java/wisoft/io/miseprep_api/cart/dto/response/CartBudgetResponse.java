package wisoft.io.miseprep_api.cart.dto.response;

public record CartBudgetResponse(
        Integer budget,
        int totalAmount,
        int remainingBudget
) {
    public static CartBudgetResponse of(Integer budget, int totalAmount) {
        int remaining = budget != null ? budget - totalAmount : 0;
        return new CartBudgetResponse(budget, totalAmount, remaining);
    }
}
