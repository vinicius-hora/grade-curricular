package com.rasmoo.cliente.escola.gradecurricular.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CursoDto extends RepresentationModel<CursoDto> {
	

	private Long id;
	
	private String nome;
	
	private String codigo;
	
	private List<MateriaEntity> materias = new ArrayList<>();
	

}
