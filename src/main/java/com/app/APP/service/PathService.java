package com.app.APP.service;

import com.app.APP.entity.Adventure;
import com.app.APP.entity.Path;
import com.app.APP.mapper.PathMapper;
import com.app.APP.model.dto.request.PathRequest;
import com.app.APP.model.dto.response.PathResponse;
import com.app.APP.repository.AdventureRepository;
import com.app.APP.repository.PathRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.app.APP.mapper.PathMapper.toResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class PathService {

    private final PathRepository pathRepository;
    private final AdventureRepository adventureRepository;

    public PathResponse start(String userId, PathRequest request) {
        log.info("Starting path for user: {} in adventure: {}", userId, request.adventureId());

        Adventure adventure = adventureRepository.findById(request.adventureId())
                .orElseThrow(() -> new IllegalArgumentException("Aventura nao encontrada"));

        int number = pathRepository.countByAdventureId(request.adventureId()) + 1;

        return toResponse(pathRepository.save(PathMapper.toEntity(request, adventure, number, userId)));
    }

    public PathResponse finish(String id, Double totalDistanceKm) {
        log.info("Finishing path: {}", id);
        Path path = findById(id);

        path.setFinishedAt(LocalDateTime.now());
        path.setTotalDistanceKm(totalDistanceKm);

        return toResponse(pathRepository.save(path));
    }

    @Transactional(readOnly = true)
    public Page<PathResponse> getByAdventure(String adventureId, Pageable pageable) {
        return pathRepository.findByAdventureId(adventureId, pageable)
                .map(PathMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PathResponse> getByUser(String userId, Pageable pageable) {
        return pathRepository.findByUserId(userId, pageable)
                .map(PathMapper::toResponse);
    }

    private Path findById(String id) {
        return pathRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Caminho nao encontrado"));
    }
}
