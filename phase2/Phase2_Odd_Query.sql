-- Type1
SELECT PostTitle, PostText, Writer, Subject
FROM POST
WHERE PostNumber = 49;
-- 특정 글 제목, 내용, 작성자, 과목

SELECT CourseName
FROM COURSE
WHERE CourseNumber = '1300000078';
-- 특정 과목의 이름



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



-- Type7
SELECT PostTitle, Writer 
FROM (SELECT * FROM POST WHERE Subject = 'science');
-- 과목이 과학인 글의 제목과 작성자

SELECT Name, Major, Email_Address
FROM (SELECT * FROM TEACHER WHERE Major = 'Computer Science');
-- 전공이 컴퓨터공학인 선생님의 이름, 전공, 이메일



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


