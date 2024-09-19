package com.codenear.butterfly.product.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    private String productImage;

    @Column(nullable = false)
    private Integer originalPrice;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal saleRate;

    @Column(nullable = false)
    private String category;

    //공동구매 신청현황(인원)
    @Column(nullable = false)
    private Integer purchaseParticipantCount;

    @Column(nullable = false)
    private Integer MaxPurchaseCount;

    //재고수량
    @Column(nullable = false)
    private Integer stockQuantity;
}