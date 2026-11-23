package com.repuestosla21.repuestosla21.model.vo;

import java.math.BigDecimal;

public record DetalleCompraVO(Long id, Long compraId, Long productoId, String productoNombre,
                              Integer cantidad, BigDecimal precioCompra, BigDecimal subtotal) {}
