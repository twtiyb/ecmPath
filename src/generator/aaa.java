/*
 * Created by JFormDesigner on Sun Sep 28 15:06:11 CST 2014
 */

package generator;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import util.FileUtil;
import util.ReflectUtil;
import util.StringUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * @author 徐纯
 *         <p/>
 *         2014-12-13 下午03:29:00
 */
public class aaa {


    public static void main(String[] args) {
        aaa aaa = new aaa();
        aaa.initComponents();
        aaa.frame1.pack();
        aaa.frame1.setVisible(true);
    }

    public static final int FULL_WEIGHT = 1330;
    public static final int SMART_WEIGHT = 520;
    public static final int HEIGHT = 1000;


    private int window_size = 1;
    private Configuration config;

    public void xmlToValue() throws IOException, XMLParserException {
        url.setText(config.getContexts().get(0)
                .getJdbcConnectionConfiguration().getConnectionURL());
        mode.setText(config.getContexts().get(0)
                .getJavaModelGeneratorConfiguration().getTargetPackage());
        mapperJava.setText(config.getContexts().get(0)
                .getJavaClientGeneratorConfiguration().getTargetPackage());
        mapperXml.setText(config.getContexts().get(0)
                .getSqlMapGeneratorConfiguration().getTargetPackage());
        if (config.getContexts().get(0).getJdbcConnectionConfiguration().getDriverClass().toLowerCase().contains("oracle")) {
            oracle.setSelected(true);
        } else {
            mysql.setSelected(true);
        }

        Context context = config.getContexts().get(0);
        String tables = "";
        for (TableConfiguration config : context.getTableConfigurations()) {
            checkBox1.setSelected((Boolean) ReflectUtil.getFieldValue(config, "insertStatementEnabled"));
            checkBox2.setSelected((Boolean) ReflectUtil.getFieldValue(config, "deleteByPrimaryKeyStatementEnabled"));
            checkBox3.setSelected((Boolean) ReflectUtil.getFieldValue(config, "selectByPrimaryKeyStatementEnabled"));
            checkBox4.setSelected((Boolean) ReflectUtil.getFieldValue(config, "updateByPrimaryKeyStatementEnabled"));
            tables += config.getTableName() + "\n";
        }
        textArea2.setText(tables);
    }

    public void valueToXml() throws IOException, XMLParserException {
        config.getContexts().get(0).getJdbcConnectionConfiguration()
                .setConnectionURL(url.getText());
        config.getContexts().get(0).getJavaModelGeneratorConfiguration()
                .setTargetPackage(mode.getText());
        config.getContexts().get(0).getJavaClientGeneratorConfiguration()
                .setTargetPackage(mapperJava.getText());
        config.getContexts().get(0).getSqlMapGeneratorConfiguration()
                .setTargetPackage(mapperXml.getText());
        if (oracle.isSelected()) {
            config.getContexts().get(0).getJdbcConnectionConfiguration().setDriverClass("oracle.jdbc.driver.OracleDriver");
        } else {
            config.getContexts().get(0).getJdbcConnectionConfiguration().setDriverClass("mysql");
        }
        // if (StringUtil.isNotEmpty(plug.getText())) {
        // PluginConfiguration pluginConfiguration = new PluginConfiguration();
        // pluginConfiguration.setConfigurationType(plug.getText());
        // if (config.getContexts().get(0).getPlugins() == null ) {
        // config.getContexts().get(0).addPluginConfiguration(pluginConfiguration);
        // }
        // }


        Context context = config.getContexts().get(0);
        if (StringUtil.isNotEmpty(textArea2.getText())) {
            String[] tables = textArea2.getText().split("\n");
            ReflectUtil.setFieldValue(context, "tableConfigurations", new ArrayList<TableConfiguration>());
            if (tables != null && tables.length > 0) {
                for (String tableName : tables) {
                    TableConfiguration config = new TableConfiguration(context);
                    config.setTableName(tableName);
                    ReflectUtil.setFieldValue(config, "selectByExampleStatementEnabled", false);
                    ReflectUtil.setFieldValue(config, "deleteByExampleStatementEnabled", false);
                    ReflectUtil.setFieldValue(config, "countByExampleStatementEnabled", false);
                    ReflectUtil.setFieldValue(config, "updateByExampleStatementEnabled", false);
                    ReflectUtil.setFieldValue(config, "selectByExampleQueryId", "false");
                    context.addTableConfiguration(config);
                }
            }
        }
        // 方法名称
        for (TableConfiguration config : context.getTableConfigurations()) {
            ReflectUtil.setFieldValue(config, "insertStatementEnabled", checkBox1.isSelected());
            ReflectUtil.setFieldValue(config, "deleteByPrimaryKeyStatementEnabled", checkBox2.isSelected());
            ReflectUtil.setFieldValue(config, "selectByPrimaryKeyStatementEnabled", checkBox3.isSelected());
            ReflectUtil.setFieldValue(config, "updateByPrimaryKeyStatementEnabled", checkBox4.isSelected());
        }
        System.out.println(checkBox1.isSelected());
        System.out.println(checkBox2.isSelected());
        System.out.println(checkBox3.isSelected());
        System.out.println(checkBox4.isSelected());
    }

