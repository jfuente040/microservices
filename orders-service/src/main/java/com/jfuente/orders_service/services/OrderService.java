package com.jfuente.orders_service.services;

import com.jfuente.orders_service.model.dtos.*;
import com.jfuente.orders_service.model.entities.Order;
import com.jfuente.orders_service.model.entities.OrderItems;
import com.jfuente.orders_service.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {

        //Check for inventory stock and return a response with the result of the operation (true or false)
        BaseResponse result = this.webClientBuilder.build()
                .post() //hago una petición post
                .uri("http://localhost:8083/api/inventory/in-stock")
                .bodyValue(orderRequest.getOrderItems())//envio la lista de orderItems
                .retrieve()//obtengo la respuesta
                .bodyToMono(BaseResponse.class) //convierto la respuesta a un objeto BaseResponse
                .block(); //bloqueo la ejecución hasta que se obtenga la respuesta

        if (result != null && !result.hasErrors()) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());

            order.setOrderItems(
                    orderRequest.getOrderItems().stream()
                            .map(orderItemRequest -> mapOrderItemRequestToOrderItem(orderItemRequest, order))
                            .toList());
            this.orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Some products are out of stock");
        }

    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = this.orderRepository.findAll();

        return orders.stream().map(this::mapToOrderResponse).toList();

    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderNumber()
                , order.getOrderItems().stream().map(this::mapToOrderItemRequest).toList());
    }

    private OrderItemsResponse mapToOrderItemRequest(OrderItems orderItems) {
        return new OrderItemsResponse(orderItems.getId(), orderItems.getSku(), orderItems.getPrice(), orderItems.getQuantity());
    }

    private OrderItems mapOrderItemRequestToOrderItem(OrderItemRequest orderItemRequest, Order order) {
        return OrderItems.builder()
                .id(orderItemRequest.getId())
                .sku(orderItemRequest.getSku())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
    }




}