package generator;

import java.io.File;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import util.FileUtil;

public class test {
	public static void main(String[] args) {
		System.out.println("1:" + Thread.currentThread().getContextClassLoader().getResource(""));  
		System.out.println("2:" + test.class.getClassLoader().getResource(""));  
		System.out.println("3:" + ClassLoader.getSystemResource(""));  
		System.out.println("4:" + test.class.getResource("").getPath());//this.class文件所在路径  
		System.out.println("5:" + test.class.getResource("/").getPath()); // Class包所在路径，得到的是URL对象，用url.getPath()获取绝对路径String  
		System.out.println("6:" + new File("/").getAbsolutePath());  
		System.out.println("7:" + System.getProperty("user.dir"));  
		System.out.println("8:" + System.getProperty("file.encoding"));//获取文件编码
		
		System.out.println(FileUtil.getFileContent(new File(test.class.getResource("").getPath()+"aa.txt")));//获取文件编码
	}
}
