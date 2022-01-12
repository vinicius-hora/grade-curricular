package com.rasmoo.cliente.escola.gradecurricular.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.rasmoo.cliente.escola.gradecurricular.constante.Mensagens;
import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.exception.MateriaException;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;
import com.rasmoo.cliente.escola.gradecurricular.service.MateriaService;

@ExtendWith(MockitoExtension.class)

@RunWith(JUnitPlatform.class)
public class MateriaServiceUnitTest {
	
	@Mock
	private IMateriaRepository materiaRepository;
	
	@InjectMocks
	private MateriaService materiaService;
	
	private static MateriaEntity materiaEntity;
	
	@BeforeAll
	public static void init() {

		materiaEntity = new MateriaEntity();
		materiaEntity.setId(1L);
		materiaEntity.setCodigo("ILP");
		materiaEntity.setFrequencia(1);
		materiaEntity.setHoras(64);
		materiaEntity.setNome("INTRODUCAO OO");

	}
	
	@Test
	public void testListarSucesso() {
		List<MateriaEntity> listMateria = new ArrayList<>();
		
		listMateria.add(materiaEntity);
		
		Mockito.when(this.materiaRepository.findAll()).thenReturn(listMateria);
		
		List<MateriaDto> listMateriaDto = this.materiaService.listar();
		
		assertNotNull(listMateriaDto);
		assertEquals("ILP", listMateriaDto.get(0).getCodigo());
		assertEquals(1, listMateriaDto.get(0).getId().longValue());
		assertEquals("/materia/1", listMateriaDto.get(0).getLinks().getRequiredLink("self").getHref());
		assertEquals(1, listMateriaDto.size());
		
		Mockito.verify(this.materiaRepository, times(1)).findAll();
	}
	
	@Test
	public void testListarPorHorarioMinimo() {
		List<MateriaEntity> listMateria = new ArrayList<>();
		
		listMateria.add(materiaEntity);
		
		Mockito.when(this.materiaRepository.findByHoraMinima(64)).thenReturn(listMateria);
		
		List<MateriaDto> listMateriaDto = this.materiaService.consultaPorHora(64);
		
		assertNotNull(listMateriaDto);
		assertEquals("ILP", listMateriaDto.get(0).getCodigo());
		assertEquals(1, listMateriaDto.get(0).getId().longValue());
		assertEquals(1, listMateriaDto.size());
		
		Mockito.verify(this.materiaRepository, times(1)).findByHoraMinima(64);
	}
	
	@Test
	public void testConsultarSucesso() {
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
		MateriaDto materiaDto = this.materiaService.consultar(1L);
		assertNotNull(materiaDto);
		assertEquals("ILP", materiaDto.getCodigo());
		assertEquals(1, materiaDto.getId().longValue());
		assertEquals(1, materiaDto.getFrequencia());
		
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		
		
	}
	
	@Test
	public void testCadastrarSucesso() {
		MateriaDto materiaDto = new MateriaDto();
		materiaDto.setCodigo("ILP");
		materiaDto.setFrequencia(1);
		materiaDto.setHoras(64);
		materiaDto.setNome("INTRODUCAO OO");
		
		materiaEntity.setId(null);
		
		Mockito.when(this.materiaRepository.save(materiaEntity)).thenReturn(materiaEntity);
		
		Boolean sucesso = this.materiaService.cadastrar(materiaDto);
		

		assertNotNull(sucesso);
		
		
		Mockito.verify(this.materiaRepository, times(1)).save(materiaEntity);
		
		materiaEntity.setId(1L);
	
	}
	
	@Test
	public void testAtualizarrSucesso() {
		MateriaDto materiaDto = new MateriaDto();
		materiaDto.setId(1L);
		materiaDto.setCodigo("ILP");
		materiaDto.setFrequencia(1);
		materiaDto.setHoras(64);
		materiaDto.setNome("INTRODUCAO OO");
		
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
		Mockito.when(this.materiaRepository.save(materiaEntity)).thenReturn(materiaEntity);
		
		Boolean sucesso = this.materiaService.atualizar(materiaDto);
		

		assertNotNull(sucesso);
		
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.materiaRepository, times(1)).save(materiaEntity);
		
		
	
	}
	// teste exception
	
	@Test
	public void testAtualizarThronMateriaException() {
		MateriaDto materiaDto = new MateriaDto();
		materiaDto.setId(1L);
		materiaDto.setCodigo("ILP");
		materiaDto.setFrequencia(1);
		materiaDto.setHoras(64);
		materiaDto.setNome("INTRODUCAO OO");
		
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.empty());
	
		MateriaException materiaException;
		
		materiaException = assertThrows(MateriaException.class, ()->{
			this.materiaService.atualizar(materiaDto);
		});
		
		assertEquals(HttpStatus.NOT_FOUND, materiaException.getHttpStatus());
		assertEquals(Mensagens.ERRO_MATERIA_NAO_ENCONTRADA.getValor(), materiaException.getMessage());
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.materiaRepository, times(0)).save(materiaEntity);
		
		
	
	}
	
	@Test
	public void testCadastrarComIdThronMateriaException() {
		MateriaDto materiaDto = new MateriaDto();
		materiaDto.setId(1L);
		materiaDto.setCodigo("ILP");
		materiaDto.setFrequencia(1);
		materiaDto.setHoras(64);
		materiaDto.setNome("INTRODUCAO OO");
		
	
		MateriaException materiaException;
		
		materiaException = assertThrows(MateriaException.class, ()->{
			this.materiaService.cadastrar(materiaDto);
		});
		
		assertEquals(HttpStatus.BAD_REQUEST, materiaException.getHttpStatus());
		assertEquals(Mensagens.ERRO_ID_INFORMADO.getValor(), materiaException.getMessage());
		
		Mockito.verify(this.materiaRepository, times(0)).save(materiaEntity);
		
		
	
	}
	@Test
	public void testAtualizarThrowException() {
		MateriaDto materiaDto = new MateriaDto();
		materiaDto.setId(1L);
		materiaDto.setCodigo("ILP");
		materiaDto.setFrequencia(1);
		materiaDto.setHoras(64);
		materiaDto.setNome("INTRODUCAO OO");
		
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
		Mockito.when(this.materiaRepository.save(materiaEntity)).thenThrow(IllegalStateException.class);
	
		MateriaException materiaException;
		
		materiaException = assertThrows(MateriaException.class, ()->{
			this.materiaService.atualizar(materiaDto);
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, materiaException.getHttpStatus());
		assertEquals(Mensagens.ERRO_GENERICO.getValor(), materiaException.getMessage());
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		Mockito.verify(this.materiaRepository, times(1)).save(materiaEntity);
		
		
	
	}
	
	@Test
	public void testExcluirThrowException() {
		
		Mockito.when(this.materiaRepository.findById(1L)).thenReturn(Optional.of(materiaEntity));
		
		Mockito.doThrow(IllegalStateException.class).when(this.materiaRepository).deleteById(1L);
	
		MateriaException materiaException;
		
		materiaException = assertThrows(MateriaException.class, ()->{
			this.materiaService.excluir(1L);
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, materiaException.getHttpStatus());
		assertEquals(Mensagens.ERRO_GENERICO.getValor(), materiaException.getMessage());
		
		Mockito.verify(this.materiaRepository, times(1)).deleteById(1L);
		Mockito.verify(this.materiaRepository, times(1)).findById(1L);
		
		
	
	}
	
	

}
