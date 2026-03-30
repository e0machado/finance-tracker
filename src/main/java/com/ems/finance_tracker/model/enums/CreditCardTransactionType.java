package com.ems.finance_tracker.model.enums;

import com.ems.finance_tracker.model.entity.CreditCard;

import java.math.BigDecimal;

/**
 * Enum representing the types of credit card transactions in the financial tracking system.
 * <p>
 * Each type encapsulates its own financial behavior, defining how it affects
 * the credit card's available limit when applied or reverted.
 * </p>
 *
 * @author Evandro Machado
 * @see com.ems.finance_tracker.model.entity.CreditCard
 */
public enum CreditCardTransactionType {

    /**
     * Represents a purchase transaction.
     * Reduces the available limit when applied and restores it when reverted.
     */
    PURCHASE {
        @Override
        public void apply(BigDecimal amount, CreditCard creditCard) {
            creditCard.addDebit(amount);
        }
        @Override
        public void revert(BigDecimal amount, CreditCard creditCard) {
            creditCard.addCredit(amount);
        }
    },

    /**
     * Represents a refund transaction.
     * Restores the available limit when applied and reduces it when reverted.
     */
    REFUND {
        @Override
        public void apply(BigDecimal amount, CreditCard creditCard) {
            creditCard.addCredit(amount);
        }
        @Override
        public void revert(BigDecimal amount, CreditCard creditCard) {
            creditCard.addDebit(amount);
        }
    };

    /**
     * Applies the financial impact of this transaction type to the given credit card.
     *
     * @param amount the transaction amount
     * @param creditCard the credit card to be affected
     */
    public abstract void apply(BigDecimal amount, CreditCard creditCard);

    /**
     * Reverts the financial impact of this transaction type on the given credit card.
     *
     * @param amount the transaction amount
     * @param creditCard the credit card to be restored
     */
    public abstract void revert(BigDecimal amount, CreditCard creditCard);

}
