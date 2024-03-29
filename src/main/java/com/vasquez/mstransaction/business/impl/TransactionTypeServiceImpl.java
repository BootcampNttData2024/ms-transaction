package com.vasquez.mstransaction.business.impl;

import com.vasquez.mstransaction.entity.TransactionType;
import com.vasquez.mstransaction.repository.TransactionTypeRepository;
import com.vasquez.mstransaction.business.TransactionTypeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Transfer type service implementation.
 *
 * @author Vasquez
 * @version 1.0.
 */
@Service
public class TransactionTypeServiceImpl implements TransactionTypeService {

  private final TransactionTypeRepository transactionTypeRepository;

  /**
   * Constructor.
   *
   * @param transactionTypeRepository transaction type repository.
   */
  public TransactionTypeServiceImpl(TransactionTypeRepository transactionTypeRepository) {
    this.transactionTypeRepository = transactionTypeRepository;
  }

  @Override
  public Mono<TransactionType> create(TransactionType request) {
    return transactionTypeRepository.save(request);
  }

  @Override
  public Mono<TransactionType> update(TransactionType request, String id) {
    return this.findById(id).flatMap(transactionType -> {
      transactionType.setName(request.getName());
      return transactionTypeRepository.save(transactionType);
    });
  }

  @Override
  public Mono<TransactionType> findById(String id) {
    return transactionTypeRepository.findById(id);
  }

  @Override
  public Flux<TransactionType> findAll() {
    return transactionTypeRepository.findAll();
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return transactionTypeRepository.deleteById(id);
  }
}
