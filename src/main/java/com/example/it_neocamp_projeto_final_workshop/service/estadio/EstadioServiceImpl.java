package com.example.it_neocamp_projeto_final_workshop.service.estadio;

import com.example.it_neocamp_projeto_final_workshop.dto.estadio.EstadioRequest;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.exception.EstadioNaoEncontradoException;
import com.example.it_neocamp_projeto_final_workshop.mapper.EstadioMapper;
import com.example.it_neocamp_projeto_final_workshop.model.Estadio;
import com.example.it_neocamp_projeto_final_workshop.repository.EstadioRepository;
import com.example.it_neocamp_projeto_final_workshop.specification.EstadioSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EstadioServiceImpl implements EstadioService{
    private final EstadioRepository estadioRepository;

    public EstadioServiceImpl(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    @Override
    public Estadio cadastrarEsrtadio(EstadioRequest estadioRequest) {
        if (estadioRepository.existsByNomeIgnoreCase(estadioRequest.getNome())) {
            throw new EstadioJaExisteException(estadioRequest.getNome());
        }
        return estadioRepository.save(EstadioMapper.toEntity(estadioRequest));
    }

    @Override
    public Estadio atualizarEstadio(UUID estadioId, EstadioRequest estadioRequest) {
        if (estadioRepository.findById(estadioId).isEmpty()) {
            throw new EstadioNaoEncontradoException(estadioId);
        }
        return estadioRepository.save(EstadioMapper.toEntity(estadioRequest));
    }

    @Override
    public Estadio listarEstadioPorId(UUID estadioId) {
        return estadioRepository.findById(estadioId).orElseThrow(
                () -> new EstadioNaoEncontradoException(estadioId)
        );
    }

    @Override
    public Page<Estadio> listarEstadios(String nomeEstadio, Pageable pageable) {
        Specification<Estadio> spec = (root, query, cb) -> cb.conjunction();
        if (nomeEstadio != null && !nomeEstadio.isBlank()) {
            spec = spec.and(EstadioSpecification.nomeContains(nomeEstadio));
        }
        return estadioRepository.findAll(spec, pageable);
    }

    @Override
    public void deletarEstadio(UUID estadioId) {
        if (!estadioRepository.existsById(estadioId)) {
            throw new EstadioNaoEncontradoException(estadioId);
        }
        estadioRepository.deleteById(estadioId);
    }
}
