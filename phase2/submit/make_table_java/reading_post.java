package insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class reading_post {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("read_post.txt");
		String str = new String();
		for (int i = 0; i < 100; i++)
		{
			str += "INSERT INTO READING_POST VALUES (" + i 
					+ ", '" + (1100000000 + i / 10) + "');\n";
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
