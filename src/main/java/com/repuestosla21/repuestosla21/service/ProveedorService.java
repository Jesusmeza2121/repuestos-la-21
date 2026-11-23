package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.ProveedorVO;
import com.repuestosla21.repuestosla21.model.dao.ProveedorDAO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {
    private final ProveedorDAO dao;
    public ProveedorService(ProveedorDAO dao){this.dao=dao;}
    public List<ProveedorVO> listar(String search){return dao.findAll(search);}
    public ProveedorVO obtener(Long id){return dao.findById(id);}
    public void guardar(ProveedorVO p){
        if(p.nombre()==null||p.nombre().isBlank())throw new IllegalArgumentException("El nombre es obligatorio");
        if(p.id()==null)dao.create(p);else dao.update(p);
    }
    public void eliminar(Long id){
        try{dao.delete(id);}catch(DataIntegrityViolationException ex){throw new IllegalArgumentException("El proveedor tiene operaciones históricas y no puede eliminarse");}
    }
}
