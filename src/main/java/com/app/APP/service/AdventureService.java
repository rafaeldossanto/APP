package com.app.APP.service;

import com.app.APP.entity.Adventure;
import com.app.APP.entity.Region;
import com.app.APP.mapper.AdventureMapper;
import com.app.APP.mapper.ParticipantMapper;
import com.app.APP.model.dto.request.AdventureRequest;
import com.app.APP.model.dto.response.AdventureResponse;
import com.app.APP.model.enums.AdventureStatus;
import com.app.APP.repository.AdventureRepository;
import com.app.APP.repository.AdventureParticipantRepository;
import com.app.APP.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.app.APP.mapper.AdventureMapper.toResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdventureService {

    private final AdventureRepository adventureRepository;
    private final AdventureParticipantRepository participantRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public AdventureResponse create(String userId, AdventureRequest request) {
        log.info("Creating adventure for user: {}", userId);

        Region region = resolveRegion(userId, request.regionId());

        var adventure = adventureRepository.save(AdventureMapper.toEntity(request, region, userId));

        participantRepository.save(ParticipantMapper.toEntity(adventure, userId));

        log.info("Adventure created with id: {}", adventure.getId());
        return toResponse(adventure);
    }

    /** Moves the adventure to a region (folder) or removes it (null regionId). */
    @Transactional
    public AdventureResponse moveRegion(String userId, String adventureId, String regionId) {
        Adventure adventure = findOwner(userId, adventureId);
        adventure.setRegion(resolveRegion(userId, regionId));
        adventure.setUpdatedAt(LocalDateTime.now());
        log.info("Adventure {} moved to region {}", adventureId, regionId);
        return toResponse(adventureRepository.save(adventure));
    }

    /** Optional region: null when not provided; if provided, must belong to the user. */
    private Region resolveRegion(String userId, String regionId) {
        if (regionId == null || regionId.isBlank()) {
            return null;
        }
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("regiao nao encontrada"));
        if (!userId.equals(region.getUserId())) {
            throw new IllegalArgumentException("Voce nao e o dono desta regiao");
        }
        return region;
    }

    public AdventureResponse getById(String id) {
        return toResponse(findById(id));
    }

    public Page<AdventureResponse> getByUser(String userId, Pageable pageable) {
        return adventureRepository.findByUserId(userId, pageable)
                .map(AdventureMapper::toResponse);
    }

    public AdventureResponse updateStatus(String userId, String id, AdventureStatus status) {
        log.info("Updating status of adventure {} to {}", id, status);
        Adventure adventure = findOwner(userId, id);
        adventure.setStatus(status);
        adventure.setUpdatedAt(LocalDateTime.now());
        return toResponse(adventureRepository.save(adventure));
    }

    public void addParticipant(String adventureId, String userId) {
        Adventure adventure = findById(adventureId);

        if (participantRepository.existsByAdventureIdAndUserId(adventureId, userId)) {
            throw new IllegalArgumentException("Usuario ja participa dessa aventura");
        }

        participantRepository.save(ParticipantMapper.toEntity(adventure, userId));
        log.info("User {} added to adventure {}", userId, adventureId);
    }

    public void delete(String userId, String id) {
        Adventure adventure = findOwner(userId, id);
        adventureRepository.delete(adventure);
        log.info("Adventure {} deleted", id);
    }

    private Adventure findById(String id) {
        return adventureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aventura nao encontrada"));
    }

    private Adventure findOwner(String userId, String id) {
        Adventure adventure = findById(id);
        if (!userId.equals(adventure.getUserId())) {
            throw new IllegalArgumentException("Voce nao e o dono desta aventura");
        }
        return adventure;
    }
}
