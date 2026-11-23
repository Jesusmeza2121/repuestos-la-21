package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.model.vo.ProductoVO;
import com.repuestosla21.repuestosla21.model.dao.CategoriaDAO;
import com.repuestosla21.repuestosla21.model.dao.ProveedorDAO;
import com.repuestosla21.repuestosla21.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService service; private final CategoriaDAO categorias; private final ProveedorDAO proveedores;
    public ProductoController(ProductoService service,CategoriaDAO categorias,ProveedorDAO proveedores){this.service=service;this.categorias=categorias;this.proveedores=proveedores;}

    @GetMapping
    public String listar(@RequestParam(required=false)String buscar,@RequestParam(required=false)Long categoriaId,@RequestParam(required=false)Long proveedorId,Model model){
        model.addAttribute("productos",service.buscar(buscar,categoriaId,proveedorId));model.addAttribute("categorias",categorias.findAll(null));model.addAttribute("proveedores",proveedores.findAll(null));model.addAttribute("buscar",buscar);return "productos/lista";
    }
    @GetMapping("/nuevo") public String nuevo(Model model){model.addAttribute("producto",new ProductoVO(null,"","",null,null,"",null,"",java.math.BigDecimal.ZERO,java.math.BigDecimal.ZERO,0,0,"Unidad",true,null,null));model.addAttribute("categorias",categorias.findAll(null));model.addAttribute("proveedores",proveedores.findAll(null));return "productos/formulario";}
    @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model model){model.addAttribute("producto",service.obtener(id));model.addAttribute("categorias",categorias.findAll(null));model.addAttribute("proveedores",proveedores.findAll(null));return "productos/formulario";}
    @PostMapping
    public String guardar(@RequestParam(required=false)Long id,@RequestParam String codigo,@RequestParam String nombre,@RequestParam(required=false)String descripcion,@RequestParam(required=false)Long categoriaId,@RequestParam(required=false)Long proveedorId,@RequestParam java.math.BigDecimal precioCompra,@RequestParam java.math.BigDecimal precioVenta,@RequestParam Integer stock,@RequestParam Integer stockMinimo,@RequestParam String unidad,@RequestParam(defaultValue="true")boolean activo,RedirectAttributes ra){
        try{service.guardar(new ProductoVO(id,codigo,nombre,descripcion,categoriaId,null,proveedorId,null,precioCompra,precioVenta,stock,stockMinimo,unidad,activo,null,null));ra.addFlashAttribute("exito","Producto guardado correctamente");}
        catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}
        return "redirect:/productos";
    }
    @PostMapping("/{id}/eliminar") public String eliminar(@PathVariable Long id,RedirectAttributes ra){try{service.eliminar(id);ra.addFlashAttribute("exito","Producto eliminado");}catch(IllegalArgumentException ex){ra.addFlashAttribute("error",ex.getMessage());}return "redirect:/productos";}
}
