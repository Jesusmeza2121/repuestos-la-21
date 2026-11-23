package com.repuestosla21.repuestosla21.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VentaVO(Long id, String clienteNombre, String clienteDocumento, String clienteTelefono,
                      LocalDateTime fecha, BigDecimal total, String metodoPago, String estado,
                      String observaciones, Long usuarioId) {}