    private void button1ActionPerformed(ActionEvent e) {
        String result = "";
        java.util.List<String> mes = new ArrayList<String>();
        try {
            ConfigurationParser cp = new ConfigurationParser(null);
            config = cp.parseConfiguration(new File((String) list2.getSelectedValue()));
            mes = Main.run(config);
        } catch (Exception ex) {
            result = "解析配置错误!";
            ex.printStackTrace();
        }

        if (mes != null || mes.size() > 0) {
            for (String str : mes) {
                result += str + "\n";
            }
            JOptionPane.showConfirmDialog(null, result, "错误", 2);
        } else {
            JOptionPane.showConfirmDialog(null, result, "成功", 2);
        }

    }

    private void viewXml() {
        if (this.window_size == 1) {
            frame1.setSize(this.SMART_WEIGHT, frame1.getHeight());
            this.window_size = 2;
        } else {
            frame1.setSize(this.FULL_WEIGHT, frame1.getHeight());
            this.window_size = 1;
        }
    }

    private void textChanged() {
        try {
            valueToXml();
            xmlConent.setText(config.toDocument().getFormattedContent());
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (XMLParserException e1) {
            e1.printStackTrace();
        }
    }

    private void textField1KeyReleased() {
        textChanged();
    }

    private void list2ValueChanged() {
        try {
            ConfigurationParser cp = new ConfigurationParser(null);
            this.config = cp.parseConfiguration(new File((String) list2.getSelectedValue()));
            xmlConent.setText(config.toDocument().getFormattedContent());
            xmlToValue();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XMLParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saveXmlFile() {
        try {
            ConfigurationParser cp = new ConfigurationParser(null);
            InputStream is = new ByteArrayInputStream(xmlConent.getText().getBytes());
            config = cp.parseConfiguration(is);
            FileUtil.saveFile(config.toDocument().getFormattedContent(), (String) list2.getSelectedValue(), false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame1, "配置文件格式错误,无法保存");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY
        // //GEN-BEGIN:initComponents
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        frame1 = new JFrame();
        separator1 = compFactory.createSeparator("\u57fa\u672c\u914d\u7f6e");
        scrollPane2 = new JScrollPane();
        xmlConent = new JTextArea();
        scrollPane6 = new JScrollPane();
        list2 = new JList();
        label1 = compFactory.createLabel("\u6570\u636e\u5e93");
        oracle = new JRadioButton();
        mysql = new JRadioButton();
        label2 = compFactory.createLabel("url");
        url = new JTextField();
        label3 = compFactory.createLabel("entity");
        mode = new JTextField();
        label4 = compFactory.createLabel("mapper.java");
        mapperJava = new JTextField();
        label6 = compFactory.createLabel("mapper.xml");
        mapperXml = new JTextField();
        label5 = compFactory.createLabel("\u65b9\u6cd5");
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        checkBox3 = new JCheckBox();
        checkBox4 = new JCheckBox();
        separator2 = compFactory.createSeparator("\u8868");
        label7 = compFactory.createLabel("\u8868\u540d\u4ee5,\u5206\u9694");
        scrollPane7 = new JScrollPane();
        textArea2 = new JTextArea();
        scrollPane4 = new JScrollPane();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();

        //======== frame1 ========
        {
            frame1.setTitle("mapper生成器");
            frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Container frame1ContentPane = frame1.getContentPane();
            frame1ContentPane.setLayout(new FormLayout(
                    "18dlu, $lcgap, right:62dlu, $lcgap, 80dlu, $lcgap, 103dlu, 19dlu, 373dlu, $lcgap, 75dlu, $lcgap, 18dlu",
                    "6dlu, 9*($lgap, default), $lgap, 41dlu, 3*($lgap, default), $lgap, 17dlu, 2*($lgap, default), $lgap, 16dlu, $lgap, pref, 2*($lgap, default)"));
            frame1ContentPane.add(separator1, CC.xywh(3, 3, 5, 1));

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(xmlConent);
            }
            frame1ContentPane.add(scrollPane2, CC.xywh(9, 3, 1, 33));

            //======== scrollPane6 ========
            {

                //---- list2 ----
                list2.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        list2ValueChanged();
                    }
                });
                scrollPane6.setViewportView(list2);
            }
            frame1ContentPane.add(scrollPane6, CC.xywh(11, 3, 1, 33));
            frame1ContentPane.add(label1, CC.xy(3, 5));

            //---- oracle ----
            oracle.setText("oracle");
            oracle.setSelected(true);
            oracle.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    viewXml();
                }
            });
            frame1ContentPane.add(oracle, CC.xy(5, 5));

            //---- mysql ----
            mysql.setText("mysql");
            mysql.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    viewXml();
                }
            });
            frame1ContentPane.add(mysql, CC.xy(7, 5));
            frame1ContentPane.add(label2, CC.xy(3, 7));

            //---- url ----
            url.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    textField1KeyReleased();
                }
            });
            frame1ContentPane.add(url, CC.xywh(5, 7, 3, 1));
            frame1ContentPane.add(label3, CC.xy(3, 9));

            //---- mode ----
            mode.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    textField1KeyReleased();
                }
            });
            frame1ContentPane.add(mode, CC.xywh(5, 9, 3, 1));
            frame1ContentPane.add(label4, CC.xy(3, 11));

            //---- mapperJava ----
            mapperJava.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    textField1KeyReleased();
                }
            });
            frame1ContentPane.add(mapperJava, CC.xywh(5, 11, 3, 1));
            frame1ContentPane.add(label6, CC.xy(3, 13));

            //---- mapperXml ----
            mapperXml.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    textField1KeyReleased();
                }
            });
            frame1ContentPane.add(mapperXml, CC.xywh(5, 13, 3, 1));
            frame1ContentPane.add(label5, CC.xy(3, 15));

            //---- checkBox1 ----
            checkBox1.setText("insert");
            checkBox1.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    textField1KeyReleased();
                }
            });
            frame1ContentPane.add(checkBox1, CC.xy(5, 15));

            //---- checkBox2 ----
            checkBox2.setText("delete");
            checkBox2.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    textField1KeyReleased();
                }
            });
            frame1ContentPane.add(checkBox2, CC.xy(7, 15));

            //---- checkBox3 ----
            checkBox3.setText("select");
            checkBox3.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    textField1KeyReleased();
                }
            });
            frame1ContentPane.add(checkBox3, CC.xy(5, 17));

            //---- checkBox4 ----
            checkBox4.setText("update");
            checkBox4.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    textField1KeyReleased();
                }
            });
            frame1ContentPane.add(checkBox4, CC.xy(7, 17));
            frame1ContentPane.add(separator2, CC.xywh(3, 19, 5, 1));
            frame1ContentPane.add(label7, CC.xywh(3, 21, 1, 13));

            //======== scrollPane7 ========
            {

                //---- textArea2 ----
                textArea2.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        textField1KeyReleased();
                    }
                });
                scrollPane7.setViewportView(textArea2);
            }
            frame1ContentPane.add(scrollPane7, CC.xywh(5, 21, 3, 13, CC.FILL, CC.FILL));
            frame1ContentPane.add(scrollPane4, CC.xy(9, 27));

            //---- button1 ----
            button1.setText("\u751f\u6210\u6587\u4ef6");
            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    button1ActionPerformed(e);
                }
            });
            frame1ContentPane.add(button1, CC.xy(3, 35));

            //---- button2 ----
            button2.setText("\u663e\u793a\u914d\u7f6e\u6587\u4ef6");
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewXml();
                }
            });
            frame1ContentPane.add(button2, CC.xy(5, 35));

            //---- button3 ----
            button3.setText("\u4fdd\u5b58\u914d\u7f6e\u6587\u4ef6");
            button3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveXmlFile();
                }
            });
            frame1ContentPane.add(button3, CC.xy(7, 35));
            frame1.setSize(1140, 575);
            frame1.setLocationRelativeTo(null);
        }

        //---- dataGroup ----
        ButtonGroup dataGroup = new ButtonGroup();
        dataGroup.add(oracle);
        dataGroup.add(mysql);
        // //GEN-END:initComponents

