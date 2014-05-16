/*
 * OpenFileAction.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package Controller;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import lattice.Context;
import lattice.ImplicationalSystem;
import View.CustomTableCellRenderer;
import View.hmi;

/**
 *
 * @author smameri
 */
public class OpenFileAction extends AbstractAction{
    //used to set reference to the mainframe
    private hmi window;
    //get the absolute path of the project
    public final static String projectPath = hmi.projectPath;
    //Create a file chooser
    final JFileChooser fc = new JFileChooser(projectPath);

    //Declaration of files and paths
    private File file;
    public static String file_name = null;
    private File myFile;
    private File out;
    private File dot;

    //temporay table to store data for the binary table to be displayed
    private Object[][] table;

    //location of the temporay files
    public static String tempFile = projectPath+"\\temp\\temp";

    private static boolean isIS = false;
    private static boolean isContext = false;

    /*
     * OpenFileAction constructor
     */
    public OpenFileAction(hmi window, String text)
    {
        super(text);
        this.window = window;
        file_name="";
    }
    
    public static boolean isContext(){ return isContext;}
    public static boolean isIS(){      return isIS;     }
    /*
     * Action listener for openFile menu item
     */
    public void actionPerformed(ActionEvent e)
    {
        if(!file_name.equals(""))
        {
            int index = OpenFileAction.file_name.lastIndexOf(".");
            String s = OpenFileAction.file_name.substring(0, index);
            
            //if a file has already been opened all previous files are deleted
            try{
                myFile = new File(s+".dot");
                myFile.delete();
                myFile = new File(s+".svg");
                myFile.delete();
                myFile = new File(s+"_to_IS.txt");
                myFile.delete();
                myFile = new File(s+"_to_Context.txt");
                myFile.delete();
                myFile = new File(s+"_Dependance_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_Dependance_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_Reduced_Lattice.dot");
                myFile.delete();
                myFile = new File(s+"_Reduced_Lattice.svg");
                myFile.delete();
                myFile = new File(s+"_Irreducible_Sub_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_Irreducible_Sub_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_Representative_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_Representative_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_precedence_Graph.dot");
                myFile.delete();
                myFile = new File(s+"_precedence_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_lattice_Graph.svg");
                myFile.delete();
                myFile = new File(s+"_lattice_Graph.dot");
                myFile.delete();
                myFile = new File(s+".xls");
                myFile.delete();
            } catch(Exception ex){}
        }
        // Force the user to only open files with the extention "txt"
        FileFilter ft = new FileNameExtensionFilter("Text Files","txt");
        fc.addChoosableFileFilter(ft);

        int returnVal = fc.showOpenDialog(window); //display the openDialog window

        if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION)
        {
            file = fc.getSelectedFile();
            file_name = file.toString();
        }
        
        loadFile(file_name);
        
