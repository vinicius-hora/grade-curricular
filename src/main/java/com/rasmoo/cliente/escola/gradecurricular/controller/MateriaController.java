package com.rasmoo.cliente.escola.gradecurricular.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.cliente.escola.gradecurricular.service.IMateriaService;

@RestController
@RequestMapping("/materia")
public class MateriaController {
	
	private static final String DELETE = "DELETE";
	
	private static final String UPDATE = "UPDATE";
	
	private static final String LISTAR = "LISTAR";
	
	@Autowired
	private IMateriaRepository materiaRepository;
	
	@Autowired
	private IMateriaService materiaService;
	
	@GetMapping
	public ResponseEntity<Response<List<MateriaDto>>> listarMaterias(){
		Response<List<MateriaDto>> response = new Response<>();
		response.setData(this.materiaService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.listarMaterias()).withSelfRel());
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Response<MateriaDto>> consultaMateria(@PathVariable Long id){
		Response<MateriaDto> response = new Response<>();
		MateriaDto materia = this.materiaService.consultar(id);
		response.setData(materia);
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.consultaMateria(id)).withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.excluirMateria(id)).withRel(DELETE));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.atualizarMateria(materia)).withRel(UPDATE));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping
	public ResponseEntity<Response<Boolean>> cadstrarMateria(@Valid @RequestBody MateriaDto materia){
		
		Response<Boolean> response = new Response<>();

		response.setData(this.materiaService.cadastrar(materia));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.cadstrarMateria(materia)).withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.listarMaterias()).withRel(LISTAR));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> excluirMateria(@PathVariable Long id) {
		
		return ResponseEntity.status(HttpStatus.OK).body(this.materiaService.excluir(id));
			
	}
	
	
	@PutMapping
	public ResponseEntity<Response<Boolean>> atualizarMateria(@Valid @RequestBody MateriaDto materia) {
		Response<Boolean> response = new Response<>();
		response.setData(this.materiaService.atualizar(materia));
		response.setStatusCode(HttpStatus.OK.value());
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.atualizarMateria(materia)).withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.listarMaterias()).withRel(LISTAR));
			
		return ResponseEntity.status(HttpStatus.OK).body(response);
			
	}
	
	@GetMapping("/horario-minimo/{horaMinima}")
	public ResponseEntity<Response<List<MateriaDto>>> consultaPorHora(@PathVariable int horaMinima){
		Response<List<MateriaDto>> response = new Response<>();
		List<MateriaDto> materia = this.materiaService.consultaPorHora(horaMinima);
		response.setData(materia);
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
				.consultaPorHora(horaMinima)).withSelfRel());
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	

}
