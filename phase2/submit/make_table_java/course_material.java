package insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class course_material {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("course_material.txt");
		String str = new String();
		
		for (int i = 0; i < 100; i++)
		{
			str += "INSERT INTO COURSE_MATERIAL VALUES ('Material " + i + "', '" + (1400000000 + i) + "', '" + (1300000000 + i) + "');\n";
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
