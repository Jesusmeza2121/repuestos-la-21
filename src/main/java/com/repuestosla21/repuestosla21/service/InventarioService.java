package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.MovimientoInventarioVO;
import com.repuestosla21.repuestosla21.model.vo.ProductoVO;
import com.repuestosla21.repuestosla21.model.dao.MovimientoInventarioDAO;
import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioService {
    private final ProductoDAO productoDAO;
    private final MovimientoInventarioDAO movimientoDAO;

    public InventarioService(ProductoDAO productoDAO,MovimientoInventarioDAO movimientoDAO){this.productoDAO=productoDAO;this.movimientoDAO=movimientoDAO;}

    public List<ProductoVO> listar(){return productoDAO.findAll(null,null,null);}
    public List<MovimientoInventarioVO> movimientos(){return movimientoDAO.findAll();}

    @Transactional
    public void ajustar(Long productoId,int cantidad,String tipo,String motivo,Long usuarioId){
        if(cantidad<=0)throw new IllegalArgumentException("La cantidad debe ser positiva");
        Integer anterior=productoDAO.getStockLocked(productoId);
        if(anterior==null)throw new IllegalArgumentException("Producto no encontrado");
        int nuevo;
        if("Entrada".equals(tipo)){nuevo=anterior+cantidad;}
        else if("Salida".equals(tipo)){nuevo=anterior-cantidad;if(nuevo<0)throw new IllegalArgumentException("La salida no puede dejar stock negativo");}
        else if("Ajuste".equals(tipo)){nuevo=cantidad;cantidad=Math.abs(nuevo-anterior);tipo="Ajuste";} else throw new IllegalArgumentException("Tipo de movimiento inválido");
        if("Ajuste".equals(tipo))productoDAO.updateStockLocked(productoId,nuevo-anterior);else productoDAO.updateStockLocked(productoId,"Entrada".equals(tipo)?cantidad:-cantidad);
        movimientoDAO.create(productoId,usuarioId,tipo,cantidad,anterior,nuevo,"AJUSTE-"+productoId,motivo);
    }
}
