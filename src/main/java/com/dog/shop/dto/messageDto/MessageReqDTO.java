package com.dog.shop.dto.messageDto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MessageReqDTO {

    private String phone;

}
