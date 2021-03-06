<%--

  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.

  This program is free software; you can redistribute it and/or modify
  it under the terms of the latest version of the GNU Lesser General
  Public License as published by the Free Software Foundation;

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program (LICENSE.txt); if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

--%>
<%@ page import="
        org.jamwiki.DataHandler,
        org.jamwiki.Environment
    "
    contentType="text/html; charset=utf-8"
%>
<script type="text/javascript">
// <![CDATA[
var DATABASE_ELEMENT_IDS = [
	"<%= Environment.PROP_DB_TYPE %>", "<%= Environment.PROP_DB_DRIVER %>", "<%= Environment.PROP_DB_URL %>",
	"<%= Environment.PROP_DB_USERNAME %>", "<%= Environment.PROP_DB_PASSWORD %>"
];
function onPersistenceType() {
	var disabled = (document.getElementById("<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>").options[document.getElementById("<%= Environment.PROP_BASE_PERSISTENCE_TYPE %>").selectedIndex].value == "<%= WikiBase.PERSISTENCE_INTERNAL %>");
	for (var i = 0; i < DATABASE_ELEMENT_IDS.length; i++) {
		document.getElementById(DATABASE_ELEMENT_IDS[i]).disabled = disabled;
	}
}
var DATABASE_SAMPLE_VALUES = new Array();
DATABASE_SAMPLE_VALUES["<%= DataHandler.DATA_HANDLER_DB2 %>"] ={
	<%= Environment.PROP_DB_DRIVER %>: "com.ibm.db2.jdbc.app.DB2Driver", <%= Environment.PROP_DB_URL %>: "jdbc:db2j:net://localhost:1527/database"
};
DATABASE_SAMPLE_VALUES["<%= DataHandler.DATA_HANDLER_DB2400 %>"] ={
	<%= Environment.PROP_DB_DRIVER %>: "com.ibm.db2.jdbc.app.DB2Driver", <%= Environment.PROP_DB_URL %>: "jdbc:db2j:net://localhost:1527/database"
};
DATABASE_SAMPLE_VALUES["<%= DataHandler.DATA_HANDLER_HSQL %>"] ={
	<%= Environment.PROP_DB_DRIVER %>: "org.hsqldb.jdbcDriver", <%= Environment.PROP_DB_URL %>: "jdbc:hsqldb:file:\database\path;shutdown=true"
};
DATABASE_SAMPLE_VALUES["<%= DataHandler.DATA_HANDLER_MSSQL %>"] ={
	<%= Environment.PROP_DB_DRIVER %>: "com.microsoft.jdbc.sqlserver.SQLServerDriver", <%= Environment.PROP_DB_URL %>: "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=database"
};
DATABASE_SAMPLE_VALUES["<%= DataHandler.DATA_HANDLER_MYSQL %>"] ={
	<%= Environment.PROP_DB_DRIVER %>: "com.mysql.jdbc.Driver", <%= Environment.PROP_DB_URL %>: "jdbc:mysql://localhost:3306/database"
};
DATABASE_SAMPLE_VALUES["<%= DataHandler.DATA_HANDLER_ORACLE %>"] ={
	<%= Environment.PROP_DB_DRIVER %>: "oracle.jdbc.driver.OracleDriver", <%= Environment.PROP_DB_URL %>: " jdbc:oracle:thin:@localhost:1521:sid"
};
DATABASE_SAMPLE_VALUES["<%= DataHandler.DATA_HANDLER_POSTGRES %>"] = {
	<%= Environment.PROP_DB_DRIVER %>: "org.postgresql.Driver", <%= Environment.PROP_DB_URL %>: "jdbc:postgresql://localhost:5432/database"
};
DATABASE_SAMPLE_VALUES["<%= DataHandler.DATA_HANDLER_ASA %>"] = {
	<%= Environment.PROP_DB_DRIVER %>: "com.sybase.jdbc2.jdbc.SybDriver", <%= Environment.PROP_DB_URL %>: "jdbc:sybase:Tds:localhost:2048/database"
};
function onDatabaseType() {
	var databaseType = (document.getElementById("<%= Environment.PROP_DB_TYPE %>").options[document.getElementById("<%= Environment.PROP_DB_TYPE %>").selectedIndex].value);
	var sampleDriver = ((DATABASE_SAMPLE_VALUES[databaseType]) ? DATABASE_SAMPLE_VALUES[databaseType]["<%= Environment.PROP_DB_DRIVER %>"] : "");
	var sampleUrl = ((DATABASE_SAMPLE_VALUES[databaseType]) ? DATABASE_SAMPLE_VALUES[databaseType]["<%= Environment.PROP_DB_URL %>"] : "");
	document.getElementById("<%= Environment.PROP_DB_DRIVER %>").value = sampleDriver;
	document.getElementById("<%= Environment.PROP_DB_URL %>").value = sampleUrl;
}
// ]]>
</script>
