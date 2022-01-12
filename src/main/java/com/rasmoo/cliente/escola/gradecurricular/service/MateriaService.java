package com.rasmoo.cliente.escola.gradecurricular.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rasmoo.cliente.escola.gradecurricular.constante.Mensagens;
import com.rasmoo.cliente.escola.gradecurricular.controller.MateriaController;
import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.exception.MateriaException;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;

@CacheConfig(cacheNames = "materia")
@Service
public class MateriaService implements IMateriaService {

	@Autowired
	private static final String MENSAGEM_ERRO = "Erro interno identificado";
	private static final String MATERIA_NAO_ENCONTRADA = "Matéria não encontrada";
	private IMateriaRepository materiaRepository;
	private ModelMapper mapper;

	@Autowired
	public MateriaService(IMateriaRepository materiaRepository) {
		this.mapper = new ModelMapper();
		this.materiaRepository = materiaRepository;

	}

	/*
	 * para várias caches
	 * 
	 * @Caching(evict = {
	 * 
	 * })
	 */
	@Override
	public Boolean atualizar(MateriaDto materia) {
		try {
			this.consultar(materia.getId());

			MateriaEntity materiaEntityAtualizada = this.mapper.map(materia, MateriaEntity.class);

			this.materiaRepository.save(materiaEntityAtualizada);

			return Boolean.TRUE;

		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.materiaRepository.deleteById(id);
			return Boolean.TRUE;

		} catch (MateriaException m) {
			throw m;

		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(MateriaDto materia) {
		try {
			if(materia.getId() != null) {
				throw new MateriaException(Mensagens.ERRO_ID_INFORMADO.getValor(),
						HttpStatus.BAD_REQUEST);
			}
			
			return this.cadastrar(materia);
			
					
		} catch (MateriaException m) {
			throw m;
		} catch (Exception e) {
			throw new MateriaException(Mensagens.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(unless = "#result.size()<3")
	@Override
	public List<MateriaDto> listar() {
		try {

			List<MateriaDto> materiaDto = this.mapper.map(this.materiaRepository.findAll(),
					new TypeToken<List<MateriaDto>>() {
					}.getType());
			materiaDto.forEach(materia -> {
				materia.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class).consultaMateria(materia.getId()))
						.withSelfRel());
			});

			return materiaDto;

		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

	@Override
	@Cacheable(key = "#id")
	public MateriaDto consultar(Long id) {
		try {

			Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(id);
			if (materiaOptional.isPresent()) {

				return this.mapper.map(materiaOptional.get(), MateriaDto.class);

			}
			throw new MateriaException(MATERIA_NAO_ENCONTRADA, HttpStatus.NOT_FOUND);

		} catch (MateriaException m) {
			throw m;

		} catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
	@Override
	public List<MateriaDto> consultaPorHora(int horaMinima) {
		try {
			return this.mapper.map(this.materiaRepository.findByHoraMinima(horaMinima),
					new TypeToken<List<MateriaDto>>() {
					}.getType());
			
		}catch (Exception e) {
			throw new MateriaException(MENSAGEM_ERRO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
			
			}

	

	

	
	
	

	
	

}
