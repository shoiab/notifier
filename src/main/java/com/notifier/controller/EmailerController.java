package com.notifier.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.notifier.dataService.MailService;
import com.notifier.model.TaskModel;

@RestController
public class EmailerController {
	
	@Autowired
	MailService mailservice;
	
	@RequestMapping (value = "/sendEmail", method=RequestMethod.POST)
	public @ResponseBody JSONObject sendEmail(@RequestBody String taskstr){
		
		TaskModel taskmodel = new TaskModel();

		Gson gson = new Gson(); 
		taskmodel = gson.fromJson(taskstr, TaskModel.class);
		
		mailservice.sendMail(taskmodel);
		
		JSONObject statusobj = new JSONObject();
		statusobj.put("status", HttpStatus.OK.value());
		statusobj.put("message", "Mail sent successfully");
		return statusobj;	
	}

}
