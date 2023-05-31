package com.fivvy.backend.challenge.repository;

import com.fivvy.backend.challenge.model.Acceptance;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableScan
@Repository
public interface AcceptanceRepository extends CrudRepository<Acceptance,String> {

    Iterable<Acceptance> findByUserId(String userId);

    Optional<Acceptance> findByDisclaimerIdAndUserId(String disclaimerId, String userId);
}
