package com.app.APP.repository;

import com.app.APP.entity.AdventureParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdventureParticipantRepository extends JpaRepository<AdventureParticipant, String> {
    boolean existsByAdventureIdAndUserId(String adventureId, String userId);
}
