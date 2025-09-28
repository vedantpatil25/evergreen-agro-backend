package in.productDelivery.evergreen.service;

import in.productDelivery.evergreen.io.UserRequest;
import in.productDelivery.evergreen.io.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    String findByUserId();
}
