package com.example.loancalculator.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Payment {
    private LocalDate date;
    private BigDecimal debtOnTheLoan;
    private BigDecimal interestPayment;
    private BigDecimal creditPayment;
    private BigDecimal totalPayment;

    public Payment() {
    }

    public Payment(LocalDate date, BigDecimal debtOnTheLoan, BigDecimal interestPayment, BigDecimal creditPayment, BigDecimal totalPayment) {
        this.date = date;
        this.debtOnTheLoan = debtOnTheLoan;
        this.interestPayment = interestPayment;
        this.creditPayment = creditPayment;
        this.totalPayment = totalPayment;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getDebtOnTheLoan() {
        return debtOnTheLoan;
    }

    public BigDecimal getInterestPayment() {
        return interestPayment;
    }

    public BigDecimal getCreditPayment() {
        return creditPayment;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(date, payment.date) &&
                Objects.equals(debtOnTheLoan, payment.debtOnTheLoan) &&
                Objects.equals(interestPayment, payment.interestPayment) &&
                Objects.equals(creditPayment, payment.creditPayment) &&
                Objects.equals(totalPayment, payment.totalPayment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, debtOnTheLoan, interestPayment, creditPayment, totalPayment);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "date=" + date +
                ", debtOnTheLoan='" + debtOnTheLoan + '\'' +
                ", interestPayment='" + interestPayment + '\'' +
                ", creditPayment='" + creditPayment + '\'' +
                ", totalPayment='" + totalPayment + '\'' +
                '}';
    }
}
