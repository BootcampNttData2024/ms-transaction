package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.Transaction;
import com.vasquez.mstransaction.model.TransactionRequest;
import com.vasquez.mstransaction.model.TransactionResponse;
import org.springframework.beans.BeanUtils;

public class TransactionMapper {

    public static Transaction toEntity(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionRequest, transaction);
        return transaction;
    }

    public static TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse transactionResponse = new TransactionResponse();
        BeanUtils.copyProperties(transaction, transactionResponse);
        return transactionResponse;
    }

}
