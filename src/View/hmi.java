/*
 * hmi.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package View;

import Controller.SaveAsAction;
import Controller.SaveAction;
import Controller.RunAction;
import Controller.QuitAction;
import Controller.OptionsAction;
import Controller.OpenFileAction;
import Controller.AboutAction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.swing.*;

/**
 *
 * @author smameri
 */
public class hmi extends JFrame
{
    private static displayTable tableWindow;
    public GraphPanel svgImg;
    public CheckBoxesFrame checkBoxesFrame;
    public JSplitPane pan1;
    public JSplitPane pan2;
    public JSplitPane pan3;//Changing the position of each component
    public JSplitPane pan6;
    public JSplitPane pan7;
    public JSplitPane pan8;
    public JSplitPane pan9;
    private JTabbedPane onglets;
    private JPanel pan ;
    private JScrollPane spane1;//The position of the image if it exceeds the window
    private JScrollPane spane2;
    private JScrollPane spane0;
    private JScrollPane spane3;

    public static JDesktopPane desktopPane;

    private JLabel statusBar;
    private JToolBar tb;
    private File myFile;

    private JButton save;
    public static JMenuItem save1;
    
    private JLabel field1Lab;
    private JLabel field2Lab;
    private JLabel tableLab;
    private JLabel graphLab;

    private ContextInterface contextInterface;
    private ISInterface isInterface;
    
    /** Path of the project/jar directory */
    public static String projectPath =  System.getProperty("user.dir")+ "\\.."; //pour la generation du .jar
    //public static String projectPath =  System.getProperty("user.dir"); //Pour la generation sous netbeans
    
    // Used to identify the windows platform.
    private static final String WIN_ID = "Windows";
    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32";
    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    // The default browser under unix.
    private static final String UNIX_PATH = "netscape";
    // The flag to display a url.
    private static final String UNIX_FLAG = "-remote openURL";


