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
package org.jamwiki.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jamwiki.WikiBase;
import org.jamwiki.WikiMessage;
import org.jamwiki.mail.WikiEmailService;
import org.jamwiki.model.WikiUser;
import org.jamwiki.model.WikiUserInfo;
import org.jamwiki.utils.Utilities;
import org.jamwiki.utils.WikiLogger;
import org.springframework.web.servlet.ModelAndView;

/**
 * Used to handle requests or redirects to the login page.
 */
public class LoginServlet extends JAMWikiServlet {

	/** Logger */
	private static final WikiLogger logger = WikiLogger.getLogger(LoginServlet.class.getName());
        private static final String JSP_LOGIN_RESET = "login-reset.jsp";
        private static final String JSP_SHOW_MESSAGE = "show-message.jsp";

        private WikiEmailService emailService;
	/**
	 *
	 */
	protected ModelAndView handleJAMWikiRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView next, WikiPageInfo pageInfo) throws Exception {
                if(request.getParameter("initReset") != null) { //from login.jsp 
                        //TODO set username in login-reset.jsp
                        pageInfo.setPageTitle(new WikiMessage("login.reset.title"));
                        pageInfo.setContentJsp(JSP_LOGIN_RESET);
                        pageInfo.setSpecial(true);
                        return next;
                }
                if(request.getParameter("sendReset") != null) { //from login-reset.jsp
                        String username = request.getParameter("j_username");
                        emailService.loadConfiguration();
                        WikiUser user =  WikiBase.getDataHandler().lookupWikiUser(username, null);
                        WikiUserInfo userInfo = WikiBase.getUserHandler().lookupWikiUserInfo(username); 
                        String emailAddress = userInfo.getEmail();
                        String validationCode = emailService.userForgotPassword(request, Utilities.buildLocale(user.getDefaultLocale()), username, emailAddress);
                        user.setEnabled(2);
                        user.setValidationCode(validationCode);
                        WikiBase.getDataHandler().writeWikiUser(user, userInfo, null);
                        pageInfo.setPageTitle(new WikiMessage("login.reset.title"));
                        pageInfo.setContentJsp(JSP_SHOW_MESSAGE);
                        pageInfo.setSpecial(true);
                        next.addObject("message", "login.reset.message");                        //TODO i18n
                        return next;
                }
		return ServletUtil.viewLogin(request, pageInfo, null, null);
	}
        public WikiEmailService getEmailService() {
                return emailService;
        }
        public void setEmailService(WikiEmailService emailService) {
                this.emailService = emailService;
        }
}
