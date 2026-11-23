package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.UsuarioVO;
import com.repuestosla21.repuestosla21.model.dao.UsuarioDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock UsuarioDAO dao;
    @Mock PasswordEncoder encoder;
    @InjectMocks UsuarioService service;

    @Test void noPermiteDejarSinAdministrador(){
        UsuarioVO admin=new UsuarioVO(1L,"admin","hash","Administrador","ADMIN",true,LocalDateTime.now(),LocalDateTime.now());
        when(dao.findById(1L)).thenReturn(Optional.of(admin));
        assertThrows(IllegalArgumentException.class,()->service.actualizar(1L,"Administrador","EMPLEADO",false,1L));
    }
}
