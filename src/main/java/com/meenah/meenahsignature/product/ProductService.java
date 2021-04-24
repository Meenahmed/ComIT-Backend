package com.meenah.meenahsignature.product;

import com.meenah.meenahsignature.bucket.Bucket;
import com.meenah.meenahsignature.category.Category;
import com.meenah.meenahsignature.category.CategoryRepository;
import com.meenah.meenahsignature.exception.ResourcesNotFoundException;
import com.meenah.meenahsignature.filestore.FileStore;
import com.meenah.meenahsignature.payload.ApiResponse;
import com.meenah.meenahsignature.util.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final Validator validator;
    private final FileStore fileStore;
    private final CategoryRepository categoryRepository;

    // Add new product
    public Product addProduct(Product product, Long categoryId) {
       return categoryRepository.findById(categoryId).map((category)-> {
            product.setCategory(category);
           product.setCreatedAt(Instant.now());
            return productRepository.save(product);
        }).orElseThrow(()-> new ResourcesNotFoundException(""+ product.getId()));
    }

    // Update product
    public Product updateProduct(Product productRequest, Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);

        if(!category.isPresent()) throw new ResourcesNotFoundException("Category not found" + categoryId);

        return productRepository.findById(productRequest.getId()).map((product) ->{
            product.setUpdatedAt(Instant.now());
            product.setBrand(productRequest.getBrand());
            product.setPrice(productRequest.getPrice());
            product.setName(productRequest.getName());
            product.setCountInStock(productRequest.getCountInStock());
            product.setNumReviews(productRequest.getNumReviews());
            product.setCategory(category.get());
            return  productRepository.save(product);
        }).orElseThrow(() -> new ResourcesNotFoundException("product with the id not updated" + productRequest.getId()));

    }

    // Get all products
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // Get one product
    public ResponseEntity<Product> getProduct(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent())
            return new ResponseEntity(new ApiResponse(false, "Product not found."),
                    HttpStatus.NOT_FOUND);

        return ResponseEntity.of(product);

    }

    // Delete a product
    public ResponseEntity<?> deleteProduct(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false, "Product not found."),
                    HttpStatus.NOT_FOUND);
        }

        productRepository.delete(product.get());

        return ResponseEntity.ok().body(
                new ApiResponse(true,
                        "Product successfully deleted."));

    }

    // Upload product image
    public ResponseEntity<?> uploadProductImage(Long productId, MultipartFile file) {
        if(file.getOriginalFilename().isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false, "No file is uploaded"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!validator.validateFile(file.getOriginalFilename())) {
            return new ResponseEntity<>(new ApiResponse(false, "Image must be in jpeg|png|bmp format."),
                    HttpStatus.BAD_REQUEST);
        }


        Product product = productRepository.getOne(productId);
        if(product.getId() == null){
            return new ResponseEntity<>(new ApiResponse(false, "Product not found"),
                    HttpStatus.NOT_FOUND);
        }

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", file.getContentType());
        map.put("Size", String.valueOf(file.getSize()));
        map.put("Name", file.getOriginalFilename());

        String path = String.format("%s/%s", Bucket.PRODUCT_IMAGE.getName(), product.getId());
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, fileName, Optional.of(map), file.getInputStream());
            //Setting imageLink
            product.setImageLink(fileName);
            productRepository.save(product);
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Something went wrong"),
                    HttpStatus.BAD_REQUEST);
        }


        return ResponseEntity.ok().body(new ApiResponse(true, "Product successfully saved to s3."));

    }

    // Download product image
    public byte[] downloadProductImage(Long productId){
        Product product = productRepository.getOne(productId);
        if(product.getId() == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found");
        }

        String path = String.format("%s/%s", Bucket.PRODUCT_IMAGE.getName(), product.getId());
        return product.getImageLink().map(key -> fileStore.download(path,key)).orElse(new byte[0]);

    }

}

