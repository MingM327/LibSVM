import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;



public class test {
	public static void main(String args[]){

		File file=new File("C:\\Users\\lenovo\\Desktop\\data\\adultTrain.txt");
	     InputStream in = null;
	     try {
	         System.out.println("以字节为单位读取文件内容，一次读一个字节：");
	         // 一次读一个字节
	         in = new FileInputStream(file);
	         int tempbyte;
	         while ((tempbyte = in.read()) != -1) {
	             System.out.write(tempbyte);
	         }
	         in.close();
	     } catch (IOException e) {
	         e.printStackTrace();
	         return;
	     }
	}

}
