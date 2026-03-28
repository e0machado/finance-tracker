package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.CreditCardTransactionDTO;
import com.ems.finance_tracker.model.entity.Category;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.CreditCardStatement;
import com.ems.finance_tracker.model.entity.CreditCardTransaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper responsible for converting {@link CreditCardTransactionDTO} to
 * {@link CreditCardTransaction} entities and vice versa.
 *
 * @author Evandro Machado
 */
@Component
public class CreditCardTransactionMapper {

    /**
     * Converts a credit card transaction creation request DTO into a CreditCardTransaction entity.
     *
     * @param dto the credit card transaction creation request data
     * @param creditCard the credit card associated with the transaction
     * @param category the category associated with the transaction
     * @param statement the credit card statement associated with the transaction
     * @return a new CreditCardTransaction entity ready for persistence
     */
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

    /**
     * Converts a CreditCardTransaction entity into a response DTO.
     *
     * @param transaction the persisted credit card transaction entity
     * @return a response DTO exposing credit card transaction data
     */
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
                new CreditCardTransactionDTO.CreditCardRef(transaction.getCreditCard().getId()),
                new CreditCardTransactionDTO.CategoryRef(transaction.getCategory().getId()),
                new CreditCardTransactionDTO.CreditCardStatementRef(transaction.getCreditCardStatement().getId())
        );
    }

    /**
     * Updates mutable fields of an existing CreditCardTransaction entity
     * using data from an update DTO.
     *
     * <p>This method mutates the provided entity directly.</p>
     *
     * @param transaction the existing credit card transaction entity to be updated
     * @param dto the DTO containing updated transaction information
     * @param category the new category wrapped in an {@link Optional}, or empty if not being updated
     */
    public void updateEntity(
            CreditCardTransaction transaction,
            CreditCardTransactionDTO.Update dto,
            Optional<Category> category
    ) {
        dto.description().ifPresent(transaction::setDescription);
        dto.type().ifPresent(transaction::setType);
        dto.amount().ifPresent(transaction::setAmount);
        dto.isInstallment().ifPresent(transaction::setInstallment);
        dto.currentInstallment().ifPresent(transaction::setCurrentInstallment);
        dto.totalInstallments().ifPresent(transaction::setTotalInstallments);
        dto.purchaseDate().ifPresent(transaction::setPurchaseDate);
        dto.comment().ifPresent(transaction::setComment);
        category.ifPresent(transaction::setCategory);
    }
}
