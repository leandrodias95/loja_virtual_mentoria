package jdev.mentoria.lojavirtual.service;

import javax.mail.PasswordAuthentication;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ServiceEnvioEmail {
	
	private String userName = "leandrobatista192@gmail.com"; 
	private String senha = "axjq ifun oxwb rmag";
	
	@Async
	public void enviarEmailHtml(String assunto, String mensagem, String emailDestino) throws UnsupportedEncodingException, MessagingException {
		Properties properties =  new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls", "false");
		properties.put("mail.smtp.ssl.host", "smtp.gmail.com");
		properties.put("mail.smtp.ssl.port", "465");
		properties.put("mail.smtp.ssl.socketFacotry.port", "465");
		properties.put("mail.smtp.ssl.socketFacotry.class", "javax.net.ssl.SSLSocketFactory" );
		
		
		Session session = Session.getInstance(properties, new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("userName", "senha");
		}
		});
		session.setDebug(true);
		Address[] toUser = InternetAddress.parse(emailDestino); 
		Message message =  new MimeMessage(session); 
		message.setFrom(new InternetAddress(userName, "Leandro - Monitoria E-Commerce", "UTF-8")); 
		message.setRecipients(Message.RecipientType.TO, toUser);
		message.setSubject(assunto);
		message.setContent(message, "text/html; charset=utf-8");
		Transport.send(message);
	}

}
