package resources;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

public class EmailSender {

    private static final String SMTP_SERVER = "smtp.live.com";
    private static final String USERNAME = "neutralCreepManagement@hotmail.com";
    private static final String PASSWORD = "<123456>";
    private static final String EMAIL_FROM = "neutralCreepManagement@hotmail.com";
    private static final String EMAIL_SUBJECT = "User Account Creation";
    String EMAIL_TO, EMAIL_TEXT;
	
    public EmailSender(String sendTo, String password) {
		EMAIL_TO = sendTo;
	    EMAIL_TEXT = "Hello from Neutral Creep Employer Management System \n Your Login Credentials is: " + password;
	}
    
    public void send() {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.host", "smtp.live.com");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "25"); // default port 25

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {
		
			// from
            msg.setFrom(new InternetAddress(EMAIL_FROM));

			// to 
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));

			// subject
            msg.setSubject(EMAIL_SUBJECT);
			
			// content 
            msg.setText(EMAIL_TEXT);
			
            msg.setSentDate(new Date());

			// Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
			// connect
            System.out.println("Authenticating");
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
            System.out.println("Authenticated");
			
			// send
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    	
    }
}
