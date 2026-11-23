package com.repuestosla21.repuestosla21.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoVO(Long id, String codigo, String nombre, String descripcion, Long categoriaId,
                        String categoriaNombre, Long proveedorId, String proveedorNombre, BigDecimal precioCompra,
                        BigDecimal precioVenta, Integer stock, Integer stockMinimo, String unidad,
                        boolean activo, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {}
