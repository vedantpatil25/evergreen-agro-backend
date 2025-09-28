package in.productDelivery.evergreen.service;

import in.productDelivery.evergreen.entity.ProductEntity;
import in.productDelivery.evergreen.io.ProductRequest;
import in.productDelivery.evergreen.io.ProductResponse;
import in.productDelivery.evergreen.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private S3Client s3Client;
    @Autowired
    private ProductRepository productRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Override
    public String uploadFileToS3Bucket(MultipartFile file) {
        String filenameExtension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String key = UUID.randomUUID() + "." + filenameExtension;
        try {

            //Creating the PutObjectRequest to provide to s3Client
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            // Calling the putObject method to save the file in s3 bucket
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if (response.sdkHttpResponse().isSuccessful()) {

                //creating the public URL of the uploaded file
                return "https://" + bucketName + ".s3.amazonaws.com/" + key;

            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
            }
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the file");
        }
    }

    @Override
    public ProductResponse addProduct(ProductRequest request, MultipartFile file) {
        ProductEntity newProductEntity = convertToEntity(request);
        String imageUrl = uploadFileToS3Bucket(file);
        newProductEntity.setImageUrl(imageUrl);
        newProductEntity = productRepository.save(newProductEntity);
        return convertToResponse(newProductEntity);
    }

    @Override
    public List<ProductResponse> readProducts() {
        List<ProductEntity> databaseEntries = productRepository.findAll();
        return databaseEntries.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public ProductResponse readProduct(String id) {
        ProductEntity existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found for the id:" + id));
        return convertToResponse(existingProduct);
    }

    @Override
    public boolean deleteFile(String filename) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteProduct(String id) {
        ProductResponse response = readProduct(id);
        String imageUrl = response.getImageUrl();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        boolean isFileDelete = deleteFile(filename);
        if (isFileDelete) {
            productRepository.deleteById(response.getId());
        }
    }

    private ProductEntity convertToEntity(ProductRequest request) {
        return ProductEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();

    }

    private ProductResponse convertToResponse(ProductEntity entity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
