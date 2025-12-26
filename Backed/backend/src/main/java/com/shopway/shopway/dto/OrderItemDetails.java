package com.shopway.shopway.dto;

import com.shopway.shopway.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDetails {

    private UUID id;

    private Product product;

    private UUID productVariantId;

    private Integer quantity;

    private Double itemPrice;
}
