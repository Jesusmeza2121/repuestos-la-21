package com.repuestosla21.repuestosla21.model.dao;

import com.repuestosla21.repuestosla21.model.vo.ProductoVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoDAO {
    private final JdbcTemplate jdbc;
    private final RowMapper<ProductoVO> mapper = (rs,n) -> new ProductoVO(
            rs.getLong("id"), rs.getString("codigo"), rs.getString("nombre"), rs.getString("descripcion"),
            rs.getObject("categoria_id", Long.class), rs.getString("categoriaNombre"), rs.getObject("proveedor_id", Long.class),
            rs.getString("proveedorNombre"), rs.getBigDecimal("precio_compra"), rs.getBigDecimal("precio_venta"),
            rs.getInt("stock"), rs.getInt("stock_minimo"), rs.getString("unidad"), true,
            rs.getTimestamp("creado_en").toLocalDateTime(), rs.getTimestamp("actualizado_en").toLocalDateTime());

    public ProductoDAO(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private String baseSelect() {
        return "SELECT p.*, c.nombre categoriaNombre, pr.nombre proveedorNombre FROM productos p "
                + "LEFT JOIN categorias c ON c.id=p.categoria_id "
                + "LEFT JOIN proveedores pr ON pr.id=p.proveedor_id ";
    }

    public List<ProductoVO> findAll(String search, Long categoriaId, Long proveedorId) {
        StringBuilder sql = new StringBuilder(baseSelect()).append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (search != null && !search.isBlank()) { sql.append("AND (p.codigo LIKE ? OR p.nombre LIKE ?) "); params.add("%"+search+"%"); params.add("%"+search+"%"); }
        if (categoriaId != null) { sql.append("AND p.categoria_id=? "); params.add(categoriaId); }
        if (proveedorId != null) { sql.append("AND p.proveedor_id=? "); params.add(proveedorId); }
        sql.append("ORDER BY p.nombre");
        return jdbc.query(sql.toString(), mapper, params.toArray());
    }

    public ProductoVO findById(Long id) { return jdbc.queryForObject(baseSelect()+"WHERE p.id=?", mapper, id); }

    public boolean existsCode(String code, Long excludedId) {
        Integer count = excludedId == null
                ? jdbc.queryForObject("SELECT COUNT(*) FROM productos WHERE codigo=?", Integer.class, code)
                : jdbc.queryForObject("SELECT COUNT(*) FROM productos WHERE codigo=? AND id<>?", Integer.class, code, excludedId);
        return count != null && count > 0;
    }

    public void create(ProductoVO p) {
        jdbc.update("INSERT INTO productos(codigo,nombre,descripcion,categoria_id,proveedor_id,precio_compra,precio_venta,stock,stock_minimo,unidad) VALUES(?,?,?,?,?,?,?,?,?,?)",
                p.codigo(), p.nombre(), p.descripcion(), p.categoriaId(), p.proveedorId(), p.precioCompra(), p.precioVenta(), p.stock(), p.stockMinimo(), p.unidad());
    }

    public void update(ProductoVO p) {
        jdbc.update("UPDATE productos SET codigo=?,nombre=?,descripcion=?,categoria_id=?,proveedor_id=?,precio_compra=?,precio_venta=?,stock=?,stock_minimo=?,unidad=? WHERE id=?",
                p.codigo(), p.nombre(), p.descripcion(), p.categoriaId(), p.proveedorId(), p.precioCompra(), p.precioVenta(), p.stock(), p.stockMinimo(), p.unidad(), p.id());
    }

    public void delete(Long id) { jdbc.update("DELETE FROM productos WHERE id=?", id); }

    public int count() { return jdbc.queryForObject("SELECT COUNT(*) FROM productos", Integer.class); }
    public int stockLowCount() { return jdbc.queryForObject("SELECT COUNT(*) FROM productos WHERE stock>0 AND stock<=stock_minimo", Integer.class); }
    public int outOfStockCount() { return jdbc.queryForObject("SELECT COUNT(*) FROM productos WHERE stock=0", Integer.class); }

    public int updateStockLocked(Long id, int delta) {
        return jdbc.update("UPDATE productos SET stock=stock+? WHERE id=? AND stock+?>=0", delta, id, delta);
    }

    public Integer getStockLocked(Long id) {
        return jdbc.queryForObject("SELECT stock FROM productos WHERE id=? FOR UPDATE", Integer.class, id);
    }
}
