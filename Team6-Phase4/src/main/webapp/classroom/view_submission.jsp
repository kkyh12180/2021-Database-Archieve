<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language = "java" import="java.text.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<link href="css/classmenu.css" rel="stylesheet" type="text/css">
<link href="css/assignment.css" rel="stylesheet" type="text/css">
<link href="css/button.css" rel="stylesheet" type="text/css">
<meta charset="UTF-8">
<title>view submission</title>
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
	
	out.println("<header class=\"top_bar\">");
	out.println("<div class=\"logo_mypage_wrapper\">");	
	out.println("<div class=\"logo_image\" OnClick=\"location.href='../post/main.jsp'\">logo</div>");
	out.println("<div class=\"mypage_link\" OnClick=\"location.href='../users/MyPage.jsp'\">| Mypage |</div>");
	out.println("</div>");
	out.println("</header>");
	
	out.println("<div class=\"contents_wrapper\">");
	out.println("<ul class=\"menu\">");
	out.println("<li><a class=\"home\" href=\"ClassroomMain.jsp\">홈</a></li>");
	out.println("<li><a class=\"item\" href=\"ClassMaterial.jsp\">자료</a></li>");
	out.println("<li><a class=\"item\" href=\"ClassAssignment.jsp\">숙제</a></li>");
	out.println("<li><a class=\"item\" href=\"ClassQA.jsp\">질문</a></li>");
	out.println("</ul>");
	
	String m_num = request.getParameter("m_no");
	String s_num = request.getParameter("s_no");
	// System.out.println(m_num);
	
	out.println("<h2></h2>");
	out.println("<div class = \"inner\">");
	out.println("<div class = \"text_box\">");
	out.println("<textarea id = \"name_area\" style = \"overflow:hidden\" readonly = \"readonly\" disabled>제출한 숙제</textarea><br>");
	out.println("<textarea id = \"write_section\" style = \"overflow:hidden\" readonly=\"readonly\" disabled>");
	
	String query_material = "SELECT Submission FROM ASSIGNMENT_SUBMISSION WHERE CID = '" + user_class + "' AND AssignmentNumber = " + m_num + " AND StudentNo = '" + s_num + "'";
	pstmt = conn.prepareStatement(query_material);
	rs = pstmt.executeQuery(query_material);
	rsmd = rs.getMetaData();
	
	while (rs.next())
		out.println(rs.getString(1));
	
	out.println("</textarea><br>");
	out.println("</div><br>");
	out.println("</div>");
	out.println("</div>");
	rs.close();
	pstmt.close();
	conn.close();
%>
</body>
</html>