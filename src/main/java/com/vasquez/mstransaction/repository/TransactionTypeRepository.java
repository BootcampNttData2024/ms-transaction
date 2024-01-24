package com.vasquez.mstransaction.repository;

import com.vasquez.mstransaction.entity.TransactionType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTypeRepository extends ReactiveMongoRepository<TransactionType, String> {
}
