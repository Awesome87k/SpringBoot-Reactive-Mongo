package myboot.reactive.mongo.service;

import lombok.RequiredArgsConstructor;
import myboot.reactive.mongo.dto.ProductDTO;
import myboot.reactive.mongo.repository.ProductRepository;
import myboot.reactive.mongo.utils.AppUtils;
import org.springframework.data.domain.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    //Flux<ProductDto>
    public Flux<ProductDTO> getProducts() {
        return repository.findAll()   //Flux<Product>
//                .map(product -> AppUtils.entityToDto(product))
                .map(AppUtils::entityToDto);    //위를 줄여서 사용
    }

    //Mono<ProductDto>, Mono<ResponseEntity<ProductDto>>
    public Mono<ProductDTO> getProducts(String _id) {
        return repository.findById(_id)
                .map(AppUtils::entityToDto);
    }

    //권장되는 방식, 사용자에게 적절한 응답을 전달할 수 있다.
    public Mono<ResponseEntity<ProductDTO>> getProductRE(String _id) {
        return repository.findById(_id)
//                .map(AppUtils::entityToDto)    //Mono<ProductDTO>
//                .map(productDTO -> new ResponseEntity<>(productDTO, HttpStatus.OK))     //Mono<ReponseEntity<ProductDTO>>
                .map(product -> ResponseEntity.ok(AppUtils.entityToDto(product)))   //위주석을 한번에처리
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //Mono<ProductDto>, Mono<ResponseEntity<ProductDto>>
    public Mono<ProductDTO> saveProduct(Mono<ProductDTO> _productDTOMono) {
        return _productDTOMono.map(AppUtils::dtoToEntity)   // ProductDTO > Product
//                .flatMap(product -> repository.insert(product))
                .flatMap(repository::insert)      //등록시 mono를반환해야하므로 flatmap사용, 위 주석소스 간결화
                .map(AppUtils::entityToDto);    // Product > ProductDTO
    }

    public Mono<ResponseEntity<ProductDTO>> saveProductRE(Mono<ProductDTO> _productDTOMono) {
        return _productDTOMono.map(AppUtils::dtoToEntity)   // ProductDTO > Product
                .flatMap(repository::insert)      //등록시 mono를반환해야하므로 flatmap사용, 위 주석소스 간결화
                .map(insProd -> ResponseEntity.ok(AppUtils.entityToDto(insProd)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());     //406에러
    }

    public Mono<ProductDTO> updateProduct(String _id, Mono<ProductDTO> _productDTOMono) {
        return repository.findById(_id) //Mono<Product>
                .flatMap(product -> _productDTOMono.map(AppUtils::dtoToEntity))     // ProductDTO > Product
                .doOnNext(p -> p.setId(_id))
                .flatMap(repository::save)
                .map(AppUtils::entityToDto);    // Product > ProductDTO
    }

    public Mono<ResponseEntity<ProductDTO>> updateProductRE(String _id, Mono<ProductDTO> _productDTOMono) {
        return repository.findById(_id) //Mono<Product>
//                .flatMap(oldProduct -> _productDTOMono.map(dto -> AppUtils.dtoToEntity(dto)))
                .flatMap(oldProduct -> _productDTOMono.map(AppUtils::dtoToEntity))      //위 주석의 간단구현
                .doOnNext(oldProduct -> oldProduct.setId(_id))
                .flatMap(repository::save)
                .map(updProduct -> ResponseEntity.ok(AppUtils.entityToDto(updProduct)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<Void>> deleteProduct(String _id) {
        return repository.findById(_id)
                .flatMap(existProduct -> repository.delete(existProduct)
                        .then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Flux<ProductDTO> getProductRage(double _min, double _max) {
        return repository.findByPriceBetween(Range.closed(_min, _max))
                .map(AppUtils::entityToDto);
    }
}
