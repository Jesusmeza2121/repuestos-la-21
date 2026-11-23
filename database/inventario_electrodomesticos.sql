CREATE DATABASE IF NOT EXISTS inventario_electrodomesticos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE inventario_electrodomesticos;

CREATE TABLE usuarios (
  id_usuario BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre_usuario VARCHAR(50) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  nombre_completo VARCHAR(120) NOT NULL,
  rol ENUM('ADMIN','EMPLEADO') NOT NULL DEFAULT 'EMPLEADO',
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario), UNIQUE KEY uq_usuarios_nombre_usuario (nombre_usuario)
) ENGINE=InnoDB;

CREATE TABLE categorias (
  id_categoria BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255) NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_categoria), UNIQUE KEY uq_categorias_nombre (nombre)
) ENGINE=InnoDB;

CREATE TABLE proveedores (
  id_proveedor BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  documento VARCHAR(30) NULL,
  telefono VARCHAR(30) NULL,
  correo VARCHAR(120) NULL,
  direccion VARCHAR(255) NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_proveedor), UNIQUE KEY uq_proveedores_documento (documento)
) ENGINE=InnoDB;

CREATE TABLE productos (
  id_producto BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  codigo VARCHAR(50) NOT NULL,
  nombre VARCHAR(150) NOT NULL,
  descripcion TEXT NULL,
  id_categoria BIGINT UNSIGNED NULL,
  id_proveedor BIGINT UNSIGNED NULL,
  precio_compra DECIMAL(12,2) NOT NULL DEFAULT 0,
  precio_venta DECIMAL(12,2) NOT NULL DEFAULT 0,
  stock INT UNSIGNED NOT NULL DEFAULT 0,
  stock_minimo INT UNSIGNED NOT NULL DEFAULT 0,
  unidad VARCHAR(30) NOT NULL DEFAULT 'Unidad',
  activo TINYINT(1) NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_producto), UNIQUE KEY uq_productos_codigo (codigo),
  KEY idx_productos_categoria (id_categoria), KEY idx_productos_proveedor (id_proveedor),
  CONSTRAINT fk_productos_categoria FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_productos_proveedor FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_productos_precios CHECK (precio_compra >= 0 AND precio_venta >= 0)
) ENGINE=InnoDB;

CREATE TABLE compras (
  id_compra BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_proveedor BIGINT UNSIGNED NOT NULL,
  numero_factura VARCHAR(50) NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total DECIMAL(12,2) NOT NULL DEFAULT 0,
  observaciones VARCHAR(500) NULL,
  id_usuario BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (id_compra), UNIQUE KEY uq_compras_factura (numero_factura),
  KEY idx_compras_proveedor (id_proveedor), KEY idx_compras_usuario (id_usuario),
  CONSTRAINT fk_compras_proveedor FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_compras_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE detalle_compras (
  id_detalle_compra BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_compra BIGINT UNSIGNED NOT NULL,
  id_producto BIGINT UNSIGNED NOT NULL,
  cantidad INT UNSIGNED NOT NULL,
  precio_compra DECIMAL(12,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id_detalle_compra),
  UNIQUE KEY uq_detalle_compras_compra_producto (id_compra,id_producto),
  KEY idx_detalle_compras_producto (id_producto),
  CONSTRAINT fk_detalle_compras_compra FOREIGN KEY (id_compra) REFERENCES compras(id_compra) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_detalle_compras_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_detalle_compras_cantidad CHECK (cantidad > 0)
) ENGINE=InnoDB;

CREATE TABLE ventas (
  id_venta BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  cliente_nombre VARCHAR(150) NULL,
  cliente_documento VARCHAR(30) NULL,
  cliente_telefono VARCHAR(30) NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total DECIMAL(12,2) NOT NULL DEFAULT 0,
  metodo_pago VARCHAR(30) NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'COMPLETADA',
  observaciones VARCHAR(500) NULL,
  id_usuario BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (id_venta), KEY idx_ventas_usuario (id_usuario),
  CONSTRAINT fk_ventas_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE detalle_ventas (
  id_detalle_venta BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_venta BIGINT UNSIGNED NOT NULL,
  id_producto BIGINT UNSIGNED NOT NULL,
  cantidad INT UNSIGNED NOT NULL,
  precio_venta DECIMAL(12,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id_detalle_venta),
  UNIQUE KEY uq_detalle_ventas_venta_producto (id_venta,id_producto),
  KEY idx_detalle_ventas_producto (id_producto),
  CONSTRAINT fk_detalle_ventas_venta FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_detalle_ventas_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_detalle_ventas_cantidad CHECK (cantidad > 0)
) ENGINE=InnoDB;

CREATE TABLE devoluciones (
  id_devolucion BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_venta BIGINT UNSIGNED NOT NULL,
  id_producto BIGINT UNSIGNED NOT NULL,
  cantidad INT UNSIGNED NOT NULL,
  motivo VARCHAR(500) NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado VARCHAR(20) NOT NULL DEFAULT 'ACEPTADA',
  id_usuario BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (id_devolucion), KEY idx_devoluciones_venta (id_venta), KEY idx_devoluciones_producto (id_producto),
  CONSTRAINT fk_devoluciones_venta FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_devoluciones_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_devoluciones_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_devoluciones_cantidad CHECK (cantidad > 0)
) ENGINE=InnoDB;

CREATE TABLE movimientos_inventario (
  id_movimiento BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  id_producto BIGINT UNSIGNED NOT NULL,
  id_usuario BIGINT UNSIGNED NOT NULL,
  tipo_movimiento ENUM('Entrada','Salida','Devolucion','Ajuste') NOT NULL,
  cantidad INT NOT NULL,
  stock_anterior INT UNSIGNED NOT NULL,
  stock_nuevo INT UNSIGNED NOT NULL,
  referencia VARCHAR(100) NULL,
  observaciones VARCHAR(500) NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_movimiento), KEY idx_movimientos_producto (id_producto), KEY idx_movimientos_usuario (id_usuario),
  CONSTRAINT uq_movimiento_operacion UNIQUE (tipo_movimiento,referencia,id_producto),
  CONSTRAINT fk_movimientos_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_movimientos_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

INSERT INTO usuarios(nombre_usuario,password_hash,nombre_completo,rol,activo) VALUES
('admin','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','Administrador inicial','ADMIN',1);

INSERT INTO categorias(nombre,descripcion) VALUES
('Repuestos para ventiladores','Repuestos y componentes para ventiladores'),
('Accesorios para ventiladores','Accesorios compatibles con ventiladores'),
('Motores','Motores y componentes de accionamiento'),
('Capacitores','Capacitores electricos'),
('Herramientas','Herramientas de trabajo y mantenimiento'),
('Otros','Otros productos relacionados');
