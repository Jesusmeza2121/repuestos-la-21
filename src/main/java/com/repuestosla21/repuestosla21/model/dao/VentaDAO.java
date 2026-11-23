package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.VentaVO;
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
public class VentaDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<VentaVO> mapper=(rs,n)->new VentaVO(rs.getLong("id"),rs.getString("cliente"),rs.getString("documento_cliente"),rs.getString("telefono_cliente"),rs.getTimestamp("fecha").toLocalDateTime(),rs.getBigDecimal("total"),rs.getString("metodo_pago"),"COMPLETADA",rs.getString("observaciones"),rs.getLong("usuario_id"));
    public VentaDAO(JdbcTemplate jdbc){this.jdbc=jdbc;}
    public Long create(String nombre,String documento,String telefono,BigDecimal total,String metodo,String observaciones,Long usuarioId){
        KeyHolder keys=new GeneratedKeyHolder();
        jdbc.update(con->{PreparedStatement ps=con.prepareStatement("INSERT INTO ventas(cliente,documento_cliente,telefono_cliente,total,metodo_pago,observaciones,usuario_id) VALUES(?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);ps.setString(1,nombre);ps.setString(2,documento);ps.setString(3,telefono);ps.setBigDecimal(4,total);ps.setString(5,metodo);ps.setString(6,observaciones);ps.setLong(7,usuarioId);return ps;},keys);return keys.getKey().longValue();
    }
    public List<VentaVO> findAll(){return jdbc.query("SELECT * FROM ventas ORDER BY fecha DESC",mapper);}
    public VentaVO findById(Long id){return jdbc.queryForObject("SELECT * FROM ventas WHERE id=?",mapper,id);}
    public BigDecimal totalVentas(){return jdbc.queryForObject("SELECT COALESCE(SUM(total),0) FROM ventas",BigDecimal.class);}
}