        updateAdvancedWindow(e);
    }

    public void loadFile(String filename)
    {
        file_name = filename;
        
        int index = filename.lastIndexOf(".");
        String s  = filename.substring(0, index);

        //condition to determine whether IS or Context
        if(fc.getSelectedFile().toString().contains("IS"))
        {
            
            window.getSaveButton().setEnabled(true);
            window.save1.setEnabled(true);

            isIS = true;
            isContext = false;
			try {
            ImplicationalSystem is = new ImplicationalSystem(filename);
            window.getISInterface().setIS(is, true);
            window.getContextInterface().setContext(is.closedSetLattice(enabled).getTable());

            //create a temporary file for the save as purpose
            SaveAction.copy(new File(filename),new File(tempFile+".txt"));
            
            FileWriter fw2;
            
                fw2 = new FileWriter(s+ "_to_Context.txt", false); //saves the reduced context into a File
                BufferedWriter output2 = new BufferedWriter(fw2);
                output2.write(window.getISInterface().getIS().toString());
                output2.flush();
                output2.close();
                String s2 = s+"_to_Context.txt";
                if(!s2.equals(tempFile+"_to_Context.txt")) //create a temporary file in case a run action is performed
                    SaveAction.copy(new File(s+"_to_Context.txt"),new File(tempFile+"_to_Context.txt"));

                createTable(s+"_to_Context.txt");//Create table for display into 2nd internal frame
                createGraph(filename, false);  //call to function that generate the dot file and the graph then displays it
            } catch (IOException ex) {}
        }
        else if(fc.getSelectedFile().toString().contains("Context"))
        {
            
            window.getSaveButton().setEnabled(true);
            window.save1.setEnabled(true);
            
            isContext = true;
            isIS = false;
			try {
            Context ct = new Context(filename);

            window.getContextInterface().setContext(ct);
            window.getISInterface().setIS(ct.conceptLattice(enabled).getCanonicalDirectBasis(), false);
            
            SaveAction.copy(new File(filename),new File(tempFile+".txt"));
            
            FileWriter fw2;
            
                fw2 = new FileWriter(s+ "_to_IS.txt", false);
                BufferedWriter output2 = new BufferedWriter(fw2);
                output2.write(window.getISInterface().getIS().toString());
                output2.flush();
                output2.close();
                String s2 = s+"_to_IS.txt";
                if(!s2.equals(tempFile+"_to_IS.txt"))
                    SaveAction.copy(new File(s+"_to_IS.txt"),new File(tempFile+"_to_IS.txt"));
                createTable(filename);//Create table for display into 2nd internal frame
                createGraph(filename,true);
                
            }catch(Exception ec){}
        }
    }
    
    
    /**
     * Function create table launched in case of the opening of a closure system
     */
    public void createTable(String file_name)
    {
        final ArrayList<Object> observations = new ArrayList<Object>();
        final ArrayList<Object> attributes = new ArrayList<Object>();
        try
        {
            BufferedReader fichier = new BufferedReader(new FileReader(file_name));
            // first line : All observations separated by a space
            // a StringTokenizer is used to divide the line into different observations
            // considering spaces as separator.
            StringTokenizer st =  new StringTokenizer(fichier.readLine());
            st.nextToken(); // first token corresponds to the string "Observations:"
            while (st.hasMoreTokens()) {
                    String n = st.nextToken();
                    observations.add(n);
                    }            
            // second line : All attributes separated by a space
            // a StringTokenizer is used to divide the line into different token,
            // considering spaces as separator.
            st =  new StringTokenizer(fichier.readLine());
            st.nextToken(); // first token corresponds to the string "Attributes:"
            while (st.hasMoreTokens()) {
                    String n = st.nextToken();
                    attributes.add(n);
                    
                    }
            table = new Object[observations.size()][attributes.size()+1];
            for(int i=0;i<observations.size();i++)
            {
                table[i][0]=observations.get(i);
            }
            // next lines : All intents of observations, one on each line:
            // observation : list of attributes
            // a StringTokenizer is used to divide each intent.
            String line;
            while ((line = fichier.readLine())!=null && !line.isEmpty())
            {
                    
                st = new StringTokenizer(line);
                String o = st.nextToken();


                if (o!=null)
                {

                    st.nextToken(); // this token corresponds to the sting ":"
                    while (st.hasMoreTokens())
                    {
                        String a = st.nextToken();
                        table[observations.indexOf(o)][attributes.indexOf(a)+1]="X";
                    }
                }
            } 
            fichier.close();
        }
        catch (Exception e) {}
        String col[] = new String[attributes.size()+1];
        col[0]="Obs \\ Att";
        int j =1;
        for(int i =0;i<attributes.size();i++)
        {

            col[j]=(String)attributes.get(i);
            j++;
        }
        //fill the blank in the table to enable the export to an  excel file
        for(int w=0;w<attributes.size()+1;w++)
        {
            for(int h=0;h<observations.size();h++)
            {
                if(table[h][w]==null)
                    table[h][w]="";
            }
        }
        DefaultTableModel dataModel = new DefaultTableModel(table,col);

        window.getDisplayTable().setTitle(file_name);
        window.getDisplayTable().getJTable().setModel(dataModel);
        TableColumn tcol = window.getDisplayTable().getJTable().getColumnModel().getColumn(0);
        tcol.setCellRenderer(new CustomTableCellRenderer());
        JTableHeader header = window.getDisplayTable().getJTable().getTableHeader();
        header.setBackground(Color.lightGray);
        ExcelExporter.fillData(window.getDisplayTable().getJTable(),new File(tempFile+".xls"));
       
    }

    /*
     * Fuction that create and display a Graph from a Context
     */
    public void createGraph(String file_name, boolean isCont) throws IOException
    {
        int index = file_name.lastIndexOf(".");
        String s = file_name.substring(0, index);
        int index2 = s.lastIndexOf("\\");
            String s2 = s.substring(index2+1);
        if(isCont)
            window.getContextInterface().getContext().conceptLattice(enabled).save(s+".dot");
        else
            window.getISInterface().getIS().closedSetLattice(enabled).save(s+".dot");

        window.getGraphLab().setText("Lattice_"+s2);
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
    } //end of function create Graph

    //update all the internal frames of the advanced window if they are selected
    private void updateAdvancedWindow(ActionEvent e)
    {      
        if(window.getCheckBoxFrame().isOrContext.isSelected())
        {
            Rectangle r = new Rectangle(window.getCheckBoxFrame().getIsOrContextiF().getBounds());
            CheckBoxesAction checkBoxesAction = new CheckBoxesAction(this.window, 0);
            checkBoxesAction.actionPerformed(e);
            window.getCheckBoxFrame().getIsOrContextiF().setBounds(r);
        }
        if(window.getCheckBoxFrame().conceptLattice.isSelected())
        {
            Rectangle r = new Rectangle(window.getCheckBoxFrame().getConceptLiF().getBounds());
            CheckBoxesAction checkBoxesAction = new CheckBoxesAction(this.window, 1);
            checkBoxesAction.actionPerformed(e);
            window.getCheckBoxFrame().getConceptLiF().setBounds(r);
        }
        if(window.getCheckBoxFrame().getRedCiF().isVisible())
        {
            Rectangle r = new Rectangle(window.getCheckBoxFrame().getRedCiF().getBounds());
            ConceptLatticeAction clAction = new ConceptLatticeAction(this.window,15,"Generate the table");
            clAction.actionPerformed(e);
            window.getCheckBoxFrame().getRedCiF().setBounds(r);
        }
        if(window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
        {
            Rectangle r = new Rectangle(window.getCheckBoxFrame().getCDBiF().getBounds());
            CheckBoxesAction checkBoxesAction = new CheckBoxesAction(this.window, 4);
            checkBoxesAction.actionPerformed(e);
            window.getCheckBoxFrame().getCDBiF().setBounds(r);
        }
        if(window.getCheckBoxFrame().getMinGeniF().isVisible())
        {
            Rectangle r = new Rectangle(window.getCheckBoxFrame().getMinGeniF().getBounds());
            ConceptLatticeAction clAction = new ConceptLatticeAction(this.window,12,"Generate minimal generators");
            clAction.actionPerformed(e);
            window.getCheckBoxFrame().getMinGeniF().setBounds(r);
        }
        if(window.getCheckBoxFrame().canonicalBasis.isSelected())
        {
            Rectangle r = new Rectangle(window.getCheckBoxFrame().getCBLiF().getBounds());
            CheckBoxesAction checkBoxesAction = new CheckBoxesAction(this.window, 6);
            checkBoxesAction.actionPerformed(e);
            window.getCheckBoxFrame().getCBLiF().setBounds(r);
        }
        if(window.getCheckBoxFrame().getDepGiF().isVisible())
        {
            Rectangle r = new Rectangle(window.getCheckBoxFrame().getDepGiF().getBounds());
            if(window.getCheckBoxFrame().isOrContext.isSelected() && isIS)
            {
                    ClosureSystemAction csAction = new ClosureSystemAction(this.window,16,"Generate a dependance graph");
                    csAction.actionPerformed(e);
            }
            else if(window.getCheckBoxFrame().conceptLattice.isSelected() )
            {
               ConceptLatticeAction clAction = new ConceptLatticeAction(this.window,13,"Generate dependance graph");
               clAction.actionPerformed(e);
            }
            else if(window.getCheckBoxFrame().canonicalDirectBasis.isSelected() && !window.getCheckBoxFrame().conceptLattice.isSelected())
            {
                ClosureSystemAction csAction = new ClosureSystemAction(this.window,29,"Generate a dependance graph");
               csAction.actionPerformed(e);
            }
            else if(window.getCheckBoxFrame().canonicalBasis.isSelected() && !window.getCheckBoxFrame().conceptLattice.isSelected() && !window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
            {
                ClosureSystemAction csAction = new ClosureSystemAction(this.window,42,"Generate a dependance graph");
               csAction.actionPerformed(e);
            }
            window.getCheckBoxFrame().getDepGiF().setBounds(r);
        }
        if(window.getCheckBoxFrame().getRepGiF().isVisible())
        {

            Rectangle r = new Rectangle(window.getCheckBoxFrame().getRepGiF().getBounds());
            if(window.getCheckBoxFrame().isOrContext.isSelected() && isIS)
            {
                    ClosureSystemAction csAction = new ClosureSystemAction(this.window,15,"Generate a representative graph");
                    csAction.actionPerformed(e);
            }
            else if(window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
            {
                ClosureSystemAction csAction = new ClosureSystemAction(this.window,28,"Generate a representative graph");
               csAction.actionPerformed(e);
            }
            else if(window.getCheckBoxFrame().canonicalBasis.isSelected() && !window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
            {
                ClosureSystemAction csAction = new ClosureSystemAction(this.window,41,"Generate a representative graph");
               csAction.actionPerformed(e);
            }
            window.getCheckBoxFrame().getRepGiF().setBounds(r);
        }
        if(window.getCheckBoxFrame().getIrrSubGiF().isVisible())
        {
            Rectangle r = new Rectangle(window.getCheckBoxFrame().getIrrSubGiF().getBounds());
            ConceptLatticeAction clAction = new ConceptLatticeAction(this.window,14,"Generate irreducible sub graph");
            clAction.actionPerformed(e);
            window.getCheckBoxFrame().getIrrSubGiF().setBounds(r);
        }
        
    }
    
    
}// End of class OpenFileAction
