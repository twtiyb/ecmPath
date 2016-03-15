package bdpanParser;

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
public class BdpanParser extends javax.swing.JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
	private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JButton jButtonGetPath;
    
    public BdpanParser() {
        initComponents();
    }
    
    private void initComponents() {
    	jScrollPane1 = new javax.swing.JScrollPane();
    	jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new JTextArea();
        jTextArea2 = new JTextArea();
        
        jButtonGetPath = new JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(20);
        jScrollPane1.setViewportView(jTextArea1);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(20);
        jScrollPane2.setViewportView(jTextArea2);

        jButtonGetPath.setText("获取下载地址");
        jButtonGetPath.addActionListener(new
        		jButtonGetPath_actionAdapter(this));
        
        Panel panel = new Panel();
        panel.add(jButtonGetPath);
        
        getContentPane().add(panel,BorderLayout.NORTH);
        getContentPane().add(jScrollPane1, BorderLayout.LINE_START);
        getContentPane().add(jScrollPane2, BorderLayout.LINE_END);

        setTitle("百度网盘地址解析器");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pack();
    } 
    
    
    class jButtonGetPath_actionAdapter implements ActionListener {
    	private BdpanParser jFrame;
    	jButtonGetPath_actionAdapter(BdpanParser jfram) {
    		this.jFrame = jfram;
        }

        public void actionPerformed(ActionEvent e) {
        	for (String str  : jFrame.jTextArea1.getText().split("\n")) {
        		if ("".equals(str.trim()))  continue;
        		
        	}
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BdpanParser().setVisible(true);
            }
        });
    }
    
}
