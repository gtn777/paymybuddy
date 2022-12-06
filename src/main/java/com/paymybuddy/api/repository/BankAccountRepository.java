package com.paymybuddy.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.entity.BankAccount;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Integer>{
}
