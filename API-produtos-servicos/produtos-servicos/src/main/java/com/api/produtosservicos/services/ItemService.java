package com.api.produtosservicos.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.produtosservicos.models.Item;
import com.api.produtosservicos.repositories.ItemRepository;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Optional<Item> findById(UUID id) {
        return itemRepository.findById(id);
    }

    @Transactional
    public void delete(Item item) {
        itemRepository.delete(item);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}