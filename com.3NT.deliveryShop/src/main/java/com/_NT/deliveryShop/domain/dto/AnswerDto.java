package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Answer;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.util.Streamable;

public interface AnswerDto {

    @With
    @Data
    @Builder
    class Create {

        private Long ownerId;
        private UUID reportId;
        private String title;
        private String content;

        public Answer asEntity(
            Function<? super Answer, ? extends Answer> initialize) {
            return initialize.apply(Answer.builder()
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
        private UUID reportId;
        private String title;
        private String content;

        public static Result of(Answer answer) {
            return Result.builder()
                .id(answer.getId())
                .ownerId(answer.getOwner().getUserId())
                .reportId(answer.getReport().getId())
                .title(answer.getTitle())
                .content(answer.getContent())
                .build();
        }

        public static List<Result> of(Streamable<Answer> answers) {
            return answers.stream()
                .map(Result::of)
                .collect(Collectors.toList());
        }

        @Data
        @Builder
        public static class Deleted {

            private UUID id;

            public static Deleted of(Answer answer) {
                return Deleted.builder()
                    .id(answer.getId())
                    .build();
            }
        }
    }
}
