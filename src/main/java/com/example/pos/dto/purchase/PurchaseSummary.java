package com.example.pos.dto.purchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseSummary {
    public Long id;
    public Long userId;
    public BigDecimal total;
    public LocalDateTime date;
    public List<PurchaseItemSummary> items;

    public PurchaseSummary(Long id, Long userId, BigDecimal total, LocalDateTime date, List<PurchaseItemSummary> items) {
        this.id = id;
        this.userId = userId;
        this.total = total;
        this.date = date;
        this.items = items;
    }
}
