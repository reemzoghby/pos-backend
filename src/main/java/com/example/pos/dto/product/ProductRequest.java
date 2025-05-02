package com.example.pos.dto.product;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Description is required")
    public String description;

    @NotNull(message = "Cost price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost price must be positive")
    public BigDecimal costPrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Selling price must be positive")
    public BigDecimal sellingPrice;

    @Min(value = 0, message = "Quantity cannot be negative")
    public int quantity;
}
