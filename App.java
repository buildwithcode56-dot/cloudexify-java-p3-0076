package com.mycompany.employeemanagementsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class App extends Application 
{
    private final EmployeeApp employeeApp = new EmployeeApp();
    private TextField codeInput;
    private TextField nameInput;
    private TextField salaryInput;
    private TextField extraInput;
    private ComboBox<String> typeSelector;

    private Label extraLabel;
    private Label messageLabel;
    private TableView<Employee> employeeTable;
    private final ObservableList<Employee> tableData =FXCollections.observableArrayList();
    private Button addButton;
    private Button saveButton;

    public void start(Stage stage) 
    {
        employeeApp.loadEmployees();
        Label heading = new Label("Employee Management System");
        heading.setStyle( "-fx-font-size: 26px;" +"-fx-font-weight: bold;");

        codeInput = new TextField();
        codeInput.setPromptText("Employee code");
        nameInput = new TextField();
        nameInput.setPromptText("Full name");

        salaryInput = new TextField();
        salaryInput.setPromptText("Monthly salary");
        typeSelector = new ComboBox<>();
        typeSelector.getItems().addAll("Manager","Developer","HR");
        typeSelector.setPromptText("Select employee type");

        extraLabel = new Label("Additional information:");
        extraInput = new TextField();
        extraInput.setPromptText("Enter value");

        typeSelector.setOnAction(event ->
                updateExtraField()
        );
        GridPane form = createForm();
        
        addButton =new Button("Add Employee");
        Button editButton =new Button("Edit Employee");
        saveButton =new Button("Save Changes");
        Button deleteButton =new Button("Delete Employee");
        Button sortButton =new Button("Sort by Salary");
        Button clearButton =new Button("Clear");           
        Button exitButton =new Button("Exit");
        saveButton.setDisable(true);
        
        addButton.setOnAction(
                event -> createEmployee()
        );
        sortButton.setOnAction(
                event -> sortTable()
        );       
        clearButton.setOnAction(
                event -> clearForm()
        );        
        deleteButton.setOnAction(
        event -> deleteSelectedEmployee()
        );
        exitButton.setOnAction(
        event -> confirmExit(stage)
        );
        editButton.setOnAction(
        event -> editSelectedEmployee()
        );

        saveButton.setOnAction(
        event -> saveEditedEmployee()
        );

        HBox buttons = new HBox(12,addButton,editButton,saveButton,sortButton,deleteButton,clearButton,exitButton);
        buttons.setAlignment(Pos.CENTER);

        messageLabel = new Label("Ready to add employees.");
        employeeTable = buildTable();
        refreshTable();

        VBox root = new VBox(18,heading,form,buttons,messageLabel,employeeTable);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(employeeTable,Priority.ALWAYS);

        Scene scene = new Scene(root,950,650);
        stage.setTitle("Employee Management System");
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createForm()
    {
        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);

        form.add(new Label("Employee Code:"),0, 0);
        form.add(codeInput, 1, 0);
        form.add(new Label("Full Name:"), 0, 1);
        form.add(nameInput, 1, 1);
        form.add(new Label("Monthly Salary:"),0, 2);
        form.add(salaryInput,1, 2);
        form.add(new Label("Employee Type:"),0, 3);
        form.add(typeSelector,1, 3);
        form.add(extraLabel,0, 4);
        form.add(extraInput,1, 4);
        return form;
    }

    private void updateExtraField()
    {
        String type = typeSelector.getValue();
        extraInput.clear();
        if ("Manager".equals(type))
        {
            extraLabel.setText("Performance Bonus:");
            extraInput.setPromptText("Enter bonus");
        } 
        else if ("Developer".equals(type)) 
        {
            extraLabel.setText("Experience (years):");
            extraInput.setPromptText("Enter years");
        } 
        else 
        {
            extraLabel.setText( "Additional information:");
            extraInput.setPromptText("Not required");
        }
    }

    private void createEmployee()
    {
        try 
        {
            String code =codeInput.getText().trim();
            String name =nameInput.getText().trim();
            String salaryText =salaryInput.getText().trim();
            String type =typeSelector.getValue();

            if (code.isEmpty()|| name.isEmpty()|| salaryText.isEmpty()|| type == null)
            {
                showMessage("Please complete all required fields.");
                return;
            }

            double salary = Double.parseDouble(salaryText);
            Employee employee;
            switch (type)
            {
                case "Manager":
                    double bonus =Double.parseDouble(extraInput.getText().trim() );
                    employee = new Manager(code,name,salary, bonus);
                    break;
                case "Developer":
                    int years =Integer.parseInt(extraInput.getText().trim());
                    employee = new Developer(code,name, salary,years);
                    break;
                case "HR":
                    employee = new HR(code,name,salary);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown employee type.");
            }
            employeeApp.register(employee);
            employeeApp.saveEmployees();
            refreshTable();
            showMessage("Employee added successfully.");
            clearForm();

        } catch (NumberFormatException e)
        {
            showMessage("Please enter valid numeric values.");
        }
        catch (IllegalArgumentException e)
        {
            showMessage(e.getMessage());
        }
    }

    private TableView<Employee> buildTable()
    {
        TableView<Employee> table =new TableView<>();
        table.setItems(tableData);

        TableColumn<Employee, String> codeColumn =new TableColumn<>("Code");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeCode"));
        
        TableColumn<Employee, String> nameColumn =new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Employee, String> departmentColumn =new TableColumn<>("Division");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("division"));

        TableColumn<Employee, String> typeColumn =new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmployeeType())
        );

        TableColumn<Employee, Double> salaryColumn =
        new TableColumn<>("Total Pay");

       salaryColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getTotalPay())
        );

       salaryColumn.setCellFactory(column -> new TableCell<Employee, Double>()
        {
            protected void updateItem(Double item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty || item == null) 
                {
                    setText(null);
                }
                else
                {
                    setText(String.format("%,.0f",item) );
                }
            }
         }
        );

       table.getColumns().addAll(codeColumn,nameColumn,departmentColumn,typeColumn,salaryColumn);
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );
        return table;
    }

    private void refreshTable() 
    {
        tableData.setAll(
                employeeApp.getEmployees()
        );
    }

    private void sortTable()
    {
        employeeApp.arrangeByPay();
        refreshTable();
        showMessage("Employees arranged by total pay.");
    }

    private void clearForm() 
    {
        codeInput.clear();
        nameInput.clear();
        salaryInput.clear();
        extraInput.clear();

        typeSelector.setValue(null);
        extraLabel.setText("Additional information:" );
        extraInput.setPromptText("Enter value");
    }

    private void showMessage(String message)
    {
        messageLabel.setText(message);
    }
    private void deleteSelectedEmployee()
    {
      Employee selectedEmployee =employeeTable.getSelectionModel().getSelectedItem();
      if (selectedEmployee == null)
      {
        showMessage("Please select an employee to delete.");
        return;
      }

      Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

      confirmation.setTitle("Delete Employee");
      confirmation.setHeaderText(null);
      confirmation.setContentText("Are you sure you want to delete this employee?");

      ButtonType result = confirmation.showAndWait().orElse(ButtonType.CANCEL);

      if (result == ButtonType.OK)
      {
         boolean removed =employeeApp.removeEmployee(selectedEmployee.getEmployeeCode());
         if (removed) 
         {
            employeeApp.saveEmployees();
            refreshTable();
            showMessage("Employee deleted successfully.");
         }
      }
    }
    
    private void editSelectedEmployee()
    {
       Employee selectedEmployee =employeeTable.getSelectionModel().getSelectedItem();
       if (selectedEmployee == null)
       {
         showMessage("Please select an employee to edit.");
         return;
       }

      codeInput.setText(selectedEmployee.getEmployeeCode());
      codeInput.setDisable(true);
      nameInput.setText(selectedEmployee.getFullName());
      salaryInput.setText(String.valueOf(selectedEmployee.getMonthlyPay()));

      String employeeType =selectedEmployee.getEmployeeType();
      typeSelector.setValue(employeeType);
      updateExtraField();

      if (selectedEmployee instanceof Manager) 
      {
         Manager manager =(Manager) selectedEmployee;
         extraInput.setText(String.valueOf(manager.getPerformanceBonus()));
      } 
      else if (selectedEmployee instanceof Developer)
      {
         Developer developer =(Developer) selectedEmployee;
          extraInput.setText(
                String.valueOf(
                        developer.getExperience()));
      }
      else 
      {
         extraInput.clear();
      }
      saveButton.setDisable(false);
      addButton.setDisable(true);
      showMessage("Employee loaded. Make your changes and save.");
    }
    
    private void saveEditedEmployee() 
    {
      Employee selectedEmployee =employeeTable.getSelectionModel().getSelectedItem();
      if (selectedEmployee == null) 
      {
         showMessage("Please select an employee.");
         return;
      }
     try 
     {
        String code =codeInput.getText().trim();
        String name =nameInput.getText().trim();
        double salary =Double.parseDouble(salaryInput.getText().trim());
        String type =typeSelector.getValue();
        if (code.isEmpty()|| name.isEmpty()|| type == null) 
        {
            showMessage("Please complete all required fields.");
            return;
        }

        Employee updatedEmployee;
        switch (type)
        {
            case "Manager":
                double bonus = Double.parseDouble(extraInput.getText().trim());
                updatedEmployee =new Manager( code, name, salary,bonus);
                break;
            case "Developer":
                int years = Integer.parseInt(extraInput.getText().trim());
                updatedEmployee = new Developer( code,name, salary, years);
                break;
            case "HR":
                updatedEmployee =new HR(code,name,salary);
                break;
            default:
                throw new IllegalArgumentException( "Invalid employee type.");
        }

        boolean updated = employeeApp.updateEmployee(selectedEmployee.getEmployeeCode(),updatedEmployee);
        if (updated)
        {
            employeeApp.saveEmployees();
            refreshTable();
            saveButton.setDisable(true);
            addButton.setDisable(false);
            codeInput.setDisable(false);

            clearForm();
            showMessage("Employee updated successfully.");
        }
     } 
     catch (NumberFormatException e)
     {
        showMessage("Please enter valid numeric values.");
     } 
     catch (IllegalArgumentException e) 
     {
        showMessage(e.getMessage());
     }
    }
    
    private void confirmExit(Stage stage)
    {
      Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
      confirmation.setTitle("Exit");
      confirmation.setHeaderText(null);
      confirmation.setContentText("Are you sure you want to exit?");
      ButtonType result =confirmation.showAndWait().orElse(ButtonType.CANCEL);
      if (result == ButtonType.OK)
      {
        stage.close();
      }
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}
