package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.DetalleCompraVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DetalleCompraDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<DetalleCompraVO> mapper=(rs,n)->new DetalleCompraVO(rs.getLong("id"),rs.getLong("compra_id"),rs.getLong("producto_id"),rs.getString("productoNombre"),rs.getInt("cantidad"),rs.getBigDecimal("precio"),rs.getBigDecimal("subtotal"));
    public DetalleCompraDAO(JdbcTemplate jdbc){this.jdbc=jdbc;}
    public void create(Long compraId,Long productoId,int cantidad,java.math.BigDecimal precio,java.math.BigDecimal subtotal){jdbc.update("INSERT INTO detalle_compras(compra_id,producto_id,cantidad,precio,subtotal) VALUES(?,?,?,?,?)",compraId,productoId,cantidad,precio,subtotal);}
    public List<DetalleCompraVO> findByCompra(Long compraId){return jdbc.query("SELECT d.*,p.nombre productoNombre FROM detalle_compras d JOIN productos p ON p.id=d.producto_id WHERE d.compra_id=?",mapper,compraId);}
}
