package com.dog.shop.dto.replyDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReplyReqDto {
    private String replyTitle;
    private String replyContent;
}
