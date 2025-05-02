package com.example.pos.controller;

import com.example.pos.common.NotFoundException;
import com.example.pos.dto.purchase.PurchaseRequest;
import com.example.pos.dto.purchase.PurchaseResponse;
import com.example.pos.dto.purchase.PurchaseSummary;
import com.example.pos.model.User;
import com.example.pos.service.PurchaseService;
import com.example.pos.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService service;
    private final UserRepository userRepo;

    public PurchaseController(PurchaseService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseSummary>> listPurchases() {
        return ResponseEntity.ok(service.getAllPurchases());
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> purchase(@RequestBody @Valid PurchaseRequest purchaseRequest, HttpServletRequest request) {
        Long tokenUserId = (Long) request.getAttribute("userId");
        User user = userRepo.findById(tokenUserId).orElseThrow(() -> new NotFoundException("User not found"));
        return ResponseEntity.ok(service.createPurchase(user, purchaseRequest));
    }

    @GetMapping("/profit")
    public Map<String, Object> getProfit(@RequestParam String month) {
        BigDecimal profit = service.calculateProfitForMonth(month);
        Map<String, Object> response = new HashMap<>();
        response.put("month", month);
        response.put("profit", profit);
        return response;
    }
}
