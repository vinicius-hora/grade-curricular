package com.rasmoo.cliente.escola.gradecurricular.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateriaDto {
	
	
	private Long id;
	
	@NotBlank(message = "Informe o nome da matéria")
	private String nome;
	
	@Min(value = 34, message = "Permitido o mínimo de 34h")
	@Max(value = 120, message = "Permitido o máximo de 120h")
	private int horas;
	
	@NotBlank(message = "Informe o código da matéria")
	private String codigo;

	@Min(value = 1, message = "Permitido o mínimo de 1 vez ao ano")
	@Max(value = 2, message = "Permitido o máximo de  de 2 vezes ao ano")
	private int frequencia;

}
