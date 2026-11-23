package com.repuestosla21.repuestosla21.security;

import com.repuestosla21.repuestosla21.model.vo.UsuarioVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final UsuarioVO usuario;

    public CustomUserDetails(UsuarioVO usuario) { this.usuario = usuario; }

    public UsuarioVO getUsuario() { return usuario; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.rol()));
    }
    @Override public String getPassword() { return usuario.passwordHash(); }
    @Override public String getUsername() { return usuario.nombreUsuario(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return usuario.activo(); }
}
