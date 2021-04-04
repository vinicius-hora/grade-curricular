package com.rasmoo.cliente.escola.gradecurricular.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.exception.MateriaException;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;

@Service
public class materiaService implements IMateriaService {

	@Autowired
	private IMateriaRepository materiaRepository;

	@Override
	public Boolean atualizar(MateriaDto materia) {
		try {
			Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(materia.getId());

			if (materiaOptional.isPresent()) {
				ModelMapper mapper = new ModelMapper();
				MateriaEntity materiaEntityAtualizada = mapper.map(materia, MateriaEntity.class);

				this.materiaRepository.save(materiaEntityAtualizada);

				return true;
			}
			return false;

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.materiaRepository.deleteById(id);
			return true;

		} catch (MateriaException m) {
			throw m;

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Boolean cadastrar(MateriaDto materia) {
		try {
			ModelMapper mapper = new ModelMapper();
			MateriaEntity materiaEntity = mapper.map(materia, MateriaEntity.class);
			this.materiaRepository.save(materiaEntity);
			return true;
		} catch (Exception e) {

			return false;

		}
	}

	@Override
	public List<MateriaDto> listar() {
		try {
			ModelMapper mapper = new ModelMapper();
			
			return mapper.map(this.materiaRepository.findAll(), new TypeToken<List<MateriaDto>>() {}.getType());

		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@Override
	public MateriaDto consultar(Long id) {
		try {
			ModelMapper mapper = new ModelMapper();
			
			Optional<MateriaEntity> materiaOptional = this.materiaRepository.findById(id);
			if (materiaOptional.isPresent()) {

				return mapper.map(materiaOptional.get(), MateriaDto.class);

			}
			throw new MateriaException("Matéria não encontrada", HttpStatus.NOT_FOUND);

		} catch (MateriaException m) {
			throw m;

		} catch (Exception e) {
			throw new MateriaException("Erro interno identificado.", HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
