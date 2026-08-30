
package com.mycompany.employeemanagementsystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EmployeeApp 
{
    private final ArrayList<Employee> staffMembers;
    private static final String DATA_FILE = "employees.txt";
    
    public EmployeeApp()
    {
        staffMembers = new ArrayList<>();
    }
    public void register(Employee employee)
    {
    if (employee == null)
    {
        throw new IllegalArgumentException("Employee cannot be null.");
    }

    for (Employee existingEmployee : staffMembers)
    {
        if (existingEmployee.getEmployeeCode()
                .equalsIgnoreCase(employee.getEmployeeCode()))
        {
            throw new IllegalArgumentException(
                    "Employee code already exists."
            );
        }
    }

    staffMembers.add(employee);
}
    
    public void saveEmployees() 
    {
       try (BufferedWriter writer =new BufferedWriter(new FileWriter(DATA_FILE))) 
       {
         for (Employee employee : staffMembers)
           {
              writer.write(
              employee.getEmployeeType() + "|" +
              employee.getEmployeeCode() + "|" +
              employee.getFullName() + "|" +
              employee.getMonthlyPay() + "|" +
              employee.getDivision()
);
              if (employee instanceof Manager)
              {
                Manager manager = (Manager) employee;
                writer.write("|"+ manager.getPerformanceBonus());

              }
              else if (employee instanceof Developer) 
              {

                Developer developer =(Developer) employee;
                writer.write( "|" + developer.getExperience() );
              }
            writer.newLine();
           }

        }
       catch (IOException e)
       {
        System.out.println("Unable to save employee data: " + e.getMessage());
       }
    }
    
    public void loadEmployees() 
    {
      staffMembers.clear();
      try (BufferedReader reader =new BufferedReader(new FileReader(DATA_FILE)))
       {
        String line;
        while ((line = reader.readLine()) != null) 
        {
            if (line.trim().isEmpty())
            {
                continue;
            }
            String[] data = line.split("\\|");
            String type = data[0];
            String code = data[1];
            String name = data[2];
            double salary = Double.parseDouble(data[3]);

            Employee employee;
            switch (type) 
            {
                case "Manager": 
                    double bonus =Double.parseDouble(data[5]);
                    employee = new Manager(code,name,salary, bonus);
                    break;
                case "Developer": 
                    int experience =Integer.parseInt(data[5]);
                    employee = new Developer(code,name,salary,experience);
                    break;
                case "HR": 
                    employee = new HR( code, name,salary);
                    break;
                default:
                    continue;
            }
            staffMembers.add(employee);
        }

     } 
      catch (IOException e)
      {
        System.out.println( "No saved employee data found yet.");

      }
      catch (NumberFormatException e)
      {
        System.out.println("Saved employee data is invalid.");
      }
    }
    
    public boolean updateEmployee(String employeeCode,Employee replacement)
    {

       for (int i = 0; i < staffMembers.size(); i++)
       {

           if (staffMembers.get(i).getEmployeeCode().equalsIgnoreCase(employeeCode)) 
           {
            staffMembers.set(i, replacement);
            return true;
           }
        }
    return false;
    }

    public List<Employee> getEmployees() 
    {
        return new ArrayList<>(staffMembers);
    }
    public void arrangeByPay() 
    {
        staffMembers.sort(Comparator.comparingDouble(Employee::getTotalPay));
    }

    public boolean isEmpty()
    {
        return staffMembers.isEmpty();
    }
    public int getEmployeeCount() 
    {
        return staffMembers.size();
    }
    public void removeAll() 
    {
        staffMembers.clear();
    }
    public boolean removeEmployee(String employeeCode) 
    {
    return staffMembers.removeIf(employee ->employee.getEmployeeCode().equalsIgnoreCase(employeeCode));
    }
}
