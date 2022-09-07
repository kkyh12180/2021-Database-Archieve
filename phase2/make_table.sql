CREATE TABLE ADMIN (
	AdminID 		VARCHAR(30) 	NOT NULL,
	PW              VARCHAR(30)     NOT NULL,
    Email_address 	VARCHAR(50)     NOT NULL,
	Phone_number 	CHAR(11) 	    NOT NULL,
	Name 			VARCHAR(30)  	NOT NULL,
	PRIMARY KEY (AdminID)
	);

CREATE TABLE TEACHER (
    TeacherID       VARCHAR(30) 	NOT NULL,
    TeacherNumber   VARCHAR(10)     NOT NULL,
	PW              VARCHAR(30)     NOT NULL,
    Email_address 	VARCHAR(50)     NOT NULL,
	Phone_number 	CHAR(11) 	    NOT NULL,
	Name 			VARCHAR(30)  	NOT NULL,
    Address         VARCHAR(100),
    Major           VARCHAR(30),
    Gender          CHAR            NOT NULL,
    Education       VARCHAR(15),
    PRIMARY KEY (TeacherNumber),
    UNIQUE (TeacherID)
);

CREATE TABLE STUDENT (
    StudentID       VARCHAR(30) 	NOT NULL,
    StudentNumber   VARCHAR(10)     NOT NULL,
    PW              VARCHAR(30)     NOT NULL,
    Email_address 	VARCHAR(50)     NOT NULL,
	Phone_number 	CHAR(11) 	    NOT NULL,
	Name 			VARCHAR(30)  	NOT NULL,
    PRIMARY KEY (StudentNumber),
    UNIQUE (StudentID)
);

--기존 relational model에 있는 writer를 TeacherName을 참조하도록
CREATE TABLE POST (
    PostNumber      INT             NOT NULL,
    PostTitle       VARCHAR(50)     NOT NULL,
    PostText        VARCHAR(300)    NOT NULL,
    Writer          VARCHAR(30)     NOT NULL,
    Subject         VARCHAR(15)     NOT NULL,
    TeacherNo       VARCHAR(10)     NOT NULL,
    PRIMARY KEY (PostNumber),
    FOREIGN KEY (Writer) REFERENCES TEACHER(TeacherID),
    FOREIGN KEY (TeacherNo) REFERENCES TEACHER(TeacherNumber)
);

CREATE TABLE MATCHING (
    MatchingNumber  VARCHAR(10)     NOT NULL,
    TeacherNumber   VARCHAR(10)     NOT NULL,
    PostNumber      INT             NOT NULL,
    PRIMARY KEY (MatchingNumber),
    FOREIGN KEY (TeacherNumber) REFERENCES TEACHER(TeacherNumber),
    FOREIGN KEY (PostNumber) REFERENCES POST(PostNumber)
);

CREATE TABLE COURSE (
    CourseNumber    VARCHAR(10)     NOT NULL,
    Subject         VARCHAR(15)     NOT NULL,
    CourseName      VARCHAR(20)     NOT NULL,
    TeacherNo       VARCHAR(10)     NOT NULL,
    MatchNO         VARCHAR(10)     NOT NULL,
    PRIMARY KEY (CourseNumber),
    FOREIGN KEY (MatchNO) REFERENCES MATCHING(MatchingNumber),
    FOREIGN KEY (TeacherNo) REFERENCES TEACHER(TeacherNumber)
);

CREATE TABLE CLASSROOM (
    ClassroomID     VARCHAR(10)     NOT NULL,
    CourseNo        VARCHAR(10)     NOT NULL,
    Syllabus        VARCHAR(100),
    PRIMARY KEY (ClassroomID, CourseNo),
    FOREIGN KEY (CourseNo) REFERENCES COURSE(CourseNumber)
);

CREATE TABLE TEACHER_MANAGEMENT (
    TeacherNumber   VARCHAR(10)     NOT NULL,
    AdminID 		VARCHAR(30) 	NOT NULL,
    PRIMARY KEY (TeacherNumber, AdminID),
    FOREIGN KEY (TeacherNumber) REFERENCES TEACHER(TeacherNumber),
    FOREIGN KEY (AdminID) REFERENCES ADMIN(AdminID)
);

CREATE TABLE POST_MANAGEMENT (
    PostNumber      INT             NOT NULL,
    AdminID 		VARCHAR(30) 	NOT NULL,
    PRIMARY KEY (PostNumber, AdminID),
    FOREIGN KEY (PostNumber) REFERENCES POST(PostNumber),
    FOREIGN KEY (AdminID) REFERENCES ADMIN(AdminID)
);

CREATE TABLE READING_POST (
    PostNumber      INT             NOT NULL,
    StudentNumber   VARCHAR(10)     NOT NULL,
    PRIMARY KEY (PostNumber, StudentNumber),
    FOREIGN KEY (PostNumber) REFERENCES POST(PostNumber),
    FOREIGN KEY (StudentNumber) REFERENCES STUDENT(StudentNumber)
);

CREATE TABLE CONTAINING_STUDENT_INFO (
    StudentNumber   VARCHAR(10)     NOT NULL,
    MatchingNumber  VARCHAR(10)     NOT NULL,
    PRIMARY KEY (StudentNumber, MatchingNumber),
    FOREIGN KEY (StudentNumber) REFERENCES STUDENT(StudentNumber),
    FOREIGN KEY (MatchingNumber) REFERENCES MATCHING(MatchingNumber)
);

CREATE TABLE TAKING_COURSE (
    StudentNumber   VARCHAR(10)     NOT NULL,
    CourseNumber    VARCHAR(10)     NOT NULL,
    PRIMARY KEY (StudentNumber, CourseNumber),
    FOREIGN KEY (StudentNumber) REFERENCES STUDENT(StudentNumber),
    FOREIGN KEY (CourseNumber) REFERENCES COURSE(CourseNumber)
);

CREATE TABLE COURSE_ASSIGNMENT (
    Description     VARCHAR(100)    NOT NULL,
    Submission      VARCHAR(100),
    CID             VARCHAR(10)     NOT NULL,
    CourseNo        VARCHAR(10)     NOT NULL,
    PRIMARY KEY (CID, CourseNo),
    FOREIGN KEY (CID, CourseNo) REFERENCES CLASSROOM(ClassroomID, CourseNo)
);

CREATE TABLE COURSE_MATERIAL (
    Material        VARCHAR(100),
    CID             VARCHAR(10)     NOT NULL,
    CourseNo        VARCHAR(10)     NOT NULL,
    PRIMARY KEY (CID, CourseNo),
    FOREIGN KEY (CID, CourseNo) REFERENCES CLASSROOM(ClassroomID, CourseNo)
);

CREATE TABLE QA (
    Question        VARCHAR(100)    NOT NULL,
    Answer          VARCHAR(100),
    CID             VARCHAR(10)     NOT NULL,
    CourseNo        VARCHAR(10)     NOT NULL,
    PRIMARY KEY (CID, CourseNo),
    FOREIGN KEY (CID, CourseNo) REFERENCES CLASSROOM(ClassroomID, CourseNo)
);
