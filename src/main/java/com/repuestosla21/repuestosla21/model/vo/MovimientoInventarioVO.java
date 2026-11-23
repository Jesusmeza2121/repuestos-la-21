package com.repuestosla21.repuestosla21.model.vo;

import java.time.LocalDateTime;

public record MovimientoInventarioVO(Long id, Long productoId, String productoNombre, Long usuarioId,
                                     String tipoMovimiento, Integer cantidad, Integer stockAnterior,
                                     Integer stockNuevo, String referencia, String observaciones,
                                     LocalDateTime fecha) {}
