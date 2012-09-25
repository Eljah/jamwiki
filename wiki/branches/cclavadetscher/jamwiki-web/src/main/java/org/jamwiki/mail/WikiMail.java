package org.jamwiki.mail;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.jamwiki.Environment;
import org.jamwiki.utils.WikiLogger;

import java.util.*;

/**
 * This class is used to send mails from JAMWiki. It is not intended to be
 * used as a mail client.
 * 
 * The configuration of the class is done using a set of properties that are stored in
 * the database (and in jamwiki.properties).
 * @author cclavadetscher
 *
 */
public class WikiMail
{
	private static final WikiLogger logger = WikiLogger.getLogger(WikiMail.class.getName());
	
	/**
	 * The content type for sending plain text messages.
	 */
	public static final String TEXT = "text/plain";
	/**
	 * The content type for sending html documents.
	 */
	public static final String HTML = "text/html";

	//-------------------------------------------------------------------------
	// Public interface
	//-------------------------------------------------------------------------
	
	/**
	 * @param recipients The list of recipients as a String[].
	 * @param subject The subject line.
	 * @param message The message itself.
	 * @param contentType The type of content. Use one of the constants if you
	 * want to explicitly use this parameter. The default value is defined in
	 * the property smtp_default_content_type in the database.
	 * @throws MessagingException If there is any problem sending the mail.
	 */
	public void postMail(String[] recipients,
			             String subject,
			             String message,
			             String contentType)
	throws MessagingException
	{
		postMail(recipients,null,null,subject,message,contentType,null);
	}

	/**
	 * @param recipients The list of recipients as a String[].
	 * @param subject The subject line.
	 * @param message The message itself.
	 * @throws MessagingException If there is any problem sending the mail.
	 */
	public void postMail(String[] recipients,
			             String subject,
			             String message)
	throws MessagingException
	{
		postMail(recipients,null,null,subject,message,null,null);
	}

	/**
	 * @param recipients The list of recipients as a String[].
	 * @param message The message itself.
	 * @throws MessagingException If there is any problem sending the mail.
	 */
	public void postMail(String[] recipients,
                         String message)
	throws MessagingException
	{
		postMail(recipients,null,null,null,message,null,null);
	}

	/**
	 * @param toRecipients The list of recipients as a single String. The mail
	 * addresses must be separated using the separator defined in the property
	 * smtp_addr_separator in the database.
	 * @param subject The subject line.
	 * @param message The message itself.
	 * @param contentType The type of content. Use one of the constants if you
	 * want to explicitly use this parameter. The default value is defined in
	 * the property smtp_default_content_type in the database.
	 * @param attachments A list of names of files that must be attached to the
	 * mail. You must use the absolute name, i.e. including the directory where
	 * the file is located
	 * @throws MessagingException If there is any problem sending the mail.
	 */
    public void postMail(String toRecipients,
    		             String ccRecipients,
    		             String bccRecipients,
                         String subject,
                         String message,
                         String contentType,
                         String[] attachments)
	throws MessagingException
	{
		String SMTP_ADDR_SEPARATOR = Environment.getValue(Environment.PROP_EMAIL_ADDRESS_SEPARATOR);
		String[] toList = toRecipients.split(SMTP_ADDR_SEPARATOR);
		String[] ccList = null;
		if(ccRecipients != null) ccList = ccRecipients.split(SMTP_ADDR_SEPARATOR);
		String[] bccList = null;
		if(bccRecipients != null) bccList = bccRecipients.split(SMTP_ADDR_SEPARATOR);
		postMail(toList,ccList,bccList,subject,message,contentType,attachments);
	}
	
	/**
	 * @param recipients The list of recipients as a single String. The mail
	 * addresses must be separated using the separator defined in the property
	 * smtp_addr_separator in the database.
	 * @param subject The subject line.
	 * @param message The message itself.
	 * @param contentType The type of content. Use one of the constants if you
	 * want to explicitly use this parameter. The default value is defined in
	 * the property smtp_default_content_type in the database.
	 * @throws MessagingException If there is any problem sending the mail.
	 */
	public void postMail(String recipients,
	                     String subject,
	                     String message,
	                     String contentType)
	throws MessagingException
	{
		postMail(recipients,null,null,subject,message,contentType,null);
	}
	
