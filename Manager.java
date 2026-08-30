package com.mycompany.employeemanagementsystem;
public class Manager extends Employee 
{
    private double performanceBonus;
    public Manager(String employeeCode, String fullName,double monthlyPay, double performanceBonus) 
    {
        super(employeeCode,fullName,monthlyPay,"Management");
        if (performanceBonus < 0)
        {
            throw new IllegalArgumentException("Manager bonus cannot be negative.");
        }
        this.performanceBonus = performanceBonus;
    }

    public double getTotalPay() 
    {
        return getMonthlyPay() + performanceBonus;
    }
    public double getPerformanceBonus() 
    {
        return performanceBonus;
    }
}
