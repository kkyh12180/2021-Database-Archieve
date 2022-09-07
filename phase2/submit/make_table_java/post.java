package insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class post {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("post.txt");
		String str = new String();
		for (int i = 0; i < 100; i++)
		{
			int PostNumber = i;
			String PostTitle;
			String PostText;
			String Writer;
			String subject;
			String TeacherNo;
			
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
			
			str += "INSERT INTO POST VALUES (" + i 
					+ ", 'Post" + i + "', 'Test Text" + i + "', 'TEACHER" + i + "', '" + subject + "', '" + (1000000000 + i) + "');\n";
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
