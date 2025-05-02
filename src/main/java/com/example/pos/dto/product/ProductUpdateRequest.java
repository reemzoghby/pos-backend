package com.example.pos.dto.product;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ProductUpdateRequest {

    public String name;

    public String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Cost price must be positive")
    public BigDecimal costPrice;

    @DecimalMin(value = "0.0", inclusive = false, message = "Selling price must be positive")
    public BigDecimal sellingPrice;

    @Min(value = 0, message = "Quantity cannot be negative")
    public Integer quantity;
}
