package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class FileUtil {
	
	public static String CHAR_SET = "UTF-8";
	
	 /**
     * 递归删除路径下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            // 递归删除目录中的子目录下
        	for (File file : dir.listFiles()) {
        		if (!deleteDir(file)) {
        			return false;
        		}
        	}
        }
        // 目录此时为空，可以删除
        if (dir.exists()) {
            return dir.delete();
        } else {
        	return true ;
        }
    }
    
    /**
     * 复制单个文件 
     * 如果目录不存在，则创建
     * @param oldPath
     * @param newPath
     */
    public static void copyFile(String oldPath, String newPath) { 
        try { 
            int bytesum = 0; 
            int byteread = 0; 
            File oldfile = new File(oldPath); 
            if (oldfile.exists()) { //文件存在时 
                InputStream inStream = new FileInputStream(oldPath); //读入原文件 
                File f = new File(newPath);
                if(!f.exists() && new File(f.getParent()).mkdirs()){
                	
                }
                FileOutputStream fs = new FileOutputStream(newPath); 
                byte[] buffer = new byte[1444]; 
                while ( (byteread = inStream.read(buffer)) != -1) { 
                    bytesum += byteread; //字节数 文件大小 
                    fs.write(buffer, 0, byteread); 
                } 
                inStream.close();
                fs.close();
            } 
        } 
        catch (Exception e) { 
            System.out.println("复制单个文件操作出错"); 
            e.printStackTrace(); 

        } 
    } 
    
    /**
     * 迭代获取文件路径
     * @param file
     * @param escape
     * @return
     */
    public static String getFilePath(File file,String escape) {
    	if (file.isDirectory()) {
    		String path = "";
    		if (!file.getName().equals(escape)){
	    		for(File f : file.listFiles()) {
	    			path += getFilePath(f,escape) + "\n";
	    		}
    		}
    		return path;
    	} else {
    		return file.getAbsolutePath();
    	}
    }
    
    
    /**
     * 读取 文件的 str
     * @param file
     * @return
     */
    public static String getFileContent(InputStream ins) {
    	if (ins == null) return ""; 
    	StringBuilder sb = new StringBuilder();   
    	try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins,"utf-8"));   
			String line = null;   
			while ((line = reader.readLine()) != null) {   
		        sb.append(line).append("\n");   
			    }
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
    	return sb.toString();
    }
    
    /**
     * 读取 文件的 str
     * @param file
     * @return
     */
    public static String getFileContent(File file) {
    	if (file == null) return "";
    	StringBuilder sb = new StringBuilder();
    	try {
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);   
			String line = null;   
			while ((line = reader.readLine()) != null) {   
			        sb.append(line + "\n");   
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
    	return sb.toString();
    }
    
    /**
     * 保存文件  
     * @param file
     * @return
     */
    public static void saveFile(String content ,String filePath,boolean append) throws Exception {
    	File file = new File(filePath);
    	if (file.isDirectory())     			throw new Exception("filePath is directory!");
    	if (file == null && file.createNewFile()) ;
		FileOutputStream fos = new FileOutputStream(file,append);
		fos.write(content.getBytes());
		fos.close();
    }
}
