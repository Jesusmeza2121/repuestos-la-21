package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.model.vo.CategoriaVO;
import com.repuestosla21.repuestosla21.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service){this.service=service;}
    @GetMapping public String listar(@RequestParam(required=false)String buscar,Model model){model.addAttribute("categorias",service.listar(buscar));model.addAttribute("buscar",buscar);return "categorias/lista";}
    @GetMapping("/nueva") public String nueva(Model model){model.addAttribute("categoria",new CategoriaVO(null,"","",true,0));return "categorias/formulario";}
    @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model model){model.addAttribute("categoria",service.obtener(id));return "categorias/formulario";}
    @PostMapping public String guardar(@RequestParam(required=false)Long id,@RequestParam String nombre,@RequestParam(required=false)String descripcion,@RequestParam(defaultValue="true")boolean activo,RedirectAttributes ra){try{service.guardar(new CategoriaVO(id,nombre,descripcion,activo,0));ra.addFlashAttribute("exito","Categoría guardada");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/categorias";}
    @PostMapping("/{id}/eliminar") public String eliminar(@PathVariable Long id,RedirectAttributes ra){try{service.eliminar(id);ra.addFlashAttribute("exito","Categoría eliminada");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/categorias";}
}
