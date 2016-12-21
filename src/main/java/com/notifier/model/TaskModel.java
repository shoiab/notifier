package com.notifier.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TaskModel {
	
	private String taskid;
	private String taskTitle;
	private String description;
	private Date taskCreationDate;
	private List<TaskRecipientModel> recipientList;
	
	@JsonIgnore
	public String getTaskid() {
		return taskid;
	}
	
	public List<TaskRecipientModel> getRecipientList() {
		return recipientList;
	}

	public void setRecipientList(List<TaskRecipientModel> recipientList) {
		this.recipientList = recipientList;
	}

	@JsonIgnore
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonIgnore
	public Date getTaskCreationDate() {
		return taskCreationDate;
	}
	
	@JsonIgnore
	public void setTaskCreationDate(Date taskCreationDate) {
		this.taskCreationDate = taskCreationDate;
	}
	
	

}
