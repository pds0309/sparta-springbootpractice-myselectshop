package com.sparta2.springcore.controller;



import com.sparta2.springcore.model.Product;
import com.sparta2.springcore.dto.ProductMypriceRequestDto;
import com.sparta2.springcore.dto.ProductRequestDto;
import com.sparta2.springcore.security.UserDetailsImpl;
import com.sparta2.springcore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class ProductController {
    // 멤버 변수 선언
    private final ProductService productService;

    // 생성자: ProductController() 가 생성될 때 호출됨
    @Autowired
    public ProductController(ProductService productService) {
        // 멤버 변수 생성
        this.productService = productService;
    }

    // 로그인한 회원이 등록한 상품들 조회
    @GetMapping("/api/products")
    public Page<Product> getProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        page = page - 1;
        return productService.getProducts(userId, page , size, sortBy, isAsc);
    }

    // 신규 상품 등록
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인 되어 있는 ID
        Long userId = userDetails.getUser().getId();

        Product product = productService.createProduct(requestDto, userId);
        // 응답 보내기
        return product;
    }

    // 설정 가격 변경
    @PutMapping("/api/products/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        Product product = productService.updateProduct(id, requestDto);
        // 응답 보내기
        return product.getId();
    }

    // (관리자용) 등록된 모든 상품 목록 조회
    @Secured("ROLE_ADMIN")
    @GetMapping("/api/admin/products")
    public Page<Product> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ) {
        return productService.getAllProducts(page , size, sortBy, isAsc);
    }
}


////import lombok.RequiredArgsConstructor;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.web.bind.annotation.*;
////
////import java.sql.SQLException;
////import java.util.List;
////
////@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
////@RestController // JSON으로 데이터를 주고받음을 선언합니다.
////public class ProductController {
////    // 멤버 변수 선언
////    private final ProductService productService;
////
////    // 생성자: ProductController() 가 생성될 때 호출됨
//////    public ProductController() {
//////        // 멤버 변수 생성
//////        productService = new ProductService();
//////    }
////
////    // 등록된 전체 상품 목록 조회
////    @GetMapping("/api/products")
////    public List<Product> getProducts() throws SQLException {
////        List<Product> products = productService.getProducts();
////        // 응답 보내기
////        return products;
////    }
////
////    // 신규 상품 등록
////    @PostMapping("/api/products")
////    public Product createProduct(@RequestBody ProductRequestDto requestDto) throws SQLException {
////        Product product = productService.createProduct(requestDto);
////        // 응답 보내기
////        return product;
////    }
////
////    // 설정 가격 변경
////    @PutMapping("/api/products/{id}")
////    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) throws SQLException {
////        Product product = productService.updateProduct(id, requestDto);
////        return product.getId();
////    }
////}
//
//
//
//import com.sparta2.springcore.dto.ProductMypriceRequestDto;
//import com.sparta2.springcore.dto.ProductRequestDto;
//import com.sparta2.springcore.security.UserDetailsImpl;
//import com.sparta2.springcore.service.ProductService;
//import com.sparta2.springcore.model.Product;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//import java.sql.SQLException;
//import java.util.List;
//
//@RestController // JSON으로 데이터를 주고받음을 선언합니다.
//public class ProductController {
//    // 멤버 변수 선언
//    private final ProductService productService;
//
//    // 생성자: ProductController() 가 생성될 때 호출됨
//
//    public ProductController(ProductService productService) {
//        // 멤버 변수 생성
//        this.productService = productService;
//    }
//
//    // 등록된 전체 상품 목록 조회
////    @GetMapping("/api/products")
////    public List<Product> getProducts() throws SQLException {
////        List<Product> products = productService.getProducts();
////        // 응답 보내기
////        return products;
////    }
//    // 로그인한 회원이 등록한 상품들 조회
//    @GetMapping("/api/products")
//    public List<Product> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        Long userId = userDetails.getUser().getId();
//        return productService.getProducts(userId);
//    }
//
//
////    // 신규 상품 등록
////    @PostMapping("/api/products")
////    public Product createProduct(@RequestBody ProductRequestDto requestDto) throws SQLException {
////        Product product = productService.createProduct(requestDto);
////        // 응답 보내기
////        return product;
////    }
//
//    @PostMapping("/api/products")
//    public Product createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        // 로그인 되어 있는 ID
//        Long userId = userDetails.getUser().getId();
//
//        Product product = productService.createProduct(requestDto, userId);
//        // 응답 보내기
//        return product;
//    }
//    // 설정 가격 변경
//    @PutMapping("/api/products/{id}")
//    public int updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) throws SQLException {
//        Product product = productService.updateProduct(id, requestDto);
//        return product.getMyprice();
//    }
//
//
//    // (관리자용) 등록된 모든 상품 목록 조회
//    @Secured("ROLE_ADMIN")
//    @GetMapping("/api/admin/products")
//    // (관리자용) 등록된 모든 상품 목록 조회
//    public List<Product> getAllProducts() {
//        return productService.getAllProducts();
//    }
//
//
//
//
//}
//
///*
//		// 신규 상품 등록
//    @PostMapping("/api/products")
//    public Product createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        // 로그인 되어 있는 ID
//        Long userId = userDetails.getUser().getId();
//
//        Product product = productService.createProduct(requestDto, userId);
//        // 응답 보내기
//        return product;
//    }
//
// */


