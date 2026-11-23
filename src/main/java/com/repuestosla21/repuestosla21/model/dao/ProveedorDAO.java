package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.ProveedorVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProveedorDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<ProveedorVO> mapper = (rs,n) -> new ProveedorVO(rs.getLong("id"),
            rs.getString("nombre"), rs.getString("documento"), rs.getString("telefono"),
            rs.getString("correo"), rs.getString("direccion"), true);

    public ProveedorDAO(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<ProveedorVO> findAll(String search) {
        String sql = "SELECT * FROM proveedores WHERE nombre LIKE ? OR documento LIKE ? ORDER BY nombre";
        if (search == null || search.isBlank()) sql = "SELECT * FROM proveedores ORDER BY nombre";
        return (search == null || search.isBlank()) ? jdbc.query(sql, mapper)
                : jdbc.query(sql, mapper, "%" + search + "%", "%" + search + "%");
    }

    public ProveedorVO findById(Long id) { return jdbc.queryForObject("SELECT * FROM proveedores WHERE id=?", mapper, id); }

    public void create(ProveedorVO p) {
        jdbc.update("INSERT INTO proveedores(nombre,documento,telefono,correo,direccion) VALUES(?,?,?,?,?)",
                p.nombre(), p.documento(), p.telefono(), p.correo(), p.direccion());
    }

    public void update(ProveedorVO p) {
        jdbc.update("UPDATE proveedores SET nombre=?,documento=?,telefono=?,correo=?,direccion=? WHERE id=?",
                p.nombre(), p.documento(), p.telefono(), p.correo(), p.direccion(), p.id());
    }

    public void delete(Long id) { jdbc.update("DELETE FROM proveedores WHERE id=?", id); }

    public int count() { return jdbc.queryForObject("SELECT COUNT(*) FROM proveedores", Integer.class); }
}
