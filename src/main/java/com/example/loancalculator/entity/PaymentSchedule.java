package com.example.loancalculator.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PaymentSchedule {
    private int summ;
    private int year;
    private double percent;
    private double totalInterestPayment;
    private double totalDebtPayment;

    private List<Payment> payments;

    public PaymentSchedule() {
    }

    public PaymentSchedule(int summ, int year, double percent) {
        this.summ = summ;
        this.year = year;
        this.percent = percent;
        //payments = getCalculation();
    }

    public double getTotalInterestPayment() {
        return totalInterestPayment;
    }

    public double getTotalDebtPayment() {
        return totalDebtPayment;
    }

    public int getSumm() {
        return summ;
    }

    public void setSumm(int summ) {
        this.summ = summ;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public List<Payment> getPayments() {
        //return payments;
        return getCalculation();
    }

    @Override
    public String toString() {
        return "PaymentSchedule{" +
                "summ=" + summ +
                ", year=" + year +
                ", percent=" + percent +
                ", payments=" + payments +
                '}';
    }

    private double getMonthlyPercent(){
        return percent / 100 / 12;
    }

    public double getMonthlyPayment(){
        double mps = getMonthlyPercent();
        double mp = summ * (mps / (1 - (Math.pow(1 + mps, (year * 12) * -1))));
        return mp;
    }

    private List<Payment> getCalculation(){
        totalDebtPayment = 0;
        totalInterestPayment = 0;
        double remainingAmount = summ;
        double mps = getMonthlyPercent();
        double mp = getMonthlyPayment();
        double summPercent;
        double summDebt;
        LocalDate localDate = LocalDate.now();
        List<Payment> list = new ArrayList<Payment>();
        while (remainingAmount > 0){
            localDate = localDate.plus(1, ChronoUnit.MONTHS); //Следующий месяц
            summPercent = remainingAmount * mps; //сумма ежемесячного платежа по процентам
            summDebt = mp - summPercent; // сумма ежемесячного платежа по основному долгу
            list.add(new Payment(localDate,
                    new BigDecimal(remainingAmount).setScale(2, BigDecimal.ROUND_HALF_DOWN),
                    new BigDecimal(summPercent).setScale(2, BigDecimal.ROUND_HALF_DOWN),
                    new BigDecimal(summDebt).setScale(2, BigDecimal.ROUND_HALF_DOWN),
                    new BigDecimal(mp).setScale(2, BigDecimal.ROUND_HALF_DOWN)));
            remainingAmount -= summDebt;
            totalDebtPayment += summDebt;
            totalInterestPayment += summPercent;
        }
        return list;
    }
}
