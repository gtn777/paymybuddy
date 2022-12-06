
package com.paymybuddy.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.entity.BankTransfer;


@Repository
public interface BankTransferRepository extends CrudRepository<BankTransfer, Integer> {
}
