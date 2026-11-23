package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.security.CustomUserDetails;
import com.repuestosla21.repuestosla21.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service){this.service=service;}
    @GetMapping public String listar(Model model){model.addAttribute("usuarios",service.listar());return "usuarios/lista";}
    @GetMapping("/nuevo") public String nuevo(){return "usuarios/formulario";}
    @PostMapping public String guardar(@RequestParam String nombreUsuario,@RequestParam String password,@RequestParam String nombreCompleto,@RequestParam String rol,RedirectAttributes ra){try{service.crear(nombreUsuario,password,nombreCompleto,rol);ra.addFlashAttribute("exito","Usuario creado");}catch(Exception ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/usuarios";}
    @PostMapping("/{id}/estado") public String estado(@PathVariable Long id,@RequestParam String nombreCompleto,@RequestParam String rol,@RequestParam(defaultValue="false")boolean activo,@AuthenticationPrincipal CustomUserDetails user,RedirectAttributes ra){try{service.actualizar(id,nombreCompleto,rol,activo,user.getUsuario().id());ra.addFlashAttribute("exito","Usuario actualizado");}catch(Exception ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/usuarios";}
    @PostMapping("/{id}/password") public String password(@PathVariable Long id,@RequestParam String password,RedirectAttributes ra){try{service.restablecerPassword(id,password);ra.addFlashAttribute("exito","Contraseña restablecida");}catch(Exception ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/usuarios";}
}
