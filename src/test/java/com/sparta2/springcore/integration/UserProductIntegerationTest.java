package com.sparta2.springcore.integration;


import com.sparta2.springcore.dto.ProductMypriceRequestDto;
import com.sparta2.springcore.dto.ProductRequestDto;
import com.sparta2.springcore.dto.SignupRequestDto;
import com.sparta2.springcore.model.Product;
import com.sparta2.springcore.model.User;
import com.sparta2.springcore.model.UserRole;
import com.sparta2.springcore.service.ProductService;
import com.sparta2.springcore.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//테스트시 스프링 기동됨
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//순서대로
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

//수행해야 될 테스트
/*
1. 회원가입 이전에 관심상품 등록을 할 때 실패하는가?
2. 회원가입이 잘 되는가?
3. 회원가입이 되었을 때 해당 회원으로 관심상품 등록이 되는가?
4. 회원으로 관심상품 등록이 되었을 때 그 관심상품을 업데이트 할 수 있는가?
5. 회원이 등록한 업데이트된 관심상품이 잘 조회되는가?
 */
class UserProductIntegerationTest {

    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @Autowired
    PasswordEncoder passwordEncoder;

    User createdUser = null;
    Product createdProduct = null;



    //관심상품 등록 할 때 사용할 정보
    String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
    String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
    String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
    int lPrice = 77000;

    @Test
    @Order(1)
    @DisplayName("1. 회원가입 이전에 관심상품 등록하기 - 에러 발생")
    void test1(){
        // 관심상품
        // given
        ProductRequestDto productRequestDto = new ProductRequestDto(title,imageUrl,linkUrl,lPrice);

        // 관심상품 등록 시
        // when
        // 에러가 나는가?
        // then
        //회원에 대한 정보가 없이 관심상품을 생성

        Exception e =
        assertThrows(NullPointerException.class,
                () -> productService.createProduct(productRequestDto , createdUser.getId()));
        assertEquals(null,e.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("2. 회원가입이 잘 되는가? ")
    void test2(){

        // 이용자가 가입할 유저의 정보 (일반유저)
        //given
        String username = "박동석";
        String password = "1234";
        String email = "ehd0309@naver.com";

        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername(username);
        signupRequestDto.setPassword(password);
        signupRequestDto.setEmail(email);

        // 주어진 유저 정보로 유저등록을 시도 할 떄
        // when
        User user = userService.registerUser(signupRequestDto);

        // 회원가입이 잘 되었는가
        // then
        assertNotNull(user.getId());
        assertEquals(user.getEmail() , email);
        assertEquals(user.getUsername() , username);
        assertEquals(user.getRole() , UserRole.USER);
        //비밀번호는
        boolean pw = passwordEncoder.matches(password, user.getPassword());
        assertEquals(true , pw);

        createdUser = user;
    }

    @Test
    @Order(3)
    @DisplayName("3. 회원가입이 되었을 때 해당 회원으로 관심상품 등록이 되는가?")
    void test3(){

        // 주어진 회원 createUser
        // 주어질 관심 상품 title , imageUrl , linkUrl , lPrice
        // given

        ProductRequestDto productRequestDto = new ProductRequestDto(title , imageUrl , linkUrl , lPrice);

        // 관심상품 등록을 할 때
        // when
        Product product = productService.createProduct(productRequestDto , createdUser.getId());
        
        // 해당 회원의 관심상품이 등록이 되었나
        // then
        assertNotNull(product.getId());
        assertEquals(product.getTitle() , title);
        assertEquals(product.getImage(),imageUrl);
        assertEquals(product.getLink() , linkUrl);
        assertEquals(product.getLprice() , lPrice);
        assertEquals(product.getMyprice() , 0);
        assertEquals(product.getUserId() , createdUser.getId());

        createdProduct = product;
    }

    @Test
    @Order(4)
    @DisplayName("4. 회원으로 관심상품 등록이 되었을 때 그 관심상품을 업데이트 할 수 있는가?")
    void test4(){
        // 등록했었던 관심상품과  myPrice 변경이 주어질 때
        //given
        int myPrice = 65432;
        Long id = createdProduct.getId();

        // 등록했었던 관심상품의 myPrice를 주어진 myPrice 로 업데이트하면
        //when
        ProductMypriceRequestDto mypriceRequestDto = new ProductMypriceRequestDto(myPrice);
        Product product = productService.updateProduct(id , mypriceRequestDto);

        // 업데이트가 되는가
        // then
        assertNotNull(product.getId());
        assertEquals(createdUser.getId(), product.getUserId());
        assertEquals(createdProduct.getTitle(), product.getTitle());
        assertEquals(createdProduct.getImage(), product.getImage());
        assertEquals(createdProduct.getLink(), product.getLink());
        assertEquals(createdProduct.getLprice(), product.getLprice());
        assertEquals(myPrice, product.getMyprice());

        createdProduct.setMyprice(myPrice);
    }

    @Test
    @Order(5)
    @DisplayName("회원이 등록한 모든 관심상품 조회")
    void test5() {
        // given
        int page = 0;
        int size = 10;
        String sortBy = "id";
        boolean isAsc = false;

        // when
        Page<Product> productList = productService.getProducts(createdUser.getId(), page, size, sortBy, isAsc);

        // then
        // 1. 전체 상품에서 테스트에 의해 생성된 상품 찾아오기 (상품의 id 로 찾음)
        Long createdProductId = this.createdProduct.getId();
        Product foundProduct = productList.stream()
                .filter(product -> product.getId().equals(createdProductId))
                .findFirst()
                .orElse(null);
        // 2. Order(1) 테스트에 의해 생성된 상품과 일치하는지 검증
        assertNotNull(foundProduct);
        assertEquals(createdUser.getId(), foundProduct.getUserId());
        assertEquals(this.createdProduct.getId(), foundProduct.getId());
        assertEquals(this.createdProduct.getTitle(), foundProduct.getTitle());
        assertEquals(this.createdProduct.getImage(), foundProduct.getImage());
        assertEquals(this.createdProduct.getLink(), foundProduct.getLink());
        assertEquals(this.createdProduct.getLprice(), foundProduct.getLprice());
        // 3. Order(2) 테스트에 의해 myPrice 가격이 정상적으로 업데이트되었는지 검증
        assertEquals(createdProduct.getMyprice(), foundProduct.getMyprice());
    }
}

