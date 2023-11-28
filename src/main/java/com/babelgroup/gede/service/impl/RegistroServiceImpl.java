package com.babelgroup.gede.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.babelgroup.gede.dao.RegistroDAO;
import com.babelgroup.gede.model.Registro;
import com.babelgroup.gede.service.RegistroService;

/**
 * The Class RegistroServiceImpl.
 */
@Service
public class RegistroServiceImpl implements RegistroService {

	/** The dao. */
	@Lazy
	@Autowired
	RegistroDAO dao;

	/**
	 * Insert.
	 *
	 * @param registro the registro
	 * @return the int
	 */
	@Override
	public int insert(Registro registro) {
		return dao.insert(registro);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@Override
	public List<Registro> findAll() {
		return dao.findAll();
	}

	/**
	 * Find all by expediente.
	 *
	 * @param expediente the expediente
	 * @return the list
	 */
	@Override
	public List<Registro> findAllByExpediente(String expediente) {
		return dao.findAllByExpediente(expediente);
	}

	/**
	 * Delete all.
	 */
	@Override
	public void deleteAll() {
		dao.deleteAll();
	}

	/**
	 * Find by expediente and documento.
	 *
	 * @param expediente the expediente
	 * @param documento  the documento
	 * @return the list
	 */
	@Override
	public List<Registro> findByExpedienteAndDocumento(String expediente, String documento) {
		return dao.findByExpedienteAndDocumento(expediente, documento);
	}

}
