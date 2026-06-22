package com.app.APP.repository;

import com.app.APP.entity.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathRepository extends JpaRepository<Path, String> {
    Page<Path> findByAdventureId(String adventureId, Pageable pageable);
    Page<Path> findByUserId(String userId, Pageable pageable);

    /** How many paths the adventure already has — base for the next sequential number. */
    int countByAdventureId(String adventureId);
}
