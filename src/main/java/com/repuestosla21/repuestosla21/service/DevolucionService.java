package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.DevolucionVO;
import com.repuestosla21.repuestosla21.model.dao.DevolucionDAO;
import com.repuestosla21.repuestosla21.model.dao.DetalleVentaDAO;
import com.repuestosla21.repuestosla21.model.dao.MovimientoInventarioDAO;
import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevolucionService {
    private final DevolucionDAO devolucionDAO;
    private final DetalleVentaDAO detalleVentaDAO;
    private final ProductoDAO productoDAO;
    private final MovimientoInventarioDAO movimientoDAO;

    public DevolucionService(DevolucionDAO devolucionDAO,DetalleVentaDAO detalleVentaDAO,ProductoDAO productoDAO,MovimientoInventarioDAO movimientoDAO){this.devolucionDAO=devolucionDAO;this.detalleVentaDAO=detalleVentaDAO;this.productoDAO=productoDAO;this.movimientoDAO=movimientoDAO;}

    @Transactional
    public void registrar(Long ventaId,Long productoId,int cantidad,String motivo,Long usuarioId){
        if(cantidad<=0)throw new IllegalArgumentException("La cantidad debe ser positiva");
        if(motivo==null||motivo.isBlank())throw new IllegalArgumentException("Debe indicar el motivo");
        Integer vendido=detalleVentaDAO.findByVenta(ventaId).stream().filter(d->d.productoId().equals(productoId)).map(d->d.cantidad()).findFirst().orElse(null);
        if(vendido==null)throw new IllegalArgumentException("El producto no corresponde a la venta seleccionada");
        int disponible=vendido-devolucionDAO.cantidadDevuelta(ventaId,productoId);
        if(cantidad>disponible)throw new IllegalArgumentException("La cantidad supera lo vendible para devolución");
        Integer anterior=productoDAO.getStockLocked(productoId);if(anterior==null)throw new IllegalArgumentException("Producto no encontrado");
        devolucionDAO.create(ventaId,productoId,cantidad,motivo,usuarioId);
        productoDAO.updateStockLocked(productoId,cantidad);
        movimientoDAO.create(productoId,usuarioId,"Devolucion",cantidad,anterior,anterior+cantidad,"DEVOLUCION-"+ventaId,motivo);
    }
    public List<DevolucionVO> listar(){return devolucionDAO.findAll();}
}
