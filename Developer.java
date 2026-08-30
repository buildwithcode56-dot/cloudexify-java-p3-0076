package com.mycompany.employeemanagementsystem;
public class Developer extends Employee
{
    private int experience;
    public Developer(String employeeCode, String fullName,double monthlyPay, int experience) {
        super(employeeCode,fullName,monthlyPay,"Software Development");
        if (experience < 0) 
        {
            throw new IllegalArgumentException("Experience cannot be negative.");
        }
        this.experience = experience;
    }

    public double getTotalPay()
    {
        if (experience >= 5)
        {
            return getMonthlyPay() * 1.20;
        }
        return getMonthlyPay() * 1.10;
    }
    
    public int getExperience() 
    {
        return experience;
    }
}
