package com.example.pos.dto.purchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseResponse {
    public Long receiptId;
    public BigDecimal total;
    public LocalDateTime date;

    public PurchaseResponse(Long receiptId, BigDecimal total, LocalDateTime date) {
        this.receiptId = receiptId;
        this.total = total;
        this.date = date;
    }
}