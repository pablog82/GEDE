package com.babelgroup.gede;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import com.babelgroup.gede.service.GestorDocumentalService;
import com.babelgroup.gede.service.RegistroService;

/**
 * The Class GedeApplication.
 */
@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:log4j2.properties"),
        @PropertySource("classpath:database.properties"),
        @PropertySource("file:${spring.config.location}/afirma.properties"),
        @PropertySource("file:${spring.config.location}/gede.properties")
//    @PropertySource("classpath:afirma.properties"),
//    @PropertySource("classpath:gede.properties")
})
public class GedeApplication implements CommandLineRunner {

    /**
     * The env.
     */
    @Autowired
    Environment env;

    /**
     * The registro service.
     */
    @Autowired
    private RegistroService registroService;

    @Autowired
    private GestorDocumentalService gestorDocumentalService;

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GedeApplication.class, args).close();

    }

    /**
     * Run.
     *
     * @param args the args
     * @throws Exception the exception
     */
    @Override
    public void run(String... args) throws Exception {
        registroService.deleteAll();
        gestorDocumentalService.subirDocumentos();
    }

    /**
     * Data source.
     *
     * @return the data source
     */
    @Bean
    DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("sqlite.driver"));
        dataSource.setUrl(env.getProperty("sqlite.url"));
        dataSource.setUsername(env.getProperty("sqlite.user"));
        dataSource.setPassword(env.getProperty("sqlite.password"));
        return dataSource;
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
