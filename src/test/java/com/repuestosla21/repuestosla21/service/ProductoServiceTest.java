package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.ProductoVO;
import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {
    @Mock ProductoDAO dao;
    @Mock JdbcTemplate jdbc;
    @InjectMocks ProductoService service;

    @Test void rechazaCodigoDuplicado(){
        ProductoVO producto=new ProductoVO(null,"MOT-1","Motor",null,1L,null,1L,null,BigDecimal.TEN,BigDecimal.TEN,5,2,"Unidad",true,null,null);
        when(dao.existsCode("MOT-1",null)).thenReturn(true);
        assertThrows(IllegalArgumentException.class,()->service.guardar(producto));
    }

    @Test void registraProductoValido(){
        ProductoVO producto=new ProductoVO(null,"CAP-1","Capacitor",null,1L,null,1L,null,BigDecimal.TEN,BigDecimal.valueOf(15),5,2,"Unidad",true,null,null);
        when(dao.existsCode("CAP-1",null)).thenReturn(false);
        service.guardar(producto);
        verify(dao).create(producto);
    }
}
