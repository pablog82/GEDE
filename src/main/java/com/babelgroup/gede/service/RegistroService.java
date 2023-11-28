package com.babelgroup.gede.service;

import java.util.List;

import com.babelgroup.gede.model.Registro;

/**
 * The Interface RegistroService.
 */
public interface RegistroService {

	/**
	 * Insert.
	 *
	 * @param registro the registro
	 * @return the int
	 */
	public int insert(Registro registro);

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<Registro> findAll();

	/**
	 * Find all by expediente.
	 *
	 * @param expediente the expediente
	 * @return the list
	 */
	public List<Registro> findAllByExpediente(String expediente);
	
	/**
	 * Delete all.
	 */
	public void deleteAll();
	
	public List<Registro> findByExpedienteAndDocumento(String expediente, String documento);

}
