package com.sparta2.springcore.service;
//
//import java.sql.SQLException;
//import java.util.List;
//
//public class ProductService {
//    // 멤버 변수 선언
//    private final ProductRepository productRepository;
//
//    // 생성자: ProductService() 가 생성될 때 호출됨
//    public ProductService() {
//        // 멤버 변수 생성
//        this.productRepository = new ProductRepository();
//    }
//
//    public List<Product> getProducts() throws SQLException {
//        // 멤버 변수 사용
//        return productRepository.getProducts();
//    }
//
//    public Product createProduct(ProductRequestDto requestDto) throws SQLException {
//        // 요청받은 DTO 로 DB에 저장할 객체 만들기
//        Product product = new Product(requestDto);
//        productRepository.createProduct(product);
//        return product;
//    }
//
//    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) throws SQLException {
//        Product product = productRepository.getProduct(id);
//        if (product == null) {
//            throw new NullPointerException("해당 아이디가 존재하지 않습니다.");
//        }
//        int myPrice = requestDto.getMyprice();
//        productRepository.updateProductMyPrice(id, myPrice);
//        return product;
//    }
//}


import com.sparta2.springcore.dto.ProductRequestDto;
import com.sparta2.springcore.model.Product;
import com.sparta2.springcore.dto.ProductMypriceRequestDto;
import com.sparta2.springcore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    // 멤버 변수 선언
    private final ProductRepository productRepository;
    // 생성자: ProductService() 가 생성될 때 호출됨
    public ProductService(ProductRepository productRepository) {
        // 멤버 변수 생성
        this.productRepository = productRepository;
    }

//    public List<Product> getProducts() throws SQLException {
//        // 멤버 변수 사용
//        return productRepository.findAll();
//    }

// 회원 ID 로 등록된 모든 상품 조회
        public List<Product> getProducts(Long userId) {
            return productRepository.findAllByUserId(userId);
        }

//    public Product createProduct(ProductRequestDto requestDto) throws SQLException {
//        // 요청받은 DTO 로 DB에 저장할 객체 만들기
//
//        Product product = new Product(requestDto);
//        productRepository.save(product);
//        return product;
//    }


    @Transactional // 메소드 동작이 SQL 쿼리문임을 선언합니다.
    public Product createProduct(ProductRequestDto requestDto, Long userId ) {
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Product product = new Product(requestDto, userId);
        productRepository.save(product);
        return product;
    }

    //변경사항 커밋
    @Transactional
    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto){
            Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("NO ID"));
            int myPrice = requestDto.getMyprice();
            product.setMyprice(myPrice);
            return product;
    }



    // 모든 상품 조회 (관리자용)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}