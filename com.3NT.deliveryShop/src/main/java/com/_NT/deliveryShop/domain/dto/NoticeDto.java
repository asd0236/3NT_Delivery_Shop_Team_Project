package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Notice;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.util.Streamable;

public interface NoticeDto {

    @With
    @Data
    @Builder
    class Create {

        @NotNull(message = "유저 ID를 입력해주세요.")
        private Long ownerId;

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 3000, message = "내용은 3000자 이하로 입력해주세요.")
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

        @NotNull(message = "유저 ID를 입력해주세요.")
        private Long updater;

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 3000, message = "내용은 3000자 이하로 입력해주세요.")
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
