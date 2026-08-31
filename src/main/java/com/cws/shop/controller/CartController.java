package com.cws.shop.controller;

import com.cws.shop.dto.request.CartItemRequest;
import com.cws.shop.dto.request.MergeCartRequest;
import com.cws.shop.dto.request.UpdateCartQuantityRequest;
import com.cws.shop.dto.response.ApiResponse;
import com.cws.shop.dto.response.CartResponse;
import com.cws.shop.exception.UnauthorizedException;
import com.cws.shop.model.User;
import com.cws.shop.service.CartService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	private Long getAuthenticatedUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User) {
			return ((User) auth.getPrincipal()).getId();
		}
		throw new UnauthorizedException("Authentication required");
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody CartItemRequest request) {
		return ResponseEntity.ok(cartService.addToCart(request, getAuthenticatedUserId()));
	}

	@GetMapping("/getcart")
	public ResponseEntity<ApiResponse<CartResponse>> getCart() {
		return ResponseEntity.ok(cartService.getCart(getAuthenticatedUserId()));
	}

	@DeleteMapping("/remove/{cartItemId}")
	public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(@PathVariable Long cartItemId) {
		return ResponseEntity.ok(cartService.removeFromCart(cartItemId, getAuthenticatedUserId()));
	}

	@PostMapping("/merge")
	public ResponseEntity<ApiResponse<CartResponse>> mergeGuestCart(@RequestBody MergeCartRequest request) {
		return ResponseEntity.ok(cartService.mergeGuestCart(request.getProductIds(), getAuthenticatedUserId()));
	}

	@PutMapping("/update-quantity/{cartItemId}")
	public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(@Valid @PathVariable Long cartItemId,
			@RequestBody UpdateCartQuantityRequest request) {

		return ResponseEntity
				.ok(cartService.updateQuantity(cartItemId, request.getQuantity(), getAuthenticatedUserId()));
	}
}
