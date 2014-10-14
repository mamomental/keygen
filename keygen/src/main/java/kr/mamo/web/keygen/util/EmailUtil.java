package kr.mamo.web.keygen.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;


@Component
public class EmailUtil {
	public void send(String from, String to, String msgBody) {
	    try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			Message msg = new MimeMessage(session);
		    msg.setFrom(new InternetAddress(from, "mamokeygen master"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to, to));
		    msg.setSubject("your private code");
		    msg.setText(msgBody);
		    Transport.send(msg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
