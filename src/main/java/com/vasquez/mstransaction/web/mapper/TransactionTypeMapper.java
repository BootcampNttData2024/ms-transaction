package com.vasquez.mstransaction.web.mapper;

import com.vasquez.mstransaction.entity.TransactionType;
import com.vasquez.mstransaction.model.TransactionTypeRequest;
import com.vasquez.mstransaction.model.TransactionTypeResponse;
import org.springframework.beans.BeanUtils;

public class TransactionTypeMapper {

    public static TransactionType toEntity(TransactionTypeRequest transactionTypeRequest) {
        TransactionType transactionType = new TransactionType();
        BeanUtils.copyProperties(transactionTypeRequest, transactionType);
        return transactionType;
    }

    public static TransactionTypeResponse toResponse(TransactionType transactionType) {
        TransactionTypeResponse transactionTypeResponse = new TransactionTypeResponse();
        BeanUtils.copyProperties(transactionType, transactionTypeResponse);
        return transactionTypeResponse;
    }

}
