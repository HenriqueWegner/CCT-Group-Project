package com.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.saturn.dao.DatabaseConnection;
import com.saturn.model.employee.Employee;
import com.saturn.model.training.EmployeeHSE;
import com.saturn.model.training.EmployeeSeaChange;
import com.saturn.model.training.EmployeeTraining;
import com.saturn.model.training.EmployeeVirtualAcademy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmployeeTrainingController implements Initializable {

	@FXML
	private ChoiceBox<String> choicebox;

	@FXML
	private TableView<EmployeeTraining> tableView;

	@FXML
	private TableColumn<EmployeeTraining, String> training;

	@FXML
	private TableColumn<EmployeeTraining, String> description;

	@FXML
	private TableColumn<EmployeeTraining, String> status;

	@FXML
	private TableColumn<EmployeeTraining, LocalDate> date;

	private List<Employee> empList = new ArrayList<>();


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ObservableList<String> list = FXCollections.observableArrayList();

		empList.addAll(DatabaseConnection.loadAllData(Employee.class));

		for (Employee e : empList) {
			list.add(e.getFirstName() + " " + e.getLastName());
		}
		choicebox.setItems(list);
	}

	// this method fills up the table with the employee information;
	@FXML
	private void fillUpTable() {
		
		// it retrieves all the training items in the database
		List<EmployeeTraining> employeeTrainingList = new ArrayList<>();
		
		// it will retrieve all the training related to employee id get from the choice box
		List<EmployeeTraining>empTraining = new ArrayList<>();
		
		// it gets the first name and last name in the choice box and query the database to get the employee with the given name
		String name = choicebox.getValue();
		int id = 0;
		List<Employee> emp = DatabaseConnection
				.queryDatabase("from Employee WHERE CONCAT(first_name,' ',last_name)='"+name+"'");
		
		// if the list is empty it returns from the method so there won't be an exception
		if(emp.size()==0) {
			return;
		}else {
			id = emp.get(0).getId();
		}
		
		// it retrieves all the training from the database
		employeeTrainingList.addAll(DatabaseConnection.loadAllData(EmployeeSeaChange.class));
		employeeTrainingList.addAll(DatabaseConnection.loadAllData(EmployeeHSE.class));
		employeeTrainingList.addAll(DatabaseConnection.loadAllData(EmployeeVirtualAcademy.class));
		
		// it adds all the training related to employee id retrieved from the choice box
		for(EmployeeTraining training:employeeTrainingList) {
			if(training.getEmployee().getId()==id) {
				empTraining.add(training);
			}
		}

		// it sets the value in the table
		training.setCellValueFactory(new PropertyValueFactory<EmployeeTraining, String>("className"));
		description.setCellValueFactory(new PropertyValueFactory<EmployeeTraining, String>("training"));
		status.setCellValueFactory(new PropertyValueFactory<EmployeeTraining, String>("status"));
		date.setCellValueFactory(new PropertyValueFactory<EmployeeTraining, LocalDate>("date"));

		tableView.getItems().setAll(empTraining);
	}
}