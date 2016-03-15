package ecmpath;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
  
/**
 */
public class EditorDropTarget implements DropTargetListener {   
  public EditorDropTarget(JEditorPane pane) {   
    this.pane = pane;   
  
    dropTarget = new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE,   
        this, true, null);   
  }   
  
  public void dragEnter(DropTargetDragEvent dtde) {   
    DnDUtils.debugPrintln("dragEnter, drop action = "  
        + DnDUtils.showActions(dtde.getDropAction()));   
    
    checkTransferType(dtde);   
     
    acceptOrRejectDrag(dtde);   
  }   
  
  public void dragExit(DropTargetEvent dte) {   
    DnDUtils.debugPrintln("DropTarget dragExit");   
  }   
  
  public void dragOver(DropTargetDragEvent dtde) {   
    DnDUtils.debugPrintln("DropTarget dragOver, drop action = "  
        + DnDUtils.showActions(dtde.getDropAction()));   
    
    acceptOrRejectDrag(dtde);   
  }   
  
  public void dropActionChanged(DropTargetDragEvent dtde) {   
    DnDUtils.debugPrintln("DropTarget dropActionChanged, drop action = "  
        + DnDUtils.showActions(dtde.getDropAction()));   
  
    acceptOrRejectDrag(dtde);   
  }   
  
  public void drop(DropTargetDropEvent dtde) {   
    DnDUtils.debugPrintln("DropTarget drop, drop action = "  
        + DnDUtils.showActions(dtde.getDropAction()));   
  
    if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {   
      dtde.acceptDrop(dtde.getDropAction());   
      Transferable transferable = dtde.getTransferable();   
  
      try {   
        boolean result = dropFile(transferable);   
  
        dtde.dropComplete(result);   
        DnDUtils.debugPrintln("Drop completed, success: " + result);   
      } catch (Exception e) {   
        DnDUtils.debugPrintln("Exception while handling drop " + e);   
        dtde.dropComplete(false);   
      }   
    } else {   
      DnDUtils.debugPrintln("Drop target rejected drop");   
      dtde.rejectDrop();   
    }   
  }   
  
  
  protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {   
    int dropAction = dtde.getDropAction();   
    int sourceActions = dtde.getSourceActions();   
    boolean acceptedDrag = false;   
  
    DnDUtils.debugPrintln("\tSource actions are "  
        + DnDUtils.showActions(sourceActions) + ", drop action is "  
        + DnDUtils.showActions(dropAction));   
   
    if (!acceptableType   
        || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {   
      DnDUtils.debugPrintln("Drop target rejecting drag");   
      dtde.rejectDrag();   
    } else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {   
      DnDUtils.debugPrintln("Drop target offering COPY");   
      dtde.acceptDrag(DnDConstants.ACTION_COPY);   
      acceptedDrag = true;   
    } else {    
      DnDUtils.debugPrintln("Drop target accepting drag");   
      dtde.acceptDrag(dropAction);   
      acceptedDrag = true;   
    }   
  
    return acceptedDrag;   
  }   
  
  protected void checkTransferType(DropTargetDragEvent dtde) {     
    acceptableType = dtde   
        .isDataFlavorSupported(DataFlavor.javaFileListFlavor);   
  
    DnDUtils.debugPrintln("File type acceptable - " + acceptableType);   
  }   
  
  protected boolean dropFile(Transferable transferable) throws IOException,   
      UnsupportedFlavorException, MalformedURLException {   
    List fileList = (List) transferable   
        .getTransferData(DataFlavor.javaFileListFlavor);   
    File transferFile = (File) fileList.get(0);   
    final URL transferURL = transferFile.toURL();   
    DnDUtils.debugPrintln("File URL is " + transferURL);   
  
    pane.setPage(transferURL);   
  
    return true;   
  }   
  
  public static void main(String[] args) {   
    try {   
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");   
    } catch (Exception evt) {}   
     
    final JFrame f = new JFrame("JEditor Pane Drop Target Example 1");   
  
    JEditorPane pane = new JEditorPane();   
    EditorDropTarget target = new EditorDropTarget(pane);   
    f.addWindowListener(new WindowAdapter() {   
      public void windowClosing(WindowEvent evt) {   
        System.exit(0);   
      }   
    });   
  
    f.getContentPane().add(new JScrollPane(pane), BorderLayout.CENTER);   
    f.setSize(500, 400);   
    f.setVisible(true);   
  }   
  
  protected JEditorPane pane;   
  
  protected DropTarget dropTarget;   
  
  protected boolean acceptableType; // Indicates whether data is acceptable   
}   
  
class DnDUtils {   
  public static String showActions(int action) {   
    String actions = "";   
    if ((action & (DnDConstants.ACTION_LINK | DnDConstants.ACTION_COPY_OR_MOVE)) == 0) {   
      return "None";   
    }   
  
    if ((action & DnDConstants.ACTION_COPY) != 0) {   
      actions += "Copy ";   
    }   
  
    if ((action & DnDConstants.ACTION_MOVE) != 0) {   
      actions += "Move ";   
    }   
  
    if ((action & DnDConstants.ACTION_LINK) != 0) {   
      actions += "Link";   
    }   
  
    return actions;   
  }   
  
  public static boolean isDebugEnabled() {   
    return debugEnabled;   
  }   
  
  public static void debugPrintln(String s) {   
    if (debugEnabled) {   
      System.out.println(s);   
    }   
  }   
  
  private static boolean debugEnabled = (System.getProperty("DnDExamples.debug") != null);   
} 