    /*
     * Class constructor
     */
    public hmi()
    {
        try {
            //set the look and feel of the application to be the same as the OS
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {}

        //Set the title for the hmi
        this.setTitle("Lattice View");
        //Set the size of the window
        this.setSize(1000, 600);
        //Allow the resize of the window
        this.setResizable(true);
        //Location of the window
        this.setLocationRelativeTo(null);
    
        field1Lab = new JLabel(" Context");
        field2Lab = new JLabel(" IS");
        tableLab = new JLabel();
        graphLab = new JLabel();

        statusBar = new JLabel(" Ready");
        
        //Initialize the 3 elements to be displayed in normal mode
        tableWindow = new displayTable(null);
        svgImg = new GraphPanel();

        desktopPane = new JDesktopPane();
        desktopPane.setBounds(0,0, 1000, 600);
        checkBoxesFrame = new CheckBoxesFrame(this);
        
        desktopPane.add(checkBoxesFrame,new Integer(3));
        desktopPane.setBackground(Color.white);
        desktopPane.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                checkBoxesFrame.setBounds(desktopPane.getWidth()-210, 5, 200, 150);
            }
            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });

        
        
        //initialize the window containers
        contextInterface = new ContextInterface();
        isInterface = new ISInterface();
        spane1 = new JScrollPane(contextInterface,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spane0 = new JScrollPane(isInterface,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spane2 = new JScrollPane(tableWindow.getJTable(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spane3 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        final JPanel p = new JPanel();
        p.add(svgImg);
        p.setBackground(Color.white);
        spane3.setViewportView(p);
        /*
         * Allow the user to zoom using ctrl+mouse wheel
         * the zoom is centered using the position of the event
         */
        p.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(final MouseWheelEvent e) {
                if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
 					if (e.getWheelRotation() > 0) {
 						svgImg.zoom_out();
                                                      p.scrollRectToVisible(new Rectangle(e.getX()-(spane3.getWidth()/2),e.getY()-(spane3.getHeight()/2),spane3.getWidth(),spane3.getHeight()));
 					} else {
                                                svgImg.zoom_in();
                                                      p.scrollRectToVisible(new Rectangle(e.getX()-(spane3.getWidth()/2),e.getY()-(spane3.getHeight()/2),spane3.getWidth(),spane3.getHeight()));
                                        }
 				}
            }
        });
        pan6 = new JSplitPane( JSplitPane.VERTICAL_SPLIT , this.field1Lab ,spane1);
        pan6.setDividerLocation(20);
        pan6.setDividerSize(1);
        pan6.setOneTouchExpandable(false);
        pan7 = new JSplitPane( JSplitPane.VERTICAL_SPLIT , this.field2Lab ,spane0);
        pan7.setDividerLocation(20);
        pan7.setDividerSize(0);
        pan7.setOneTouchExpandable(false);
        pan8 = new JSplitPane( JSplitPane.VERTICAL_SPLIT , this.tableLab ,spane2);
        pan8.setDividerLocation(20);
        pan8.setDividerSize(0);
        pan8.setOneTouchExpandable(false);
        pan3 = new JSplitPane( JSplitPane.VERTICAL_SPLIT , pan7 ,pan8);//pan5 => pan7
        pan2 = new JSplitPane( JSplitPane.VERTICAL_SPLIT , pan6 ,pan3);//pan4 => pan6
        pan3.setOneTouchExpandable(true);
        pan2.setOneTouchExpandable(true);
        pan2.setBackground(Color.white);
        pan2.setDividerLocation(120);
        pan3.setDividerLocation(120);
        pan9 = new JSplitPane( JSplitPane.VERTICAL_SPLIT , this.graphLab ,spane3);
        pan9.setDividerLocation(20);
        pan9.setDividerSize(0);
        pan9.setOneTouchExpandable(false);
        pan1 = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , pan2 , pan9);
        pan1.setDividerLocation(0.5);
        pan1.setOneTouchExpandable(true);
        pan1.setBackground(Color.white);
        pan = new JPanel();
        pan.setLayout(new BorderLayout());
        this.onglets = new JTabbedPane();
        this.onglets.add("normal",pan1);
        this.onglets.add("advanced",desktopPane);
        this.buildMenuBar();
        this.tb = new JToolBar();
        this.buildToolBar();

        pan.add(tb,BorderLayout.NORTH);
        pan.add(this.onglets,BorderLayout.CENTER);
        pan.add(this.statusBar,BorderLayout.SOUTH);
        this.setContentPane(pan);
        desktopPane.setDesktopManager(new SampleDesktopMgr());
        //Action on close operation
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e){
                exit();
            }
        });
         
       this.setVisible(true);
    }

    /*
     * Get functions
     */
    public ContextInterface getContextInterface(){ return this.contextInterface;}

    public ISInterface getISInterface(){ return this.isInterface;}

    public JLabel getTableLab(){return this.tableLab;}

    public JLabel getGraphLab(){return this.graphLab;}

    public displayTable getDisplayTable(){return tableWindow;}

    public GraphPanel getGraphPanel(){return svgImg;}

    public CheckBoxesFrame getCheckBoxFrame(){return this.checkBoxesFrame;}

    public JButton getSaveButton(){return this.save;}

    public void setStatus(String status){this.statusBar.setText(status);}

    /*
     * function to build the menu bar of the main window
     */
    private void buildMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu1 = new JMenu("File");
        JMenuItem openFile = new JMenuItem(new OpenFileAction(this,"Open File"));
        openFile.setAccelerator(KeyStroke.getKeyStroke('N', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        openFile.setIcon(new ImageIcon(projectPath+"\\icons\\open.png"));
        menu1.add(openFile);
        save1 = new JMenuItem(new SaveAction(this,"Save"));
        save1.setAccelerator(KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        save1.setIcon(new ImageIcon(projectPath+"\\icons\\save.png"));
        menu1.add(save1);
        JMenuItem saveAs = new JMenuItem(new SaveAsAction(this,"SaveAs"));
        menu1.add(saveAs);
        menu1.addSeparator();
        JMenuItem quitter = new JMenuItem(new QuitAction("Exit"));
        menu1.add(quitter);

        menuBar.add(menu1);

        JMenu menu3 = new JMenu("Tools");
        JMenuItem Options = new JMenuItem(new OptionsAction(this,"Options"));
        menu3.add(Options);
        menuBar.add(menu3);

        JMenu menu2 = new JMenu("Help");

        JMenuItem about = new JMenuItem(new AboutAction(this, "About"));
        menu2.add(about);
        JMenuItem javadoc = new JMenuItem(new AbstractAction("Javadoc") {

            public void actionPerformed(ActionEvent e) {
                Toast(contextInterface.toString());
                File index = new File(projectPath+"\\javadoc\\index.html");
                displayURL(index.toString());
            }
        });

        menu2.add(javadoc);

        menuBar.add(menu2);
        setJMenuBar(menuBar);
    }

    /*
     * Function that build the ToolBar on the main window
     * flotable is disabled
     */
    private void buildToolBar()
    {
        OpenFileAction openAction = new OpenFileAction(this,"Open File");
        
        tb.setFloatable(false);
        
        JButton open = new JButton(new ImageIcon(projectPath+"\\icons\\open.png"));
        open.addActionListener(openAction);
        
        save = new JButton(new ImageIcon(projectPath+"\\icons\\save.png"));
        save.addActionListener(new SaveAction(this,"Save project"));
        
        DropDownButtonRun exec = new DropDownButtonRun(openAction);
       
        JButton zoom_in = new JButton(new ImageIcon(projectPath+"\\icons\\zoom-in.png"));
        zoom_in.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {svgImg.zoom_in();}});

        JButton zoom_out = new JButton(new ImageIcon(projectPath+"\\icons\\zoom-out.png"));
        zoom_out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {svgImg.zoom_out();}});

        JButton zoom_auto = new JButton(new ImageIcon(projectPath+"\\icons\\open.png"));
        zoom_auto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) { svgImg.zoom_auto(spane3.getWidth()-15, spane3.getHeight()-15);}});
        
        final AddClosure closure = new AddClosure(this);
        JButton btAdd = new JButton(new ImageIcon(projectPath+"\\icons\\plus.png"));
        btAdd.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent ae) {closure.setVisible(true);}});
        
        open.setToolTipText("Open a new File");
        save.setToolTipText("Save into a folder all project files");
        zoom_in.setToolTipText("Zoom in");
        zoom_out.setToolTipText("Zoom out");
        zoom_auto.setToolTipText("Zoom auto");

        tb.add(open);
        tb.add(save);
        tb.addSeparator();
        exec.addToToolBar(tb);
        tb.addSeparator();
        tb.add(zoom_in);
        tb.add(zoom_out);
        tb.add(zoom_auto);
        tb.add(btAdd);
    }

    

    /*
     * Function called when window is closing
     */
    void exit()
    {
        JOptionPane jop = new JOptionPane();
        // language English for this button
        jop.setDefaultLocale(new Locale("en"));
        //Display confirm dialog
        int confirmed = jop.showConfirmDialog(this,
                "Are you sure you want to exit?", "Confirm Exit",
                jop.YES_NO_OPTION);
       
        //Close if user confirmed
        if (confirmed == jop.YES_OPTION)
        {
                //remove all temporary files
            try{
                int index = RunAction.tempFile.lastIndexOf(".");
                String s = RunAction.tempFile.substring(0, index);
                myFile = new File(s+".txt");
                myFile.delete();
                myFile = new File(s+".dot");
                myFile.delete();
                myFile = new File(s+".svg");
                myFile.delete();

                    myFile = new File(s+".xls");
                    myFile.delete();
                    myFile = new File(s+"_to_IS.txt");
                    myFile.delete();

                    myFile = new File(s+"_to_Context.txt");
                    myFile.delete();
                    myFile = new File(s+"_Dependance_Graph.dot");
                    myFile.delete();
                    myFile = new File(s+"_Dependance_Graph.svg");
                    myFile.delete();
                    myFile = new File(s+"_Representative_Graph.dot");
                    myFile.delete();
                    myFile = new File(s+"_Representative_Graph.svg");
                    myFile.delete();
                    myFile = new File(s+"_Reduced_Lattice.dot");
                    myFile.delete();
                    myFile = new File(s+"_Reduced_Lattice.svg");
                    myFile.delete();
                    myFile = new File(s+"_Irreducible_Sub_Graph.dot");
                    myFile.delete();
                    myFile = new File(s+"_Irreducible_Sub_Graph.svg");
                    myFile.delete();
                    myFile = new File(s+"_precedence_Graph.dot");
                    myFile.delete();
                    myFile = new File(s+"_precedence_Graph.svg");
                    myFile.delete();
                    myFile = new File(s+"_lattice_Graph.svg");
                    myFile.delete();
                    myFile = new File(s+"_lattice_Graph.dot");
                    myFile.delete();
                    

                index = OpenFileAction.file_name.lastIndexOf(".");
                s = OpenFileAction.file_name.substring(0, index);
                myFile = new File(s+".dot");
                myFile.delete();
                myFile = new File(s+".svg");
                myFile.delete();
                myFile = new File(s+"_Dependance_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_Dependance_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_Representative_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_Representative_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_precedence_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_precedence_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_lattice_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_lattice_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_Reduced_Lattice.dot");
                myFile.delete();
                myFile = new File(s+"_Reduced_Lattice.svg");
                myFile.delete();
                myFile = new File(s+"_Irreducible_Sub_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_Irreducible_Sub_Graph.svg");
                myFile.delete();
                
                myFile = new File(s+".xls");
                myFile.delete();
                if(OpenFileAction.isIS())
                {
                    myFile = new File(s+"_to_IS.txt");
                    myFile.delete();
                }
                else if(OpenFileAction.isContext())
                {
                    myFile = new File(s+"_to_Context.txt");
                    myFile.delete();
                }
            } catch(Exception e){}

            //Close frame
            System.exit(0);
        }
    }

    public static void displayURL(String url)
    {
        boolean windows = isWindowsPlatform();
        String cmd = null;
        try
        {
            if (windows)
            {
                // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
                cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                Process p = Runtime.getRuntime().exec(cmd);
            }
            else
            {
                // Under Unix, Netscape has to be running for the "-remote"
                // command to work.  So, we try sending the command and
                // check for an exit value.  If the exit command is 0,
                // it worked, otherwise we need to start the browser.
                // cmd = 'netscape -remote openURL(http://www.javaworld.com)'
                cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
                Process p = Runtime.getRuntime().exec(cmd);
                try
                {
                    // wait for exit code -- if it's 0, command worked,
                    // otherwise we need to start the browser up.
                    int exitCode = p.waitFor();
                    if (exitCode != 0)
                    {
                        // Command failed, start up the browser
                        // cmd = 'netscape http://www.javaworld.com'
                        cmd = UNIX_PATH + " "  + url;
                        p = Runtime.getRuntime().exec(cmd);
                    }
                }
                catch(InterruptedException x)
                {
                    System.err.println("Error bringing up browser, cmd='" +
                                       cmd + "'");
                    System.err.println("Caught: " + x);
                }
            }
        }
        catch(IOException x)
        {
            // couldn't exec browser
            System.err.println("Could not invoke browser, command=" + cmd);
            System.err.println("Caught: " + x);
        }
    }
    /**
     * Try to determine whether this application is running under Windows
     * or some other platform by examing the "os.name" property.
     *
     * @return true if this application is running under a Windows OS
     */
    public static boolean isWindowsPlatform()
    {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;

    }

    public static void Toast(String message)
    {
        javax.swing.JOptionPane.showMessageDialog(null, message);
    }
    
    private class DropDownButtonRun extends DropDownButton
    {
        private JPopupMenu menu;
        private RunAction runAction;
        private JCheckBoxMenuItem item;
        public DropDownButtonRun(OpenFileAction open)
        {
                menu = new JPopupMenu();
                ButtonGroup group = new ButtonGroup();
                item = new JCheckBoxMenuItem("Context");
                group.add(item);
                menu.add(item);
                item.setSelected(true);
                
                item = new JCheckBoxMenuItem("IS");
                group.add(item);
                menu.add(item);

                super.setToolTipText("Run the project");
                super.setArrowToolTipText("Choice the closure system");
                super.setIcon(new ImageIcon(projectPath+"\\icons\\exec.png"));
                
                runAction = new RunAction(hmi.this, "Run project", open, false);
        }
        
        @Override
        protected JPopupMenu getPopupMenu() { return menu;}        

        @Override
        protected void actionListener(ActionEvent ae) { runAction.runAction(ae, item.isSelected());}
    }
}
