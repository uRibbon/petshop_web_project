package com.dog.shop.dto.replyDto;

import com.dog.shop.domain.time.BaseTimeEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReplyResDto extends BaseTimeEntity {
    private Long id;
    private Long inquiryId;
    private String replyTitle;
    private String replyContent;
}
