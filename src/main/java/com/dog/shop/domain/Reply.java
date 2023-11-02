package com.dog.shop.domain;

import com.dog.shop.domain.inquiry.Inquiry;
import com.dog.shop.domain.time.BaseTimeEntity;
import com.dog.shop.myenum.InquiryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reply")
public class Reply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String replyTitle;
    private String replyContent;
    private LocalDateTime replyDate;

    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;
}
