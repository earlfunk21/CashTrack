package com.morax.cashtrack.database.entity;

import java.math.BigDecimal;

public class IncomeExpense {
    public BigDecimal income;
    public BigDecimal expense;

    public IncomeExpense(BigDecimal income, BigDecimal expense) {
        this.income = income;
        this.expense = expense;
    }
}
