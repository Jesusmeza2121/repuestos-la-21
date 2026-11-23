package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.DevolucionVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DevolucionDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<DevolucionVO> mapper=(rs,n)->new DevolucionVO(rs.getLong("id"),rs.getLong("venta_id"),rs.getLong("producto_id"),rs.getString("productoNombre"),rs.getInt("cantidad"),rs.getString("motivo"),rs.getTimestamp("fecha").toLocalDateTime(),rs.getString("estado"),rs.getLong("usuario_id"));
    public DevolucionDAO(JdbcTemplate jdbc){this.jdbc=jdbc;}
    public void create(Long ventaId,Long productoId,int cantidad,String motivo,Long usuarioId){jdbc.update("INSERT INTO devoluciones(venta_id,producto_id,usuario_id,cantidad,motivo,estado) VALUES(?,?,?,?,?, 'ACEPTADA')",ventaId,productoId,usuarioId,cantidad,motivo);}
    public List<DevolucionVO> findAll(){return jdbc.query("SELECT d.*,p.nombre productoNombre FROM devoluciones d JOIN productos p ON p.id=d.producto_id ORDER BY d.fecha DESC",mapper);}
    public int cantidadDevuelta(Long ventaId,Long productoId){Integer c=jdbc.queryForObject("SELECT COALESCE(SUM(cantidad),0) FROM devoluciones WHERE venta_id=? AND producto_id=?",Integer.class,ventaId,productoId);return c==null?0:c;}
}