//		// 初始化 xmlList
        try {
            File file = new File(this.getClass().getResource("/").toURI());
            DefaultListModel listModel = new DefaultListModel();
            for (File xml : file.listFiles()) {
                if (xml.getName().toLowerCase().contains(".xml")) {
                    listModel.addElement(xml.getName());
                }
            }
            if (listModel.size() == 0) {
                config = new Configuration();
                config.addContext(new Context(ModelType.CONDITIONAL));
                try {
                    FileUtil.saveFile(config.toDocument().getFormattedContent(), "config.xml", false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listModel.addElement("config.xml");
            }
            list2.setModel(listModel);
            list2.setSelectedIndex(0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    private JFrame frame1;
    private JComponent separator1;
    private JScrollPane scrollPane2;
    private JTextArea xmlConent;
    private JScrollPane scrollPane6;
    private JList list2;
    private JLabel label1;
    private JRadioButton oracle;
    private JRadioButton mysql;
    private JLabel label2;
    private JTextField url;
    private JLabel label3;
    private JTextField mode;
    private JLabel label4;
    private JTextField mapperJava;
    private JLabel label6;
    private JTextField mapperXml;
    private JLabel label5;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JComponent separator2;
    private JLabel label7;
    private JScrollPane scrollPane7;
    private JTextArea textArea2;
    private JScrollPane scrollPane4;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
