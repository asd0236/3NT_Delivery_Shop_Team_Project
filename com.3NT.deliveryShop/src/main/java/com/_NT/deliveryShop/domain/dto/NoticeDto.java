package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Notice;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.util.Streamable;

public interface NoticeDto {

    @With
    @Data
    @Builder
    class Create {

        private Long ownerId;
        private String title;
        private String content;

        public Notice asEntity(
            Function<? super Notice, ? extends Notice> initialize) {
            return initialize.apply(Notice.builder()
                .title(title)
                .content(content)
                .build());
        }
    }

    @With
    @Data
    @Builder
    class Put {

        private Long updater;
        private String title;
        private String content;
    }

    @With
    @Data
    @Builder
    class Result {

        private UUID id;
        private Long ownerId;
        private String title;
        private String content;

        public static Result of(Notice notice) {
            return Result.builder()
                .id(notice.getId())
                .ownerId(notice.getOwner().getUserId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .build();
        }

        public static List<Result> of(Streamable<Notice> notices) {
            return notices.stream()
                .map(Result::of)
                .collect(Collectors.toList());
        }

        @Data
        @Builder
        public static class Deleted {

            private UUID id;

            public static Deleted of(Notice notice) {
                return Deleted.builder()
                    .id(notice.getId())
                    .build();
            }
        }
    }
}
