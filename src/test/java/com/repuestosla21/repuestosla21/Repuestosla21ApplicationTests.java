package com.repuestosla21.repuestosla21;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class Repuestosla21ApplicationTests {
	@Autowired JdbcTemplate jdbc;

	@Test
	void contextLoads() {
	}

	@Test
	void initializesServiceDetailsTable() {
		jdbc.queryForList("SELECT venta_id,descripcion,cantidad,precio,subtotal FROM detalle_servicios LIMIT 0");
	}

}
