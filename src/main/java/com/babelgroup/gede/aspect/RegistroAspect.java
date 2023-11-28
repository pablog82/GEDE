package com.babelgroup.gede.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Aspect
//@Log4j2
public class RegistroAspect {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before("execution(* com.babelgroup.gede.dao.impl.RegistroDAOImpl.*(..))")
	public void initElements() {

		int count = jdbcTemplate.queryForObject(
				"SELECT count(*) FROM sqlite_master WHERE type='table' AND name='{registro}'", Integer.class);

		if (count == 0) {
//			log.info("Inicializando la base de datos");
			jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS registro(id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "	expediente TEXT NOT NULL," + "	documento TEXT NOT NULL," + "	referenciaRepositorio TEXT,"
					+ "	observaciones TEXT," + "	estado TEXT)");
		}
	}
}
