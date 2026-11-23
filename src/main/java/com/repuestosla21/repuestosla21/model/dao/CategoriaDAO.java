package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.CategoriaVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriaDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<CategoriaVO> mapper = (rs,n) -> new CategoriaVO(rs.getLong("id"),
            rs.getString("nombre"), rs.getString("descripcion"), true,
            rs.getInt("cantidadProductos"));

    public CategoriaDAO(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<CategoriaVO> findAll(String search) {
        if (search == null || search.isBlank()) {
            return jdbc.query("SELECT c.*, (SELECT COUNT(*) FROM productos p WHERE p.categoria_id=c.id) cantidadProductos FROM categorias c ORDER BY c.nombre", mapper);
        }
        return jdbc.query("SELECT c.*, (SELECT COUNT(*) FROM productos p WHERE p.categoria_id=c.id) cantidadProductos FROM categorias c WHERE c.nombre LIKE ? ORDER BY c.nombre", mapper, "%" + search + "%");
    }

    public CategoriaVO findById(Long id) {
        return jdbc.queryForObject("SELECT c.*, (SELECT COUNT(*) FROM productos p WHERE p.categoria_id=c.id) cantidadProductos FROM categorias c WHERE c.id=?", mapper, id);
    }

    public void create(String nombre, String descripcion) {
        jdbc.update("INSERT INTO categorias(nombre,descripcion) VALUES(?,?)", nombre, descripcion);
    }

    public void update(Long id, String nombre, String descripcion, boolean activo) {
        jdbc.update("UPDATE categorias SET nombre=?,descripcion=? WHERE id=?", nombre, descripcion, id);
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM categorias WHERE id=?", id);
    }

    public int count() { return jdbc.queryForObject("SELECT COUNT(*) FROM categorias", Integer.class); }
}
