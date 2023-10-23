package com.dog.shop;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShopProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopProjectApplication.class, args);

	}
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper; // @Bean으로 설정하였기때문에
//@Autowired로 인젝션 주입 받을 수 있다.
	}
}
