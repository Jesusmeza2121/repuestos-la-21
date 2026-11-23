package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import com.repuestosla21.repuestosla21.model.dao.ProveedorDAO;
import com.repuestosla21.repuestosla21.model.dao.UsuarioDAO;
import com.repuestosla21.repuestosla21.service.CompraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {
    private final CompraService service; private final ProveedorDAO proveedores; private final ProductoDAO productos; private final UsuarioDAO usuarios;
    public CompraController(CompraService service,ProveedorDAO proveedores,ProductoDAO productos,UsuarioDAO usuarios){this.service=service;this.proveedores=proveedores;this.productos=productos;this.usuarios=usuarios;}
    @GetMapping public String listar(Model model){model.addAttribute("compras",service.listar());return "compras/lista";}
    @GetMapping("/nueva") public String nueva(Model model){model.addAttribute("proveedores",proveedores.findAll(null));model.addAttribute("productos",productos.findAll(null,null,null));return "compras/formulario";}
    @GetMapping("/{id}") public String detalle(@PathVariable Long id,Model model){model.addAttribute("compra",service.obtener(id));model.addAttribute("detalles",service.detalle(id));return "compras/detalle";}
    @PostMapping public String guardar(@RequestParam Long proveedorId,@RequestParam(required=false)String factura,@RequestParam(required=false)List<Long> productoId,@RequestParam(required=false)List<Integer> cantidad,@RequestParam(required=false)List<BigDecimal> precio,@RequestParam(required=false)String observaciones,Principal principal,RedirectAttributes ra){
        try{List<CompraService.Item> items=new ArrayList<>();if(productoId!=null)for(int i=0;i<productoId.size();i++)items.add(new CompraService.Item(productoId.get(i),cantidad.get(i),precio.get(i)));Long id=service.registrar(proveedorId,factura,items,observaciones,usuarios.findByUsername(principal.getName()).orElseThrow().id());ra.addFlashAttribute("exito","Compra registrada correctamente");return "redirect:/compras/"+id;}catch(Exception ex){ra.addFlashAttribute("error",ex.getMessage());return "redirect:/compras/nueva";}
    }
}
