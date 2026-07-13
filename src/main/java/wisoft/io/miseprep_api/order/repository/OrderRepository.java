package wisoft.io.miseprep_api.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.io.miseprep_api.order.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByMemberId(Long memberId);
    Optional<Order> findByIdAndMemberId(Long id, Long memberId);
}
