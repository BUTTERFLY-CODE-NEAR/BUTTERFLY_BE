package com.codenear.butterfly.consent.application;

import com.codenear.butterfly.consent.domain.Consent;
import com.codenear.butterfly.consent.domain.ConsentRepository;
import com.codenear.butterfly.consent.domain.ConsentType;
import com.codenear.butterfly.consent.dto.ConsentInfoResponseDTO;
import com.codenear.butterfly.consent.dto.ConsentSingleResponseDTO;
import com.codenear.butterfly.consent.dto.ConsentUpdateRequest;
import com.codenear.butterfly.fcm.application.FCMFacade;
import com.codenear.butterfly.member.application.MemberService;
import com.codenear.butterfly.member.domain.Member;
import com.codenear.butterfly.member.domain.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsentService {

    private final ConsentRepository consentRepository;
    private final MemberService memberService;
    private final FCMFacade fcmFacade;

    public ConsentInfoResponseDTO getConsentsInfo(MemberDTO memberDTO) {
        List<Consent> consents = loadConsentsByMemberId(memberDTO.getId());
        List<ConsentSingleResponseDTO> consentSingleDTOS = getConsentSingleResponseDTOS(consents);
        return new ConsentInfoResponseDTO(consentSingleDTOS);
    }

    private List<ConsentSingleResponseDTO> getConsentSingleResponseDTOS(List<Consent> consents) {
        List<ConsentSingleResponseDTO> responseDTOList = new ArrayList<>();

        for (ConsentType type : ConsentType.values()) {
            boolean agreed = isAgreed(consents, type);
            responseDTOList.add(createConsentSingleResponseDTO(type, agreed));
        }
        return responseDTOList;
    }

    private boolean isAgreed(List<Consent> consents, ConsentType type) {
        return consents.stream()
                .filter(consent -> consent.getConsentType().equals(type))
                .map(Consent::isAgreed)
                .findFirst()
                .orElse(false);
    }

    private ConsentSingleResponseDTO createConsentSingleResponseDTO(ConsentType value, boolean agreed) {
        return new ConsentSingleResponseDTO(value, agreed);
    }

    public void updateConsent(ConsentUpdateRequest updateRequestDTO, MemberDTO memberDTO) {
        List<Consent> consents = loadConsentsByMemberId(memberDTO.getId());
        ConsentType type = updateRequestDTO.getConsentType();

        Consent consent = consents.stream()
                .filter(findConsent -> findConsent.getConsentType().equals(type))
                .findFirst()
                .orElseGet(() -> createConsent(type, false, memberService.loadMemberByMemberId(memberDTO.getId())));

        consent.toggleAgreement();

        if (consent.getConsentType().hasTopic()) {
            updateFCMSubscription(memberDTO, consent);
        }
        consentRepository.save(consent);
    }

    private void updateFCMSubscription(MemberDTO memberDTO, Consent consent) {
        if (consent.isAgreed()) {
            fcmFacade.subscribeToTopic(memberDTO.getId(), consent.getConsentType().getTopic());
            return;
        }
        fcmFacade.unsubscribeFromTopic(memberDTO.getId(), consent.getConsentType().getTopic());
    }

    public void saveConsent(ConsentType type, boolean agreed, Member member) {
        Consent consent = createConsent(type, agreed, member);
        consentRepository.save(consent);
    }

    protected List<Consent> loadConsentsByMemberId(Long id) {
        return consentRepository.findByMemberId(id);
    }

    private Consent createConsent(ConsentType type, boolean agreed, Member member) {
        return Consent.builder()
                .consentType(type)
                .isAgreed(agreed)
                .member(member)
                .build();
    }
}
