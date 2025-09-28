package in.productDelivery.evergreen.controller;

import in.productDelivery.evergreen.io.CartRequest;
import in.productDelivery.evergreen.io.CartResponse;
import in.productDelivery.evergreen.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class    CartController {


    private final CartService cartService;
    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request) {
        String productId = request.getProductId();
        if (productId == null || productId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Id not found");
        }
        return cartService.addToCart(request);
    }

    @GetMapping
    public CartResponse getCart() {
        return cartService.getCart();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart() {
        cartService.clearCart();
    }

    @PostMapping("/remove")
    public CartResponse removeFromCart(@RequestBody CartRequest request) {
        String productId = request.getProductId();
        if (productId == null || productId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Id not found");
        }
        return cartService.removeFromCart(request);
    }
}
