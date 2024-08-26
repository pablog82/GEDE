package com.babelgroup.gede.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.babelgroup.gede.dao.RegistroDAO;
import com.babelgroup.gede.model.Registro;

import lombok.extern.log4j.Log4j2;

/**
 * The Class RegistroDAOImpl.
 */
@Component
@Log4j2
public class RegistroDAOImpl implements RegistroDAO {

    public static final String SELECT_SQL = "SELECT id,expediente,identificadorExpediente, documento,referenciaRepositorio,observaciones,estado FROM registro";
    /**
     * The insert sql.
     */
    private static final String INSERT_SQL = "INSERT INTO registro(expediente,identificadorExpediente, documento,referenciaRepositorio,observaciones,estado) values (?,?,?,?,?,?)";

    /**
     * The jdbc template.
     */
    @Lazy
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Insert.
     *
     * @param registro the registro
     */
    @Override
    public int insert(Registro registro) {
        log.info("Insertando registro: " + registro.toString());
        Object[] params = {registro.getExpediente(), registro.getDocumento(), registro.getReferenciaRepositorio(),
                registro.getObservaciones(), registro.getEstado()};

        int[] types = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
        return this.jdbcTemplate.update(INSERT_SQL, params, types);
    }

    /**
     * Find all.
     *
     * @return the list
     */
    @Override
    public List<Registro> findAll() {
        return jdbcTemplate.query(
                SELECT_SQL,

                (resultSet, rowNum) -> new Registro(
                        resultSet.getInt("id"),
                        resultSet.getString("expediente"),
                        resultSet.getString("identificadorExpediente"),
                        resultSet.getString("documento"),
                        resultSet.getString("referenciaRepositorio"),
                        resultSet.getString("observaciones"),
                        resultSet.getString("estado")));
    }

    /**
     * Find all by expediente.
     *
     * @param expediente the expediente
     * @return the list
     */
    @Override
    public List<Registro> findAllByExpediente(String expediente) {
        return jdbcTemplate.query(
                SELECT_SQL + " WHERE expediente = '"
                        + expediente + "'",

                (resultSet, rowNum) -> new Registro(
                        resultSet.getInt("id"),
                        resultSet.getString("expediente"),
                        resultSet.getString("identificadorExpediente"),
                        resultSet.getString("documento"),
                        resultSet.getString("referenciaRepositorio"),
                        resultSet.getString("observaciones"),
                        resultSet.getString("estado")));
    }

    @Override
    public List<Registro> findByExpedienteAndDocumento(String expediente, String documento) {
        return jdbcTemplate.query(
                SELECT_SQL + " WHERE expediente = '"
                        + expediente + "' AND documento = '" + documento + "'",

                (resultSet, rowNum) -> new Registro(
                        resultSet.getInt("id"),
                        resultSet.getString("expediente"),
                        resultSet.getString("identificadorExpediente"),
                        resultSet.getString("documento"),
                        resultSet.getString("referenciaRepositorio"),
                        resultSet.getString("observaciones"),
                        resultSet.getString("estado")));
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.execute("DELETE FROM registro");
    }

}
