package com.api.produtosservicos.controllers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.produtosservicos.dtos.OrderDto;
import com.api.produtosservicos.models.Order;
import com.api.produtosservicos.models.OrderItems;
import com.api.produtosservicos.services.OrderItemsService;
import com.api.produtosservicos.services.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemsService orderItemsService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid OrderDto orderDto) {
        if (orderDto.getNumber() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - ESCOLHA UM VALOR VÁLIDO");
        } else if (orderDto.getDate().compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - DATA INVÁLIDA");
        } else if (orderDto.getPercentualDiscount() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - DESCONTO INVÁLIDO");
        } else if (orderDto.getTotalValue() != null && orderDto.getTotalValue() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - TOTAL INVÁLIDO");
        }

        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> read(@PathVariable(value = "id") UUID id) {
        Optional<Order> orderOptional = orderService.findById(id);

        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OS NÃO LOCALIZADA");
        }

        return ResponseEntity.status(HttpStatus.OK).body(orderOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id,
                                         @RequestBody @Valid OrderDto orderDto) {
        if (orderDto.getNumber() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - ESCOLHA UM VALOR VÁLIDO");
        } else if (orderDto.getDate().compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - DATA INVÁLIDA");
        } else if (orderDto.getPercentualDiscount() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - DESCONTO INVÁLIDO");
        } else if (orderDto.getTotalValue() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - TOTAL INVÁLIDO");
        }

        Optional<Order> orderOptional = orderService.findById(id);

        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OS NÃO LOCALIZADA");
        }

        Order Order = new Order();
        BeanUtils.copyProperties(orderDto, Order);
        Order.setId(orderOptional.get().getId());

        return ResponseEntity.status(HttpStatus.OK).body(orderService.save(Order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {
        Optional<Order> orderOptional = orderService.findById(id);

        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OS NÃO LOCALIZADA");
        }

        orderService.delete(orderOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("OS DELETADA");
    }

    @GetMapping
    public ResponseEntity<Object> list() {
        List<Order> orders = orderService.findAll();

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body
                    ("SEM OS REGISTRADAS");
        }

        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @PutMapping("/{orderId}/close")
    public ResponseEntity<Object> applyDiscount(@PathVariable(value = "orderId") UUID id,
                                                @RequestBody OrderDto orderDto) {

        Optional<Order> orderOptional = orderService.findById(id);

        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OS NÃO LOCALIZADA");
        } else if (!orderDto.getOrder().equals(orderOptional.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("OS NÃO LOCALIZADA");
        } else if (orderDto.getPercentualDiscount() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - DESCONTO INVÁLIDO");
        } else if (orderOptional.get().getOrderItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("ERRO - OS VAZIA");
        }

        List<OrderItems> orderItems = orderItemsService.findAllByOrderId(id);
        Double totalValue = 0.0d;

        for (OrderItems orderItem : orderItems) {
            if (orderItem.getItem().getType() == 'P') {
                Double discountValue = orderItem.getTotalValue() * (orderDto.getPercentualDiscount() / 100);
                totalValue += orderItem.getTotalValue() - discountValue;
            } else {
                totalValue += orderItem.getTotalValue();
            }
        }

        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);
        order.setId(orderOptional.get().getId());
        order.setNumber(orderOptional.get().getNumber());
        order.setDate(orderOptional.get().getDate());
        order.setTotalValue(totalValue);
        order.setOrderItems(orderOptional.get().getOrderItems());

        return ResponseEntity.status(HttpStatus.OK).body(orderService.save(order));
    }

}
