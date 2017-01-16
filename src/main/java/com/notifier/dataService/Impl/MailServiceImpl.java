package com.notifier.dataService.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.notifier.dataService.MailService;
import com.notifier.model.TaskModel;
import com.notifier.model.TaskRecipientModel;

import freemarker.template.Configuration;

@Service
public class MailServiceImpl implements MailService {
	
	private static final Logger logger = Logger.getLogger(MailServiceImpl.class.getName());

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	Configuration freemarkerConfiguration;

	@Override
	public void sendMail(TaskModel taskmodel) {

		List<TaskRecipientModel> recipientlist = taskmodel.getRecipientList();
		for (TaskRecipientModel reciepientmodel : recipientlist) {

			if (reciepientmodel.getRecipientType().equals("user")) {
				String recipientEmail = reciepientmodel.getRecipient();
				MimeMessagePreparator preparator = getMessagePreparator(
						taskmodel, reciepientmodel, recipientEmail);

				try {
					mailSender.send(preparator);
					
				} catch (MailException ex) {
					logger.error(ex.getMessage());
				}
			} else {
				String[] emailarr = reciepientmodel.getRecipient().split(",");
				for (String email : emailarr) {
					MimeMessagePreparator preparator = getMessagePreparator(
							taskmodel, reciepientmodel, email);

					try {
						mailSender.send(preparator);
					} catch (MailException ex) {
						logger.error(ex.getMessage());
					}
				}
			}

		}

	}

	private MimeMessagePreparator getMessagePreparator(
			final TaskModel taskmodel, TaskRecipientModel recipientmodel,
			String recipientEmail) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
						true);
				helper.setSubject("Task Notification");
				helper.setFrom(taskmodel.getTaskCreator());
				logger.info("recipient :: " + recipientEmail);
				helper.setTo(recipientEmail);

				Map<String, Object> templatemodel = new HashMap<String, Object>();

				Map<String, String> taskmap = new HashMap<String, String>();
				
				
				taskmap.put("taskdescription", taskmodel.getDescription());
				taskmap.put("title", taskmodel.getTaskTitle());
				taskmap.put("username", recipientEmail);

				templatemodel.put("task", taskmap);

				String text = geFreeMarkerTemplateContent(templatemodel);
				// use the true flag to indicate you need a multipart message
				helper.setText(text, true);				
			}

		};
		return preparator;
	}

	public String geFreeMarkerTemplateContent(Map<String, Object> model) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
					freemarkerConfiguration.getTemplate("fm_mailTemplate.txt"),
					model));
			return content.toString();
		} catch (Exception e) {
			logger.error("Exception occured while processing fmtemplate:"
					+ e.getMessage());
		}
		return "";
	}

}
