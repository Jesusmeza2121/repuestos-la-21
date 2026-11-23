package com.repuestosla21.repuestosla21.model.vo;

import java.math.BigDecimal;

public record DetalleVentaVO(Long id, Long ventaId, Long productoId, String productoNombre,
                             Integer cantidad, BigDecimal precioVenta, BigDecimal subtotal) {}
