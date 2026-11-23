package com.repuestosla21.repuestosla21.controller;

import com.repuestosla21.repuestosla21.service.ReporteService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    private final ReporteService service;
    public ReporteController(ReporteService service){this.service=service;}

    @GetMapping
    public String listar(@RequestParam(defaultValue="inventario")String tipo,Model model){
        model.addAttribute("tipo",tipo);
        model.addAttribute("inventario",tipo.equals("inventario")?service.inventario():java.util.List.of());
        model.addAttribute("movimientos",tipo.equals("movimientos")?service.movimientos():java.util.List.of());
        return "reportes/lista";
    }

    @GetMapping("/csv")
    public void csv(@RequestParam(defaultValue="inventario")String tipo,HttpServletResponse response) throws IOException {
        response.setContentType("text/csv;charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition","attachment; filename=\"reporte-"+tipo+".csv\"");
        service.csv(response.getWriter(),tipo.equals("movimientos")?service.movimientos():service.inventario());
    }
}
