package myboot.reactive.mongo.controller;

import lombok.RequiredArgsConstructor;
import myboot.reactive.mongo.dto.ProductDTO;
import myboot.reactive.mongo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    @GetMapping
    public Flux<ProductDTO> getProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDTO> getProducts2(
            @PathVariable String id
    ) {
        return service.getProducts(id);
    }

    @GetMapping("/re/{id}")
    public Mono<ResponseEntity<ProductDTO>> getProductsRE(
            @PathVariable String id
    ) {
        return service.getProductRE(id);
    }

    @PostMapping
    public Mono<ProductDTO> saveProduct(
            @RequestBody Mono<ProductDTO> _productDTOMono
    ) {
        return service.saveProduct(_productDTOMono);
    }

    @PostMapping("/re")
    public Mono<ResponseEntity<ProductDTO>> saveProductRE(
            @RequestBody Mono<ProductDTO> _productDTOMono
    ) {
        return service.saveProductRE(_productDTOMono);
    }

    @PutMapping("/{id}")
    public Mono<ProductDTO> updateProduct(
            @PathVariable String id
            , @RequestBody Mono<ProductDTO> _productDTOMono
    ) {
        return service.updateProduct(id, _productDTOMono);
    }

    @PutMapping("/re/{id}")
    public Mono<ProductDTO> updateProductRE(
            @PathVariable String id
            , @RequestBody Mono<ProductDTO> _productDTOMono
    ) {
        return service.updateProduct(id, _productDTOMono);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(
            @PathVariable String id
    ) {
        return service.deleteProduct(id);
    }

    @GetMapping("/product-rage")
    public Flux<ProductDTO> getProductInRage(
            @RequestParam double _min
            , @RequestParam double _max
    ) {
        return service.getProductRage(_min, _max);
    }


}
