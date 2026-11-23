package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.model.dao.UsuarioDAO;
import com.repuestosla21.repuestosla21.service.InventarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/inventario")
public class InventarioController {
    private final InventarioService service;private final UsuarioDAO usuarios;
    public InventarioController(InventarioService service,UsuarioDAO usuarios){this.service=service;this.usuarios=usuarios;}
    @GetMapping public String inventario(Model model){model.addAttribute("productos",service.listar());model.addAttribute("movimientos",service.movimientos());return "inventario/lista";}
    @PostMapping("/ajuste") public String ajuste(@RequestParam Long productoId,@RequestParam int cantidad,@RequestParam String tipo,@RequestParam String motivo,Principal principal,RedirectAttributes ra){try{service.ajustar(productoId,cantidad,tipo,motivo,usuarios.findByUsername(principal.getName()).orElseThrow().id());ra.addFlashAttribute("exito","Movimiento registrado");}catch(Exception ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/inventario";}
}
