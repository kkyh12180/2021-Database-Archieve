<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language = "java" import="java.text.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Delete Post Complete</title>
</head>
<body>
<%
	String serverIP = "localhost";
	String strSID = "orcl";
	String portNum = "1521";
	String user = "team6";
	String pass = "comp322";
	String url = "jdbc:oracle:thin:@" + serverIP + ":" + portNum + ":" + strSID;
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs;
	Class.forName("oracle.jdbc.driver.OracleDriver");
	conn = DriverManager.getConnection(url, user, pass);	
	
	int cnt;
	ResultSetMetaData rsmd;
	request.setCharacterEncoding("UTF-8");
	String query;
	
	try {
		conn.setAutoCommit(false); // transaction
		String[] num = request.getParameterValues("delete_number");
		for (int i = 0; i < num.length; i++) {
			query = "DELETE FROM POST WHERE PostNumber = " + num[i];
			// System.out.println(query);
			pstmt = conn.prepareStatement(query);
			cnt = pstmt.executeUpdate();
		
			/*
			if (cnt > 0)
				System.out.println("Success");
			else
				System.out.println("Fail");
			*/
		}
		
		conn.commit();
		conn.setAutoCommit(true);
		
		out.println("<h1>Delete Post Complete.</h1><br>");
		out.println("<h2><a href=\"admin_menu.html\">Back to Main.</a></h2><br>");
		out.println("<h2><a href=\"manage_post.jsp\">Back to Post Management Page.</a></h2>");
		
	} catch (Exception e) {
		e.printStackTrace();
		conn.rollback();
	} finally {
		pstmt.close();
	  	conn.close();
	}
%>
</body>
</html>