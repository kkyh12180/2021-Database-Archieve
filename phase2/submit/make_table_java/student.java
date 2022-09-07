package insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class student {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("student.txt");
		String str = new String();
		for (int i = 0; i < 100; i++)
		{
			String major;
			String address;
			String gender;
			String education;
			
			if (i % 5 == 0) {
				major = "Business";
				address = "Seoul";
				gender = "M";
				education = "korean";
			}
			else if (i % 5 == 1) {
				major = "Mathematics";
				address = "Busan";
				gender = "F";
				education = "math";
			}
			else if (i % 5 == 2) {
				major = "Biology";
				address = "Daegu";
				gender = "M";
				education = "science";
			}
			else if (i % 5 == 3) {
				major = "Computer Science";
				address = "Jeju";
				gender = "F";
				education = "math";
			}
			else {
				major = "Department of Law";
				address = "Seoul";
				gender = "M";
				education = "korean";
			}
			
			str += "INSERT INTO STUDENT VALUES ('STUDENT" + i 
					+ "', '" + (1100000000 + i) + "', 'comp322', 'student" + i + "@gmail.com', '010" + (22222234 + i) + "', 'student" + i + "');\n";
		}
		
		System.out.println(str);
		byte[] bytes = str.getBytes();
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(bytes);
			outputStream.close();
		    } catch (IOException e) {
			    e.printStackTrace();
		    }
	}

}
