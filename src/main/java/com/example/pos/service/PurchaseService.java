package com.example.pos.service;

import com.example.pos.common.NotFoundException;
import com.example.pos.common.QuantityException;
import com.example.pos.dto.purchase.*;
import com.example.pos.model.Purchase;
import com.example.pos.model.PurchaseItem;
import com.example.pos.model.User;
import com.example.pos.model.Product;
import com.example.pos.repository.ProductRepository;
import com.example.pos.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepo;
    private final ProductRepository productRepo;

    public PurchaseService(PurchaseRepository purchaseRepo, ProductRepository productRepo) {
        this.purchaseRepo = purchaseRepo;
        this.productRepo = productRepo;
    }

    public List<PurchaseSummary> getAllPurchases() {
        return purchaseRepo.findAll().stream()
                .map(purchase -> new PurchaseSummary(
                        purchase.getId(),
                        purchase.getUser().getId(),
                        purchase.getTotalPrice(),
                        purchase.getPurchaseDate(),
                        purchase.getItems().stream()
                                .map(item -> new PurchaseItemSummary(
                                        item.getProduct().getId(),
                                        item.getProduct().getName(),
                                        item.getQuantity()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public PurchaseResponse createPurchase(User user, PurchaseRequest request) {
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        List<PurchaseItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseItemRequest itemReq : request.items) {
            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + itemReq.getProductId()));

            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new QuantityException("Insufficient stock for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - itemReq.getQuantity());

            PurchaseItem item = new PurchaseItem();
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPricePerUnit(product.getSellingPrice());
            item.setCostPrice(product.getCostPrice());
            item.setPurchase(purchase);

            items.add(item);

            total = total.add(product.getSellingPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }

        purchase.setTotalPrice(total);
        purchase.setItems(items);
        purchaseRepo.save(purchase); // cascade saves items

        return new PurchaseResponse(purchase.getId(), total, purchase.getPurchaseDate());
    }

    public BigDecimal calculateProfitForMonth(String month) {
        YearMonth targetMonth = YearMonth.parse(month);
        LocalDateTime start = targetMonth.atDay(1).atStartOfDay();
        LocalDateTime end = targetMonth.atEndOfMonth().atTime(LocalTime.MAX);

        List<Purchase> purchases = purchaseRepo.findByPurchaseDateBetween(start, end);

        BigDecimal profit = BigDecimal.ZERO;

        for (Purchase purchase : purchases) {
            for (PurchaseItem item : purchase.getItems()) {
                BigDecimal revenue = item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity()));
                BigDecimal cost = item.getCostPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                profit = profit.add(revenue.subtract(cost));
            }
        }

        return profit;
    }
}
