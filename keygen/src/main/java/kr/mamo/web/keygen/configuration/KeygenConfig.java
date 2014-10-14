package kr.mamo.web.keygen.configuration;

import java.util.List;

import kr.mamo.web.keygen.interceptor.AccountInterceptor;
import kr.mamo.web.keygen.resolver.CurrentUserResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = {"kr.mamo.web.keygen"})
@EnableWebMvc
public class KeygenConfig extends WebMvcConfigurerAdapter {
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountInterceptor()).addPathPatterns("/**")/*.excludePathPatterns("/")*/;
    }
	
	@Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new CurrentUserResolver());
    }
	
	@Bean
	public AccountInterceptor accountInterceptor() {
		return new AccountInterceptor();
	}
}
