<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language = "java" import="java.text.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<link href="css/classmenu.css" rel="stylesheet" type="text/css">
<link href="css/classboard.css" rel="stylesheet" type="text/css">
<link href="css/button.css" rel="stylesheet" type="text/css">
<meta charset="UTF-8">
<title>Class Material</title>
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
	
	out.println("<h2></h2>");
	out.println("<div class = \"inner\">");
	out.println("<h2>자료 게시판</h2>");
	
	String query_material = "SELECT MaterialNumber FROM COURSE_MATERIAL WHERE CID = '" + user_class + "' ORDER BY MaterialNumber DESC";
	// System.out.println(query_material);
	pstmt = conn.prepareStatement(query_material);
	rs = pstmt.executeQuery(query_material);
	rsmd = rs.getMetaData();
	
	// href로 수정 예정
	while (rs.next()) {
		String title = "자료 " + (rs.getInt(1));
%>
		<article><a id="link" href = "material.jsp?no=<%=rs.getInt(1)%>"><%out.println(title);%></a></article>
<%
	}	
	out.println("</div>");
	
	if (user_status.equals("teacher")) {
		out.println("<br><div class = \"button_wrapper\">");
		out.println("<div class = \"down_button\">");
		out.println("<button type=\"button\" onclick=\"location.href='write_material.jsp' \">자료 작성</button>");
		out.println("</div>");
		out.println("</div><br>");
	}
	out.println("</div>");
	rs.close();
	pstmt.close();
	conn.close();
%>
</body>
</html>