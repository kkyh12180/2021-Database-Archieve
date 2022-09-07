<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language = "java" import="java.text.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Write Assignment Action</title>
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

	ResultSetMetaData rsmd;
	request.setCharacterEncoding("UTF-8");

	String user_info_name = "user_number";
	String user_info = "";
	String user_status_name = "status";
	String user_status = "";
	String user_class_name = "class_number";
	String user_class = "";

	// get cookie
	Cookie co[]= request.getCookies();
	for (Cookie c : co) {
		if (c.getName().equals(user_info_name))
			user_info = c.getValue();
		else if (c.getName().equals(user_status_name))
			user_status = c.getValue();
		else if (c.getName().equals(user_class_name))
			user_class = c.getValue();
	}
	
	String description = request.getParameter("description");
	
	int num = 0;
	String query = "SELECT MAX(AssignmentNumber) FROM COURSE_ASSIGNMENT WHERE CID = '" + user_class + "'";
	pstmt = conn.prepareStatement(query);
	rs = pstmt.executeQuery(query);
	rsmd = rs.getMetaData();
	while (rs.next())
		num = rs.getInt(1);
	
	query = "INSERT INTO COURSE_ASSIGNMENT VALUES (" + (num + 1) + ", '" + description + "', '"+ user_class +"', '" + (Integer.parseInt(user_class) - 100000000) + "')";
	// System.out.println(query);
	pstmt = conn.prepareStatement(query);
	int ret = pstmt.executeUpdate();
	// System.out.println("Success");
	
	rs.close();
	pstmt.close();
	conn.close();
	response.sendRedirect("ClassAssignment.jsp");
%>
</body>
</html>