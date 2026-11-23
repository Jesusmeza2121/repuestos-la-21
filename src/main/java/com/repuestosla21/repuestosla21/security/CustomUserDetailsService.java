package com.repuestosla21.repuestosla21.security;

import com.repuestosla21.repuestosla21.model.dao.UsuarioDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioDAO usuarioDAO;

    public CustomUserDetailsService(UsuarioDAO usuarioDAO) { this.usuarioDAO = usuarioDAO; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioDAO.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
