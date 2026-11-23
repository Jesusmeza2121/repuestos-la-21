package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.VentaVO;
import com.repuestosla21.repuestosla21.model.vo.DetalleVentaVO;
import com.repuestosla21.repuestosla21.model.vo.DetalleServicioVO;
import com.repuestosla21.repuestosla21.model.dao.DetalleVentaDAO;
import com.repuestosla21.repuestosla21.model.dao.DetalleServicioDAO;
import com.repuestosla21.repuestosla21.model.dao.MovimientoInventarioDAO;
import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import com.repuestosla21.repuestosla21.model.dao.VentaDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {
    private final VentaDAO ventaDAO; private final DetalleVentaDAO detalleDAO;
    private final DetalleServicioDAO detalleServicioDAO;
    private final ProductoDAO productoDAO; private final MovimientoInventarioDAO movimientoDAO;

    public VentaService(VentaDAO ventaDAO,DetalleVentaDAO detalleDAO,DetalleServicioDAO detalleServicioDAO,ProductoDAO productoDAO,MovimientoInventarioDAO movimientoDAO){this.ventaDAO=ventaDAO;this.detalleDAO=detalleDAO;this.detalleServicioDAO=detalleServicioDAO;this.productoDAO=productoDAO;this.movimientoDAO=movimientoDAO;}
    public record Item(Long productoId,int cantidad,BigDecimal precio){}
    public record ServicioItem(String descripcion,BigDecimal precio){}

    @Transactional
    public Long registrar(String nombre,String documento,String telefono,List<Item> items,String metodo,String observaciones,Long usuarioId){
        return registrar(nombre, documento, telefono, items, List.of(), metodo, observaciones, usuarioId);
    }

    @Transactional
    public Long registrar(String nombre,String documento,String telefono,List<Item> items,List<ServicioItem> servicios,String metodo,String observaciones,Long usuarioId){
        boolean hayProductos = items != null && !items.isEmpty();
        boolean hayServicios = servicios != null && !servicios.isEmpty();
        if(!hayProductos && !hayServicios)throw new IllegalArgumentException("Agregue al menos un producto o servicio");
        BigDecimal total=BigDecimal.ZERO;
        if (items != null) for(Item item:items){
            if(item.cantidad<=0||item.precio()==null||item.precio().signum()<0)throw new IllegalArgumentException("Cantidad o precio inválido");
            Integer stock=productoDAO.getStockLocked(item.productoId());
            if(stock==null)throw new IllegalArgumentException("Producto no encontrado");
            if(item.cantidad>stock)throw new IllegalArgumentException("Existencias insuficientes para el producto seleccionado");
            total=total.add(item.precio().multiply(BigDecimal.valueOf(item.cantidad)));
        }
        if (servicios != null) for (ServicioItem servicio : servicios) {
            if (servicio.descripcion() == null || servicio.descripcion().isBlank() || servicio.descripcion().length() > 255) {
                throw new IllegalArgumentException("La descripción del servicio es obligatoria y no debe superar 255 caracteres");
            }
            if (servicio.precio() == null || servicio.precio().signum() < 0) {
                throw new IllegalArgumentException("El precio del servicio no es válido");
            }
            total = total.add(servicio.precio());
        }
        Long ventaId=ventaDAO.create(nombre,documento,telefono,total,metodo,observaciones,usuarioId);
        if (items != null) for(Item item:items){
            Integer anterior=productoDAO.getStockLocked(item.productoId());
            detalleDAO.create(ventaId,item.productoId(),item.cantidad(),item.precio(),item.precio().multiply(BigDecimal.valueOf(item.cantidad())));
            productoDAO.updateStockLocked(item.productoId(),-item.cantidad());
            movimientoDAO.create(item.productoId(),usuarioId,"Salida",item.cantidad(),anterior,anterior-item.cantidad(),"VENTA-"+ventaId,"Venta registrada");
        }
        if (servicios != null) for (ServicioItem servicio : servicios) {
            detalleServicioDAO.create(ventaId, servicio.descripcion().trim(), 1, servicio.precio(), servicio.precio());
        }
        return ventaId;
    }
    public List<VentaVO> listar(){return ventaDAO.findAll();}
    public VentaVO obtener(Long id){return ventaDAO.findById(id);}
    public List<DetalleVentaVO> detalle(Long id){return detalleDAO.findByVenta(id);}
    public List<DetalleServicioVO> detalleServicios(Long id){return detalleServicioDAO.findByVenta(id);}
}
