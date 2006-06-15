/**
 * Copyright 2006 - Martijn van der Kleijn.
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
 * along with this program (gpl.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.vqwiki.web;

/**
 * The <code>EditTopicController</code> servlet is the servlet which allows users to edit topics and
 * makes sure that responses are dispatched to the appropriate views.
 *
 * This controller uses methods from the WikiBase class to parse the URI and determine which virtual wiki
 * and which topic are requested. The URI should be formed as below:
 *
 * www.somesite.com/<context-root>/<virtualwiki>/<action>/<topic>.html
 */
public class EditTopicController {
}
