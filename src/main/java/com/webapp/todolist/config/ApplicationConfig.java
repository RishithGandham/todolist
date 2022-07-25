package com.webapp.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Locale;


// dis class dont do anthing
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    // Configurates the view resolver instead of doing it in the
    // application.properties page

//    @Bean
//    public ViewResolver viewResolver() {
//        InternalResourceViewResolver bean = new InternalResourceViewResolver();
//        bean.setPrefix("/jsp/");
//        bean.setSuffix(".jsp");
//        bean.setOrder(0);
//        return bean;
//    }


    // breaks the functionality of jsp pages so i commented, you can also set the
    // bean priority to greater than 0 and it will also work
    /*
     * @Bean public ViewResolver thymeleafResolver() { ThymeleafViewResolver
     * viewResolver = new ThymeleafViewResolver();
     * viewResolver.setTemplateEngine(templateEngine()); viewResolver.setOrder(0);
     * return viewResolver; }
     *
     * // to go to find the thymeleaf pages
     *
     * @Bean public SpringResourceTemplateResolver templateResolver() {
     * SpringResourceTemplateResolver templateResolver = new
     * SpringResourceTemplateResolver();
     * templateResolver.setApplicationContext(applicationContext);
     * templateResolver.setPrefix("/views/"); templateResolver.setSuffix(".html");
     * return templateResolver; }
     *
     * // Creates a spring template engine so the templates can resolve
     *
     * @Bean public SpringTemplateEngine templateEngine() { SpringTemplateEngine
     * templateEngine = new SpringTemplateEngine();
     * templateEngine.setTemplateResolver(templateResolver());
     * templateEngine.setEnableSpringELCompiler(true); return templateEngine; }
     */


    // i d k what this does
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//    }
//
//    // i d k what this does
//    @Bean
//    public SessionLocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        slr.setDefaultLocale(Locale.US);
//        return slr;
//    }
//
//    // i d k what this does
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        lci.setParamName("lang");
//        return lci;
//    }

}
