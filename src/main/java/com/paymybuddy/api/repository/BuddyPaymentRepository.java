package com.paymybuddy.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.entity.BuddyPayment;

@Repository
public interface BuddyPaymentRepository extends CrudRepository<BuddyPayment, Integer> {

}
