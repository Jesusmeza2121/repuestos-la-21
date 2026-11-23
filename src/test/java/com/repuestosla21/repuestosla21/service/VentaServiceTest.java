package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.dao.DetalleVentaDAO;
import com.repuestosla21.repuestosla21.model.dao.DetalleServicioDAO;
import com.repuestosla21.repuestosla21.model.dao.MovimientoInventarioDAO;
import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import com.repuestosla21.repuestosla21.model.dao.VentaDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {
    @Mock VentaDAO ventaDAO;
    @Mock DetalleVentaDAO detalleDAO;
    @Mock DetalleServicioDAO detalleServicioDAO;
    @Mock ProductoDAO productoDAO;
    @Mock MovimientoInventarioDAO movimientoDAO;
    @InjectMocks VentaService service;

    @Test void rechazaCantidadSuperiorAlStock(){
        when(productoDAO.getStockLocked(1L)).thenReturn(2);
        VentaService.Item item=new VentaService.Item(1L,3,BigDecimal.valueOf(1000));
        assertThrows(IllegalArgumentException.class,()->service.registrar("Cliente",null,null,List.of(item),"Efectivo",null,1L));
    }

    @Test void registraUnaVentaCompuestaSoloPorServiciosSinMoverInventario() {
        when(ventaDAO.create("Cliente", null, null, BigDecimal.valueOf(15000), "Efectivo", null, 1L))
                .thenReturn(25L);

        Long ventaId = service.registrar(
                "Cliente", null, null, List.of(),
                List.of(new VentaService.ServicioItem("Reparación de ventilador", BigDecimal.valueOf(15000))),
                "Efectivo", null, 1L);

        assertEquals(25L, ventaId);
        verify(detalleServicioDAO).create(
                25L, "Reparación de ventilador", 1, BigDecimal.valueOf(15000), BigDecimal.valueOf(15000));
        verifyNoInteractions(productoDAO, movimientoDAO);
    }

    @Test void sumaServiciosYProductosEnLaMismaVenta() {
        when(productoDAO.getStockLocked(5L)).thenReturn(4, 4);
        when(ventaDAO.create("Cliente", null, null, BigDecimal.valueOf(40), "Efectivo", null, 1L))
                .thenReturn(26L);

        Long ventaId = service.registrar(
                "Cliente", null, null,
                List.of(new VentaService.Item(5L, 1, BigDecimal.valueOf(25))),
                List.of(new VentaService.ServicioItem("Taladrado", BigDecimal.valueOf(15))),
                "Efectivo", null, 1L);

        assertEquals(26L, ventaId);
        verify(detalleDAO).create(26L, 5L, 1, BigDecimal.valueOf(25), BigDecimal.valueOf(25));
        verify(detalleServicioDAO).create(26L, "Taladrado", 1, BigDecimal.valueOf(15), BigDecimal.valueOf(15));
        verify(productoDAO).updateStockLocked(5L, -1);
    }
}
