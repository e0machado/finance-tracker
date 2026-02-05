package com.ems.finance_tracker.model.enums;

public enum CreditCardTransactionType {
    PURCHASE(false),
    REFUND(true);

    private final boolean income;

    CreditCardTransactionType(boolean income) {
        this.income = income;
    }

    public boolean isIncome() {
        return income;
    }

    public boolean isExpense() {
        return !income;
    }

}
