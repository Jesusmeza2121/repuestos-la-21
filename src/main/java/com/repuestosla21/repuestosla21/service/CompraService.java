package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.CompraVO;
import com.repuestosla21.repuestosla21.model.vo.DetalleCompraVO;
import com.repuestosla21.repuestosla21.model.dao.CompraDAO;
import com.repuestosla21.repuestosla21.model.dao.DetalleCompraDAO;
import com.repuestosla21.repuestosla21.model.dao.MovimientoInventarioDAO;
import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompraService {
    private final CompraDAO compraDAO;
    private final DetalleCompraDAO detalleDAO;
    private final ProductoDAO productoDAO;
    private final MovimientoInventarioDAO movimientoDAO;

    public CompraService(CompraDAO compraDAO,DetalleCompraDAO detalleDAO,ProductoDAO productoDAO,MovimientoInventarioDAO movimientoDAO){this.compraDAO=compraDAO;this.detalleDAO=detalleDAO;this.productoDAO=productoDAO;this.movimientoDAO=movimientoDAO;}

    public record Item(Long productoId,int cantidad,BigDecimal precio) {}

    @Transactional
    public Long registrar(Long proveedorId,String factura,List<Item> items,String observaciones,Long usuarioId){
        if(items==null||items.isEmpty())throw new IllegalArgumentException("Debe agregar al menos un producto");
        BigDecimal total=BigDecimal.ZERO;
        for(Item item:items){
            if(item.cantidad<=0||item.precio==null||item.precio().signum()<0)throw new IllegalArgumentException("Cantidad y precio inválidos");
            total=total.add(item.precio().multiply(BigDecimal.valueOf(item.cantidad)));
        }
        Long compraId=compraDAO.create(proveedorId,factura,total,observaciones,usuarioId);
        for(Item item:items){
            Integer anterior=productoDAO.getStockLocked(item.productoId());
            if(anterior==null)throw new IllegalArgumentException("Producto no encontrado");
            detalleDAO.create(compraId,item.productoId(),item.cantidad(),item.precio(),item.precio().multiply(BigDecimal.valueOf(item.cantidad())));
            productoDAO.updateStockLocked(item.productoId(),item.cantidad());
            movimientoDAO.create(item.productoId(),usuarioId,"Entrada",item.cantidad(),anterior,anterior+item.cantidad(),"COMPRA-"+compraId,"Compra de mercancía");
        }
        return compraId;
    }

    public List<CompraVO> listar(){return compraDAO.findAll();}
    public CompraVO obtener(Long id){return compraDAO.findById(id);}
    public List<DetalleCompraVO> detalle(Long id){return detalleDAO.findByCompra(id);}
}
