package in.productDelivery.evergreen.io;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequest {
    private List<OrderItem> orderedItems;
    private String userAddress;
    private double amount;
    private String phone;
    private String phoneNumber;
    private String orderStatus;

}
