package com.mycompany.employeemanagementsystem;
public abstract class Employee 
{
    private final String employeeCode;
    private String fullName;
    private double monthlyPay;
    private String division;

    public Employee(String employeeCode, String fullName,double monthlyPay, String division) 
    {
        if (monthlyPay <= 0)
        {
            throw new IllegalArgumentException("Monthly salary must be greater than zero.");
        }
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.monthlyPay = monthlyPay;
        this.division = division;
    }
    
    public String getEmployeeCode() 
    {
        return employeeCode;
    }
    public String getFullName() 
    {
        return fullName;
    }
    public double getMonthlyPay() 
    {
        return monthlyPay;
    }
    public String getDivision() 
    {
        return division;
    }
    public abstract double getTotalPay();
    public String getEmployeeType() 
    {
        return getClass().getSimpleName();
    }
}

