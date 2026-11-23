package com.repuestosla21.repuestosla21.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CompraVO(Long id, Long proveedorId, String proveedorNombre, String numeroFactura,
                       LocalDateTime fecha, BigDecimal total, String observaciones, Long usuarioId) {}