	/**
	 * @param recipients The list of recipients as a single String. The mail
	 * addresses must be separated using the separator defined in the property
	 * smtp_addr_separator in the database.
	 * @param subject The subject line.
	 * @param message The message itself.
	 * @throws MessagingException If there is any problem sending the mail.
	 */
	public void postMail(String recipients,
                         String subject,
                         String message)
	throws MessagingException
	{
		postMail(recipients,null,null,subject,message,null,null);
	}
	
	/**
	 * @param recipients The list of recipients as a single String. The mail
	 * addresses must be separated using the separator defined in the property
	 * smtp_addr_separator in the database.
	 * @param message The message itself.
	 * @throws MessagingException If there is any problem sending the mail.
	 */
	public void postMail(String recipients,
                         String message)
	throws MessagingException
	{
		postMail(recipients,null,null,null,message,null,null);
	}

	/**
	 * The central method for sending mails. The other methods basically
	 * call this one, setting the missing parameters to null.
	 * @param toRecipients The list of recipients as a String[].
	 * @param ccRecipients The list of recipients on CC as a String[].
	 * @param bccRecipients The list of recipients on BCC as a String[].
	 * @param subject The subject line.
	 * @param message The message itself.
	 * @param contentType The type of content. Use one of the constants if you
	 * want to explicitly use this parameter. The default value is defined in
	 * the property smtp_default_content_type in the database.
	 * @param attachments A list of names of files that must be attached to the
	 * mail. You must use the absolute name, i.e. including the directory where
	 * the file is located
	 * @throws MessagingException If there is any problem sending the mail.
	 */
    public void postMail(String[] toRecipients,
    		             String[] ccRecipients,
    		             String[] bccRecipients,
    		             String subject,
    		             String message,
    		             String contentType,
    		             String[] attachments)
    throws MessagingException
    {
    	if(contentType == null || !contentType.equals(WikiMail.TEXT) || !contentType.equals(WikiMail.HTML))
    	{
    		contentType = Environment.getValue(Environment.PROP_EMAIL_DEFAULT_CONTENT_TYPE);
    	}
    	try
    	{
	        //boolean debug = Boolean.valueOf(Environment.PROP_EMAIL_SMTP_REQUIRES_AUTH);
	        // Get the properties
	        String SMTP_HOST      = Environment.getValue(Environment.PROP_EMAIL_SMTP_HOST);
	        int    SMTP_PORT      = Environment.getIntValue(Environment.PROP_EMAIL_SMTP_PORT);
	        String SMTP_USER      = Environment.getValue(Environment.PROP_EMAIL_SMTP_USERNAME);
	        // the data necessary if the smtp server requires smtp authentication.
	        String SMTP_AUTH_HOST = Environment.getValue(Environment.PROP_EMAIL_SMTP_AUTH_HOST);
	        int    SMTP_AUTH_PORT = Environment.getIntValue(Environment.PROP_EMAIL_SMTP_AUTH_PORT);
	        String SMTP_AUTH_USER = Environment.getValue(Environment.PROP_EMAIL_SMTP_USERNAME);
	        String SMTP_AUTH_PASS = Environment.getValue(Environment.PROP_EMAIL_SMTP_PASSWORD);
	        String SMTP_AUTH_FROM = Environment.getValue(Environment.PROP_EMAIL_REPLY_ADDRESS);
	        boolean smtpAuthentication = Environment.getBooleanValue(Environment.PROP_EMAIL_SMTP_REQUIRES_AUTH);

	        // Add some logging. Notice that this will only be logged if the log level is at least
	        // FINE
        	StringBuffer sb = new StringBuffer();
	        if(smtpAuthentication)
	        {
		        sb.append("Trying to send mail using smtp authentication:\n");
		        sb.append("host: " + SMTP_AUTH_HOST + "\n");
		        sb.append("port: " + SMTP_AUTH_PORT + "\n");
		        sb.append("user: " + SMTP_AUTH_USER + "\n");
		        sb.append("from: " + SMTP_AUTH_FROM + "\n");
		        sb.append("type: " + contentType);
	        }
	        else
	        {
		        sb.append("Trying to send mail without smtp authentication:\n");
		        sb.append("host: " + SMTP_HOST + "\n");
		        sb.append("port: " + SMTP_PORT + "\n");
		        sb.append("from: " + SMTP_AUTH_FROM + "\n");
		        sb.append("type: " + contentType);
	        }
	        logger.error(sb.toString());
	
	        //Set the host smtp address
	        Properties props = new Properties();
	        if(smtpAuthentication)
	        {
		        props.put("mail.smtp.host",SMTP_AUTH_HOST);
		        props.put("mail.smtp.port",SMTP_AUTH_PORT);
		        props.put("mail.smtp.auth","true");
	        }
	        else
	        {
		        props.put("mail.smtp.host",SMTP_HOST);
		        props.put("mail.smtp.port",SMTP_PORT);
		        props.put("mail.smtp.auth","false");
	        }
	        
	        Authenticator auth = new SMTPAuthenticator(SMTP_AUTH_USER,SMTP_AUTH_PASS);
	        Session session = Session.getDefaultInstance(props,auth);
	
	        // create a message
	        Message msg = new MimeMessage(session);
	
	        // set the from and to address
	        InternetAddress addressFrom = new InternetAddress(SMTP_AUTH_FROM);
	        msg.setFrom(addressFrom);
	
	        InternetAddress[] addressTo = new InternetAddress[toRecipients.length];
	        for (int i = 0; i < toRecipients.length; i++)
	        {
	            addressTo[i] = new InternetAddress(toRecipients[i]);
	        }
	        msg.setRecipients(Message.RecipientType.TO, addressTo);

	        if(ccRecipients != null)
	        {
		        InternetAddress[] addressCC = new InternetAddress[ccRecipients.length];
		        for (int i = 0; i < ccRecipients.length; i++)
		        {
		            addressCC[i] = new InternetAddress(ccRecipients[i]);
		        }
		        msg.setRecipients(Message.RecipientType.CC, addressCC);
	        }

	        if(bccRecipients != null)
	        {
		        InternetAddress[] addressBCC = new InternetAddress[bccRecipients.length];
		        for (int i = 0; i < bccRecipients.length; i++)
		        {
		            addressBCC[i] = new InternetAddress(bccRecipients[i]);
		        }
		        msg.setRecipients(Message.RecipientType.BCC, addressBCC);
	        }
	        
	        // handle attachments
	        Multipart multipart = new MimeMultipart();
	        
	        // create the message part 
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        //messageBodyPart.setText(message);
	        messageBodyPart.setContent(message,contentType);
	        multipart.addBodyPart(messageBodyPart);

	        // additional parts are attachment
	        if(attachments != null && attachments.length > 0)
	        {
		        for(int i=0;i<attachments.length;i++)
		        {
			        messageBodyPart = new MimeBodyPart();
			        DataSource source = new FileDataSource(attachments[i]);
			        messageBodyPart.setDataHandler(new DataHandler(source));
			        messageBodyPart.setFileName(attachments[i]);
			        multipart.addBodyPart(messageBodyPart);
		        }
	        }

            // Setting the Subject and Content Type
	        if(subject == null) subject = "No subject";
	        msg.setSubject(subject);
	        msg.setContent(multipart);
	        Transport.send(msg);
    	}
    	catch(MessagingException me)
    	{
    		logger.error("Exception", me);
    		throw me;
    	}
    }

    /**
    * SimpleAuthenticator is used to do simple authentication
    * when the SMTP server requires it.
    */
    private class SMTPAuthenticator extends javax.mail.Authenticator
    {
    	String user = null;
    	String pass = null;
    	
    	public SMTPAuthenticator(String username,String password)
    	{
    		user = username;
    		pass = password;
    	}
    	
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(user,pass);
        }
    }

    /**
     * This method is a shortcut to retrieve the address separator delimiter
     * defined in the database.
     * @return The address separator to use in a String containing many mail
     * addresses.
     */
    public String getAddressSeparator()
    {
    	return Environment.getValue(Environment.PROP_EMAIL_ADDRESS_SEPARATOR);
    }
}
