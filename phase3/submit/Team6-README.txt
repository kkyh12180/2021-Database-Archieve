DATABASE TEAMPROJECT PHASE 3 : TEAM6 "김이장네 팀" 

1. 동작 환경
OS : Window 10 Home Edition<br>
Oracle Version : 19c

2. 실행 및 사용 방법

기초작업
	user: team6/comp322 사용
	실행 시 user 내부에 테이블 존재 유무를 판별하여 첫 실행인지 확인.
	만약 첫 실행일 경우 테이블을 생성하고 데이터를 작성.
	이후 화면에 보여지는 메뉴를 통해 사용자는 로그인, 회원가입, 종료 중 선택할 수 있음.

a. 로그인: 로그인(sign in) 메뉴 선택
	- 사용자는 본인의 ID와 비밀번호를 입력하여 로그인 가능.
	- ADMIN, STUDENT, TEACHER 테이블 어디에도 존재하지 않는 아이디를 입력할 시, 사용자에게 "You have entered an invalid ID." 문구를 보여주고 다시 로그인 실행.
	- 아이디와 맞지 않는 비밀번호를 입력할 시, 사용자에게 "Wrong password!" 문구를 보여주고 다시 로그인 실행.
	- 사용자가 올바른 아이디와 비밀번호를 입력할 시 로그인 성공. 아이디를 통해 사용자의 status(ADMIN, TEACHER, STUDENT)를 파악하여 사용자에 맞는 메뉴를 보여줌.

b. 회원가입: 회원가입(sign up) 메뉴 선택
	- 사용자는 ADMIN, TEACHER, STUDENT 중 하나의 status를 골라 회원가입을 진행할 수 있음.
	- 회원가입을 진행할 시 사용자는 각자 선택한 지위에 따라 각각 다른 정보를 입력하게 됨. 이때 전화번호에 숫자가 아닌 값을 입력하거나,
	성별에 M/F가 아닌 다른 값을 입력하게 되면 올바른 값을 입력할 때 까지 반복하여 값을 입력받음.
	- 회원가입이 성공적으로 완료되면, 바로 로그인이 가능하도록 함.

c. 메뉴 선택(SELECT MENU): 로그인이 완료되면, 사용자는 INSERT(1), SELECT(2), DELETE(3), SELECT(4), QUIT(q) 중 하나의 기능을 선택.
	- QUIT 메뉴를 선택할 시
	- 기능을 선택하면, 사용자의 지위(ADMIN, TEACHER, STUDENT)에서 각각의 기능을 사용하여 수행할 수 있는 작업들을 메뉴로 나열하고 사용자가 메뉴를 선택하면 해당 작업을 수행할 수 있음.

3. 기능 설명

insert
	- admin: teacher와 post를 관리할 수 있는 메뉴를 보여줌.
	- teacher: 새로운 매칭과 강좌를 개설하고, 관리할 수 있는 메뉴를 보여줌.
	- student: post를 읽었음을 나타내거나, 매칭, 강의에 참여했다는 정보를 넣을 수 있음.

update
	- StudentNumber를 입력받아 특정 학생의 Name(이름)을 수정할 수 있음.
	- TeacherNumber를 입력받아 특정 선생님의 Major(전공)을 수정할 수 있음.
	- PostNumber를 입력받아 특정 글의 Title(글 제목)을 수정할 수 있음.
	- CourseNumber를 입력받아 특정 과목의 Subject(과목명)을 수정할 수 있음.

delete
	- admin
			1. 본인의 계정을 삭제할 수 있음.
	- teacher
			1. 본인의 계정을 삭제할 수 있음.
			2. 본인이 개설한 강의실(Classroom)을 삭제할 수 있음.
			3. 본인이 개설한 강좌(Course)를 삭제할 수 있음.
			4. 본인의 강의실(Classroom)에 있는 QA를 삭제할 수 있음.
			5. 본인이 작성한 게시물(Post)을 삭제할 수 있음.
	- student	
		 	1. 본인의 계정을 삭제할 수 있음.
			2. 수강하던 강좌에서 나올 수 있음.

select
	- admin
			1. 커뮤니티 룰을 위반한 학생을 제재하기위해 StudentID를 입력받아 특정 학생의 정보를 조회할 수 있음
			2. 커뮤니티 룰을 위반한 선생님을 제재하기위해 TeacherID를 입력받아 특정 선생님의 정보를 조회할 수 있음
	- teacher
			1. TeacherNumber를 입력받아 특정 선생님과 매칭된 모든 학생의 리스트를 조회할 수 있음
	- student
			1. Subject(과목명)을 입력받아 해당 과목명으로 게시된 모든 글의 리스트를 조회할 수 있음
			2. StudentNumber(학번)을 입력받아 해당 학생이 듣고있는 모든 강좌의 리스트를 조회할 수 있음
4. SQL 수정 사항

각 테이블의 foreign key들에 cascade 옵션 추가.
ADMIN, TEACHER, STUDENT 테이블에서 이름 더미 데이터 양식 변경.
	EX) admin12 -> admin_name_12 / teacher12 -> teacher_name_12 / student12 -> student_name_12
