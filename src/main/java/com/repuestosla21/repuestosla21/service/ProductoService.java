package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.ProductoVO;
import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoDAO dao;
    private final JdbcTemplate jdbc;

    public ProductoService(ProductoDAO dao, JdbcTemplate jdbc) { this.dao=dao; this.jdbc=jdbc; }

    public List<ProductoVO> buscar(String search, Long categoriaId, Long proveedorId) { return dao.findAll(search,categoriaId,proveedorId); }
    public ProductoVO obtener(Long id) { return dao.findById(id); }

    public void guardar(ProductoVO p) {
        if (p.codigo()==null || p.codigo().isBlank()) throw new IllegalArgumentException("El código es obligatorio");
        if (p.nombre()==null || p.nombre().isBlank()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (p.precioCompra()==null || p.precioCompra().signum()<0 || p.precioVenta()==null || p.precioVenta().signum()<0) throw new IllegalArgumentException("Los precios no pueden ser negativos");
        if (p.stock()==null || p.stock()<0 || p.stockMinimo()==null || p.stockMinimo()<0) throw new IllegalArgumentException("Las cantidades no pueden ser negativas");
        if (dao.existsCode(p.codigo().trim(),p.id())) throw new IllegalArgumentException("Ya existe un producto con ese código");
        if (p.id()==null) dao.create(p); else dao.update(p);
    }

    public void eliminar(Long id) {
        Integer compras=jdbc.queryForObject("SELECT COUNT(*) FROM detalle_compras WHERE id_producto=?",Integer.class,id);
        Integer ventas=jdbc.queryForObject("SELECT COUNT(*) FROM detalle_ventas WHERE id_producto=?",Integer.class,id);
        if ((compras!=null&&compras>0)||(ventas!=null&&ventas>0)) throw new IllegalArgumentException("No se puede eliminar: existen operaciones históricas relacionadas");
        try { dao.delete(id); } catch (DataIntegrityViolationException ex) { throw new IllegalArgumentException("El producto está relacionado con otros registros"); }
    }
}
