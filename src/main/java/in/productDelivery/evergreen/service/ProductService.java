package in.productDelivery.evergreen.service;

import in.productDelivery.evergreen.io.ProductRequest;
import in.productDelivery.evergreen.io.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    String uploadFileToS3Bucket(MultipartFile file);

    ProductResponse addProduct(ProductRequest request, MultipartFile file);

    List<ProductResponse> readProducts();

    ProductResponse readProduct(String id);

    boolean deleteFile(String filename);

    void deleteProduct(String id);
}
