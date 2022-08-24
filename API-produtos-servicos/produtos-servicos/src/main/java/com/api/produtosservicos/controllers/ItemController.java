package com.api.produtosservicos.controllers;

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
import com.api.produtosservicos.dtos.ItemDto;
import com.api.produtosservicos.models.Item;
import com.api.produtosservicos.models.OrderItems;
import com.api.produtosservicos.services.ItemService;
import com.api.produtosservicos.services.OrderItemsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderItemsService orderItemsService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto itemDto) {
        if (itemDto.getType() != 'P' && itemDto.getType() != 'S') {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Error: type must be 'P' for product or 'S' for service.");
        } else if (itemDto.getValue() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Error: value must be positive.");
        }

        Item item = new Item();
        BeanUtils.copyProperties(itemDto, item);

        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.save(item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> read(@PathVariable(value = "id") UUID id) {
        Optional<Item> itemOptional = itemService.findById(id);

        if (!itemOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(itemOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id,
                                         @RequestBody @Valid ItemDto itemDto) {
        if (itemDto.getType() != 'P' && itemDto.getType() != 'S') {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Error: type must be 'P' for product or 'S' for service.");
        } else if (itemDto.getValue() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Error: value must be positive.");
        }

        Optional<Item> itemOptional = itemService.findById(id);

        if (!itemOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }

        Item item = new Item();
        BeanUtils.copyProperties(itemDto, item);
        item.setId(itemOptional.get().getId());
        item.setOrderItems(itemOptional.get().getOrderItems());

        if (!item.getOrderItems().isEmpty()) {
            for (OrderItems orderItem : item.getOrderItems()) {
                Double totalValue = item.getValue() * orderItem.getQuantity();
                orderItem.setTotalValue(totalValue);
                orderItemsService.save(orderItem);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(itemService.save(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {
        Optional<Item> item = itemService.findById(id);

        if (!item.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }

        itemService.delete(item.get());

        return ResponseEntity.status(HttpStatus.OK).body("Item successfully deleted.");
    }

    @GetMapping
    public ResponseEntity<Object> list() {
        List<Item> items = itemService.findAll();

        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "There are no items registered.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

}