package com.meenah.meenahsignature.product;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "productmanagement/api/v1")
@AllArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @GetMapping(path = "/all")
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @GetMapping(path = "{productId}/product")
    public ResponseEntity<Product> getProduct(@PathVariable(value = "productId") Long productId){
        return productService.getProduct(productId);
    }

    @PostMapping("{categoryId}/add")
    public Product addProduct(@RequestBody Product product, @PathVariable(value = "categoryId") Long categoryId) {
        return productService.addProduct(product, categoryId);
    }

    @PutMapping("{categoryId}/update")
    public Product updateProduct(@RequestBody Product product, @PathVariable(value = "categoryId") Long categoryId) {
       return productService.updateProduct(product, categoryId);
    }

    @DeleteMapping("{productId}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "productId") Long productId){
        return productService.deleteProduct(productId);
    }

    @PostMapping(path = "{productId}/image/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProductImage(@PathVariable(value = "productId") Long productId,
                                             @RequestParam(value = "file") MultipartFile file){

        return productService.uploadProductImage(productId,file);
    }

    @GetMapping(path = "{productId}/image/download")
    public byte[] downloadProductImage(@PathVariable(value = "productId") Long productId){

        return productService.downloadProductImage(productId);
    }


}
