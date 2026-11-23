package com.repuestosla21.repuestosla21.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {
    private final JdbcTemplate jdbc;
    public ReporteService(JdbcTemplate jdbc){this.jdbc=jdbc;}

    public List<Map<String,Object>> inventario(){return jdbc.queryForList("SELECT p.codigo,p.nombre,c.nombre categoria,p.stock,p.stock_minimo,p.precio_venta FROM productos p LEFT JOIN categorias c ON c.id=p.categoria_id ORDER BY p.nombre");}
    public List<Map<String,Object>> movimientos(){return jdbc.queryForList("SELECT m.fecha,p.codigo,p.nombre,m.tipo AS tipo_movimiento,m.cantidad,m.stock_anterior,m.stock_nuevo,m.referencia,m.observaciones FROM movimientos_inventario m JOIN productos p ON p.id=m.producto_id ORDER BY m.fecha DESC");}

    public void csv(PrintWriter out,List<Map<String,Object>> rows){
        if(rows.isEmpty()){out.println("Sin datos");return;}
        Object[] headers=rows.get(0).keySet().toArray();for(int i=0;i<headers.length;i++){if(i>0)out.print(',');out.print(headers[i]);}out.println();
        for(Map<String,Object> row:rows){int i=0;for(Object h:headers){if(i++>0)out.print(',');Object v=row.get(h);out.print(v==null?"":v.toString().replaceAll("[\\r\\n,]"," "));}out.println();}
    }
}
