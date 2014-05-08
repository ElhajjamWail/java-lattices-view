/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.GraphViz;
import Controller.SaveAction;
import Controller.OpenFileAction;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import lattice.ConceptLattice;

/**
 *
 * @author JunXcola
 */
public class Options extends JFrame {

    private JLabel graphViz;
    private JTabbedPane onglets;
    private JLabel labeltempDir = new JLabel("Temporary directory path :");
    private static JTextField tempDir;
    private JLabel labelGraphVizPath = new JLabel("GraphViz dot path :");
    private static JTextField GraphVizPath;
    private JButton browse1;
    private JButton browse2;
    private JButton ok;
    private JButton cancel;

    private GridBagLayout gridbag;
    private JPanel pane;

    public static JFrame mainframe;

    public File dot,out;

    private static hmi window;

    /*
     * Path of the project/jar directory
     */
        String projectPath = hmi.projectPath;
        public String tempFile = projectPath+"\\temp\\temp";

    
    
    public Options(hmi window)
    {
        this.window = window;
        mainframe = this;
        //Set the title for the hmi
        this.setTitle("Options");
        //Set the size of the window
        this.setSize(350, 400);
        //Allow the resize of the window
        this.setResizable(false);
        //Location of the window
        this.setLocationRelativeTo(null);
        this.graphViz = new JLabel("GraphViz properties");
        tempDir = new JTextField();
        GraphVizPath = new JTextField();
        this.GraphVizPath.setMaximumSize(this.GraphVizPath.getPreferredSize());
        setDefaultPaths();



        this.browse1 = new JButton("Browse");
        this.browse2 = new JButton("Browse");
        this.ok = new JButton("OK");
        this.cancel = new JButton("Cancel");
        this.ok.setPreferredSize(this.cancel.getPreferredSize());
        this.ok.setMinimumSize(this.cancel.getMinimumSize());
        this.setActions();

        pane = new JPanel();
        gridbag = new GridBagLayout();
        pane.setBorder(BorderFactory.createTitledBorder(this.graphViz.getText()));
        pane.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy=0;
        c.gridwidth = GridBagConstraints.REMAINDER; // seul composant de sa colonne, il est donc le dernier.
        c.gridheight = 1; // valeur par défaut - peut s'étendre sur une seule ligne.
        c.anchor = GridBagConstraints.LINE_START; // ou BASELINE_LEADING mais pas WEST.

        c.insets = new Insets(10, 0, 0, 0);
        pane.add(this.labelGraphVizPath,c);

        c.gridx=0;
        c.gridy=1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weightx = 0.;
        c.weighty = 0.;

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.BASELINE_LEADING; // pas LINE_START ni WEST !!
        c.insets = new Insets(10, 0, 0, 0);
        pane.add(this.GraphVizPath, c);

        c.gridx=2;
        c.gridy=1;
        c.gridwidth = GridBagConstraints.REMAINDER;;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.BASELINE;

        c.insets = new Insets(10, 5, 0, 0);
        pane.add(this.browse2, c);

        c.gridx = 0;
        c.gridy=2;
        c.gridwidth = GridBagConstraints.REMAINDER; // seul composant de sa colonne, il est donc le dernier.
        c.gridheight = 1; // valeur par défaut - peut s'étendre sur une seule ligne.
        c.anchor = GridBagConstraints.LINE_START; // ou BASELINE_LEADING mais pas WEST.

        c.insets = new Insets(20, 0, 0, 0);
        pane.add(this.labeltempDir,c);

        c.gridx=0;
        c.gridy=3;
        c.gridwidth = 2;
        c.gridheight = 1;

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.BASELINE_LEADING; // pas LINE_START ni WEST !!
        c.insets = new Insets(10, 0, 0, 0);
        pane.add(this.tempDir, c);

        c.gridx=2;
        c.gridy=3;
        c.gridwidth = GridBagConstraints.REMAINDER;;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.BASELINE;

        c.insets = new Insets(10, 5, 0, 0);
        pane.add(this.browse1, c);
        
        c.gridx=1;
        c.gridy=4;
        c.gridwidth = 1;
        c.gridheight = 1;

        c.anchor = GridBagConstraints.BASELINE_LEADING; // pas LINE_START ni WEST !!
        c.insets = new Insets(120, 140, 0, 0);
        pane.add(this.ok, c);

        c.gridx=3;
        c.gridy=4;
        c.gridwidth = GridBagConstraints.REMAINDER;;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.BASELINE;

        c.insets = new Insets(120, 5, 5, 0);
        pane.add(this.cancel, c);
      
        this.onglets = new JTabbedPane();
        this.onglets.add("GraphViz",pane);
        this.setContentPane(this.onglets);

    }

    public static JTextField getTempDir()
    {
        return Options.tempDir;
    }

    public static JTextField getGraphViz()
    {
        return Options.GraphVizPath;
    }

    public void setDefaultPaths()
    {
        try
        {
            BufferedReader fichier = new BufferedReader(new FileReader(projectPath+"\\config.txt"));
            // first line : All observations separated by a space
            // a StringTokenizer is used to divide the line into different observations
            // considering spaces as separator.
            String tempdir  = fichier.readLine();
            this.tempDir = new JTextField(tempdir,20);
            String graphviz = fichier.readLine();
            this.GraphVizPath = new JTextField(graphviz,20);

        }catch(Exception e){}
    }

    public void setActions()
    {
        /*
         * Action of the boutton browse1
         */
        this.browse1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showOpenDialog(Options.getTempDir());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                Options.getTempDir().setText(fc.getSelectedFile().toString());
      }
      if (returnVal == JFileChooser.CANCEL_OPTION) {}
            }
        });

        /*
         * Action of the boutton browse2
         */
        this.browse2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = fc.showOpenDialog(Options.getGraphViz());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                Options.getGraphViz().setText(fc.getSelectedFile().toString());
      }
      if (returnVal == JFileChooser.CANCEL_OPTION) {}
            }
        });

        /*
         * Action of the button Cancel
         */
        this.cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Options.mainframe.dispose();
            }
        });
        
        /*
         * Action of the button OK
         */
         this.ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try {
                    FileWriter fw = new FileWriter(projectPath+"\\config.txt", false);
                    BufferedWriter output = new BufferedWriter(fw);
                    output.write(Options.getTempDir().getText());
                    output.newLine();
                    output.write(Options.getGraphViz().getText());
                    output.flush();
                    output.close();
                    Options.mainframe.dispose();
                    //Si aucun fichier n'a été ouvert alors erreur
                    int index = OpenFileAction.file_name.lastIndexOf(".");
                    String s = OpenFileAction.file_name.substring(0, index);
                    if(OpenFileAction.isContext())
                        window.getContextInterface().getContext().conceptLattice(true).save(s+".dot");
                    else
                        window.getISInterface().getIS().closedSetLattice(true).save(s+".dot");

                    GraphViz gv = new GraphViz();
                    gv.readSource(s+".dot");
                    dot = new File(s+".dot");
                    out = new File(s+".svg");

                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), out );
                    SVGUniverse svgUniverse = new SVGUniverse();
                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s+".svg").toURI().toURL()));
                    window.getGraphPanel().setD(diagram);
                    window.getGraphPanel().setDiagram(window.getGraphPanel().getD());
                    //create a temporary file for the save as purpose when the input file isn't a temporary one
                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+".svg"),new File(tempFile+".svg"));
                        SaveAction.copy(new File(s+".dot"),new File(tempFile+".dot"));
                    }
                } catch (Exception ex) {}
            }
        });

    }
}

