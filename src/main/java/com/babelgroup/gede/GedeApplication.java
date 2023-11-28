package com.babelgroup.gede;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import com.babelgroup.gede.service.GestorDocumentalService;
import com.babelgroup.gede.service.RegistroService;

/**
 * The Class GedeApplication.
 */
@SpringBootApplication
public class GedeApplication implements CommandLineRunner {

	/** The env. */
	@Autowired
	Environment env;

	/** The registro service. */
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

//		Registro r1 = new Registro("EXP012023", "DOC001", UUID.randomUUID().toString(), "OBS01", 1);
//		Registro r2 = new Registro("EXP022023", "DOC002", UUID.randomUUID().toString(), "OBS02", 2);
//		Registro r3 = new Registro("EXP032023", "DOC003", UUID.randomUUID().toString(), "OBS03", 3);
//		Registro r4 = new Registro("EXP042023", "DOC004", UUID.randomUUID().toString(), "OBS04", 4);
//		Registro r5 = new Registro("EXP052023", "DOC005", UUID.randomUUID().toString(), "OBS05", 5);

		registroService.deleteAll();

		/*
		 * registroService.insert(r1); registroService.insert(r2);
		 * registroService.insert(r3); registroService.insert(r4);
		 * registroService.insert(r5);
		 */

		gestorDocumentalService.subirDocumentos();

//		List<Registro> registros = registroService.findAll();
//		registros.forEach(System.out::println);

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
