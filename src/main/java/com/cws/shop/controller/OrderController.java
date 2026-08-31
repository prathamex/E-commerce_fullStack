package com.cws.shop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.model.Order;
import com.cws.shop.service.OrderService;
import com.cws.shop.dto.response.ApiResponse;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 📦 Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // 🔍 Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // 🔎 GLOBAL SEARCH (ADMIN DASHBOARD)
    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(@RequestParam String keyword) {
        return ResponseEntity.ok(orderService.searchOrders(keyword));
    }

    // 💰 revenue
    @GetMapping("/revenue")
    public ResponseEntity<Double> getRevenue(@RequestParam String status) {
        return ResponseEntity.ok(orderService.getRevenueByStatus(status));
    }

    // ➕ create order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    // ✏ update order
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                            @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    // ❌ delete order (DELETE method)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id) {
        log.info("Received DELETE request for order id={}", id);
        try {
            orderService.deleteOrder(id);
            log.info("Order id={} deleted successfully", id);
            ApiResponse<String> resp = new ApiResponse<>(true, "Order deleted successfully", null);
            return ResponseEntity.ok(resp);
        } catch (com.cws.shop.exception.ResourceNotFoundException rnfe) {
            log.warn("Attempted to delete non-existing order id={}", id);
            ApiResponse<String> resp = new ApiResponse<>(false, rnfe.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        } catch (Exception ex) {
            log.error("Failed to delete order id={}: {}", id, ex.getMessage(), ex);
            ApiResponse<String> resp = new ApiResponse<>(false, "Failed to delete order: " + ex.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    // Some frontends (or older forms) may send POST for delete actions — provide a POST
    // endpoint as a safe fallback so the action works even if the client cannot send
    // an HTTP DELETE request.
    @PostMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<String>> deleteOrderViaPost(@PathVariable Long id) {
        log.info("Received POST (delete) request for order id={}", id);
        return deleteOrder(id);
    }

    // Some clients (legacy forms, JS libraries, or misconfigured frontends)
    // may submit a POST to /api/admin/orders/{id} with a form param
    // _method=delete or action=delete. Provide a guarded POST mapping
    // that triggers deletion only when an explicit param is present so
    // accidental POSTs don't remove data.
    @PostMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<String>> postToOrderId(
            @PathVariable Long id,
            @RequestParam(required = false, name = "_method") String methodOverride,
            @RequestParam(required = false, name = "action") String action) {

        log.info("Received POST to /{}/ with _method='{}' action='{}'", id, methodOverride, action);

        boolean wantsDelete = false;
        if (methodOverride != null && methodOverride.equalsIgnoreCase("delete")) {
            wantsDelete = true;
        }
        if (action != null && action.equalsIgnoreCase("delete")) {
            wantsDelete = true;
        }

        if (wantsDelete) {
            return deleteOrder(id);
        }

        ApiResponse<String> resp = new ApiResponse<>(false,
                "POST to /{id} is not supported without action=_method=delete. Use DELETE or POST /{id}/delete.", null);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(resp);
    }
    
    // 📄 Download invoice as PDF
    @GetMapping(value = "/{id}/invoice", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {
        log.info("Received invoice download request for order id={}", id);
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            com.cws.shop.model.Order order = orderService.getOrderById(id);

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(50, 720);
                content.showText("Invoice for Order #" + (order.getOrderId() != null ? order.getOrderId() : order.getId()));
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.newLineAtOffset(50, 690);
                content.showText("Customer: " + (order.getCustomerName() != null ? order.getCustomerName() : "-"));
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.newLineAtOffset(50, 670);
                content.showText("Products: " + (order.getProductPurchased() != null ? order.getProductPurchased() : "-"));
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.newLineAtOffset(50, 650);
                content.showText("Amount: " + (order.getAmount() != null ? String.format("%.2f", order.getAmount()) : "0.00"));
                content.endText();
            }

            document.save(baos);
            byte[] pdfBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice-" + id + ".pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (com.cws.shop.exception.ResourceNotFoundException rnfe) {
            log.warn("Invoice requested for non-existing order id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException ioe) {
            log.error("IOException while generating invoice for order id={}: {}", id, ioe.getMessage(), ioe);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception ex) {
            log.error("Failed to generate invoice for order id={}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<Order>> filterOrders(
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false, defaultValue = "ALL") String paymentMethod,
            @RequestParam(required = false, defaultValue = "ALL") String transactionStatus,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) {

        return ResponseEntity.ok(orderService.filterOrders(
                status, paymentMethod, transactionStatus,
                fromDate, toDate, minAmount, maxAmount));
    }
}