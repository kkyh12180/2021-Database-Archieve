import java.sql.*; // import JDBC package
import java.util.Random;
import java.util.Scanner;

public class Team6 {
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_ID = "team6";
	public static final String USER_PASSWD = "comp322";

	// ID, PW
	public static String inputID = "";
	public static String inputPW = "";
	public static int user_status = -1; // 0: admin, 1: teacher, 2: student

	public static void main(String[] args) {
		Connection conn = null; // Connection object
		Statement stmt = null; // Statement object

		System.out.println();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver Loading: Success!");
		} catch (ClassNotFoundException e) {
			System.err.println("error = " + e.getMessage());
			System.exit(1);
		}

		try {
			conn = DriverManager.getConnection(URL, USER_ID, USER_PASSWD);
			System.out.println("Oracle Connected.\n");
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("Cannot get a connection: " + ex.getLocalizedMessage());
			System.err.println("Cannot get a connection: " + ex.getMessage());
			System.exit(1);
		}

		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
		} catch (SQLException ex2) {
			ex2.printStackTrace();
		}

		if (!is_table(conn, stmt)) {
			// create table
			System.out.println("First Run Detected.");
			System.out.println("Creating Table...");
			create_table(conn, stmt);
			System.out.println("Creating Table Complete.\n");

			// insert_init_data
			System.out.println("Loading Initial Data...");
			insert_init_table(conn, stmt);
			System.out.println("Loading Initial Data Complete.\n");

			System.out.println("Hello!\n");
		} else {
			System.out.println("Welcome Back!\n");
		}

		Scanner keyboard = new Scanner(System.in);
		String firstInput = "";
		
		while(true) {
			System.out.println("");
			System.out.println("***********************************");
			System.out.println("      WELCOME! It's TEAM 6!");
			System.out.println("***********************************");
			System.out.println("             <<MENU>>");
			System.out.println("1: Sign in");
			System.out.println("2: Sign up");
			System.out.println("q: Quit");
			System.out.println("***********************************");
			System.out.printf("Input : ");
			firstInput = keyboard.nextLine();
			if(firstInput.equals("q")) {
				keyboard.close();
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}else if(firstInput.equals("2")) {
				sign_up(conn, stmt, keyboard);
				break;
			}
			else if(!firstInput.equals("1")) {
				System.out.println("You entered wrong menu. Try again!");
				continue;
			} else
				break;
		}
		/* LOGIN */
		while (true) {
			System.out.println("");
			System.out.println("************Let's login!***********");
			System.out.printf("Please enter your ID : ");
			inputID = keyboard.nextLine();
			System.out.printf("Please enter your password : ");
			inputPW = keyboard.nextLine();

			if (login_set_status(conn, stmt))
				break;
		}

		String input;
		while (true) {
			System.out.println("");
			System.out.println("********** <SELECT MENU> **********");
			System.out.println("1. Insert New Information");
			System.out.println("2. Delete");
			System.out.println("3. Update");
			System.out.println("4. Select");
			System.out.println();
			System.out.println("q. Quit");
			System.out.println("***********************************");
			System.out.printf("Input : ");
			input = keyboard.nextLine();
			if (input.compareTo("1") == 0) {
				System.out.println("[1. Insert New Information] Selected.\n");
				insert(conn, stmt, keyboard);
			} else if (input.compareTo("2") == 0) {
				System.out.println("[2. Delete] Selected.\n");
				delete(conn, stmt, keyboard);
			} else if (input.compareTo("3") == 0) {
				System.out.println("[3. Update] Selected.\n");
				update(conn, stmt, new Scanner(System.in));

			} else if (input.compareTo("4") == 0) {
				System.out.println("[4. Select] Selected.\n");
				select(conn, stmt, new Scanner(System.in));

			} else if (input.toLowerCase().compareTo("q") == 0) {
				System.out.println("\nGood bye!");
				break;
			} else {
				System.out.println("Invalid input. Try again.\n");
				continue;
			}
		}

		keyboard.close();
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean login_set_status(Connection conn, Statement stmt) {
		/*
		 * user_status: 0) admin, 1) teacher, 2) student
		 */
		ResultSet rs = null;
		String sql;
		String pw_of_ID; // pw from query
		try {
			sql = "SELECT PW FROM ADMIN WHERE AdminID = '" + inputID + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				pw_of_ID = rs.getString(1);
				user_status = 0; // user_status: admin
				rs.close();
				if (!pw_of_ID.equals(inputPW)) { // invalid
					System.out.println("Wrong password!");
					return false;
				}
				System.out.println("");
				System.out.println("===============ADMIN===============");
				return true;
			} else
				rs.close();

			sql = "SELECT PW FROM TEACHER WHERE TeacherID = '" + inputID + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				pw_of_ID = rs.getString(1);
				user_status = 1; // user_status: teacher
				rs.close();
				if (!pw_of_ID.equals(inputPW)) { // invalid
					System.out.println("Wrong password!");
					return false;
				}
				System.out.println("");
				System.out.println("==============TEACHER==============");
				return true;
			} else
				rs.close();

			sql = "SELECT PW FROM STUDENT WHERE StudentID = '" + inputID + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				pw_of_ID = rs.getString(1);
				user_status = 2; // user_status: student
				rs.close();
				if (!pw_of_ID.equals(inputPW)) { // invalid
					System.out.println("Wrong password!");
					return false;
				}
				System.out.println("");
				System.out.println("==============STUDENT==============");
				return true;
			} else
				rs.close();
		} catch (SQLException e) {
			return false;
		}
		System.out.println("You have entered an invalid ID.");
		return false;
	}

	public static void sign_up(Connection conn, Statement stmt, Scanner keyboard) {
		System.out.println("*** Nice to meet you! this is Team6! ***");
		System.out.println("*** Choose menu of STATUS you would like to join for! ***");
		System.out.println("0: ADMIN");
		System.out.println("1: TEACHER");
		System.out.println("2: STUDENT");
		System.out.println("q: quit sign-up menu");
		String input = keyboard.nextLine();
		String sql = "";
		String table = "";
		int num = 0;
		Random rd = new Random();
		switch (input) {
		case "q":
			return;
		case "0":
			table = "ADMIN";
			while (true) {
				System.out.printf("\nWrite your ID: ");
				String adminID = keyboard.nextLine();
				if (is_num(conn, stmt, table, adminID)) {
					System.out.println("\nInvalid ID or ID already exists");
					continue;
				}
				System.out.printf("\nWrite your password: ");
				String pw = keyboard.nextLine();
				System.out.printf("\nWrite your email: ");
				String email = keyboard.nextLine();
				System.out.printf("\nWrite your phone number: ");
				String phoneNum = keyboard.nextLine();
				boolean phone_is_num = true;
				while(true) {
					for(int i=0; i<phoneNum.length(); i++) {
						if(Character.isDigit(phoneNum.charAt(i)) == false) { // if characters at phoneNum are all number,
							phone_is_num = false;
							break;
						}
					}
					if(phone_is_num) break;
					else {
						System.out.println("You can only use numbers as your phone number.");
						System.out.printf("try again: ");
						phoneNum = keyboard.nextLine();
						phone_is_num = true;
					}
				}
				System.out.printf("\nWrite your name: ");
				String name = keyboard.nextLine();
				System.out.println();
				sql = "INSERT INTO ADMIN VALUES ('" + adminID + "', '" + pw + "', '" + email + "', '" + phoneNum
						+ "', '" + name + "')";
				try {
					int res = stmt.executeUpdate(sql);
					if (res != 1) {
						System.out.println("You entered wrong information!");
						continue;
					} else break;
				} catch (SQLException e) {
					System.out.println("Error occured.");
					System.exit(1);
				}
			}
			break;

		case "1":
			table = "TEACHER";
			while (true) {
				while (true) {
					num = 1000000000 + rd.nextInt(100000000);
					if (is_num(conn, stmt, table, Integer.toString(num)))
						continue;
					else
						break;
				}
				String teacherID;
				while (true) {
					System.out.printf("\nWrite your ID: ");
					teacherID = keyboard.nextLine();
					if (is_ID(conn, stmt, teacherID)) {
						System.out.println("\nInvalid ID or ID already exists");
						continue;
					} else
						break;
				}
				System.out.printf("\nWrite your password: ");
				String pw = keyboard.nextLine();
				System.out.printf("\nWrite your email: ");
				String email = keyboard.nextLine();
				System.out.printf("\nWrite your phone number: ");
				String phoneNum = keyboard.nextLine();
				boolean phone_is_num = true;
				while(true) {
					for(int i=0; i<phoneNum.length(); i++) {
						if(Character.isDigit(phoneNum.charAt(i)) == false) { // if characters at phoneNum are all number,
							phone_is_num = false;
							break;
						}
					}
					if(phone_is_num) break;
					else {
						System.out.println("You can only use numbers as your phone number.");
						System.out.printf("try again: ");
						phoneNum = keyboard.nextLine();
						phone_is_num = true;
					}
				}
				System.out.printf("\nWrite your name: ");
				String name = keyboard.nextLine();
				System.out.printf("\nWrite your address: ");
				String address = keyboard.nextLine();
				System.out.printf("\nWrite your major: ");
				String major = keyboard.nextLine();
				System.out.printf("\nWrite your gender (M/F): ");
				String gender = keyboard.nextLine();
				while(!gender.equals("M") && !gender.equals("F")) {
					System.out.printf("You can only choose M or F. Try again: ");
					gender = keyboard.nextLine();
				}
				System.out.printf("\nWrite your education: ");
				String education = keyboard.nextLine();
				System.out.println();
				sql = "INSERT INTO TEACHER VALUES ('" + teacherID + "', '" + num + "', '" + pw + "', '" + email + "', '"
						+ phoneNum + "', '" + name + "', '" + address + "', '" + major + "', '" + gender + "', '"
						+ education + "')";
				try {
					int res = stmt.executeUpdate(sql);
					if (res != 1) {
						System.out.println("You entered wrong information!");
						continue;
					} else break;
				} catch (SQLException e) {
					System.out.println("Error occured.");
					System.exit(1);
				}
			}
			break;

		case "2":
			table = "STUDENT";
			while (true) {
				while (true) {
					num = 1100000000 + rd.nextInt(100000000);
					if (is_num(conn, stmt, table, Integer.toString(num)))
						continue;
					else
						break;
				}
				String studentID;
				while (true) {
					System.out.printf("\nWrite your ID: ");
					studentID = keyboard.nextLine();
					if (is_ID(conn, stmt, studentID)) {
						System.out.println("\nInvalid ID or ID already exists");
						continue;
					} else
						break;
				}
				System.out.printf("\nWrite your password: ");
				String pw = keyboard.nextLine();
				System.out.printf("\nWrite your email: ");
				String email = keyboard.nextLine();
				System.out.printf("\nWrite your phone number: ");
				String phoneNum = keyboard.nextLine();
				boolean phone_is_num = true;
				while(true) {
					for(int i=0; i<phoneNum.length(); i++) {
						if(Character.isDigit(phoneNum.charAt(i)) == false) { // if characters at phoneNum are all number,
							phone_is_num = false;
							break;
						}
					}
					if(phone_is_num) break;
					else {
						System.out.println("You can only use numbers as your phone number.");
						System.out.printf("try again: ");
						phoneNum = keyboard.nextLine();
						phone_is_num = true;
					}
				}
				System.out.printf("\nWrite your name: ");
				String name = keyboard.nextLine();
				System.out.println();
				sql = "INSERT INTO STUDENT VALUES ('" + studentID + "', '" + num + "', '" + pw + "', '" + email + "', '"
						+ phoneNum + "', '" + name + "')";
				try {
					int res = stmt.executeUpdate(sql);
					if (res != 1) {
						System.out.println("You entered wrong information!");
						continue;
					} else break;
				} catch (SQLException e) {
					System.out.println("Error occured.");
					System.exit(1);
				}
			}
			break;
		default:
			System.out.println("Invalid input.");
			return;
		}
	}
	public static boolean is_num(Connection conn, Statement stmt, String table, String num) {
		ResultSet rs = null;
		String sql = "";
		try {
			if (table.toLowerCase().compareTo("classroom") == 0)
				sql = "SELECT * FROM CLASSROOM WHERE ClassroomID = '" + num + "'";
			else if (table.toLowerCase().compareTo("admin") == 0)
				sql = "SELECT * FROM ADMIN WHERE AdminID = '" + num + "'";
			else
				sql = "SELECT * FROM " + table.toUpperCase() + " WHERE " + table.substring(0, 1).toUpperCase()
						+ table.substring(1).toLowerCase() + "Number = '" + num + "'";

			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rs.close();
				return true;
			} else
				rs.close();
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	public static boolean is_ID(Connection conn, Statement stmt, String id) {
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "SELECT * FROM ADMIN WHERE AdminID = '" + id + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rs.close();
				return true;
			} else
				rs.close();

			sql = "SELECT * FROM TEACHER WHERE TeacherID = '" + id + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rs.close();
				return true;
			} else
				rs.close();

			sql = "SELECT * FROM STUDENT WHERE StudentID = '" + id + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rs.close();
				return true;
			} else
				rs.close();
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	public static boolean is_ID(Connection conn, Statement stmt, String table, String id) {
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "SELECT * FROM " + table.toUpperCase() + " WHERE " + table.substring(0, 1).toUpperCase()
					+ table.substring(1).toLowerCase() + "ID = '" + id + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rs.close();
				return true;
			} else
				rs.close();
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	public static int get_max_post_num(Connection conn, Statement stmt) {
		ResultSet rs = null;
		String sql = "SELECT MAX(PostNumber) FROM POST";
		int max = 0;
		try {
			rs = stmt.executeQuery(sql);
			rs.next();
			max = rs.getInt(1);
		} catch (SQLException e) {
			return 0;
		}
		return max;
	}

	public static String get_num(Connection conn, Statement stmt, String table, String ID) {
		ResultSet rs = null;
		String sql = "";
		String num = "";
		try {
			sql = "SELECT " + table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase() + "Number FROM "
					+ table.toUpperCase() + " WHERE " + table.substring(0, 1).toUpperCase()
					+ table.substring(1).toLowerCase() + "ID = '" + ID + "'";
			rs = stmt.executeQuery(sql);
			rs.next();
			num = rs.getString(1);
		} catch (SQLException e) {
			System.out.println("No ID found.");
		}
		return num;
	}

	public static int get_count(Connection conn, Statement stmt, String table) {
		ResultSet rs = null;
		String sql = "";
		int num = 0;
		try {
			sql = "SELECT COUNT(*) FROM " + table.toUpperCase();
			rs = stmt.executeQuery(sql);
			rs.next();
			num = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}

	public static void insert(Connection conn, Statement stmt, Scanner keyboard) {

		/*****************************************************************************
		 * 10s: teacher / 11s: student / 12s: matching / 13s: course / 14s: classroom
		 * 0: admin, 1: teacher, 2: student
		 *****************************************************************************/

		String table = "";
		String sql = "";
		String input = "0";
		int num = 0;

		if (user_status == 0) {
			System.out.println("---------------- Select a menu. ----------------");
			System.out.println("1. Manage Teacher"); // admin
			System.out.println("2. Manage Post"); // admin
			System.out.println("------------------------------------------------");
			
			input = keyboard.nextLine();
			
			switch (input) {
			case "1":
				table = "TEACHER_MANAGEMENT";
				String mtid = "";
				while (true) {
					System.out.printf("\nWrite teacher ID that you will manage: ");
					String ID = keyboard.nextLine();
					if (is_ID(conn, stmt, "teacher", ID)) {
						mtid = get_num(conn, stmt, "teacher", ID);
						break;
					} else {
						System.out.println("\nID not found.");
						continue;
					}
				}

				sql = "INSERT INTO TEACHER_MANAGEMENT VALUES ('" + mtid + "', '" + inputID + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;

			case "2":
				table = "POST_MANAGEMENT";
				int mpnum;
				while (true) {
					System.out.printf("\nWrite post number that you will manage: ");
					mpnum = keyboard.nextInt();
					keyboard.nextLine();
					if (mpnum <= get_max_post_num(conn, stmt)) {
						break;
					} else {
						System.out.println("\nInvalid post number.");
						continue;
					}
				}

				sql = "INSERT INTO POST_MANAGEMENT VALUES (" + mpnum + ", '" + inputID + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;
				
			default:
				System.out.println("Invalid input.");
				return;
			}
			
		} else if (user_status == 1) {
			System.out.println("---------------- Select a menu. ----------------");
			System.out.println("1. Write New Post"); // teacher
			System.out.println("2. Make new Matching"); // teacher
			System.out.println("3. Make new Course"); // make classroom too, teacher
			System.out.println("4. Write Course Assingment"); // teacher
			System.out.println("5. Write Course Material"); // teacher
			System.out.println("------------------------------------------------");
			
			input = keyboard.nextLine();
			
			switch (input) {
			case "1":
				table = "POST";
				num = get_max_post_num(conn, stmt) + 1;
				System.out.printf("\nWrite title: ");
				String title = keyboard.nextLine();
				System.out.printf("\nWrite text: ");
				String text = keyboard.nextLine();
				String writer = new String(inputID);
				System.out.printf("\nWrite your subject: ");
				String subject = keyboard.nextLine();
				String number = get_num(conn, stmt, "teacher", writer);
				System.out.println();
				
				sql = "INSERT INTO POST VALUES (" + num + ", '" + title + "', '" + text + "', '" + writer + "', '" + subject
						+ "', '" + number + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;

			case "2":
				table = "MATCHING";
				num = get_count(conn, stmt, table);
				num += 1200000000;
				String t_num = get_num(conn, stmt, "teacher", inputID);
				System.out.printf("\nWrite post number: ");
				String p_num = keyboard.nextLine();
				System.out.println();
				
				sql = "INSERT INTO MATCHING VALUES ('" + num + "', '" + t_num + "', " + p_num + ")";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;

			case "3":
				table = "COURSE";
				num = get_count(conn, stmt, table);
				num += 1300000000;
				System.out.printf("\nWrite your subject: ");
				String subj = keyboard.nextLine();
				System.out.printf("\nWrite your course name: ");
				String cn = keyboard.nextLine();
				String tid = get_num(conn, stmt, "teacher", inputID);
				String mn = "";
				while (true) {
					System.out.printf("\nWrite your matching number: ");
					mn = keyboard.nextLine();
					if (is_num(conn, stmt, "matching", mn))
						break;
					else
						continue;
				}
				System.out.println();
				
				sql = "INSERT INTO COURSE VALUES ('" + num + "', '" + subj + "', '" + cn + "', '" + tid + "', '" + mn
						+ "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}

				int cnum = num + 100000000;
				System.out.printf("\nWrite syllabus: ");
				String syllabus = keyboard.nextLine();
				
				sql = "INSERT INTO CLASSROOM VALUES ('" + cnum + "', '" + num + "', '" + syllabus + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;
				
			case "4":
				table = "COURSE_ASSIGNMENT";
				System.out.printf("\nWrite assignment description: ");
				String description = keyboard.nextLine();
				String cacn = "";
				while (true) {
					System.out.printf("\nWrite course number that you participate in: ");
					cacn = keyboard.nextLine();
					if (is_num(conn, stmt, "course", cacn))
						break;
					else {
						System.out.println("\nCourse not found.");
						continue;
					}
				}
				String cacid = Integer.toString(Integer.parseInt(cacn) + 100000000);
				
				sql = "INSERT INTO COURSE_ASSIGNMENT VALUES ('" + description + "', '', '" + cacid + "', '" + cacn + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;

			case "5":
				table = "COURSE_MATERIAL";
				System.out.printf("\nWrite material: ");
				String material = keyboard.nextLine();
				String cmcn = "";
				while (true) {
					System.out.printf("\nWrite course number that you participate in: ");
					cmcn = keyboard.nextLine();
					if (is_num(conn, stmt, "course", cmcn))
						break;
					else {
						System.out.println("\nCourse not found.");
						continue;
					}
				}
				String cmcid = Integer.toString(Integer.parseInt(cmcn) + 100000000);
				
				sql = "INSERT INTO COURSE_MATERIAL VALUES ('" + material + "', '" + cmcid + "', '" + cmcn + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;
				
			default:
				System.out.println("Invalid input.");
				return;
			}
			
		} else if (user_status == 2) {
			System.out.println("---------------- Select a menu. ----------------");
			System.out.println("1. Read New Post"); // student
			System.out.println("2. Want to Take New Course. Participate in Matching"); // student
			System.out.println("3. Take New Course"); // studnet
			System.out.println("4. Write Qusetion"); // student
			System.out.println("------------------------------------------------");
			
			input = keyboard.nextLine();
			
			switch (input) {
			case "1":
				table = "READING_POST";
				int rpnum;
				while (true) {
					System.out.printf("\nWrite post number that you read: ");
					rpnum = keyboard.nextInt();
					keyboard.nextLine();
					if (rpnum <= get_max_post_num(conn, stmt)) {
						break;
					} else {
						System.out.println("\nInvalid post number.");
						continue;
					}
				}
				String rnum = get_num(conn, stmt, "student", inputID);
	
				sql = "INSERT INTO READING_POST VALUES (" + rpnum + ", '" + rnum + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;

			case "2":
				table = "CONTAINING_STUDENT_INFO";
				String csnum = get_num(conn, stmt, "student", inputID);
				String cmn = "";
				while (true) {
					System.out.printf("\nWrite matching number that you want to participate: ");
					cmn = keyboard.nextLine();
					if (is_num(conn, stmt, "matching", cmn))
						break;
					else {
						System.out.println("\nMatching not found.");
						continue;
					}
				}
				
				sql = "INSERT INTO CONTAINING_STUDENT_INFO VALUES ('" + csnum + "', '" + cmn + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;

			case "3":
				table = "TAKING_COURSE";
				String tcsnum = get_num(conn, stmt, "student", inputID);
				String tcn = "";
				while (true) {
					System.out.printf("\nWrite course number that you want to participate: ");
					tcn = keyboard.nextLine();
					if (is_num(conn, stmt, "course", tcn))
						break;
					else {
						System.out.println("\nCourse not found.");
						continue;
					}
				}
				
				sql = "INSERT INTO TAKING_COURSE VALUES ('" + tcsnum + "', '" + tcn + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;

			case "4":
				table = "QA";
				System.out.printf("\nWrite question: ");
				String question = keyboard.nextLine();
				String qacn = "";
				while (true) {
					System.out.printf("\nWrite course number that you participate in: ");
					qacn = keyboard.nextLine();
					if (is_num(conn, stmt, "course", qacn))
						break;
					else {
						System.out.println("\nCourse not found.");
						continue;
					}
				}
				String qacid = Integer.toString(Integer.parseInt(qacn) + 100000000);
				sql = "INSERT INTO QA VALUES ('" + question + "', '', '" + qacid + "', '" + qacn + "')";
				// System.out.println(sql);
				try {
					stmt.addBatch(sql);
				} catch (SQLException e) {
					System.out.println("Error occured.");
					return;
				}
				break;

			default:
				System.out.println("Invalid input.");
				return;
			}
		} else {
			System.err.println("User_status error.");
			keyboard.close();
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		
		try {
			// execute
			stmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			System.out.println("\nFailed. Data already exists.");
			return;
		}

		System.out.println();
		if (user_status == 1 && (input.compareTo("1") == 0)) {
			System.out.println("Post Created.");
			System.out.println("Your Post Number: " + num);
		} else if (user_status == 1 && (input.compareTo("2") == 0)) {
			System.out.println("Matching Created.");
			System.out.println("Matching Number: " + num);
		} else if (user_status == 1 && (input.compareTo("3") == 0)) {
			System.out.println("Course and Classroom Created.");
			System.out.println("Course Number: " + num);
		} else {
			System.out.println("Data Saved Successfully.");
		}
		System.out.println();
	}

	public static void delete(Connection conn, Statement stmt, Scanner sc) {
		String sql = "";

		/**
		 * <student> 1. membership withdrawal 2. stop taking course
		 * 
		 * <teacher> 1. membership withdrawal 2. delete classroom 3. delete course 4.
		 * delete QA 5. delete Post
		 */

		if (user_status == 1) { // teacher
			String teacherNumber = "";
			try {
				ResultSet rs = null;
				sql = "SELECT TeacherNumber FROM TEACHER WHERE TeacherID = '" + inputID + "'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					teacherNumber = rs.getString(1);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			while (true) {
				System.out.println("");
				System.out.println("***** What do you want to do? Select menu. *****");
				System.out.println("1: membership withdrawal");
				System.out.println("2: delete classroom");
				System.out.println("3: delete course");
				System.out.println("4: delete QA");
				System.out.println("5: delete post");
				System.out.println("q: quit delete");
				String input = sc.nextLine();

				if (input.compareTo("q") == 0) { // quit delete
					System.out.println("quit \"delete\" menu.");
					// sc.close();
					return;
				} else if (input.compareTo("1") == 0) { // (teacher) membership withdrawal
					System.out.println("-----1: membership withdrawal-----");
					System.out.println("(teacher) Do you really want to withdraw the membership? (y/n)");
					while (true) {
						input = sc.nextLine();
						if (input.compareTo("n") != 0 && input.compareTo("y") != 0)
							System.out.println("Wrong input. Try again. (y/n)");
						else
							break;
					}
					if (input.compareTo("n") == 0) {
						System.out.println("Withdrawal canceled.");
						continue;
					}
					System.out.println("Please reenter a password to verify your identity.");
					String input_pw = sc.nextLine();
					if (input_pw.compareTo(inputPW) != 0) {
						System.out.println("Wrong password.");
					}
					try {
						sql = "DELETE FROM TEACHER WHERE TeacherNumber = '" + teacherNumber + "'";
						stmt.executeUpdate(sql);

					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Withdrawal successed! Bye!");
					// sc.close();
					return;
				} else if (input.compareTo("2") == 0) { // (teacher) delete classroom
					System.out.println("-----2: delete classroom-----");
					System.out.println("Please enter the classroomID that you want to delete.");
					String classroomID = sc.nextLine();
					try {
						sql = "DELETE FROM CLASSROOM WHERE ClassroomID = '" + classroomID + "'"
								+ " AND CourseNo = (SELECT C.CourseNumber " + " FROM COURSE C "
								+ " WHERE C.TeacherNo = '" + teacherNumber + "')";

						int res = stmt.executeUpdate(sql);
						if (res != 1) {
							System.out.println("You entered wrong information!");
							continue;
						}
						System.out.println("The classroom successfully deleted!");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Classroom deleted.");
				} else if (input.compareTo("3") == 0) { // (teacher) delete course
					System.out.println("-----3: delete course-----");
					String courseNumber;
					System.out.println("Please enter the courseNumber of the course you want to delete.");
					courseNumber = sc.nextLine();
					try {
						sql = "DELETE FROM COURSE WHERE CourseNumber = '" + courseNumber + "' AND TeacherNo = '"
								+ teacherNumber + "'";
						int res = stmt.executeUpdate(sql);
						if (res != 1) {
							System.out.println("You entered wrong information!");
							continue;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Course deleted.");
				} else if (input.compareTo("4") == 0) { // (teacher) delete QA
					System.out.println("-----4: delete QA-----");
					String CID, courseNumber;
					System.out.println("Please enter the courseID of the QA.");
					CID = sc.nextLine();
					System.out.println("Please enter the courseNumber of the QA.");
					courseNumber = sc.nextLine();
					try {
						sql = "DELETE FROM QA WHERE CourseNo = '" + courseNumber + "' AND CID = '" + CID + "'"
								+ " AND CourseNo = (SELECT C.CourseNumber" + " FROM COURSE C" + " WHERE C.TeacherNo = '"
								+ teacherNumber + "')";

						int res = stmt.executeUpdate(sql);
						if (res != 1) {
							System.out.println("You entered wrong information!");
							continue;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("QA deleted.");

				} else if (input.compareTo("5") == 0) { // (teacher) delete post
					System.out.println("-----5: delete post-----");
					String PostNumber;
					System.out.println("Please enter the postNumber of the course you want to delete.");
					PostNumber = sc.nextLine();
					try {
						sql = "DELETE FROM POST WHERE PostNumber = '" + PostNumber + "' AND TeacherNo = '"
								+ teacherNumber + "'";
						int res = stmt.executeUpdate(sql);
						if (res != 1) {
							System.out.println("You entered wrong information!");
							continue;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Post deleted.");
				}
			}
		} else if (user_status == 2) { // student
			String studentNumber = "";
			ResultSet rs = null;
			try {
				sql = "SELECT StudentNumber FROM STUDENT WHERE StudentID = '" + inputID + "'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					studentNumber = rs.getString(1);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			while (true) {
				String input;
				System.out.println("");
				System.out.println("*** What do you want to do? ***");
				System.out.println("1: membership withdrawal");
				System.out.println("2: stop taking course");
				System.out.println("q: quit delete");
				input = sc.nextLine();

				if (input.compareTo("q") == 0) { // quit delete
					System.out.println("quit \"delete\" menu.");
					// sc.close();
					return;
				} else if (input.compareTo("1") == 0) { // (student) membership withdrawal
					System.out.println("-----1: membership withdrawal-----");
					System.out.println("(student) Do you want to withdraw the membership? (y/n)");
					while (true) {
						input = sc.nextLine();
						if (input.compareTo("n") != 0 && input.compareTo("y") != 0)
							System.out.println("Wrong input. Try again. (y/n)");
						else
							break;
					}
					if (input.compareTo("n") == 0) {
						System.out.println("Withdrawal canceled.");
						continue;
					}

					System.out.println("Please enter a password to verify your identity.");
					String input_pw = sc.nextLine();
					if (input_pw.compareTo(inputPW) != 0) {
						System.out.println("Wrong password.");
						// sc.close();
						return;
					}
					try {
						sql = "DELETE FROM STUDENT WHERE StudentNumber = '" + studentNumber + "'";
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Withdrawal successed! Bye!");
					// sc.close();
					return;

				} else if (input.compareTo("2") == 0) { // (student) stop taking course
					System.out.println("-----2: stop taking course-----");
					System.out.println("Please enter the courseNumber of the course you want to stop taking.");
					String CourseNumber;
					CourseNumber = sc.nextLine();
					try {
						sql = "DELETE FROM TAKING_COURSE WHERE CourseNumber = '" + CourseNumber
								+ "' AND StudentNumber = '" + studentNumber + "'";
						int res = stmt.executeUpdate(sql);
						if (res != 1) {
							System.out.println("You entered wrong information!");
							continue;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Successfully stopped taking the course.");
				}
			}
		} else {

			while (true) {
				String input;
				System.out.println("");
				System.out.println("*** What do you want to do? ***");
				System.out.println("1: membership withdrawal");
				System.out.println("q: quit delete");
				input = sc.nextLine();

				if (input.compareTo("q") == 0) { // quit delete
					System.out.println("quit \"delete\" menu.");
					return;
				} else if (input.compareTo("1") == 0) { // (admin) membership withdrawal
					System.out.println("-----1: membership withdrawal-----");
					System.out.println("(admin) Do you want to withdraw the membership? (y/n)");
					while (true) {
						input = sc.nextLine();
						if (input.compareTo("n") != 0 && input.compareTo("y") != 0)
							System.out.println("Wrong input. Try again. (y/n)");
						else
							break;
					}
					if (input.compareTo("n") == 0) {
						System.out.println("Withdrawal canceled.");
						continue;
					}

					System.out.println("Please enter a password to verify your identity.");
					String input_pw = sc.nextLine();
					if (input_pw.compareTo(inputPW) != 0) {
						System.out.println("Wrong password.");
						return;
					}
					try {
						sql = "DELETE FROM ADMIN WHERE AdminID = '" + inputID + "'";
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("Withdrawal successed! Bye!");
					return;

				}
			}
		}
	}

	public static void update(Connection conn, Statement stmt, Scanner sc) {
		// teacher, student, post, course. material
		String input;

		while (true) {
			System.out.println("hello you choose update part! you can update 4 options of our database\n\n"
					+ "1. you can update Name Column of STUDENT TABLE\n"
					+ "2. you can update Major Column of TEACHER TABLE\n"
					+ "3. you can update Title Column of POST TABLE\n"
					+ "4. you can update Subject Column of COURSE TABLE\n\n" + "push 'q' to quit update part!\n"
					+ "select number between 1 and 4 referencing above options!\n");

			System.out.printf("Input : ");
			input = sc.nextLine();
			if (input.compareTo("1") == 0) {
				System.out.println(
						"you selected 1 option\n" + "I'll show you all Names of STUDENT TABLE with StudentNumber\n");

				System.out.println("StudentNumber    Name\n" + "------------------------------");

				ResultSet rs = null;

				try {
					String name = "";
					String studentNumber = "";
					String sql = "SELECT StudentNumber, Name FROM STUDENT";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						name = rs.getNString(1);
						studentNumber = rs.getNString(2);
						System.out.println(name + "       " + studentNumber);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (true) {
					System.out.println(
							"Give me specific row's StudentNumber you want to update (type 'q' to quit update!)");
					System.out.printf("StudentNumber: ");
					input = sc.nextLine();

					if (input.compareTo("q") == 0) {
						break;
					}

					String sql = "SELECT Name FROM STUDENT WHERE StudentNumber = ";
					sql += input;

					try {
						rs = stmt.executeQuery(sql);
						rs.next();
						if (rs.getNString(1) != null) {
							System.out.println("Give me Name you want to change (length small than 30!)\n");
							System.out.printf("Name: ");
							String name = sc.nextLine();
							PreparedStatement pstmt = null;
							sql = "UPDATE STUDENT SET Name = ? WHERE StudentNumber = ?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, name);
							pstmt.setString(2, input);

							int rowCount = pstmt.executeUpdate();
							if (rowCount == 1) {
								System.out.println("update!\n");
								conn.commit();
								break;
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.printf("Oops! you gave wrong StudentNumber\nType again!\n\n");
					}
				}
			} else if (input.compareTo("2") == 0) {
				// // all teacher majors
				System.out.println(
						"you selected 2 option\n" + "I'll show you all Majors of TEACHER TABLE with TeacherNumber\n");

				System.out.println("TeacherNumber    Major\n" + "------------------------------");

				ResultSet rs = null;

				try {
					String major = "";
					String teacherNumber = "";
					String sql = "SELECT TeacherNumber, Major FROM TEACHER";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						major = rs.getNString(1);
						teacherNumber = rs.getNString(2);
						System.out.println(major + "       " + teacherNumber);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (true) {
					System.out.println(
							"Give me specific row's TeacherNumber you want to update (type 'q' to quit update!)");
					System.out.printf("TeacherNumber: ");
					input = sc.nextLine();

					if (input.compareTo("q") == 0) {
						break;
					}

					String sql = "SELECT Major FROM TEACHER WHERE TeacherNumber = ";
					sql += input;

					try {
						rs = stmt.executeQuery(sql);
						rs.next();
						if (rs.getNString(1) != null) {
							System.out.println("Give me Major you want to change (length small than 30!)\n");
							System.out.printf("Major: ");
							String major = sc.nextLine();
							PreparedStatement pstmt = null;
							sql = "UPDATE TEACHER SET Major = ? WHERE TeacherNumber = ?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, major);
							pstmt.setString(2, input);

							int rowCount = pstmt.executeUpdate();
							if (rowCount == 1) {
								System.out.println("update!\n");
								conn.commit();
								break;
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.printf("Oops! you gave wrong TeacherNumber\nType again!\n\n");
					}
				}

			} else if (input.compareTo("3") == 0) {
				// all post titles
				System.out.println(
						"you selected 3 option\n" + "I'll show you all titles of POST TABLE with PostNumber\n");

				System.out.println("PostNumber    Title\n" + "------------------------------");

				ResultSet rs = null;

				try {
					String title = "";
					String postNumber = "";
					String sql = "SELECT PostNumber, PostTitle FROM POST";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						postNumber = rs.getNString(1);
						title = rs.getNString(2);
						System.out.println(postNumber + "       " + title);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (true) {
					System.out
							.println("Give me specific row's PostNumber you want to update (type 'q' to quit update!)");
					System.out.printf("PostNumber: ");
					input = sc.nextLine();

					if (input.compareTo("q") == 0) {
						break;
					}

					String sql = "SELECT PostTitle FROM POST WHERE PostNumber = ";
					sql += input;

					try {
						rs = stmt.executeQuery(sql);
						rs.next();
						if (rs.getNString(1) != null) {
							System.out.println("Give me PostTitle you want to change (length smaller than 50!)\n");
							System.out.printf("Title: ");
							String title = sc.nextLine();
							PreparedStatement pstmt = null;
							sql = "UPDATE POST SET PostTitle = ? WHERE PostNumber = ?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, title);
							pstmt.setString(2, input);

							int rowCount = pstmt.executeUpdate();
							if (rowCount == 1) {
								System.out.println("update!\n");
								conn.commit();
								break;
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.printf("Oops! you gave wrong PostNumber\nType again!\n\n");
					}
				}

			} else if (input.compareTo("4") == 0) {
				// all course subjects
				System.out.println(
						"you selected 4 option\n" + "I'll show you all subjects of COURSE TABLE with CourseNumber\n");

				System.out.println("CourseNumber    Subject\n" + "------------------------------");

				ResultSet rs = null;

				try {
					String courseNum = "";
					String subject = "";
					String sql = "SELECT CourseNumber, Subject FROM COURSE";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						courseNum = rs.getNString(1);
						subject = rs.getNString(2);
						System.out.println(courseNum + "       " + subject);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (true) {
					System.out.println(
							"Give me specific row's CourseNumber you want to update (type 'q' to quit update!)");
					System.out.printf("CourseNumber: ");
					input = sc.nextLine();

					if (input.compareTo("q") == 0) {
						break;
					}

					String sql = "SELECT Subject FROM Course WHERE CourseNumber = ";
					sql += input;

					try {
						rs = stmt.executeQuery(sql);
						rs.next();
						if (rs.getNString(1) != null) {
							System.out.println("Give me Subject you want to change (length smaller than 15!)\n");
							System.out.printf("Subject: ");
							String subject = sc.nextLine();
							PreparedStatement pstmt = null;
							sql = "UPDATE COURSE SET Subject = ? WHERE CourseNumber = ?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, subject);
							pstmt.setString(2, input);

							int rowCount = pstmt.executeUpdate();
							if (rowCount == 1) {
								System.out.println("update!\n");
								conn.commit();
								break;
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						System.out.printf("Oops! you gave wrong CourseNumber\nType again!\n\n");
					}
				}

			} else if (input.toLowerCase().compareTo("q") == 0) {
				System.out.println("\nGood bye!");
				break;
			} else {
				System.out.println("Invalid input. Try again.\n");
				continue;
			}
		}

	}

	public static void select(Connection conn, Statement stmt, Scanner sc) {
		String input;

		while (true) {
			System.out.println("hello you choose select part!\n"
							 + "1. if you logined admin then type 1\n"
							 + "2. if you logined teacher then type 2\n"
							 + "3. if you logined student then type 3\n\n"
							 + "push 'q' to quit select part!\n"
							 + "pick number between 1 and 3 referencing above options!\n");
			
			System.out.printf("Input : ");
			input = sc.nextLine();
			
			if (input.compareTo("1") == 0) {
				while (true) {
					System.out.println("you choose admin part!\n"
							 + "Here you can see one student or teacher to restrict the user who violate rules in the community");
					System.out.println("type 1 to see student's list\n"
									 + "type 2 to see teacher's list\n"
									 + "type 'q' to quit admin select part");
					System.out.printf("Input : ");		
					input = sc.nextLine();
			
					if (input.compareTo("1") == 0) {
						System.out.println("you want to see student's list\n"
										  +"I'll show you student's list below\n"
										  +"StudentID       Name\n"
										  +"--------------------------");
						
						String sql = "SELECT StudentID, Name FROM STUDENT";
						ResultSet rs = null;
						try {
							rs = stmt.executeQuery(sql);
							String studentId = "";
							String name = "";
							while (rs.next()) {
								studentId = rs.getNString(1);
								name = rs.getNString(2);
								System.out.println(studentId + "     " + name);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						while (true) {
							
							System.out.println("you can see one student's information if you give studentId (type 'q' to quit!)");
							System.out.printf("StudentId : ");		
							input = sc.nextLine();

							if (input.compareTo("q") == 0) {
								break;
							}

							sql = "SELECT StudentNumber, Email_address, Phone_number, Name FROM STUDENT WHERE StudentID = ";
							sql += "'" + input + "'";

							try {
								rs = stmt.executeQuery(sql);
								String studentNum = "";
								String email = "";
								String phoneNum = "";
								String name = "";
								if (rs.next()) {
									studentNum = rs.getNString(1);
									email = rs.getNString(2);
									phoneNum = rs.getNString(3);
									name = rs.getNString(4);
									System.out.println("StudentNumber       Email              Phone              Name");
									System.out.println("------------------------------------------------------------------------------");
									System.out.println(studentNum + "     " + email + "      " + phoneNum + "      " + name);
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.printf("Oops! you gave wrong StudentID\nType again!\n\n");
							}
						}
					} else if (input.compareTo("2") == 0) {
						System.out.println("you want to see teacher's list\n"
								  +"I'll show you teacher's list below\n"
								  +"TeacherID       Name\n"
								  +"--------------------------");
				
						String sql = "SELECT TeacherID, Name FROM TEACHER";
						ResultSet rs = null;
						try {
							rs = stmt.executeQuery(sql);
							String teacherId = "";
							String name = "";
							while (rs.next()) {
								teacherId = rs.getNString(1);
								name = rs.getNString(2);
								System.out.println(teacherId + "     " + name);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						while (true) {
							
							System.out.println("you can see one teacher's information if you give teacherId (type 'q' to quit!)");
							System.out.printf("TeacherId : ");		
							input = sc.nextLine();
		
							if (input.compareTo("q") == 0) {
								break;
							}
		
							sql = "SELECT TeacherNumber, Email_address, Phone_number, Name FROM TEACHER WHERE TeacherID = ";
							sql += "'" + input + "'";
		
							try {
								rs = stmt.executeQuery(sql);
								String teacherNum = "";
								String email = "";
								String phoneNum = "";
								String name = "";
								if (rs.next()) {
									teacherNum = rs.getNString(1);
									email = rs.getNString(2);
									phoneNum = rs.getNString(3);
									name = rs.getNString(4);
									System.out.println("TeacherNumber       Email               Phone              Name");
									System.out.println("-------------------------------------------------------------------------------");
									System.out.println(teacherNum + "     " + email + "      " + phoneNum + "      " + name);
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.printf("Oops! you gave wrong StudentID\nType again!\n\n");
							}
						}
					} else if (input.toLowerCase().compareTo("q") == 0) {
						System.out.println("quit admin select part!.\n");
						break;
					} else {
						System.out.println("Invalid input. Try again.\n");
						continue;
					}
				}
			} else if (input.compareTo("2") == 0) {
				while (true) {
					System.out.println("you choose teacher part!\n"
							 + "Here you can see list of all students matched specific teacher");
					System.out.println("type 1 to see the result and type 'q' to quit teacher select part");
					System.out.printf("Input : ");		
					input = sc.nextLine();
			
					if (input.compareTo("1") == 0) {
						System.out.println("I'll show you teacher's number list below\n"
										  +"TeacherNumber\n"
										  +"--------------------------");
						
						String sql = "SELECT TeacherNumber FROM TEACHER";
						ResultSet rs = null;
						try {
							rs = stmt.executeQuery(sql);
							String teacherNum = "";
							while (rs.next()) {
								teacherNum = rs.getNString(1);
								System.out.println(teacherNum);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						while (true) {
							
							System.out.println("you can see list of all students matched specific teacher if you give TeacherNumber (type 'q' to quit!)");
							System.out.printf("TeacherNumber : ");		
							input = sc.nextLine();

							if (input.compareTo("q") == 0) {
								break;
							}

							sql = "SELECT student.studentnumber, student.name "
									+ "FROM teacher, matching, student, containing_student_info "
									+ "WHERE matching.matchingnumber = containing_student_info.matchingnumber "
									+ "AND student.studentnumber = containing_student_info.studentnumber "
									+ "AND teacher.teachernumber = matching.teachernumber "
									+ "AND teacher.teachernumber = ";
							sql += input;

							try {
								rs = stmt.executeQuery(sql);
								String studentNum = "";
								String name = "";
								if (rs.next()) {
									studentNum = rs.getNString(1);
									name = rs.getNString(2);
									System.out.println("StudentNumber       Name");
									System.out.println("-------------------------------");
									System.out.println(studentNum + "     " + name);
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.printf("Oops! you gave wrong StudentID\nType again!\n\n");
							}
						}
					} else if (input.toLowerCase().compareTo("q") == 0) {
						System.out.println("quit admin select part!.\n");
						break;
					} else {
						System.out.println("Invalid input. Try again.\n");
						continue;
					}
				}
			} else if (input.compareTo("3") == 0) {
				while (true) {
					System.out.println("you choose student part!");
					System.out.println("type 1 to query list of all posts that subject is your input\n"
									 + "type 2 to query list of all the courses that some student takes\n"
									 + "type 'q' to quit admin select part");
					System.out.printf("Input : ");		
					input = sc.nextLine();
			
					if (input.compareTo("1") == 0) {
						System.out.println("I'll show you course's subjects below\n"
										  +"Subject\n"
										  +"--------------");
						
						String sql = "SELECT DISTINCT Subject FROM Course";
						ResultSet rs = null;
						try {
							rs = stmt.executeQuery(sql);
							String subject = "";
							while (rs.next()) {
								subject = rs.getNString(1);
								System.out.println(subject);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						while (true) {
							
							System.out.println("you can see list of all posts that subject is your input value (type 'q' to quit!)");
							System.out.printf("Subject : ");		
							input = sc.nextLine();

							if (input.compareTo("q") == 0) {
								break;
							}

							try {
								sql = "SELECT PostTitle, Writer FROM POST WHERE Subject = ";
								sql += "'" + input + "'";
								rs = stmt.executeQuery(sql);
								String title = "";
								String writer = "";
								System.out.println("Title     Writer");
								System.out.println("-------------------");
								while (rs.next()) {
									title = rs.getNString(1);
									writer = rs.getNString(2);

									System.out.println(title + "     " + writer);
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.printf("Oops! you gave wrong Subject value\nType again!\n\n");
								e.printStackTrace();
							}
						}
					} else if (input.compareTo("2") == 0) {
						System.out.println("I'll show you student's number below\n"
								  +"StudentNumber\n"
								  +"--------------------");
				
						String sql = "SELECT StudentNumber FROM STUDENT";
						ResultSet rs = null;
						try {
							rs = stmt.executeQuery(sql);
							String studentNum = "";
							while (rs.next()) {
								studentNum = rs.getNString(1);
								System.out.println(studentNum);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						while (true) {
							
							System.out.println("type StudentNumber if you wanna see courses that the student takes (type 'q' to quit!)");
							System.out.printf("StudentNumber : ");		
							input = sc.nextLine();

							if (input.compareTo("q") == 0) {
								break;
							}

							try {
								sql = "SELECT COURSE.CourseNumber, COURSE.CourseName, COURSE.Subject "
										+ "FROM COURSE, TAKING_COURSE, STUDENT "
										+ "WHERE COURSE.CourseNumber = TAKING_COURSE.CourseNumber "
										+ "AND TAKING_COURSE.StudentNumber = STUDENT.StudentNumber "
										+ "AND STUDENT.StudentNumber = ";
								sql += "'" + input + "'";
								rs = stmt.executeQuery(sql);
								String courseNum = "";
								String courseName = "";
								String subject = "";
								
								System.out.println("CourseNumber       CourseName         Subject\n"
										 + "------------------------------------------------------");
								while (rs.next()) {
									courseNum = rs.getNString(1);
									courseName = rs.getNString(2);
									subject = rs.getNString(3);
									
									System.out.println(courseNum + "     " + courseName + "      " + subject);
								}
						
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.printf("Oops! you gave wrong Subject value\nType again!\n\n");
							}
						}
					} else if (input.toLowerCase().compareTo("q") == 0) {
						System.out.println("quit admin select part!.\n");
						break;
					} else {
						System.out.println("Invalid input. Try again.\n");
						continue;
					}
				}
			} else if (input.toLowerCase().compareTo("q") == 0) {
				System.out.println("\nGood bye!");
				break;
			} else {
				System.out.println("Invalid input. Try again.\n");
				continue;
			}
		}
	}

	public static boolean is_table(Connection conn, Statement stmt) {
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM ADMIN";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}

	public static void create_table(Connection conn, Statement stmt) {

		/*********************************************
		 * ON DELETE CASCADE constraints added
		 **********************************************/

		String sql = "";
		try {
			int res;

			// CREATE ADMIN TABLE
			sql = "CREATE TABLE ADMIN ( " + "	AdminID 		VARCHAR(30) 	NOT NULL, "
					+ "	PW              VARCHAR(30)     NOT NULL, " + " Email_address 	VARCHAR(50)     NOT NULL, "
					+ "	Phone_number 	CHAR(11) 	    NOT NULL, " + "	Name 			VARCHAR(30)  	NOT NULL, "
					+ "	PRIMARY KEY (AdminID) )";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table ADMIN was successfully created.");
			conn.commit();

			// CREATE TEACHER TABLE
			sql = "CREATE TABLE TEACHER ( " + "TeacherID       VARCHAR(30) 		NOT NULL, "
					+ "TeacherNumber   VARCHAR(10)      NOT NULL, " + "PW              VARCHAR(30)      NOT NULL, "
					+ "Email_address   VARCHAR(50)      NOT NULL, " + "Phone_number    CHAR(11) 	    NOT NULL, "
					+ "Name 		   VARCHAR(30)  	NOT NULL, " + "Address         VARCHAR(100), "
					+ "Major           VARCHAR(30), " + "Gender          CHAR             NOT NULL, "
					+ "Education       VARCHAR(15), " + "PRIMARY KEY (TeacherNumber), " + "UNIQUE (TeacherID) )";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table TEACHER was successfully created.");
			conn.commit();

			// CREATE STUDENT TABLE
			sql = "CREATE TABLE STUDENT ( " + "StudentID        VARCHAR(30) 	NOT NULL, "
					+ "StudentNumber    VARCHAR(10)     NOT NULL, " + "PW               VARCHAR(30)     NOT NULL, "
					+ "Email_address 	VARCHAR(50)     NOT NULL, " + "Phone_number 	CHAR(11) 	    NOT NULL, "
					+ "Name 			VARCHAR(30)  	NOT NULL, " + "PRIMARY KEY (StudentNumber), "
					+ "UNIQUE (StudentID) )";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table STUDENT was successfully created.");
			conn.commit();

			// CREATE POST TABLE
			sql = "CREATE TABLE POST ( " + "PostNumber      INT             NOT NULL, "
					+ "PostTitle       VARCHAR(50)     NOT NULL, " + "PostText        VARCHAR(300)    NOT NULL, "
					+ "Writer          VARCHAR(30)     NOT NULL, " + "Subject         VARCHAR(15)     NOT NULL, "
					+ "TeacherNo       VARCHAR(10)     NOT NULL, " + "PRIMARY KEY (PostNumber), "
					+ "FOREIGN KEY (Writer) REFERENCES TEACHER(TeacherID) ON DELETE CASCADE, "
					+ "FOREIGN KEY (TeacherNo) REFERENCES TEACHER(TeacherNumber) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table POST was successfully created.");
			conn.commit();

			// CREATE MATCHING TABLE
			sql = "CREATE TABLE MATCHING ( " + "MatchingNumber  VARCHAR(10)     NOT NULL, "
					+ "TeacherNumber   VARCHAR(10)     NOT NULL, " + "PostNumber      INT             NOT NULL, "
					+ "PRIMARY KEY (MatchingNumber), "
					+ "FOREIGN KEY (TeacherNumber) REFERENCES TEACHER(TeacherNumber) ON DELETE CASCADE, "
					+ "FOREIGN KEY (PostNumber) REFERENCES POST(PostNumber) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table MATCHING was successfully created.");
			conn.commit();

			// CREATE COURSE TABLE
			sql = "CREATE TABLE COURSE ( " + "CourseNumber    VARCHAR(10)     NOT NULL, "
					+ "Subject         VARCHAR(15)     NOT NULL, " + "CourseName      VARCHAR(20)     NOT NULL, "
					+ "TeacherNo       VARCHAR(10)     NOT NULL, " + "MatchNO         VARCHAR(10)     NOT NULL, "
					+ "PRIMARY KEY (CourseNumber), "
					+ "FOREIGN KEY (MatchNO) REFERENCES MATCHING(MatchingNumber) ON DELETE CASCADE, "
					+ "FOREIGN KEY (TeacherNo) REFERENCES TEACHER(TeacherNumber) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table COURSE was successfully created.");
			conn.commit();

			// CREATE CLASSROOM TABLE
			sql = "CREATE TABLE CLASSROOM ( " + "ClassroomID     VARCHAR(10)     NOT NULL, "
					+ "CourseNo        VARCHAR(10)     NOT NULL, " + "Syllabus        VARCHAR(100), "
					+ "PRIMARY KEY (ClassroomID, CourseNo), "
					+ "FOREIGN KEY (CourseNo) REFERENCES COURSE(CourseNumber) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table CLASSROOM was successfully created.");
			conn.commit();

			// CREATE TEACHER_MANAGEMENT TABLE
			sql = "CREATE TABLE TEACHER_MANAGEMENT (\r\n" + "TeacherNumber   VARCHAR(10)     NOT NULL, "
					+ "AdminID 		   VARCHAR(30) 	   NOT NULL, " + "PRIMARY KEY (TeacherNumber, AdminID), "
					+ "FOREIGN KEY (TeacherNumber) REFERENCES TEACHER(TeacherNumber) ON DELETE CASCADE, "
					+ "FOREIGN KEY (AdminID) REFERENCES ADMIN(AdminID) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table TEACHER_MANAGEMENT was successfully created.");
			conn.commit();

			// CREATE POST_MANAGEMENT TABLE
			sql = "CREATE TABLE POST_MANAGEMENT ( " + "PostNumber      INT          NOT NULL, "
					+ "AdminID 		VARCHAR(30) 	NOT NULL, " + "PRIMARY KEY (PostNumber, AdminID), "
					+ "FOREIGN KEY (PostNumber) REFERENCES POST(PostNumber) ON DELETE CASCADE, "
					+ "FOREIGN KEY (AdminID) REFERENCES ADMIN(AdminID) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table POST_MANAGEMENT was successfully created.");
			conn.commit();

			// CREATE READING_POST TABLE
			sql = "CREATE TABLE READING_POST ( " + "PostNumber      INT             NOT NULL, "
					+ "StudentNumber   VARCHAR(10)     NOT NULL, " + "PRIMARY KEY (PostNumber, StudentNumber), "
					+ "FOREIGN KEY (PostNumber) REFERENCES POST(PostNumber) ON DELETE CASCADE, "
					+ "FOREIGN KEY (StudentNumber) REFERENCES STUDENT(StudentNumber) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table READING_POST was successfully created.");
			conn.commit();

			// CREATE CONTAINING_STUDENT_INFO TABLE
			sql = "CREATE TABLE CONTAINING_STUDENT_INFO ( " + "StudentNumber   VARCHAR(10)     NOT NULL, "
					+ "MatchingNumber  VARCHAR(10)     NOT NULL, " + "PRIMARY KEY (StudentNumber, MatchingNumber), "
					+ "FOREIGN KEY (StudentNumber) REFERENCES STUDENT(StudentNumber) ON DELETE CASCADE, "
					+ "FOREIGN KEY (MatchingNumber) REFERENCES MATCHING(MatchingNumber) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table CONTAINING_STUDENT_INFO was successfully created.");
			conn.commit();

			// CREATE TAKING COURSE TABLE
			sql = "CREATE TABLE TAKING_COURSE ( " + "StudentNumber   VARCHAR(10)     NOT NULL, "
					+ "CourseNumber    VARCHAR(10)     NOT NULL, " + "PRIMARY KEY (StudentNumber, CourseNumber), "
					+ "FOREIGN KEY (StudentNumber) REFERENCES STUDENT(StudentNumber) ON DELETE CASCADE, "
					+ "FOREIGN KEY (CourseNumber) REFERENCES COURSE(CourseNumber) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table TAKING_COURSE was successfully created.");
			conn.commit();

			// CREATE COURSE_ASSIGNMENT TABLE
			sql = "CREATE TABLE COURSE_ASSIGNMENT ( " + "Description     VARCHAR(100)    NOT NULL, "
					+ "Submission      VARCHAR(100), " + "CID             VARCHAR(10)     NOT NULL, "
					+ "CourseNo        VARCHAR(10)     NOT NULL, " + "PRIMARY KEY (CID, CourseNo), "
					+ "FOREIGN KEY (CID, CourseNo) REFERENCES CLASSROOM(ClassroomID, CourseNo) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table COURSE_ASSIGNMENT was successfully created.");
			conn.commit();

			// CREATE COURSE_MATERIAL TABLE
			sql = "CREATE TABLE COURSE_MATERIAL ( " + "Material        VARCHAR(100), "
					+ "CID             VARCHAR(10)     NOT NULL, " + "CourseNo        VARCHAR(10)     NOT NULL, "
					+ "PRIMARY KEY (CID, CourseNo), "
					+ "FOREIGN KEY (CID, CourseNo) REFERENCES CLASSROOM(ClassroomID, CourseNo) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table COURSE_MATERIAL was successfully created.");
			conn.commit();

			// CREATE TABLE QA
			sql = "CREATE TABLE QA ( " + "Question        VARCHAR(100)    NOT NULL, " + "Answer          VARCHAR(100), "
					+ "CID             VARCHAR(10)     NOT NULL, " + "CourseNo        VARCHAR(10)     NOT NULL, "
					+ "PRIMARY KEY (CID, CourseNo), "
					+ "FOREIGN KEY (CID, CourseNo) REFERENCES CLASSROOM(ClassroomID, CourseNo) ON DELETE CASCADE)";
			res = stmt.executeUpdate(sql);
			if (res == 0)
				System.out.println("Table COURSE_MATERIAL was successfully created.");
			conn.commit();
		} catch (SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}

	public static void insert_init_table(Connection conn, Statement stmt) {
		/***************************************
		 * 10s : teacher 11s : student 12s : matching 13s : course 14s : classroom
		 * 
		 * ADMIN, TEACHER, STUDENT NAME CHANGED EX) admin12 -> admin_name_12 teacher12
		 * -> teacher_name_12 student12 -> student_name_12
		 ****************************************/
		String sql = "";
		try {
			// INSERT ADMIN TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO ADMIN VALUES ('ADMIN" + i + "', 'comp322', 'admin" + i + "@gmail.com', '"
						+ (01012340000 + i) + "', 'admin_name_" + i + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert ADMIN complete.");

			// INSERT TEACHER TABLE
			for (int i = 0; i < 100; i++) {
				String major;
				String address;
				String gender;
				String education;

				if (i % 5 == 0) {
					major = "Business";
					address = "Seoul";
					gender = "M";
					education = "korean";
				} else if (i % 5 == 1) {
					major = "Mathematics";
					address = "Busan";
					gender = "F";
					education = "math";
				} else if (i % 5 == 2) {
					major = "Biology";
					address = "Daegu";
					gender = "M";
					education = "science";
				} else if (i % 5 == 3) {
					major = "Computer Science";
					address = "Jeju";
					gender = "F";
					education = "math";
				} else {
					major = "Department of Law";
					address = "Seoul";
					gender = "M";
					education = "korean";
				}

				sql = "INSERT INTO TEACHER VALUES ('TEACHER" + i + "', '" + (1000000000 + i) + "', 'comp322', 'teacher"
						+ i + "@gmail.com', '010" + (11112234 + i) + "', 'teacher_name_" + i + "', '" + address + "', '"
						+ major + "', '" + gender + "', '" + education + "')";
				stmt.addBatch(sql);
			}
			sql = "INSERT INTO TEACHER VALUES ('TEACHER100', '1000000100', 'comp322', 'teacher100@gmail.com', '01011112334', 'teacher_name_100', 'Seoul', 'Department of Law', 'M', 'korean')";
			stmt.addBatch(sql);
			sql = "INSERT INTO TEACHER VALUES ('TEACHER101', '1000000101', 'comp322', 'teacher101@gmail.com', '01011112335', 'teacher101', 'Daegu', 'Mathematics', 'M', 'math')";
			stmt.addBatch(sql);

			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert TEACHER complete.");

			// INSERT STUDENT TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO STUDENT VALUES ('STUDENT" + i + "', '" + (1100000000 + i) + "', 'comp322', 'student"
						+ i + "@gmail.com', '010" + (22222234 + i) + "', 'student_name_" + i + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert STUDENT complete.");

			// INSERT POST TABLE
			for (int i = 0; i < 100; i++) {
				String subject;

				if (i % 5 == 0) {
					subject = "korean";
				} else if (i % 5 == 1) {
					subject = "math";
				} else if (i % 5 == 2) {
					subject = "science";
				} else if (i % 5 == 3) {
					subject = "math";
				} else {
					subject = "korean";
				}

				sql = "INSERT INTO POST VALUES (" + i + ", 'Post" + i + "', 'Test Text" + i + "', 'TEACHER" + i + "', '"
						+ subject + "', '" + (1000000000 + i) + "')";
				stmt.addBatch(sql);
			}
			sql = "INSERT INTO POST VALUES (100, 'Post100', 'Test Text100', 'TEACHER100', 'korean', '1000000100')";
			stmt.addBatch(sql);

			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert POST complete.");

			// INSERT MATCHING TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO MATCHING VALUES ('" + (1200000000 + i) + "', '" + (1000000000 + i) + "', " + i + ")";
				stmt.addBatch(sql);
			}
			sql = "INSERT INTO MATCHING VALUES ('1200000100', '1000000100', 100)";
			stmt.addBatch(sql);

			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert MATCHING complete.");

			// INSERT COURSE TABLE
			for (int i = 0; i < 100; i++) {
				String subject;

				if (i % 5 == 0) {
					subject = "korean";
				} else if (i % 5 == 1) {
					subject = "math";
				} else if (i % 5 == 2) {
					subject = "science";
				} else if (i % 5 == 3) {
					subject = "math";
				} else {
					subject = "korean";
				}

				sql = "INSERT INTO COURSE VALUES ('" + (1300000000 + i) + "', '" + subject + "', 'course" + i + "', '"
						+ (1000000000 + i) + "', '" + (1200000000 + i) + "')";
				stmt.addBatch(sql);
			}
			sql = "INSERT INTO COURSE VALUES ('1300000100', 'korean', 'course100', '1000000100', '1200000100')";
			stmt.addBatch(sql);

			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert COURSE complete.");

			// INSERT CLASSROOM TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO CLASSROOM VALUES ('" + (1400000000 + i) + "', '" + (1300000000 + i)
						+ "', 'Test Syllabus " + i + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert CLASSROOM complete.");

			// INSERT TEACHER_MANAGEMENT TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO TEACHER_MANAGEMENT VALUES ('" + (1000000000 + i) + "', 'ADMIN" + (i / 10) + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert TEACHER_MANAGEMENT complete.");

			// INSERT POST_MANAGEMENT TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO POST_MANAGEMENT VALUES (" + i + ", 'ADMIN" + (10 + i / 10) + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert POST_MANAGEMENT complete.");

			// INSERT READING_POST TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO READING_POST VALUES (" + i + ", '" + (1100000000 + i / 10) + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert READING_POST complete.");

			// INSERT INTO CONTAINING_STUDENT_INFO TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO CONTAINING_STUDENT_INFO VALUES ('" + (1100000000 + i) + "', '" + (1200000000 + i)
						+ "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert CONTAINING_STUDENT_INFO complete.");

			// INSERT INTO TAKING_COURSE TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO TAKING_COURSE VALUES ('" + (1100000000 + i) + "', '" + (1300000000 + i) + "')";
				stmt.addBatch(sql);
			}
			sql = "INSERT INTO TAKING_COURSE VALUES ('1100000013', '1300000041')";
			stmt.addBatch(sql);
			sql = "INSERT INTO TAKING_COURSE VALUES ('1100000083', '1300000041')";
			stmt.addBatch(sql);

			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert TAKING_COURSE complete.");

			// INSERT INTO COURSE_ASSIGNMENT TABLE
			for (int i = 0; i < 100; i++) {
				String submit;
				if (i % 2 == 0)
					submit = "submission test " + i;
				else
					submit = "";

				sql = "INSERT INTO COURSE_ASSIGNMENT VALUES ('Description " + i + "', '" + submit + "', '"
						+ (1400000000 + i) + "', '" + (1300000000 + i) + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert COURSE_ASSIGNMENT complete.");

			// INSERT INTO COURSE_MATERIAL TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO COURSE_MATERIAL VALUES ('Material " + i + "', '" + (1400000000 + i) + "', '"
						+ (1300000000 + i) + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert COURSE_MATERIAL complete.");

			// INSERT INTO QA TABLE
			for (int i = 0; i < 100; i++) {
				sql = "INSERT INTO QA VALUES ('Question " + i + "', 'Answer " + i + "', '" + (1400000000 + i) + "', '"
						+ (1300000000 + i) + "')";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			System.out.println("Insert QA complete.");
		} catch (SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}
}
