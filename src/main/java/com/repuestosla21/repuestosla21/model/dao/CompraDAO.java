package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.CompraVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CompraDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<CompraVO> mapper = (rs,n) -> new CompraVO(rs.getLong("id"),
            rs.getLong("proveedor_id"), rs.getString("proveedorNombre"), rs.getString("numero_factura"),
            rs.getTimestamp("fecha").toLocalDateTime(), rs.getBigDecimal("total"), rs.getString("observaciones"), rs.getLong("usuario_id"));

    public CompraDAO(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Long create(Long proveedorId, String factura, BigDecimal total, String observaciones, Long usuarioId) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> { PreparedStatement ps=con.prepareStatement("INSERT INTO compras(proveedor_id,numero_factura,total,observaciones,usuario_id) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, proveedorId); ps.setString(2, factura); ps.setBigDecimal(3,total); ps.setString(4,observaciones); ps.setLong(5,usuarioId); return ps; }, keys);
        return keys.getKey().longValue();
    }

    public List<CompraVO> findAll() { return jdbc.query("SELECT c.*,p.nombre proveedorNombre FROM compras c JOIN proveedores p ON p.id=c.proveedor_id ORDER BY c.fecha DESC", mapper); }
    public CompraVO findById(Long id) { return jdbc.queryForObject("SELECT c.*,p.nombre proveedorNombre FROM compras c JOIN proveedores p ON p.id=c.proveedor_id WHERE c.id=?", mapper, id); }
    public BigDecimal totalCompras() { return jdbc.queryForObject("SELECT COALESCE(SUM(total),0) FROM compras", BigDecimal.class); }
}
