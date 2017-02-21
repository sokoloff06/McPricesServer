package com.sokoloff06.mcprices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.beans.beancontext.BeanContext;

@SpringBootApplication
@Configuration
@EnableScheduling
public class McpricesApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpricesApplication.class, args);
	}
}
