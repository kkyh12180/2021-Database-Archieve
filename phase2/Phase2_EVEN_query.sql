--2.
--1) coursenumber가 1300000011인 course의 question정보
SELECT CR.CourseNo, Q.CID, Q.question
FROM QA Q, CLASSROOM CR, COURSE C
WHERE Q.CID = CR.classroomID
AND CR.courseNo = C.CourseNumber
AND C.courseNumber = 1300000011;

--2) 과목이 math이고, 매칭이 이루어진 포스트
SELECT P.Postnumber, P.PostTitle, M.MatchingNumber
FROM POST P, MATCHING M
WHERE P.PostNumber = M.PostNumber
AND P.Subject = 'math';

--4.
--1)  주소에 대구를 갖는 선생이 게시한 포스트 
SELECT T.Teachernumber, P.postnumber, P.posttitle
FROM POST P, TEACHER T
WHERE T.TeacherNumber = (SELECT T.TeacherNumber
                        FROM TEACHER T
                        WHERE T.teacherid = P.writer
                        AND T.address = 'Daegu');
--2) course의 courseNumber가 1300000050보다 큰 course를 수강하는 학생들의 정보
SELECT C.courseNumber, S.studentID, S.Phone_number, S.Name, S.Email_Address
FROM STUDENT S, COURSE C
WHERE S.studentID = (SELECT S.studentID
                    FROM CONTAINING_STUDENT_INFO CSI
                    WHERE CSI.StudentNumber = S.studentNumber
                    AND C.matchNo = CSI.MatchingNumber
                    AND C.courseNumber > 1300000050);

--6.
--1) ID가 'ADMIN1'인 관리자가 관리하는 선생님의 이메일과 ID
SELECT T.teacherID, T.Email_Address As Teacher_Email
FROM TEACHER T, ADMIN A
WHERE T.teacherNumber IN (SELECT Tm.teacherNumber
                        FROM TEACHER_MANAGEMENT TM
                        WHERE TM.AdminID = 'ADMIN1'
                        AND A.AdminID = TM.AdminID);

--2)성별이 M이고 science을 가르치는 POST를 작성한 선생님의 정보                        
SELECT T.Email_address, T.TeacherID, T.Name, T.Phone_Number, P.subject
FROM TEACHER T, POST P
WHERE T.Gender = 'M'
AND T.teacherID IN (SELECT T.teacherID FROM TEACHER
                    WHERE P.writer = T.TeacherID
                    AND P.subject = 'science');

--8.
--1)COURSE 이름이 'course15'인 course를 수강하는 학생의 이름
SELECT C.CourseName, S.name
FROM COURSE C, STUDENT S, TAKING_COURSE TC
WHERE S.StudentNumber = TC.StudentNumber
AND TC.CourseNumber = C.CourseNumber
AND C.CourseName = 'course15'
ORDER BY S.name;

--2) 이름이 course15인 course에서 답변이 존재하는 question
SELECT Q.Question
FROM QA Q, COURSE C
WHERE Q.CourseNo = C.CourseNumber
AND Q.Answer IS NOT NULL
AND C.CourseName = 'course15'
ORDER BY Q.question, Q.Answer;


--10.
--1)선생님 중 이름이 teacher_인 사람과 학생 중 이름이 student_인 사람의 합집합
SELECT T.name
FROM TEACHER T
WHERE T.name LIKE 'teacher_'
UNION
SELECT S.name
FROM STUDENT S
WHERE S.name LIKE 'student_';


--2)데이터베이스에 있는 모든 사람의 이메일 주소
SELECT T.Email_Address
FROM TEACHER T
UNION
SELECT S.Email_Address
FROM STUDENT S
UNION
SELECT A.Email_Address
FROM ADMIN A;