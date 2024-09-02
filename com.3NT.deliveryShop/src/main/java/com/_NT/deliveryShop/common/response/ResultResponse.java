package com._NT.deliveryShop.common.response;

import lombok.*;

import java.io.Serializable;

/**
 * API Response 공통 클래스
 */
@Getter
public class ResultResponse<T> implements Serializable {
    // API 응답 결과
    private T result;

    // API 응답 코드
    private int resultCode;

    // API 응답 메시지
    private String resultMessage;

    @Builder
    public ResultResponse(final T result, final int resultCode, final String resultMessage) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }
}
