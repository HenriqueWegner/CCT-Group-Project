package com.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


import com.saturn.dao.DataSource;
import com.saturn.model.checklists.ChecklistSuperClass;
import com.saturn.model.maintenance.Maintenance;
import com.saturn.model.task.Task;

public class Dao {

	private DataSource data;

	protected Dao() {
		data = DataSource.getInstance();
	}

	// it adds a object to the database
	protected void add(Object obj) {

		data.openSession();
		try {
			data.beginTransaction();
			data.add(obj);
			data.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data.closeSession();
		}
	}

	// it retrieves all objects from maintenance
	protected List<Maintenance> loadAllMaintenance() {
		List<Maintenance> list = null;
		data.openSession();
		try {
			data.beginTransaction();
			list = data.loadAll("from Maintenance");
			data.commit();
		} catch (Exception e) {
			data.closeSession();
		}
		return list;
	}

	// it retrieves all objects from Task
	protected List<Task> loadAllTask() {
		List<Task> list = null;
		data.openSession();
		try {
			data.beginTransaction();
			list = data.loadAll("from Task");
			data.commit();
		} catch (Exception e) {
			data.closeSession();
		}
		return list;

	}

	// it retrieves all objects from Task
	protected List<ChecklistSuperClass> loadAllChecklistItems(String hql) {
		List<ChecklistSuperClass> list = null;
		data.openSession();
		try {
			data.beginTransaction();
			list = data.loadAll(hql);
			data.commit();
		} catch (Exception e) {
			data.closeSession();
		}
		return list;

	}

	protected List<ChecklistSuperClass> LoadChecklistItems(){
		
		List<ChecklistSuperClass> list = new ArrayList<>();
		list.addAll(loadAllChecklistItems("from FireWarden"));
		list.addAll(loadAllChecklistItems("from HealthSafetyChecklist"));
		list.addAll(loadAllChecklistItems("from DeliHACCP"));
		list.addAll(loadAllChecklistItems("from FloorHACCP"));
		list.addAll(loadAllChecklistItems("from CoffeeHACCP"));
		
		return list;
	}
	
	// it deletes from the database
	protected void delete(Object obj) {
		data.openSession();
		try {
			data.beginTransaction();
			data.delete(obj);
			data.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data.closeSession();
		}
	}

	// it gets a maintenance object
	protected Maintenance getMaintenance(int id) {
		data.openSession();
		try {
			data.beginTransaction();
			Maintenance maintenance = (Maintenance) data.get(Maintenance.class, id);
			data.commit();
			return maintenance;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data.closeSession();
		}
		return null;
	}

	// it updates a maintenance object
	protected void updateMaintenance(int id, String contractor, String service, LocalDate lastDate,
			LocalDate nextDate) {
		data.openSession();
		try {
			data.beginTransaction();
			Maintenance maintenance = data.getSession().get(Maintenance.class, id);
			maintenance.setCompany(contractor);
			maintenance.setDescription(service);
			maintenance.setLastDate(lastDate);
			maintenance.setNextDate(nextDate);
			data.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data.closeSession();
		}
	}
	
	// it queries the database and returns a list
	protected <T> List<T> queryDB(String query){
		data.openSession();
		List<T> queryResults = new ArrayList<>();

		try {
			data.beginTransaction();
			@SuppressWarnings("unchecked")
			List<T> results = data.getSession().createQuery(query).getResultList();
			queryResults.addAll(results);

			data.commit();
			
			return queryResults;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data.closeSession();;
		}
		return null;
	}
	
	// it loads all the data from a given table
	protected <T> List<T> loadAllData(Class<T> type) {
		data.openSession();
		List<T> list = null;
		try {
			data.beginTransaction();
			CriteriaBuilder builder = data.getSession().getCriteriaBuilder();
			CriteriaQuery<T> criteria = builder.createQuery(type);
			criteria.from(type);
			list = data.getSession().createQuery(criteria).getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data.closeSession();
		}
		return list;
	}

	// it updates the table status column
	protected void updateChecklist(List<ChecklistSuperClass>list){
		
		List<ChecklistSuperClass> update = list;
		
		data.openSession();
		
		try {
			data.beginTransaction();
			
			for(ChecklistSuperClass item: update) {
				
				ChecklistSuperClass obj = (ChecklistSuperClass) data.getSession().get(item.getClass().getName(), item.getId());
				obj.setStatus("Done");
				data.getSession().save(obj);
			}
			data.commit();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			data.closeSession();
		}
		
	}
}
