package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.AiChat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AiChatDto {

    @Data
    @Builder
    class Create {
        private final Long userId;
        private final String content;
    }

    @Data
    @Builder
    class Response {
        private final UUID chatId;
        private final Long userId;
        private final boolean isUserChat;
        private final String content;
        private final LocalDateTime chatTime;

        public static Response of(AiChat aiChat){
            return Response.builder()
                    .chatId(aiChat.getChatId())
                    .userId(aiChat.getUser().getUserId())
                    .isUserChat(aiChat.isUserChat())
                    .content(aiChat.getContent())
                    .chatTime(aiChat.getChatTime())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor(force = true)
    final class GetAllChatsResponse{
        private final UUID chatId;
        private final Long userId;
        private final boolean isUserChat;
        private final String content;
        private final LocalDateTime chatTime;

        public GetAllChatsResponse(AiChat aiChat){
            this.chatId = aiChat.getChatId();
            this.userId = aiChat.getUser().getUserId();
            this.isUserChat = aiChat.isUserChat();
            this.content = aiChat.getContent();
            this.chatTime = aiChat.getChatTime();
        }
    }


}
