package com.repuestosla21.repuestosla21.model.vo;

import java.time.LocalDateTime;

public record UsuarioVO(Long id, String nombreUsuario, String passwordHash, String nombreCompleto,
                        String rol, boolean activo, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {}
