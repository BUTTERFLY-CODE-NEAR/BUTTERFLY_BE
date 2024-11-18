package com.codenear.butterfly.admin.support.domain.dto;

import com.codenear.butterfly.support.domain.FAQ;

public record FAQRequest(
        String question,
        String answer,
        boolean status
) {

    public FAQ toEntity() {
        return FAQ.builder()
                .question(question)
                .answer(answer)
                .status(status)
                .build();
    }
}
