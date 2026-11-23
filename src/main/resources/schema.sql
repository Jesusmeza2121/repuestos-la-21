CREATE TABLE IF NOT EXISTS detalle_servicios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  venta_id BIGINT NOT NULL,
  descripcion VARCHAR(255) NOT NULL,
  cantidad INT NOT NULL DEFAULT 1,
  precio DECIMAL(12,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_detalle_servicios_venta (venta_id)
) ENGINE=InnoDB;
