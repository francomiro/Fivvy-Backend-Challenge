package com.fivvy.backend.challenge.repository;

import com.fivvy.backend.challenge.model.Disclaimer;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@EnableScan
@Repository
public interface DisclaimerRepository extends CrudRepository<Disclaimer, String> {

    Iterable<Disclaimer> findAllByTextContaining(@Param("text")String text);
}
