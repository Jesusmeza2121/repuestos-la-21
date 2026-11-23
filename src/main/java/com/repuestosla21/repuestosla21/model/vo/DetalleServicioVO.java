package com.repuestosla21.repuestosla21.model.vo;

import java.math.BigDecimal;

public record DetalleServicioVO(
        Long id,
        Long ventaId,
        String descripcion,
        int cantidad,
        BigDecimal precio,
        BigDecimal subtotal) {
}
