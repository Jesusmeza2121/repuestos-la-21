package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.MovimientoInventarioVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovimientoInventarioDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<MovimientoInventarioVO> mapper=(rs,n)->new MovimientoInventarioVO(rs.getLong("id"),rs.getLong("producto_id"),rs.getString("productoNombre"),rs.getLong("usuario_id"),rs.getString("tipo"),rs.getInt("cantidad"),rs.getInt("stock_anterior"),rs.getInt("stock_nuevo"),rs.getString("referencia"),rs.getString("observaciones"),rs.getTimestamp("fecha").toLocalDateTime());
    public MovimientoInventarioDAO(JdbcTemplate jdbc){this.jdbc=jdbc;}
    public void create(Long productoId,Long usuarioId,String tipo,int cantidad,int anterior,int nuevo,String referencia,String observaciones){
        jdbc.update("INSERT INTO movimientos_inventario(producto_id,usuario_id,tipo,cantidad,stock_anterior,stock_nuevo,referencia,observaciones) VALUES(?,?,?,?,?,?,?,?)",productoId,usuarioId,tipo,cantidad,anterior,nuevo,referencia,observaciones);
    }
    public List<MovimientoInventarioVO> findRecent(int limit){return jdbc.query("SELECT m.*,p.nombre productoNombre FROM movimientos_inventario m JOIN productos p ON p.id=m.producto_id ORDER BY m.fecha DESC LIMIT ?",mapper,limit);}
    public List<MovimientoInventarioVO> findAll(){return jdbc.query("SELECT m.*,p.nombre productoNombre FROM movimientos_inventario m JOIN productos p ON p.id=m.producto_id ORDER BY m.fecha DESC",mapper);}
}
