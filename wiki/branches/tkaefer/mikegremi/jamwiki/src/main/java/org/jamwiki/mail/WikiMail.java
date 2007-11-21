/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the latest version of the GNU Lesser General
 * Public License as published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (LICENSE.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jamwiki.mail;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.jamwiki.Environment;
import org.jamwiki.utils.WikiLogger;

/**
 * Sends mail via SMTP to the specified host. <b>REDISTRIBUTION:</b> you
 * will either have to hard-code your own SMTP host name into the constructor
 * function and recompile, or rewrite the Environment class to record
 * this information in the jamwiki.properties file.
 */
public class WikiMail {

	private static final WikiLogger logger = WikiLogger.getLogger(WikiMail.class.getName());
	
        private final Session session;

        /**
	 * Construct the object by opening a JavaMail session. Use getInstance to provide Singleton behavior.
	 */
	public WikiMail(String host, int port, final String account, final String password) {
                logger.fine("Host=" + host);
                logger.fine("Port=" + port);
                logger.fine("Account=" + account);
                logger.fine("Pasword=" + password);
                
                Properties props = new Properties();
                props.setProperty("mail.smtp.host", host); 
                props.setProperty("mail.smtp.port", Integer.toString(port));
                props.setProperty("mail.smtp.auth" , "true"); 
                session = Session.getInstance(props, new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(account, password);
                    }
                });
                if (logger.isLoggable(Level.FINE)) {
                        session.setDebug(true);
                        session.setDebugOut(System.out);
                        //TODO: redirect to logging system?
                }
	}

	/**
	 * Send mail via SMTP. MessagingExceptions are silently dropped.
	 *
	 * @param from the RFC 821 "MAIL FROM" parameter
	 * @param to the RFC 821 "RCPT TO" parameter
	 * @param subject the RFC 822 "Subject" field
	 * @param body the RFC 822 "Body" field
	 */
	public void sendMail(String from, String to, String subject, String body) {
                logger.info("From:" + from);
                logger.info("To:" + to);
                logger.info("Subject:" + subject);
                logger.info("Body:" + body);
		try {
			MimeMessage message = new MimeMessage(session);
			InternetAddress internetAddress = new InternetAddress(from);
			message.setFrom(internetAddress);
			message.setReplyTo(new InternetAddress[]{internetAddress});
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(body);
			message.setSentDate(new Date());
			message.saveChanges();
                        
			Transport.send(message);
		} catch (MessagingException e) {
			logger.warning("Mail error", e);
		}
	}

}
