package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.UsuarioVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<UsuarioVO> mapper = (rs, n) -> new UsuarioVO(
            rs.getLong("id"), rs.getString("usuario"), rs.getString("password"),
            rs.getString("nombre"), rs.getString("rol") == null ? "EMPLEADO" : rs.getString("rol").toUpperCase(java.util.Locale.ROOT), rs.getBoolean("estado"),
            rs.getTimestamp("creado_en").toLocalDateTime(), rs.getTimestamp("creado_en").toLocalDateTime());

    public UsuarioDAO(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Optional<UsuarioVO> findByUsername(String username) {
        List<UsuarioVO> rows = jdbc.query("SELECT * FROM usuarios WHERE usuario = ?", mapper, username);
        return rows.stream().findFirst();
    }

    public Optional<UsuarioVO> findById(Long id) {
        List<UsuarioVO> rows = jdbc.query("SELECT * FROM usuarios WHERE id = ?", mapper, id);
        return rows.stream().findFirst();
    }

    public List<UsuarioVO> findAll() { return jdbc.query("SELECT * FROM usuarios ORDER BY nombre", mapper); }

    public void create(String username, String hash, String name, String role) {
        jdbc.update("INSERT INTO usuarios(nombre,usuario,password,rol,estado) VALUES(?,?,?,?,1)",
                name, username, hash, role);
    }

    public void update(Long id, String name, String role, boolean active) {
        jdbc.update("UPDATE usuarios SET nombre=?, rol=?, estado=? WHERE id=?", name, role, active, id);
    }

    public void resetPassword(Long id, String hash) {
        jdbc.update("UPDATE usuarios SET password=? WHERE id=?", hash, id);
    }

    public int countActiveAdministrators() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM usuarios WHERE UPPER(rol)='ADMIN' AND estado=1", Integer.class);
        return count == null ? 0 : count;
    }
}
