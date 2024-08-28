package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.dto.AiChatDto;
import com._NT.deliveryShop.domain.entity.AiChat;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.AiChatRepository;
import com._NT.deliveryShop.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

import static com._NT.deliveryShop.domain.entity.UserRoleEnum.ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatService {

    private final UserRepository userRepository;
    private final AiChatRepository aiChatRepository;

    @Value("${ai.secret.key}")
    private String secretKey;

    @Transactional
    public AiChatDto.Response createChat(AiChatDto.Create aiChatDto, User user) {

        // 관리자가 아닐 경우 타인의 userId 입력 시 403 에러 반환
        if (user.getRole() != ADMIN && !Objects.equals(user.getUserId(), aiChatDto.getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        Long userId = aiChatDto.getUserId();

        if(!userRepository.existsById(userId)){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        // 외부 API 요청 생성
        // uri
        URI uri = UriComponentsBuilder
                .fromUriString("https://generativelanguage.googleapis.com")
                .path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
                .queryParam("key", secretKey)
                .encode()
                .build()
                .toUri();

        log.info("Gemini API 요청 URI = " + uri);

        // header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // payload
        String jsonPayload = "{\"contents\":[{\"parts\":[{\"text\":\"" + aiChatDto.getContent() + "\"}]}]}";

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

        // 응답으로부터 content 추출
        ObjectMapper objectMapper = new ObjectMapper();
        String result;
        try {
            JsonNode node = objectMapper.readTree(responseEntity.getBody());

            result = node
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        AiChat userChat = new AiChat(user, true, aiChatDto.getContent(), LocalDateTime.now());
        AiChat aiChat = new AiChat(user, false, result, LocalDateTime.now());

        aiChatRepository.save(userChat);
        return AiChatDto.Response.of(aiChatRepository.save(aiChat));
    }

    @Transactional(readOnly = true)
    public Collection<AiChatDto.GetAllChatsResponse> getAllChats(int page, int size, String sortBy, User user) {
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AiChat> aiChatList = aiChatRepository.findChatForAUser(user.getUserId(), pageable);

        return aiChatList.map(AiChatDto.GetAllChatsResponse::new).stream().toList();
    }
}
