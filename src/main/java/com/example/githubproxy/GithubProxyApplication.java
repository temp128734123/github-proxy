package com.example.githubproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GithubProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubProxyApplication.class, args);
	}
}
