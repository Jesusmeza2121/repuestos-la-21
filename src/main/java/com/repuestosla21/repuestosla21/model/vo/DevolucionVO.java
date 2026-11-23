package com.repuestosla21.repuestosla21.model.vo;

import java.time.LocalDateTime;

public record DevolucionVO(Long id, Long ventaId, Long productoId, String productoNombre,
                           Integer cantidad, String motivo, LocalDateTime fecha, String estado, Long usuarioId) {}
