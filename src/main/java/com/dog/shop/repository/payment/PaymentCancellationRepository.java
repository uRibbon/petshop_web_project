package com.dog.shop.repository.payment;

import com.dog.shop.domain.PaymentCancellation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCancellationRepository extends JpaRepository<PaymentCancellation, Long> {
}
