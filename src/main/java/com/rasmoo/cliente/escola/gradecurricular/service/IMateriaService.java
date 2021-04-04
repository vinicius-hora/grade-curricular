package com.rasmoo.cliente.escola.gradecurricular.service;

import java.util.List;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;

public interface IMateriaService {
	
	public Boolean atualizar(final MateriaDto materia);
	
	public Boolean excluir(final Long id);
	
	public Boolean cadastrar(final MateriaDto materia);
	
	public List<MateriaDto> listar();
	
	public MateriaDto consultar (final Long id);


}
