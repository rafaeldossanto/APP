package com.app.APP.service;

import com.app.APP.entity.Adventure;
import com.app.APP.entity.Path;
import com.app.APP.mapper.PathMapper;
import com.app.APP.model.dto.request.PathRequest;
import com.app.APP.model.dto.response.PathDiscoveryResponse;
import com.app.APP.model.dto.response.PathResponse;
import com.app.APP.repository.AdventureRepository;
import com.app.APP.repository.PathRepository;
import com.app.APP.util.OwnershipValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.app.APP.mapper.PathMapper.toResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class PathService {

    private final PathRepository pathRepository;
    private final AdventureRepository adventureRepository;
    private final AdventureAccessService accessService;

    public PathResponse start(String userId, PathRequest request) {
        log.info("Starting path for user: {} in adventure: {}", userId, request.adventureId());

        Adventure adventure = adventureRepository.findById(request.adventureId())
                .orElseThrow(() -> new IllegalArgumentException("Aventura nao encontrada"));
        accessService.validateContribute(userId, adventure);

        int number = pathRepository.countByAdventureId(request.adventureId()) + 1;

        return toResponse(pathRepository.save(PathMapper.toEntity(request, adventure, number, userId)));
    }

    public PathResponse finish(String userId, String id, Double totalDistanceKm) {
        log.info("Finishing path: {}", id);
        Path path = findById(id);
        OwnershipValidator.requireOwner(userId, path.getUserId(), "Voce nao e o dono deste caminho");

        path.setFinishedAt(LocalDateTime.now());
        path.setTotalDistanceKm(totalDistanceKm);

        return toResponse(pathRepository.save(path));
    }

    @Transactional(readOnly = true)
    public Page<PathResponse> getByAdventure(String observerId, String adventureId, Pageable pageable) {
        Adventure adventure = adventureRepository.findById(adventureId)
                .orElseThrow(() -> new IllegalArgumentException("Aventura nao encontrada"));
        accessService.validateView(observerId, adventure);
        return pathRepository.findByAdventureId(adventureId, pageable)
                .map(PathMapper::toResponse);
    }

    /** Own list comes whole; someone else's follows the adventures' visibility. */
    @Transactional(readOnly = true)
    public Page<PathResponse> getByUser(String observerId, String userId, Pageable pageable) {
        Page<Path> page = observerId.equals(userId)
                ? pathRepository.findByUserId(userId, pageable)
                : pathRepository.findVisibleByUser(userId, observerId, pageable);
        return page.map(PathMapper::toResponse);
    }

    /** Access check for the BFF to gate the GPS points served by the loc service. */
    @Transactional(readOnly = true)
    public boolean canView(String observerId, String pathId) {
        return accessService.canViewPath(observerId, pathId);
    }

    /**
     * Filtra, entre os caminhos que o mapa encontrou na regiao visivel (ids
     * vindos do servico de Localizacao via BFF), os que o observador pode ver:
     * PUBLICA para todos, SO_GRUPO so para participantes; os proprios ficam de
     * fora — o app ja os desenha.
     */
    @Transactional(readOnly = true)
    public List<PathDiscoveryResponse> discover(String observerId, List<String> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return pathRepository.findDiscoverableByIds(ids, observerId)
                .stream().map(PathMapper::toDiscoveryResponse).toList();
    }

    private Path findById(String id) {
        return pathRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Caminho nao encontrado"));
    }
}
