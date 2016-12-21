package com.notifier.dataService.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

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
public class MailServiceImpl implements MailService{
	
	 @Autowired
	 JavaMailSender mailSender;
	 
	 @Autowired
	 Configuration freemarkerConfiguration;

	@Override
	public void sendMail(TaskModel taskmodel) {
		
		List<TaskRecipientModel> recipientlist = taskmodel.getRecipientList();
		for(TaskRecipientModel reciepientmodel : recipientlist){
			MimeMessagePreparator preparator = getMessagePreparator(taskmodel, reciepientmodel);
	         
	        try {
	            mailSender.send(preparator);
	            System.out.println("Message has been sent.............................");
	        }
	        catch (MailException ex) {
	            System.err.println(ex.getMessage());
	        }
		}
		 
		
	}
	
	private MimeMessagePreparator getMessagePreparator(final TaskModel taskmodel, TaskRecipientModel recipientmodel){
        
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
 
        	@Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
  
                helper.setSubject("Task Notification");
                helper.setFrom("rpgayatri@gmail.com");
                
                System.out.println("recipient :: "+recipientmodel.getRecipient());
                helper.setTo(recipientmodel.getRecipient());
      
                Map<String, Object> templatemodel = new HashMap<String, Object>();
                
                Map<String, String> taskmap = new HashMap<String, String>();
                taskmap.put("description", taskmodel.getDescription());
                taskmap.put("title", taskmodel.getTaskTitle());
                taskmap.put("user", recipientmodel.getRecipient());
                
                templatemodel.put("task", taskmap);
   
               /* templatemodel.put("task", taskmodel);
                templatemodel.put("recipients", recipientmodel);*/
             
                String text = geFreeMarkerTemplateContent(templatemodel);
                System.out.println("Template content : "+text);
 
                // use the true flag to indicate you need a multipart message
                helper.setText(text, true);
 
            }

        };
        return preparator;
    }
	
	public String geFreeMarkerTemplateContent(Map<String, Object> model){
        StringBuffer content = new StringBuffer();
        try{
         content.append(FreeMarkerTemplateUtils.processTemplateIntoString( 
                 freemarkerConfiguration.getTemplate("fm_mailTemplate.txt"),model));
         return content.toString();
        }catch(Exception e){
            System.out.println("Exception occured while processing fmtemplate:"+e.getMessage());
        }
          return "";
    }

}
