Entity and Attribute

ADMIN : 선생님과 게시글을 관리하는 관리자
AdminID : 관리자 로그인용 ID
PW : 관리자 로그인용 PW
Name : 관리자 이름
Email_Address : 관리자 이메일 주소
Phone_Number : 관리자 전화번호

TEACHER : 매칭을 통해 강의를 진행할 선생님
TeacherID : 선생님 로그인용 ID
TeacherNumber : 선생님 PK
PW : 선생님 로그인용 PW
Name : 선생님 이름
Email_Address : 선생님 이메일 주소
Phone_Number : 선생님 핸드폰 번호
Address : 선생님 주소
Major : 전공
Gender : 성별
Education : 학력

STUDENT : 매칭을 통해 강의를 들을 학생
StudentID : 학생 로그인용 ID
StudentNumber : 학생 PK
PW : 학생 로그인용 PW
Name : 학생 이름
Email_Address : 학생 이메일 주소
Phone_Number : 학생 핸드폰 번호

POST : 매칭을 위해 선생님이 학생을 찾는 게시글
PostNumber : 글 PK
PostTitle : 글 제목
PostText : 글 내용
PostWriter : 작성자
Subject : 과목

MATCHING : 게시글을 통해 만들어진 매칭으로, 학생, 선생님, 해당 게시글에 관한 정보를 담고 있음
MatchingNumber : 매칭 PK
TeacherNumber : 선생님 PK 
StudentNumber : 학생 PK
PostNumber : 글 PK
Subject : 매칭 과목

COURSE : 매칭을 통해 개설된 강좌
CourseNumber : 강좌 PK
CourseName : 강좌 이름
Subject : 강좌에서 가르치는 과목

CLASSROOM : 강좌를 위한 하나의 강의 게시판
ClassroomID : 클래스룸 PK
QA : 질문과 답변
Syllabus : 강의 계획서
Course_Material : 과목 강의자료
Course_Assignment : 과목 과제

Relationship

MANAGES : ADMIN이 부적절한 TEACHER이나 POST를 관리함
WRITES : TEACHER이 STUDENT를 구하는 POST를 남김
READS : TEACHER이 적은 POST를 STUDENT가 읽음
CONTAINS : MATCHING은 STUDENT와 TEACHER에 대한 정보를 담고 있음
CONNECTS : POST를 통해 MATCHING을 연결
OPENS : MATCHING을 통해 COURSE가 개설
TEACHES : TEACHER이 COURSE를 통해 STUDENT를 가르침
TAKES : STUDENT가 COURSE를 통해 강의를 수강함
HAS : COURSE는 CLASSROOM을 가지고 있음
