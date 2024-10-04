package com.nus.nexchange.userservice.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.nus.nexchange.userservice.infrastructure.persistence")  // 指定Repository的包
@EntityScan(basePackages = "com.nus.nexchange.userservice.domain")  // 指定实体类所在的包
public class JpaConfig {
}
