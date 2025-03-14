package com.jfuente.inventory_service.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    void isInStock() {
        assertTrue(inventoryService.isInStock("000001"));
        assertFalse(inventoryService.isInStock("000007"));
    }

}