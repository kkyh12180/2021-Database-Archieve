<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language = "java" import="java.text.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Post</title>
</head>
<body>
	<h1>Post List</h1>
<%
	String serverIP = "localhost";
	String strSID = "orcl";
	String portNum = "1521";
	String user = "team6";
	String pass = "comp322";
	String url = "jdbc:oracle:thin:@" + serverIP + ":" + portNum + ":" + strSID;
	
	Connection conn = null;
	PreparedStatement pstmt;
	ResultSet rs;
	Class.forName("oracle.jdbc.driver.OracleDriver");
	conn = DriverManager.getConnection(url, user, pass);
	
	int cnt;
	ResultSetMetaData rsmd;
	request.setCharacterEncoding("UTF-8");
	
	String query = "SELECT * "
			  + "FROM Post "
  			  + "ORDER BY PostNumber ASC";
	// System.out.println(query);
	pstmt = conn.prepareStatement(query);
	rs = pstmt.executeQuery(query);
	rsmd = rs.getMetaData();
	cnt = rsmd.getColumnCount();
	out.println("<h2>----------------------------------------------------------------------------</h2>");
	out.println("<form action=\"post_delete_action.jsp\" method=\"post\">");
	out.println("<table border=\"0\">");
	for (int i = 1; i <= cnt; i++) {
		if (i == 1)
			out.println("<th align = \"center\">POST_NO</th>");
		else if (i == 2)
			out.println("<th align = \"center\">POST_TITLE</th>");
		else if (i == 3)
			continue;
		else if (i == 6)
			out.println("<th align = \"center\">TEACHER_NO</th>");
		else
			out.println("<th align = \"center\">" + rsmd.getColumnName(i) + "</th>");
	}
	out.println("<th align = \"center\">DELETE</th>");
	
	while (rs.next()) {
		out.println("<tr align = \"center\">");
		out.println("<td align = \"center\">" + rs.getInt(1) + "</td>");
		out.println("<td align = \"center\">" + rs.getString(2) + "</td>");
		out.println("<td align = \"center\">" + rs.getString(4) + "</td>");
		out.println("<td align = \"center\">" + rs.getString(5) + "</td>");
		out.println("<td align = \"center\">" + rs.getString(6) + "</td>");
		out.println("<td align = \"center\"><input type=\"checkbox\" name=\"delete_number\" value=\""+ rs.getInt(1) + "\"></td>");
		out.println("</tr>");
	}
	out.println("</table><br>");
	out.println("<input type=\"submit\" value=\"Delete\"> <input type=\"reset\" value=\"Reset\"></form>");
	
  	rs.close();
  	pstmt.close();
  	conn.close();
%>
</body>
</html>