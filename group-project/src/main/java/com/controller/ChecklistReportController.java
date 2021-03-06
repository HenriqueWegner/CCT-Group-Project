package com.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.saturn.model.checklists.ChecklistSuperClass;
import com.saturn.model.checklists.CoffeeHACCP;
import com.saturn.model.checklists.DeliHACCP;
import com.saturn.model.checklists.FireWarden;
import com.saturn.model.checklists.FloorHACCP;
import com.saturn.model.checklists.Frequency;
import com.saturn.model.checklists.HealthSafetyChecklist;
import com.saturn.model.reports.ChecklistReport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class ChecklistReportController implements Initializable {

	@FXML
	private ChoiceBox<String> checklist;

	@FXML
	private ChoiceBox<String> status;

	@FXML
	private ChoiceBox<String> frequency;

	@FXML
	private ChoiceBox<String> checklistDate;

	private String All = "All";

	// it initialises the choice boxes with lists
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> list = FXCollections.observableArrayList();
		list.addAll(All, "Coffee HACCP", "Deli HACCP", "Fire Warden", "Floor HACCP", "Health and Safety");
		checklist.setItems(list);
		checklist.setValue("All");

		ObservableList<String> list1 = FXCollections.observableArrayList();
		list1.addAll(All, "Pending", "Done");
		status.setItems(list1);
		status.setValue("Pending");

		ObservableList<String> list2 = FXCollections.observableArrayList();
		list2.addAll(All, Frequency.DAILY.getFrequency(), Frequency.WEEKLY.getFrequency(),
				Frequency.BIWEEKLY.getFrequency(), Frequency.MONTHLY.getFrequency(),
				Frequency.SEMIANNUAL.getFrequency(), Frequency.YEARLY.getFrequency());
		frequency.setItems(list2);
		frequency.setValue("All");

	}

	// it generates a checklist report according to the user input
	@FXML
	private void generateReport() {

		String checkList = checklist.getValue();
		String statusString = status.getValue();
		String frequencyString = frequency.getValue();

		Dao dao = new Dao();
		List<ChecklistSuperClass> list = new ArrayList<>();
		List<ChecklistSuperClass> custom = new ArrayList<>();

		list.addAll(dao.loadChecklistItems());

		for (ChecklistSuperClass check : list) {

			if (check instanceof FireWarden) {
				check.setClassName("Fire Warden");
			} else if (check instanceof HealthSafetyChecklist) {
				check.setClassName("Health and Safety");
			} else if (check instanceof DeliHACCP) {
				check.setClassName("Deli HACCP");
			} else if (check instanceof FloorHACCP) {
				check.setClassName("Floor HACCP");
			} else if (check instanceof CoffeeHACCP) {
				check.setClassName("Coffee HACCP");
			}

			// if statements that allow the user to customise the report
			
			if (checkList.matches(All) && statusString.matches(All) && frequencyString.matches(All)) {
				custom.add(check);
				continue;

			} else if (checkList.matches(All) && statusString.matches(All) && !frequencyString.matches(All)) {

				if (check.getFrequency().matches(frequencyString)) {
					custom.add(check);
					continue;
				}

			} else if (checkList.matches(All) && !statusString.matches(All) && frequencyString.matches(All)) {

				if (check.getStatus().matches(statusString)) {
					custom.add(check);
					continue;
				}

			} else if (checkList.matches(All) && !statusString.matches(All) && !frequencyString.matches(All)) {

				if (check.getStatus().matches(statusString) && check.getFrequency().matches(frequencyString)) {
					custom.add(check);
					continue;
				}

			} else if (!checkList.matches(All) && statusString.matches(All) && frequencyString.matches(All)) {

				if (check.getClassName().matches(checkList)) {
					custom.add(check);
					continue;
				}

			} else if (!checkList.matches(All) && !statusString.matches(All) && frequencyString.matches(All)) {

				if (check.getClassName().matches(checkList) && check.getStatus().matches(statusString)) {
					custom.add(check);
					continue;
				}

			} else if (!checkList.matches(All) && !statusString.matches(All) && !frequencyString.matches(All)) {

				if (check.getClassName().matches(checkList) && check.getStatus().matches(statusString)
						&& check.getFrequency().matches(frequencyString)) {
					custom.add(check);
					continue;
				}
			}

		}

		new ChecklistReport(custom, "Checklist Report", "Checklist.pdf", "Checklist.jasper");
	}

	// it generates all the checklist items that must be done in the day
	@FXML
	private void generateTodayReport() {

		Dao dao = new Dao();
		List<ChecklistSuperClass> list = new ArrayList<>();
		List<ChecklistSuperClass> custom = new ArrayList<>();

		list.addAll(dao.loadChecklistItems());

		for (ChecklistSuperClass item : list) {
			
			if (item instanceof FireWarden) {
				item.setClassName("Fire Warden");
			} else if (item instanceof HealthSafetyChecklist) {
				item.setClassName("Health and Safety");
			} else if (item instanceof DeliHACCP) {
				item.setClassName("Deli HACCP");
			} else if (item instanceof FloorHACCP) {
				item.setClassName("Floor HACCP");
			} else if (item instanceof CoffeeHACCP) {
				item.setClassName("Coffee HACCP");
			}

			if (item.getDueDate() == null) {
				continue;
			}

			if (item.getStatus().matches("Pending") && item.getDueDate().equals(LocalDate.now())) {
				custom.add(item);
			}
		}

		new ChecklistReport(custom, "Checklist Report", "Checklist.pdf", "Checklist.jasper");
	}
	
	// it generates a report of all late checklist items

	@FXML
	private void generateLate() {
		Dao dao = new Dao();
		List<ChecklistSuperClass> list = new ArrayList<>();
		List<ChecklistSuperClass> custom = new ArrayList<>();

		list.addAll(dao.loadChecklistItems());

		for (ChecklistSuperClass item : list) {
			
			if (item instanceof FireWarden) {
				item.setClassName("Fire Warden");
			} else if (item instanceof HealthSafetyChecklist) {
				item.setClassName("Health and Safety");
			} else if (item instanceof DeliHACCP) {
				item.setClassName("Deli HACCP");
			} else if (item instanceof FloorHACCP) {
				item.setClassName("Floor HACCP");
			} else if (item instanceof CoffeeHACCP) {
				item.setClassName("Coffee HACCP");
			}

			if (item.getDueDate() == null) {
				continue;
			}
			LocalDate current = LocalDate.now();
			if (item.getStatus().matches("Pending") && item.getDueDate().isBefore(current)) {
				custom.add(item);
			}
		}

		new ChecklistReport(custom, "Checklist Report", "Checklist.pdf", "Checklist.jasper");
	}
	
	// it generates all pending checklist items from the day and backwards
	@FXML
	protected void generateTodayLate() {
		Dao dao = new Dao();
		List<ChecklistSuperClass> list = new ArrayList<>();
		List<ChecklistSuperClass> custom = new ArrayList<>();

		list.addAll(dao.loadChecklistItems());

		for (ChecklistSuperClass item : list) {
			
			if (item instanceof FireWarden) {
				item.setClassName("Fire Warden");
			} else if (item instanceof HealthSafetyChecklist) {
				item.setClassName("Health and Safety");
			} else if (item instanceof DeliHACCP) {
				item.setClassName("Deli HACCP");
			} else if (item instanceof FloorHACCP) {
				item.setClassName("Floor HACCP");
			} else if (item instanceof CoffeeHACCP) {
				item.setClassName("Coffee HACCP");
			}

			if (item.getDueDate() == null) {
				continue;
			}
			LocalDate current = LocalDate.now();
			if (item.getStatus().matches("Pending") && item.getDueDate().isBefore(current.plusDays(1))) {
				custom.add(item);
			}
		}

		new ChecklistReport(custom, "Checklist Report", "Checklist.pdf", "Checklist.jasper");
	}
}
