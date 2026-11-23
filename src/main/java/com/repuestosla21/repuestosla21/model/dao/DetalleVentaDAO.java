package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.DetalleVentaVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class DetalleVentaDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<DetalleVentaVO> mapper=(rs,n)->new DetalleVentaVO(rs.getLong("id"),rs.getLong("venta_id"),rs.getLong("producto_id"),rs.getString("productoNombre"),rs.getInt("cantidad"),rs.getBigDecimal("precio"),rs.getBigDecimal("subtotal"));
    public DetalleVentaDAO(JdbcTemplate jdbc){this.jdbc=jdbc;}
    public void create(Long ventaId,Long productoId,int cantidad,BigDecimal precio,BigDecimal subtotal){jdbc.update("INSERT INTO detalle_ventas(venta_id,producto_id,cantidad,precio,subtotal) VALUES(?,?,?,?,?)",ventaId,productoId,cantidad,precio,subtotal);}
    public List<DetalleVentaVO> findByVenta(Long ventaId){return jdbc.query("SELECT d.*,p.nombre productoNombre FROM detalle_ventas d JOIN productos p ON p.id=d.producto_id WHERE d.venta_id=?",mapper,ventaId);}
}
