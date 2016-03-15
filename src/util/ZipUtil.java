package util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * java自带的zip压缩
 * @author 徐纯
 *
 *  2014-9-4 下午02:21:00
 */
public class ZipUtil {
	private static int BUFFER_SIZE = 4096;  
	public static void zip(String zipFileName, File inputFile) throws Exception {  
		if (inputFile == null || !inputFile.exists()) return ;
		
        System.out.println("压缩中...");  
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(  
                zipFileName));  
        zip(out, inputFile, inputFile.getName());  
        out.close(); // 输出流关闭  
        System.out.println("压缩完成");  
    }  
  
    public static void zip(ZipOutputStream out, File f, String base) throws Exception { // 方法重载  
        if (f.isDirectory()) {  
            File[] fl = f.listFiles();  
            if (fl.length == 0) {  
                out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base  
            }  
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + "/" + fl[i].getName()); // 递归遍历子文件夹  
            }  
        } else {  
            out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base  
            FileInputStream in = new FileInputStream(f);  
            byte[] data = new byte[BUFFER_SIZE];  
            int count = -1;  
            while((count = in.read(data,0,BUFFER_SIZE)) != -1) {  
                out.write(data,0, count);// 将字节流写入当前zip目录
            }
            in.close(); // 输入流关闭  
        }
    }
    
    public String unZip(String zipFile,String outFolder) {
    	String result = "";
    	long startTime=System.currentTimeMillis();  
        try {  
            ZipInputStream Zin=new ZipInputStream(new FileInputStream(zipFile));//输入源zip路径  
            BufferedInputStream Bin=new BufferedInputStream(Zin);  
            String Parent = outFolder; //输出路径（文件夹目录）  
            File Fout=null;  
            ZipEntry entry;  
            try {  
                while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){  
                    Fout=new File(Parent,entry.getName());  
                    if(!Fout.exists()){  
                        (new File(Fout.getParent())).mkdirs();  
                    }  
                    FileOutputStream out=new FileOutputStream(Fout);  
                    BufferedOutputStream Bout=new BufferedOutputStream(out);  
                    int b;  
                    while((b=Bin.read())!=-1){  
                        Bout.write(b);  
                    }  
                    Bout.close();  
                    out.close();  
                    result = "解压成功";
                }  
                Bin.close();  
                Zin.close();  
            } catch (IOException e) {  
            	result = "解压失败";
            }  
        } catch (FileNotFoundException e) {  
        	result = "压缩文件未找到";
        }  
        long endTime=System.currentTimeMillis();  
        result += " 耗费时间:"+(endTime-startTime)+" ms";  
        return result ;
    }  
}
