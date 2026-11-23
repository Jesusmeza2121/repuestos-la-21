package com.repuestosla21.repuestosla21.service;

import com.repuestosla21.repuestosla21.model.vo.CategoriaVO;
import com.repuestosla21.repuestosla21.model.dao.CategoriaDAO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaDAO dao;
    public CategoriaService(CategoriaDAO dao){this.dao=dao;}
    public List<CategoriaVO> listar(String search){return dao.findAll(search);}
    public CategoriaVO obtener(Long id){return dao.findById(id);}
    public void guardar(CategoriaVO c){
        if(c.nombre()==null||c.nombre().isBlank())throw new IllegalArgumentException("El nombre es obligatorio");
        if(c.id()==null)dao.create(c.nombre(),c.descripcion());else dao.update(c.id(),c.nombre(),c.descripcion(),c.activo());
    }
    public void eliminar(Long id){
        CategoriaVO c=dao.findById(id);
        if(c.cantidadProductos()>0)throw new IllegalArgumentException("No se puede eliminar una categoría con productos asociados");
        try{dao.delete(id);}catch(DataIntegrityViolationException ex){throw new IllegalArgumentException("La categoría está relacionada con otros registros");}
    }
}
