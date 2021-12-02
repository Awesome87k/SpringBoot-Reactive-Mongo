package myboot.reactive.mongo.utils;

import myboot.reactive.mongo.dto.ProductDTO;
import myboot.reactive.mongo.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {
    public static ProductDTO entityToDto(Product _product) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(_product, productDTO); //DTO to DTO 데이터를 카피해준다
        return productDTO;
    }

    public static Product dtoToEntity(ProductDTO _productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(_productDto, product); //DTO to DTO 데이터를 카피해준다
        return product;
    }
}
