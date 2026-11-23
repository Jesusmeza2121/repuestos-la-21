package com.repuestosla21.repuestosla21.model.dao;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UsuarioDAOTest {
    @Test
    void createStoresNameUsernameAndPasswordInTheirMatchingColumns() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        UsuarioDAO dao = new UsuarioDAO(jdbc);

        dao.create("nuevo.usuario", "encoded-password", "Nombre completo", "EMPLEADO");

        verify(jdbc).update(
                "INSERT INTO usuarios(nombre,usuario,password,rol,estado) VALUES(?,?,?,?,1)",
                "Nombre completo", "nuevo.usuario", "encoded-password", "EMPLEADO");
    }
}
