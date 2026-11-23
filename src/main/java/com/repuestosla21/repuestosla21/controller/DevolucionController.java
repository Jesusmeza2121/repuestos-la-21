package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.model.dao.UsuarioDAO;
import com.repuestosla21.repuestosla21.model.dao.VentaDAO;
import com.repuestosla21.repuestosla21.model.dao.DetalleVentaDAO;
import com.repuestosla21.repuestosla21.service.DevolucionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/devoluciones")
public class DevolucionController {
    private final DevolucionService service;private final VentaDAO ventas;private final DetalleVentaDAO detalles;private final UsuarioDAO usuarios;
    public DevolucionController(DevolucionService service,VentaDAO ventas,DetalleVentaDAO detalles,UsuarioDAO usuarios){this.service=service;this.ventas=ventas;this.detalles=detalles;this.usuarios=usuarios;}
    @GetMapping public String listar(Model model){model.addAttribute("devoluciones",service.listar());model.addAttribute("ventas",ventas.findAll());return "devoluciones/lista";}
    @GetMapping("/nueva") public String nueva(Model model){model.addAttribute("ventas",ventas.findAll());return "devoluciones/formulario";}
    @PostMapping public String guardar(@RequestParam Long ventaId,@RequestParam Long productoId,@RequestParam int cantidad,@RequestParam String motivo,Principal principal,RedirectAttributes ra){try{service.registrar(ventaId,productoId,cantidad,motivo,usuarios.findByUsername(principal.getName()).orElseThrow().id());ra.addFlashAttribute("exito","Devolución registrada");}catch(Exception ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/devoluciones";}
    @GetMapping("/venta/{id}/productos") @ResponseBody public Object productosVenta(@PathVariable Long id){return detalles.findByVenta(id);}
}
