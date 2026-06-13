package com.app.APP.service;

import com.app.APP.mapper.RegiaoMapper;
import com.app.APP.model.dto.response.RegiaoResponse;
import com.app.APP.repository.RegiaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegiaoService {

    private final RegiaoRepository regiaoRepository;

    public List<RegiaoResponse> listar() {
        return regiaoRepository.findAll().stream()
                .map(RegiaoMapper::toResponse)
                .toList();
    }
}
