package com.example.pos.dto.purchase;

public class PurchaseItemSummary {
    public Long productId;
    public String productName;
    public int quantity;

    public PurchaseItemSummary(Long productId, String productName, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }
}
