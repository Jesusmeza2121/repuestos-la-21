package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.UsuarioVO;
import com.repuestosla21.repuestosla21.model.dao.UsuarioDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioDAO dao;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioDAO dao, PasswordEncoder encoder) { this.dao=dao; this.encoder=encoder; }

    public List<UsuarioVO> listar() { return dao.findAll(); }
    public UsuarioVO obtener(Long id) { return dao.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")); }

    public void crear(String username,String password,String name,String role) {
        if (username==null||username.isBlank()) throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        if (password==null||password.length()<8) throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        if (dao.findByUsername(username).isPresent()) throw new IllegalArgumentException("El nombre de usuario ya existe");
        dao.create(username,encoder.encode(password),name,role);
    }

    public void actualizar(Long id,String name,String role,boolean active,Long currentUserId) {
        UsuarioVO u=obtener(id);
        if (id.equals(currentUserId) && u.rol().equals("ADMIN") && (!active || !role.equals("ADMIN"))) throw new IllegalArgumentException("No puede quitarse su propio acceso de administrador");
        if ((u.rol().equals("ADMIN")&&u.activo()) && (!active || !role.equals("ADMIN"))) {
            if (dao.countActiveAdministrators()<=1) throw new IllegalArgumentException("Debe existir al menos un administrador activo");
        }
        dao.update(id,name,role,active);
    }

    public void restablecerPassword(Long id,String password) {
        if (password==null||password.length()<8) throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        dao.resetPassword(id,encoder.encode(password));
    }
}
