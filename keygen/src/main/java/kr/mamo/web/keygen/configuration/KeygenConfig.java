package kr.mamo.web.keygen.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = {"kr.mamo.web.keygen"})
@EnableWebMvc
public class KeygenConfig {

}
