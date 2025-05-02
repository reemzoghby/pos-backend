package com.example.pos.dto.purchase;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class PurchaseRequest {
    @NotNull(message = "Items list cannot be null")
    @Size(min = 1, message = "At least one item is required")
    public List<@Valid PurchaseItemRequest> items;

    public List<PurchaseItemRequest> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemRequest> items) {
        this.items = items;
    }
}