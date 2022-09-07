package insert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class matching {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("matching.txt");
		String str = new String();
		for (int i = 0; i < 100; i++)
		{
			str += "INSERT INTO MATCHING VALUES ('" + (1200000000 + i) 
					+ "', '" + (1000000000 + i) + "', " + i + ");\n";
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
