package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.model.vo.ProveedorVO;
import com.repuestosla21.repuestosla21.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {
    private final ProveedorService service;
    public ProveedorController(ProveedorService service){this.service=service;}
    @GetMapping public String listar(@RequestParam(required=false)String buscar,Model model){model.addAttribute("proveedores",service.listar(buscar));model.addAttribute("buscar",buscar);return "proveedores/lista";}
    @GetMapping("/nuevo") public String nuevo(Model model){model.addAttribute("proveedor",new ProveedorVO(null,"","","","","",true));return "proveedores/formulario";}
    @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model model){model.addAttribute("proveedor",service.obtener(id));return "proveedores/formulario";}
    @PostMapping public String guardar(@RequestParam(required=false)Long id,@RequestParam String nombre,@RequestParam(required=false)String documento,@RequestParam(required=false)String telefono,@RequestParam(required=false)String correo,@RequestParam(required=false)String direccion,@RequestParam(defaultValue="true")boolean activo,RedirectAttributes ra){try{service.guardar(new ProveedorVO(id,nombre,documento,telefono,correo,direccion,activo));ra.addFlashAttribute("exito","Proveedor guardado");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/proveedores";}
    @PostMapping("/{id}/eliminar") public String eliminar(@PathVariable Long id,RedirectAttributes ra){try{service.eliminar(id);ra.addFlashAttribute("exito","Proveedor eliminado");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/proveedores";}
}
