package com.rasmoo.cliente.escola.gradecurricular.controller.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.cliente.escola.gradecurricular.service.IMateriaService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
public class MateriaControllerIntegratedTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private IMateriaRepository materiaRepository;

	//before e after não podem ser staticos somente os all
	@BeforeEach
	public  void init() {
		this.montarBaseDeDados();

		

	}
	
	@AfterEach
	public  void finish() {
		this.materiaRepository.deleteAll();


	}
	
	private void montarBaseDeDados() {
		MateriaEntity m1 = new MateriaEntity();
		MateriaEntity m2 = new MateriaEntity();
		m2.setCodigo("ILT");
		m2.setFrequencia(2);
		m2.setHoras(64);
		m2.setNome("INTRODUCAO A LINGUAGEM DE TESTES");
		
		m1.setCodigo("ILP");
		m1.setFrequencia(2);
		m1.setHoras(80);
		m1.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");
		
		this.materiaRepository.saveAll(Arrays.asList(m1,m2));
	}

	@Test
	public void testListarMaterias() {
		

		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
				});
		assertNotNull(materias.getBody().getData());
		assertEquals(2,materias.getBody().getData().size());
		assertEquals(200, materias.getBody().getStatusCode());
	}

	@Test
	public void testListarMateriasPorHoraMinima() {
		
		ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia/horario-minimo/70", HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
				});
		assertNotNull(materias.getBody().getData());
		assertEquals(1,materias.getBody().getData().size());
		assertEquals(200, materias.getBody().getStatusCode());
	}
	
	@Test
	public void testConsultarMateriaPorId() {
		List<MateriaEntity> materiasList = this.materiaRepository.findAll();
		Long id = materiasList.get(0).getId();

		ResponseEntity<Response<MateriaDto>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia/"+id, HttpMethod.GET, null,
				new ParameterizedTypeReference<Response<MateriaDto>>() {
				});
		assertNotNull(materias.getBody().getData());
		assertEquals(id,materias.getBody().getData().getId());
		assertEquals(80, materias.getBody().getData().getHoras());
		assertEquals("ILP", materias.getBody().getData().getCodigo());
		assertEquals(200, materias.getBody().getStatusCode());
	}
	
	@Test
	public void testAtualizarMateria() {
		List<MateriaEntity> materiasList = this.materiaRepository.findAll();
		MateriaEntity materia = materiasList.get(0);
		materia.setNome("Teste Atualizar");
		HttpEntity<MateriaEntity> request = new HttpEntity<>(materia);

		ResponseEntity<Response<Boolean>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		MateriaEntity materiaAtualizada = this.materiaRepository.findById(materia.getId()).get();
		assertTrue(materias.getBody().getData());
		assertEquals("Teste Atualizar", materiaAtualizada.getNome());
		assertEquals(201, materias.getBody().getStatusCode());
	}
	
	@Test
	public void testCadastrarMateria() {
		
		MateriaEntity m3 = new MateriaEntity();
		m3.setCodigo("CALC1");
		m3.setFrequencia(2);
		m3.setHoras(100);
		m3.setNome("CALCULO 1");
		
		HttpEntity<MateriaEntity> request = new HttpEntity<>(m3);
		
		List<MateriaEntity> materiasList = this.materiaRepository.findAll();
		MateriaEntity materia = materiasList.get(materiasList.size() - 1);

		ResponseEntity<Response<Boolean>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia/", HttpMethod.POST, request,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		List<MateriaEntity> materiaAtualizada = this.materiaRepository.findAll();
		assertTrue(materias.getBody().getData());
		assertEquals(3, materiaAtualizada.size());
		assertEquals(201, materias.getBody().getStatusCode());
	}
	
	@Test
	public void testExcluirMateriaPorId() {
		List<MateriaEntity> materiasList = this.materiaRepository.findAll();
		Long id = materiasList.get(0).getId();

		ResponseEntity<Response<Boolean>> materias = restTemplate.exchange(
				"http://localhost:" + this.port + "/materia/"+id, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<Response<Boolean>>() {
				});
		
		List<MateriaEntity> materiaAtualizada = this.materiaRepository.findAll();
		
		assertTrue(materias.getBody().getData());
		assertEquals(2, materiaAtualizada.size());
		assertEquals(200, materias.getBody().getStatusCode());
	}

}
