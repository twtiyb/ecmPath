package ecmpath;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.FileUtil;
import util.ZipUtil;


/**
*
* @author xuchun
*/
public class NewJFrame extends javax.swing.JFrame {
    
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton jButtonCreate;
    private javax.swing.JButton jButtonGetPath;
    private javax.swing.JTextField jTextField;
    
    public NewJFrame() {
        initComponents();
    }
    
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        
        jTextArea1 = new MyTextArea();
        jButtonCreate = new JButton();
        jTextField = new JTextField();
        jButtonGetPath = new JButton();
        
        jTextField.setText("示例  开发路径下: G:\\workspace\\ecm\\WebRoot\\      tomcat下: G:\\apache-tomcat-6.0.32\\webapps\\ecm\\");
        
        jTextArea1.setColumns(20);
        jTextArea1.setRows(20);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField.setColumns(60);
        jButtonCreate.setText("打包压缩");
        jButtonCreate.addActionListener(new
        		jButtonCreate_actionAdapter(this));

        jButtonGetPath.setText("获取路径");
        jButtonGetPath.addActionListener(new
        		jButtonGetPath_actionAdapter(this));

        
        Panel panel = new Panel();
        panel.add(jTextField,BorderLayout.WEST);
        panel.add(jButtonGetPath,BorderLayout.CENTER);
        panel.add(jButtonCreate, BorderLayout.EAST);
        
        getContentPane().add(panel,BorderLayout.NORTH);
        getContentPane().add(jScrollPane1, BorderLayout.CENTER);
        setTitle("打包器");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pack();
    } 
    
    
    class jButtonGetPath_actionAdapter implements ActionListener {
    	private NewJFrame jFrame;
    	jButtonGetPath_actionAdapter(NewJFrame jfram) {
    		this.jFrame = jfram;
        }

        public void actionPerformed(ActionEvent e) {
        	if (jFrame.jTextArea1.getText().split("\n").length <= 0 ) {
            	jFrame.jTextField.setText("请拖入文件!");
            	return ;
        	}
        	for (String str  : jFrame.jTextArea1.getText().split("\n")) {
        		if (str.contains("WebRoot")) {
        			jFrame.jTextField.setText(str.substring(0,str.indexOf("WebRoot")+8));
        			return ;
        		}
        		if (str.contains("Web")) {
        			jFrame.jTextField.setText(str.substring(0,str.indexOf("Web")+3));
        			return ;
        		}
        		if (str.contains("web")) {
        			jFrame.jTextField.setText(str.substring(0,str.indexOf("web")+3));
        			return ;
        		}
        	}
        }
    }
    
    class jButtonCreate_actionAdapter implements ActionListener {
    	private NewJFrame jFrame;
    	jButtonCreate_actionAdapter(NewJFrame jfram) {
    		this.jFrame = jfram;
        }

        public void actionPerformed(ActionEvent e) {
        	if (jFrame.jTextField.getText() == null 
        			|| "".equals(jFrame.jTextField.getText().trim())) {
        		jFrame.jTextField.setText("请填写项目名称");
        		return ;
        	}
        	new File("c:\\ecm\\").mkdir();
        	if (!FileUtil.deleteDir(new File("c:\\ecm\\ecm\\")) 
        			|| !FileUtil.deleteDir(new File("c:\\ecm\\ecmpatch.zip"))) {
        		jFrame.jTextField.setText("原来生成的文件正在被占用,不能删除.请退出当前正在使用的生成的ecm文件.");
        		return ;
        	}
    		
        	for (String str  : jFrame.jTextArea1.getText().split("\n")) {
        		if ("".equals(str.trim()))  continue;
        		if (str.contains("成功")) continue;
        		if (!str.contains(jFrame.jTextField.getText())) {
            		jFrame.jTextField.setText("路径错误!");
            		return ;
        		}
        		FileUtil.copyFile(str, str.replace(jFrame.jTextField.getText(),"c:\\ecm\\ecm\\"));
        	}
        	try {
        	    ZipUtil.zip("c:\\ecm\\ecmpatch.zip", new File("c:\\ecm\\ecm"));
        		jFrame.jTextArea1.setText("打包成功!");
    			Runtime.getRuntime().exec("cmd /c " + " explorer c:\\ecm" );
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }
    
    class MyTextArea extends JTextArea implements DropTargetListener{
        public MyTextArea(){
            new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        }
        public void dragEnter(DropTargetDragEvent dtde) {
        }

        public void dragOver(DropTargetDragEvent dtde) {
        }

        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        public void dragExit(DropTargetEvent dte) {
        }

        public void drop(DropTargetDropEvent dtde) {
            try {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List list = (List) (dtde.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor));
                    Iterator iterator = list.iterator();
                    while (iterator.hasNext()) {
                        File f = (File) iterator.next();
                        this.setText(FileUtil.getFilePath(f,".svn")+"\n" + this.getText());
                    }
                    dtde.dropComplete(true);
                    this.updateUI();
                }else {
                    dtde.rejectDrop();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            }
        }
    }
}
