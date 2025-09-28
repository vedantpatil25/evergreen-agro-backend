package in.productDelivery.evergreen.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.productDelivery.evergreen.io.ProductRequest;
import in.productDelivery.evergreen.io.ProductResponse;
import in.productDelivery.evergreen.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse addProduct(@RequestPart("product") String ProductString, @RequestPart("file") MultipartFile file) {

        // To convert the JSON request string into Java Object
        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequest request = null;
        try {
            request = objectMapper.readValue(ProductString, ProductRequest.class);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }
        return productService.addProduct(request, file);
    }

    @GetMapping
    public List<ProductResponse> readProducts() {
        return productService.readProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse readProduct(@PathVariable String id) {
        return productService.readProduct(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
