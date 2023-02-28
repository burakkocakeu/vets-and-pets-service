package eu.burakkocak.vetsandpetsservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "eu.burakkocak.vetsandpetsservice.data.repository")
public class DataConfig {
}
