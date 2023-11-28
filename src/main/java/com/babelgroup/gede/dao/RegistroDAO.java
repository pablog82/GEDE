package com.babelgroup.gede.dao;

import java.util.List;

import com.babelgroup.gede.model.Registro;

/**
 * The Interface RegistroDAO.
 */
public interface RegistroDAO {

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

	/**
	 * Find by expediente and documento.
	 *
	 * @param expediente the expediente
	 * @param documento  the documento
	 * @return the list
	 */
	public List<Registro> findByExpedienteAndDocumento(String expediente, String documento);

}
