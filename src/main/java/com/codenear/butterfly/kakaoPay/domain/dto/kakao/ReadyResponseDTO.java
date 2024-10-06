package com.codenear.butterfly.kakaoPay.domain.dto.kakao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReadyResponseDTO {

    private String tid; // 결제 고유 번호
//    private String next_redirect_app_url; // 결제 페이지 url 받기
    private String next_redirect_pc_url; // todo: FE와 연동 후 app_url로 수정
    private String created_at;
//    private String partner_order_id;
//    private String partner_user_id;
//    private String quantity;
}
