<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page language = "java" import="java.text.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<link href="css/classmenu.css" rel="stylesheet" type="text/css">
<link href="css/classmain.css" rel="stylesheet" type="text/css">
<meta charset="UTF-8">
<title>Classroom Main Page</title>
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
	int cnt = 0;

	/* 로그인 정보 */
	//*****************************************************
	
	String c_status = "";
    String c_id = "";
    String c_number = "";
    
    Cookie[] cookies = request.getCookies();
    if(cookies != null) {
        for(int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            if(cookies[i].getName().equals("whoAreYou")) { // 쿠키 이름으로 설정해준 부분 참고하여 추후에 수정
                c_status = c.getValue();
            }
            if(cookies[i].getName().equals("id")) {
            	c_id = c.getValue();
            }
            if(cookies[i].getName().equals("userNumber")) {
            	c_number = c.getValue();
            }
        }
    }
	
	// ****************************************************	
	
	String coursenum = request.getParameter("CourseNumber"); 
	if (coursenum == null) {
		for(int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            if(cookies[i].getName().equals("class_number")) { // 쿠키 이름으로 설정해준 부분 참고하여 추후에 수정
                coursenum = Integer.toString(Integer.parseInt(c.getValue()) - 100000000);
            }
        }
	}
	
	/* cookie */
	//*****************************************************
	
	// make cookie
	Cookie co_num = new Cookie("user_number", c_number);
	Cookie co_status = new Cookie("status", c_status);
	Cookie co_classnum = new Cookie("class_number", Integer.toString(Integer.parseInt(coursenum) + 100000000));
	
	// set cookie age
	co_num.setMaxAge(60*60*24);
	co_status.setMaxAge(60*60*24);
	co_classnum.setMaxAge(60*60*24);
	
	// can use this cookie in classroom directory
	co_num.setPath("/");
	co_status.setPath("/");
	co_classnum.setPath("/");
	
	// save cookie
	response.addCookie(co_num);
	response.addCookie(co_status);
	response.addCookie(co_classnum);
	
	//*****************************************************

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
	String query_syllabus;
	out.println("<div class = \"syllabus\">");
	out.println("<div class=\"syllabus_title\">");
	out.println("<h2> 수업 정보 </h2>");
	out.println("</div>");
	out.println("<div class=\"syllabus_content\">");
	// get information
	query_syllabus = "SELECT CourseName, Subject, Syllabus FROM COURSE, CLASSROOM WHERE COURSE.CourseNumber = '" + coursenum + "' AND CourseNumber = CourseNo";
	// System.out.println(query_syllabus);
	pstmt = conn.prepareStatement(query_syllabus);
	rs = pstmt.executeQuery(query_syllabus);
	rsmd = rs.getMetaData();
	cnt = rsmd.getColumnCount();
	
	while (rs.next())
	{
		out.println("수업 이름: " + rs.getString(1) + "<br>");
		out.println("과목: " + rs.getString(2)+ "<br>");
		out.println("계획: " + rs.getString(3)+ "<br>");
	}
	out.println("</div>");
	out.println("<br></div><br>");
	
	String query_material;
	out.println("<div class = \"material\"><h2><a id=\"title\" href=\"ClassMaterial.jsp\">자료 게시판</a></h2>");
	query_material = "SELECT MaterialNumber FROM COURSE_MATERIAL WHERE CourseNo = '" + coursenum + "' ORDER BY MaterialNumber DESC";
	// href에 이용
	// System.out.println(query_material);
	pstmt = conn.prepareStatement(query_material);
	rs = pstmt.executeQuery(query_material);
	rsmd = rs.getMetaData();
	cnt = rsmd.getColumnCount();
	
	int material_num = 0;
	while (rs.next())
	{
%>
		<article><a id="link" href = "material.jsp?no=<%=rs.getInt(1)%>"><%out.println("자료 " + (rs.getInt(1)));%></a></article>
<%
		material_num++;
		if (material_num == 5) break;
	}
	out.println("<br></div><br>");
	
	String query_assignment;
	out.println("<div class = \"assignment\"><h2><a id=\"title\" href=\"ClassAssignment.jsp\">숙제 게시판</a></h2>");
	query_assignment = "SELECT AssignmentNumber FROM COURSE_ASSIGNMENT WHERE CourseNo = '" + coursenum + "' ORDER BY AssignmentNumber DESC";
	// href에 이용
	// System.out.println(query_assignment);
	pstmt = conn.prepareStatement(query_assignment);
	rs = pstmt.executeQuery(query_assignment);
	rsmd = rs.getMetaData();
	cnt = rsmd.getColumnCount();
	
	int assignment_num = 0;
	while (rs.next())
	{
%>
		<article><a id="link" href = "assignment.jsp?no=<%=rs.getInt(1)%>"><%out.println("숙제 " + (rs.getInt(1)));%></a></article>
<%
		assignment_num++;
		if (assignment_num == 5) break;
	}
	out.println("<br></div><br>");
	
	String query_qa;
	out.println("<div class = \"qa\"><h2><a id=\"title\" href=\"ClassQA.jsp\">질문과 답변 게시판</a></h2>");
	query_qa = "SELECT QuestionNumber FROM QA WHERE CourseNo = '" + coursenum + "' ORDER BY QuestionNumber DESC";
	// href에 이용
	// System.out.println(query_qa);
	pstmt = conn.prepareStatement(query_qa);
	rs = pstmt.executeQuery(query_qa);
	rsmd = rs.getMetaData();
	cnt = rsmd.getColumnCount();
	
	int qa_num = 0;
	while (rs.next())
	{
%>
		<article><a id="link" href = "question.jsp?no=<%=rs.getInt(1)%>"><%out.println("질문 " + (rs.getInt(1)));%></a></article>
<%
		qa_num++;
		if (qa_num == 5) break;
	}
	out.println("<br></div><br>");
	if (c_status.equals("teacher")) {
		out.println("<div class=\"student_list\"><h2><a id=\"title\" href=\"student_list.jsp\">학생 명단</a></h2>");
		out.println("</div><br>");
	}
	out.println("</div>");
	out.println("</div>");
	rs.close();
	pstmt.close();
	conn.close();
%>
</body>
</html>