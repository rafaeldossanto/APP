package com.app.APP.service;

import com.app.APP.entity.User;
import com.app.APP.model.dto.response.PublicUserResponse;
import com.app.APP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserSearchService {

    private static final int AUTOCOMPLETE_LIMIT = 10;

    private final UserRepository userRepository;

    public PublicUserResponse findByCode(String userCode) {
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        return toPublic(user);
    }

    public List<PublicUserResponse> autocomplete(String term) {
        return userRepository.findByUserCodePrefix(term).stream()
                .limit(AUTOCOMPLETE_LIMIT)
                .map(this::toPublic)
                .toList();
    }

    private PublicUserResponse toPublic(User user) {
        return PublicUserResponse.builder()
                .userCode(user.getUserCode())
                .name(user.getName())
                .build();
    }
}
