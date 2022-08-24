package com.api.produtosservicos.repositories;


import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.produtosservicos.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

}