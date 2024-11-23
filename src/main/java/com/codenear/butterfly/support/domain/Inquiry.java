package com.codenear.butterfly.support.domain;

import static com.codenear.butterfly.support.domain.InquiryStatus.ANSWERED;
import static com.codenear.butterfly.support.domain.InquiryStatus.PENDING;

import com.codenear.butterfly.global.domain.BaseEntity;
import com.codenear.butterfly.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String inquiryContent;

    @Column(length = 1000)
    private String responseContent;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void toggleStatus() {
        if (this.status == PENDING)
            this.status = ANSWERED;

        if (this.status == ANSWERED)
            this.status = PENDING;
    }

    public void updateResponseContent(String answer) {
        this.responseContent = answer;
    }
}
