-- Type1
SELECT PostTitle, PostText, Writer, Subject
FROM POST
WHERE PostNumber = 49;
-- 특정 글 제목, 내용, 작성자, 과목

SELECT CourseName
FROM COURSE
WHERE CourseNumber = '1300000078';
-- 특정 과목의 이름

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

-- Type3
SELECT STUDENT.StudentNumber, COUNT(*) AS Cnt
FROM COURSE, TAKING_COURSE, STUDENT
WHERE COURSE.CourseNumber = TAKING_COURSE.CourseNumber
AND STUDENT.StudentNumber = TAKING_COURSE.StudentNumber
AND COURSE.Subject = 'math'
GROUP BY STUDENT.StudentNumber
HAVING COUNT(*) >= 2;
-- 학생별 서로 다른 수학 과목을 2개이상 듣는 학생의 학번과 그 수

SELECT COURSE.Subject, COUNT(*) AS Cnt
FROM COURSE, TAKING_COURSE, STUDENT
WHERE COURSE.CourseNumber = TAKING_COURSE.CourseNumber
AND STUDENT.StudentNumber = TAKING_COURSE.StudentNumber
GROUP BY COURSE.Subject;
-- 과목별 학생 수

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

-- Type5
SELECT TEACHER.Name, TEACHER.Phone_Number, TEACHER.Major 
FROM TEACHER
WHERE TEACHER.TeacherNumber NOT IN (SELECT TeacherNo from POST)
AND EXISTS (SELECT 1 FROM TEACHER WHERE TEACHER.TeacherNumber NOT IN (SELECT TeacherNo from POST));
-- 회원가입은 했지만 하나도 글을 쓰지 않은 선생님들의 이름과 전화번호 전공을 쿼리

SELECT COURSE.CourseNumber, COURSE.Subject
FROM COURSE
WHERE COURSE.CourseNumber NOT IN (SELECT CourseNumber FROM TAKING_COURSE)
AND EXISTS (SELECT 1 FROM COURSE WHERE COURSE.CourseNumber NOT IN (SELECT CourseNumber FROM TAKING_COURSE));
-- 과목 개설은 되었지만 해당 과목을 수강신청한 학생은 없는 과목

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

-- Type7
SELECT PostTitle, Writer 
FROM (SELECT * FROM POST WHERE Subject = 'science');
-- 과목이 과학인 글의 제목과 작성자

SELECT Name, Major, Email_Address
FROM (SELECT * FROM TEACHER WHERE Major = 'Computer Science');
-- 전공이 컴퓨터공학인 선생님의 이름, 전공, 이메일

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

-- Type9
SELECT STUDENT.StudentNumber, COUNT(*) AS Cnt
FROM COURSE, TAKING_COURSE, STUDENT
WHERE COURSE.CourseNumber = TAKING_COURSE.CourseNumber
AND STUDENT.StudentNumber = TAKING_COURSE.StudentNumber
AND COURSE.Subject = 'math'
GROUP BY STUDENT.StudentNumber
HAVING COUNT(*) >= 2
ORDER BY STUDENT.StudentNumber ASC;
-- 학생별 서로 다른 수학 과목을 2개이상 듣는 학생의 이름과 수 그리고 이를 학생의 이름을 기준으로 오름차순 정렬

SELECT TEACHER.TeacherNumber, COUNT(*) AS StudentCnt
FROM TEACHER, COURSE, TAKING_COURSE, STUDENT
WHERE COURSE.CourseNumber = TAKING_COURSE.CourseNumber
AND STUDENT.StudentNumber = TAKING_COURSE.StudentNumber
AND COURSE.TeacherNo = TEACHER.TeacherNumber
AND COURSE.Subject = 'math'
AND TEACHER.Major = 'Computer Science'
GROUP BY TEACHER.TeacherNumber
ORDER BY StudentCnt DESC;
-- 전공이 컴퓨터공학이고 수학 과목을 가르치는 선생님의 번호와 매칭된 학생 수를 내림차순으로 정렬 

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