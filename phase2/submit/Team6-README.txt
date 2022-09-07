1. 동작 환경

OS : Window 10 Home Edition
Oracle Version : 19c



2. 스키마

Entities : 
            ADMIN, TEACHER, STUDENT, POST, MATCHING, COURSE, 
            CLASSROOM, TEACHER_MANAGEMENT, POST_MANAGEMENT, 
            READING_POST, CONTAINING_STUDENT_INFO, TAKING_COURSE,
            COURSE_ASSIGNMENT, COURSE_MATERIAL, QA

RelationShip Cardinality : 
                            ADMIN(M) <-> TEACHER(N)
                            ADMIN(M) <-> POST(N)
                            TEACHER(1) <-> POST(N)
                            TEACHER(1) <-> COURSE(N)
                            STUDENT(M) <-> COURSE(N)
                            STUDENT(M) <-> MATCHING(N)
                            STUDENT(M) <-> POST(N)
                            COURSE(1) <-> CLASSROOM(1)
                            COURSE(1) <-> COURSE_ASSIGNMENT(N)
                            COURSE(1) <-> COURSE_MATERIAL(N)
                            COURSE(1) <-> QA(N)

3. Team6-Phase2-3.sql 타입별 쿼리 설명

    Type1
        1) PostNumber로 특정 글 제목, 내용, 작성자, 과목을 조회
        2) CourseNumber로 특정 과목의 과목명을 조회
    
    Type2
        1) coursenumber가 '1300000011'인 course의 question정보 조회
        2) 과목이 'math'이고, 매칭이 이루어진 포스트정보 조회

    Type3
        1) 서로 다른 수학 과목을 2개이상 듣는 학생의 학번과 그 수를 조회
        2) 과목별 학생 수를 조회

    Type4
        1) 주소에 대구를 갖는 선생이 게시한 포스트 정보 조회
        2) course의 courseNumber가 1300000050보다 큰 course를 수강하는 학생들의 정보 조회

    Type5
        1) 회원가입은 했지만 하나도 글을 쓰지 않은 선생님들의 이름, 전화번호, 전공을 조회
        2) 과목 개설은 되었지만 해당 과목을 수강신청한 학생은 없는 강좌의 과목, 과목번호를 조회

    Type6
        1) ID가 'ADMIN1'인 관리자가 관리하는 선생님의 이메일과 ID 조회
        2) 성별이 'M'(남성)이고 'science' 과목을 가르치는 POST를 작성한 선생님의 정보

    Type7
        1) 강좌의 과목이 과학인 글의 제목과 작성자를 조회
        2) 전공이 컴퓨터공학인 선생님의 이름, 전공, 이메일을 조회

    Type8
        1) COURSE 이름이 'course15'인 course를 수강하는 학생의 이름 조회
        2) 이름이 course15인 course에서 답변이 존재하는 question 조회

    Type9
        1) 학생별 서로 다른 수학 과목을 2개이상 듣는 학생의 학번과 그 수를 조회 그리고 이를 학번을 기준으로 오름차순 정렬
        2) 전공이 컴퓨터공학이고 수학 과목을 가르치는 선생님의 번호와 매칭된 학생 수를 조회 그리고 이를 학생 수를 기준으로 내림차순으로 정렬 

    Type10
        1) 선생님 중 이름이 teacher_인 사람과 학생 중 이름이 student_인 사람의 합집합 조회
        2) 데이터베이스에 있는 모든 사람의 이메일 주소 

    
4. 기타 파일
    make_table_java.zip : 테이블 생성을 위한 더미데이터를 만드는 코드
    Relational model.png : relational model 이미지

