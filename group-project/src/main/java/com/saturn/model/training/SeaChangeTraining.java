package com.saturn.model.training;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.saturn.model.employee.Employee;

import lombok.NoArgsConstructor;

@Entity
@Table(name="seachange")
@NoArgsConstructor
public class SeaChangeTraining extends TrainingSuperClass implements Training {

	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE,
			 CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="emp_seachange_training",
			joinColumns=@JoinColumn(name="employee_id"),
			inverseJoinColumns=@JoinColumn(name="training_id")
			)
	List<Employee>seaChangeList;
	
	@OneToMany(mappedBy="seaChange")
	List<EmployeeSeaChange>employeeSeaChange;
	
	public SeaChangeTraining(String trainingDescription) {
		this.setTraining(trainingDescription);
		this.setCreationDate(LocalDate.now());
		this.setClassName(null);
		this.setCheckbox(null);
	}
	
	@Override
	public void addTraining(Employee emp) {

		if(seaChangeList==null) {
			seaChangeList = new ArrayList<>();
			seaChangeList.add(emp);
		}else {
			seaChangeList.add(emp);
		}
	}
	
}
