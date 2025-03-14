package com.jfuente.products_service.services;

import com.jfuente.products_service.model.dtos.ProductRequest;
import com.jfuente.products_service.model.dtos.ProductResponse;
import com.jfuente.products_service.model.entities.Product;
import com.jfuente.products_service.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List ;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(ProductRequest productRequest) {
        //obtengo un dto (ProductRequest) y tendré q generar un Product
        var product = Product.builder()
                .sku(productRequest.getSku())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .status(productRequest.getStatus())
                .build();

        productRepository.save(product);

        log.info("Product added: {}", product);
    }


    public List<ProductResponse> getAllProduct() {
        var products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus())
                .build();
   }


}
