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
import com.app.APP.repository.FollowerRepository;
import com.app.APP.repository.RegionRepository;
import com.app.APP.util.OwnershipValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.app.APP.mapper.AdventureMapper.toResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdventureService {

    private final AdventureRepository adventureRepository;
    private final AdventureParticipantRepository participantRepository;
    private final RegionRepository regionRepository;
    private final FollowerRepository followerRepository;
    private final AdventureAccessService accessService;

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
        OwnershipValidator.requireOwner(userId, region.getUserId(), "Voce nao e o dono desta regiao");
        return region;
    }

    @Transactional(readOnly = true)
    public AdventureResponse getById(String observerId, String id) {
        Adventure adventure = findById(id);
        accessService.validateView(observerId, adventure);
        return toResponse(adventure);
    }

    /** Own list comes whole; someone else's is filtered by each adventure's visibility. */
    @Transactional(readOnly = true)
    public Page<AdventureResponse> getByUser(String observerId, String userId, Pageable pageable) {
        Page<Adventure> page = observerId.equals(userId)
                ? adventureRepository.findByUserId(userId, pageable)
                : adventureRepository.findVisibleByUser(userId, observerId, pageable);
        return page.map(AdventureMapper::toResponse);
    }

    /**
     * Feed: own adventures plus the visible ones from the users the observer
     * follows, newest first. The sentinel keeps the IN clause valid when the
     * observer follows nobody (same trick as RegionService.discover).
     */
    @Transactional(readOnly = true)
    public Page<AdventureResponse> getFeed(String observerId, Pageable pageable) {
        List<String> authors = followerRepository.findFollowedIds(observerId);
        List<String> filter = authors.isEmpty() ? List.of("__sem_seguidos__") : authors;
        return adventureRepository.findFeed(observerId, filter, pageable)
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
        OwnershipValidator.requireOwner(userId, adventure.getUserId(), "Voce nao e o dono desta aventura");
        return adventure;
    }
}
