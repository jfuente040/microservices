package com.jfuente.inventory_service.controllers;

import com.jfuente.inventory_service.dtos.BaseResponse;
import com.jfuente.inventory_service.dtos.OrderItemRequest;
import com.jfuente.inventory_service.model.entities.Inventory;
import com.jfuente.inventory_service.repositories.InventoryRepository;
import com.jfuente.inventory_service.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;

    @GetMapping("/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable String sku) {
        return inventoryService.isInStock(sku);
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse areInStock(@RequestBody List<OrderItemRequest> orderItems) {
        return inventoryService.areInStock(orderItems);
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> test() {
        return inventoryRepository.findAll();
    }


}
