package com.notifier.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailerController {
	
	@RequestMapping (value = "/sendEmail", method=RequestMethod.POST)
	public @ResponseBody String sendEmail(@RequestBody String taskstr){
		
		return taskstr;	
	}

}
