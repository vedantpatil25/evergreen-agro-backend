package in.productDelivery.evergreen.service;

import in.productDelivery.evergreen.io.CartRequest;
import in.productDelivery.evergreen.io.CartResponse;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    CartResponse getCart();

    void clearCart();

    CartResponse removeFromCart(CartRequest cartRequest);
}
