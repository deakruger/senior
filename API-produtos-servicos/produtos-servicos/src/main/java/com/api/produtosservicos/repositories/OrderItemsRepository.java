package com.api.produtosservicos.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.produtosservicos.models.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, UUID> {

    Optional<OrderItems> findByOrderId(UUID orderId);
    List<OrderItems> findAllByOrderId(UUID orderId);
    Optional<OrderItems> findByIdAndOrderId(UUID orderId, UUID orderItemsId);

}

