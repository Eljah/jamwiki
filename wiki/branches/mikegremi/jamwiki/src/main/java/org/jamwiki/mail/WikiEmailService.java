package org.jamwiki.mail;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.jamwiki.Environment;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiException;
import org.jamwiki.WikiMessage;
import org.jamwiki.db.WikiDatabase;
import org.jamwiki.model.WikiUser;
import org.jamwiki.model.WikiUserInfo;
import org.jamwiki.servlets.ServletUtil;
import org.jamwiki.utils.Encryption;
import org.jamwiki.utils.LinkUtil;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLink;
import org.jamwiki.utils.WikiLogger;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;

import sun.misc.UUEncoder;

import com.sun.corba.se.spi.servicecontext.UEInfoServiceContext;
import com.sun.syndication.feed.atom.Link;


public class WikiEmailService  {

        private static final WikiLogger logger = WikiLogger.getLogger(WikiEmailService.class.getName());
        
        public static final String PARAMETER_ACTION = "action";
        public static final String PARAMETER_USER = "user";
        public static final String PARAMETER_CODE = "code";
        
        public static final String ACTION_CONFIRM_REGISTRATION = "registration";
        public static final String ACTION_RESET_PASSWORD = "reset";
        public static final String ACTION_NEW_PASSWORD = "new";
        
        public WikiEmailService() {
                loadConfiguration();
        }
        
        /** Loads current configuration from Environment
         */
        public void loadConfiguration() {
                setHost(Environment.getValue(Environment.PROP_EMAIL_SMTP_HOST));
                setPort(Environment.getIntValue(Environment.PROP_EMAIL_SMTP_PORT));
                setAccount(Environment.getValue(Environment.PROP_EMAIL_SMTP_USERNAME));
                setPassword(Encryption.getEncryptedProperty(Environment.PROP_EMAIL_SMTP_PASSWORD, Environment.getInstance()));
                setReplyAddress(Environment.getValue(Environment.PROP_EMAIL_REPLY_ADDRESS));
                setActivated(Environment.getBooleanValue(Environment.PROP_EMAIL_ACTIVATED));
        }
        
        private void logSettings(){
                logger.info("Host=" + host + "\n" + 
                "Port=" + port + "\n" +
                "Account=" + account + "\n" + 
                "Pasword=" + password + "\n" +
                "ReplyAddress=" + replyAddress + "\n");
        }
        
        
        private boolean isActivated;
        private String host;
        private int port;
        private String account;
        private String password;
        private String replyAddress;
        
        private String wikiName = "JAMWiki";
        private String servletName;
        
        private String createValidationCode() {
                SecureRandom random = new SecureRandom();
                byte[] id = new byte[21];
                random.nextBytes(id);
                Charset cs = Charset.forName("US-ASCII");
                String validationCode = new String(Base64.encodeBase64(id), cs);
                validationCode = validationCode.replace('+', '-').replace('/', '-');
                return validationCode;
        }
        
        private URL createServletUrl(HttpServletRequest request) throws MalformedURLException {
                return new URL(request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath() + "/en/" + servletName);
        } 
        
        /** Sends an email to a new user and returns a created unique validation code. 
         * @param request Request needed for creating URL to EmailServlet.
         * @param locale Locale for user
         * @param username name of new user
         * @param userEmailAddress email address of new user
         * @return activation code sent to user
         * @throws Exception
         */
        public String sendActivationLink(HttpServletRequest request, Locale locale, String username, String userEmailAddress) throws Exception {
                String validationCode = createValidationCode();                
                String servletUrl = createServletUrl(request).toExternalForm();
                
                String link = servletUrl + "?" + WikiEmailService.PARAMETER_ACTION + "=" + WikiEmailService.ACTION_CONFIRM_REGISTRATION +
                "&" + WikiEmailService.PARAMETER_USER + "=" + username + 
                "&" + WikiEmailService.PARAMETER_CODE + "=" + validationCode;
                
                String subject = Utilities.formatMessage("email.activation.subject", locale, new String[] {wikiName});
                String body = Utilities.formatMessage("email.activation.body", locale, new String[] {
                                username, userEmailAddress, link, wikiName});
                //TODO: make async call?
                new WikiMail(host, port, account, password).sendMail(replyAddress, userEmailAddress, subject, body);
                
                
                logger.info("Created validation code: " + validationCode);
                
                return validationCode; 
        }
        
        /** Sends an email to a user who has forgotten his password and returns a created unique validation code. 
         * @param request Request needed for creating URL to EmailServlet.
         * @param locale Locale for user
         * @param username name of new user
         * @param userEmailAddress email address of new user
         * @return activation code sent to user
         * @throws Exception
         */
        public String userForgotPassword(HttpServletRequest request, Locale locale, String username, String emailAddress) throws Exception {
                logger.info("Reset request for " + username);
                if (! isActivated) {
                        throw new WikiException(new WikiMessage("email.support.notactivated")); 
                }

                String validationCode = createValidationCode();
                String servletUrl = createServletUrl(request).toExternalForm();
                
                String link = servletUrl + "?" + PARAMETER_ACTION + "=" + ACTION_RESET_PASSWORD +
                "&" + PARAMETER_USER  + "=" + username + 
                "&" + PARAMETER_CODE  + "=" + validationCode;
                
                String subject = Utilities.formatMessage("email.reset.subject", locale, new String[] {wikiName});
                String body = Utilities.formatMessage("email.reset.body", locale, new String[] {
                                username, request.getServerName(), emailAddress, link, wikiName});
                //TODO: make async call?
                new WikiMail(host, port, account, password).sendMail(replyAddress, emailAddress, subject, body);
                return validationCode;
        }

        
        public String getAccount() {
                return account;
        }



        public void setAccount(String account) {
                this.account = account;
        }



        public String getHost() {
                return host;
        }



        public void setHost(String host) {
                this.host = host;
        }



        public String getPassword() {
                return password;
        }



        public void setPassword(String password) {
                this.password = password;
        }



        public int getPort() {
                return port;
        }



        public void setPort(int port) {
                this.port = port;
        }



        public String getReplyAddress() {
                return replyAddress;
        }



        public void setReplyAddress(String replyAddress) {
                this.replyAddress = replyAddress;
        }



        public boolean isActivated() {
                return isActivated;
        }



        public void setActivated(boolean isActivated) {
                this.isActivated = isActivated;
        }

        public String getServletName() {
                return servletName;
        }

        public void setServletName(String servletName) {
                this.servletName = servletName;
        }
        
}
