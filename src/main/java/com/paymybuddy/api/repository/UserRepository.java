
package com.paymybuddy.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.api.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	Iterable<User> findAllByUsername(String username);

	boolean existsByUsername(String authUsername);

}
