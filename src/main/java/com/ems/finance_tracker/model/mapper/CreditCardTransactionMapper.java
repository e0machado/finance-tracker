package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.CreditCardTransactionDTO;
import com.ems.finance_tracker.model.entity.Category;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.CreditCardStatement;
import com.ems.finance_tracker.model.entity.CreditCardTransaction;
import org.springframework.stereotype.Component;

@Component
public class CreditCardTransactionMapper {
    public CreditCardTransaction toEntity(
            CreditCardTransactionDTO.Request dto,
            CreditCard creditCard,
            Category category,
            CreditCardStatement statement
    ) {
        return CreditCardTransaction.builder()
                .description(dto.description())
                .type(dto.type())
                .amount(dto.amount())
                .isInstallment(dto.isInstallment())
                .currentInstallment(dto.currentInstallment())
                .totalInstallments(dto.totalInstallments())
                .purchaseDate(dto.purchaseDate())
                .comment(dto.comment())
                .creditCard(creditCard)
                .category(category)
                .creditCardStatement(statement)
                .build();
    }

    public CreditCardTransactionDTO.Response toResponse(CreditCardTransaction transaction) {
        return new CreditCardTransactionDTO.Response(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.isInstallment(),
                transaction.getCurrentInstallment(),
                transaction.getTotalInstallments(),
                transaction.getPurchaseDate(),
                transaction.getComment(),
                new CreditCardTransactionDTO.CreditCardRef(
                        transaction.getCreditCard() != null ? transaction.getCreditCard().getId() : null),
                new CreditCardTransactionDTO.CategoryRef(
                        transaction.getCategory() != null ? transaction.getCategory().getId() : null),
                new CreditCardTransactionDTO.CreditCardStatementRef(
                        transaction.getCreditCardStatement() != null ? transaction.getCreditCardStatement().getId() : null)
        );
    }

    public void updateEntity(CreditCardTransaction transaction, CreditCardTransactionDTO.Update dto, Category category) {
        dto.description().ifPresent(transaction::setDescription);
        dto.type().ifPresent(transaction::setType);
        dto.amount().ifPresent(transaction::setAmount);
        dto.isInstallment().ifPresent(transaction::setInstallment);
        dto.currentInstallment().ifPresent(transaction::setCurrentInstallment);
        dto.totalInstallments().ifPresent(transaction::setTotalInstallments);
        dto.purchaseDate().ifPresent(transaction::setPurchaseDate);
        dto.comment().ifPresent(transaction::setComment);
        if (category != null) {
            transaction.setCategory(category);
        }

    }
}
