package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import com.repuestosla21.repuestosla21.model.dao.UsuarioDAO;
import com.repuestosla21.repuestosla21.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    private final VentaService service;private final ProductoDAO productos;private final UsuarioDAO usuarios;
    public VentaController(VentaService service,ProductoDAO productos,UsuarioDAO usuarios){this.service=service;this.productos=productos;this.usuarios=usuarios;}
    @GetMapping public String listar(Model model){model.addAttribute("ventas",service.listar());return "ventas/lista";}
    @GetMapping("/nueva") public String nueva(Model model){model.addAttribute("productos",productos.findAll(null,null,null));return "ventas/formulario";}
    @GetMapping("/{id}") public String detalle(@PathVariable Long id,Model model){model.addAttribute("venta",service.obtener(id));model.addAttribute("detalles",service.detalle(id));model.addAttribute("servicios",service.detalleServicios(id));return "ventas/detalle";}
    @PostMapping
    public String guardar(
            @RequestParam(required=false) String clienteNombre,
            @RequestParam(required=false) String clienteDocumento,
            @RequestParam(required=false) String clienteTelefono,
            @RequestParam(required=false) List<Long> productoId,
            @RequestParam(required=false) List<Integer> cantidad,
            @RequestParam(required=false) List<BigDecimal> precio,
            @RequestParam(required=false) List<String> servicioDescripcion,
            @RequestParam(required=false) List<BigDecimal> servicioPrecio,
            @RequestParam String metodoPago,
            @RequestParam(required=false) String observaciones,
            Principal principal,
            RedirectAttributes ra) {
        try {
            List<VentaService.Item> items = new ArrayList<>();
            if (productoId != null) {
                for (int i = 0; i < productoId.size(); i++) {
                    Long idProducto = at(productoId, i);
                    if (idProducto == null) {
                        continue;
                    }
                    Integer cantidadProducto = at(cantidad, i);
                    BigDecimal precioProducto = at(precio, i);
                    if (cantidadProducto == null || precioProducto == null) {
                        throw new IllegalArgumentException("Complete cantidad y precio para cada producto");
                    }
                    items.add(new VentaService.Item(idProducto, cantidadProducto, precioProducto));
                }
            }

            List<VentaService.ServicioItem> servicios = new ArrayList<>();
            if (servicioDescripcion != null) {
                for (int i = 0; i < servicioDescripcion.size(); i++) {
                    String descripcion = at(servicioDescripcion, i);
                    BigDecimal precioItem = at(servicioPrecio, i);
                    if (descripcion == null || descripcion.isBlank()) {
                        if (precioItem != null) {
                            throw new IllegalArgumentException("Escriba una descripción para cada servicio");
                        }
                        continue;
                    }
                    servicios.add(new VentaService.ServicioItem(descripcion, precioItem));
                }
            }

            Long ventaId = service.registrar(
                    clienteNombre, clienteDocumento, clienteTelefono, items, servicios, metodoPago,
                    observaciones, usuarios.findByUsername(principal.getName()).orElseThrow().id());
            ra.addFlashAttribute("exito", "Venta registrada correctamente");
            return "redirect:/ventas/" + ventaId;
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/ventas/nueva";
        }
    }

    private static <T> T at(List<T> values, int index) {
        return values != null && index < values.size() ? values.get(index) : null;
    }
}
