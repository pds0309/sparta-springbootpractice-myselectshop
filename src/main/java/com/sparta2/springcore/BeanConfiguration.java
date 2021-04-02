package com.sparta2.springcore;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //처음 스프링이 실행될 때 이 클래스를 바라보고 @bean 함수를 살펴보고 필요한 내용들을 bean에 담는다
public class BeanConfiguration {
    @Bean
    public ProductRepository productRepository() {
        String dbId = "sa";
        String dbPassword = "";
        String dbUrl = "jdbc:h2:mem:springcoredb";
        return new ProductRepository(dbId, dbPassword, dbUrl);
    }

    @Bean
    @Autowired     //스프링 IOC 컨테이너에 있는 productrepository bean 을 꺼내서 DI를 해준다
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}