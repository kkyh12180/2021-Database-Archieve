package insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class classroom {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("classroom.txt");
		String str = new String();
		for (int i = 0; i < 100; i++)
		{
			String subject;
			
			if (i % 5 == 0) {
				subject = "korean";
			}
			else if (i % 5 == 1) {
				subject = "math";
			}
			else if (i % 5 == 2) {
				subject = "science";
			}
			else if (i % 5 == 3) {
				subject = "math";
			}
			else {
				subject = "korean";
			}
			
			str += "INSERT INTO CLASSROOM VALUES ('" + (1400000000 + i) 
					+ "', '" + (1300000000 + i) + "', 'Test Syllabus " + i + "');\n";
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
