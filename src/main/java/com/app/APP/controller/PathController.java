package com.app.APP.controller;

import com.app.APP.auth.AuthenticatedUser;
import com.app.APP.model.dto.request.PathRequest;
import com.app.APP.model.dto.response.PathResponse;
import com.app.APP.service.PathService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caminho")
@RequiredArgsConstructor
public class PathController {

    private final PathService pathService;

    @PostMapping
    public PathResponse start(AuthenticatedUser user, @RequestBody @Valid PathRequest request) {
        return pathService.start(user.id(), request);
    }

    @PatchMapping("/{id}/finalizar")
    public PathResponse finish(@PathVariable String id,
                               @RequestParam Double totalDistanceKm) {
        return pathService.finish(id, totalDistanceKm);
    }

    @GetMapping("/aventura/{adventureId}")
    public Page<PathResponse> getByAdventure(@PathVariable String adventureId, Pageable pageable) {
        return pathService.getByAdventure(adventureId, pageable);
    }

    @GetMapping("/usuario/{userId}")
    public Page<PathResponse> getByUser(@PathVariable String userId, Pageable pageable) {
        return pathService.getByUser(userId, pageable);
    }
}
