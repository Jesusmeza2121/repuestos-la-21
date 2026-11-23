package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.model.dao.CategoriaDAO;
import com.repuestosla21.repuestosla21.model.dao.CompraDAO;
import com.repuestosla21.repuestosla21.model.dao.MovimientoInventarioDAO;
import com.repuestosla21.repuestosla21.model.dao.ProductoDAO;
import com.repuestosla21.repuestosla21.model.dao.ProveedorDAO;
import com.repuestosla21.repuestosla21.model.dao.VentaDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final ProductoDAO productos; private final CategoriaDAO categorias;
    private final ProveedorDAO proveedores; private final CompraDAO compras;
    private final VentaDAO ventas; private final MovimientoInventarioDAO movimientos;

    public DashboardController(ProductoDAO productos,CategoriaDAO categorias,ProveedorDAO proveedores,CompraDAO compras,VentaDAO ventas,MovimientoInventarioDAO movimientos){this.productos=productos;this.categorias=categorias;this.proveedores=proveedores;this.compras=compras;this.ventas=ventas;this.movimientos=movimientos;}

    @GetMapping({"/","/dashboard"})
    public String dashboard(Model model){
        model.addAttribute("totalProductos",productos.count());
        model.addAttribute("totalCategorias",categorias.count());
        model.addAttribute("totalProveedores",proveedores.count());
        model.addAttribute("stockBajo",productos.stockLowCount());
        model.addAttribute("sinExistencias",productos.outOfStockCount());
        model.addAttribute("totalCompras",compras.totalCompras());
        model.addAttribute("totalVentas",ventas.totalVentas());
        model.addAttribute("movimientos",movimientos.findRecent(8));
        return "dashboard";
    }
}
