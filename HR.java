package com.mycompany.employeemanagementsystem;
public class HR extends Employee {

    private static final double HR_ALLOWANCE = 0.05;

    public HR(String employeeCode, String fullName,double monthlyPay) 
    {
        super(employeeCode,fullName,monthlyPay,"Human Resources");
    }

    public double getTotalPay()
    {
        return getMonthlyPay()+(getMonthlyPay()* HR_ALLOWANCE);
    }
}
