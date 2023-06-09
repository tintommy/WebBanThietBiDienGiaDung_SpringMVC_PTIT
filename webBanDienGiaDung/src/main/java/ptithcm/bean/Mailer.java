package ptithcm.bean;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("mailer")
 public class Mailer{
       @Autowired
       JavaMailSender mailer;
       public void send(String from, String to, String subject, String body) {
    	   
    	   try{
                    MimeMessage mail =mailer.createMimeMessage();
                    MimeMessageHelper helper
                                 = new MimeMessageHelper(mail, true, "utf-8");
                    helper.setFrom(from, from);
                    helper.setTo(to);
                    helper.setReplyTo(from, from);
                    helper.setSubject(subject);
                    helper.setText(body, true);
              
                    mailer.send(mail);
             }
             catch(Exception ex){
                    throw new RuntimeException(ex);
             }
 }
       public void sendMailAsync(String from, String to, String subject, String body) {
    	   
    	   ExecutorService executorService=Executors.newSingleThreadExecutor();
    	   executorService.submit(() ->{
    		   send(from, to, subject, body);
    	   });
       }
       
}
