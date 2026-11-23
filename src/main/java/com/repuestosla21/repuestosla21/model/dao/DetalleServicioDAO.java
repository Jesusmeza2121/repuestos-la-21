package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.DetalleServicioVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class DetalleServicioDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<DetalleServicioVO> mapper = (rs, row) -> new DetalleServicioVO(
            rs.getLong("id"),
            rs.getLong("venta_id"),
            rs.getString("descripcion"),
            rs.getInt("cantidad"),
            rs.getBigDecimal("precio"),
            rs.getBigDecimal("subtotal"));

    public DetalleServicioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void create(Long ventaId, String descripcion, int cantidad, BigDecimal precio, BigDecimal subtotal) {
        jdbc.update(
                "INSERT INTO detalle_servicios(venta_id,descripcion,cantidad,precio,subtotal) VALUES(?,?,?,?,?)",
                ventaId, descripcion, cantidad, precio, subtotal);
    }

    public List<DetalleServicioVO> findByVenta(Long ventaId) {
        return jdbc.query("SELECT * FROM detalle_servicios WHERE venta_id=? ORDER BY id", mapper, ventaId);
    }
}
