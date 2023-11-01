package com.dog.shop.repository;

import com.dog.shop.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByInquiryId(Long inquiryId);
}
