package wisoft.io.miseprep_api.cart.event;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class CartEventListener {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCartEvent(CartEvent event) {
        simpMessagingTemplate.convertAndSend(
                "/topic/carts/" + event.cartId(),
                new CartEventPayload(event.eventType(), event.data()));

    }
}
