/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import View.hmi;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;


/**
 *
 * @author smameri
 */
public class SaveAction extends AbstractAction
{
    private hmi window;
    private String path = "\\examples\\output\\";



    public SaveAction(hmi window,String text)
    {
        super(text);
        this.window = window;
    }
    /*
     * Action listener for save menu item
     */
    public void actionPerformed(ActionEvent e)
    {
        String projectPath = hmi.projectPath;
        projectPath = projectPath+path;
        int index = OpenFileAction.file_name.lastIndexOf(".");
        String s1 = OpenFileAction.file_name.substring(0, index);
        int index2 = OpenFileAction.file_name.lastIndexOf("\\");
        String s2 = s1.substring(index2+1);
        new File(projectPath+s2+"\\").mkdir();
        File myFile = new File(projectPath+s2+"\\"+s2+".txt");
        copy(new File(s1+".txt"),myFile);
        myFile = new File(projectPath+s2+"\\"+s2+".dot");
        copy(new File(s1+".dot"),myFile);
        myFile = new File(projectPath+s2+"\\"+s2+".svg");
        copy(new File(s1+".svg"),myFile);
        if(OpenFileAction.isContext())
        {
            myFile = new File(projectPath+s2+"\\"+s2+"_to_IS.txt");
            copy(new File(s1+"_to_IS.txt"),myFile);
        }
        else if(OpenFileAction.isIS())
        {
            myFile = new File(projectPath+s2+"\\"+s2+"_to_Context.txt");
            copy(new File(s1+"_to_Context.txt"),myFile);
        }
        ExcelExporter.fillData(window.getDisplayTable().getJTable(),new File(projectPath+s2+"\\"+s2+".xls"));

        //
        if(window.getCheckBoxFrame().isOrContext.isSelected())
        {
            FileWriter fw;
            try {
                fw = new FileWriter(projectPath+s2+"\\"+window.getCheckBoxFrame().getIsOrContextiF().getTitle()+".txt", false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(window.checkBoxesFrame.getIsOrContextiF().getTextfield().getText());
                output.flush();
                output.close();
            } catch (IOException ex) {}
        }
        if(window.getCheckBoxFrame().conceptLattice.isSelected())
        {
            FileWriter fw;
            try {
                fw = new FileWriter(projectPath+s2+"\\"+window.checkBoxesFrame.getConceptLiF().getTitle()+".txt", false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(window.checkBoxesFrame.getConceptLiF().getTextfield().getText());
                output.flush();
                output.close();
            } catch (IOException ex) {}
        }
        if(window.getCheckBoxFrame().getRedCiF().isVisible())
        {
            FileWriter fw;
            try {
                fw = new FileWriter(projectPath+s2+"\\"+window.checkBoxesFrame.getRedCiF().getTitle()+".txt", false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(window.checkBoxesFrame.getRedCiF().getTextfield().toString());
                output.flush();
                output.close();
            } catch (IOException ex) {}
        }
        if(window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
        {
            FileWriter fw;
            try {
                fw = new FileWriter(projectPath+s2+"\\"+window.checkBoxesFrame.getCDBiF().getTitle()+".txt", false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(window.checkBoxesFrame.getCDBiF().getTextfield().getText());
                output.flush();
                output.close();
            } catch (IOException ex) {}
        }
        if(window.getCheckBoxFrame().getMinGeniF().isVisible())
        {
            FileWriter fw;
            try {
                fw = new FileWriter(projectPath+s2+"\\"+window.getCheckBoxFrame().getMinGeniF().getTitle()+".txt", false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(ConceptLatticeAction.mg.getText().toString());
                output.flush();
                output.close();
            } catch (IOException ex) {}
        }
        if(window.getCheckBoxFrame().canonicalBasis.isSelected())
        {
            FileWriter fw;
            try {
                fw = new FileWriter(projectPath+s2+"\\"+window.checkBoxesFrame.getCBLiF().getTitle()+".txt", false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(window.checkBoxesFrame.getCBLiF().getTextfield().getText());
                output.flush();
                output.close();
            } catch (IOException ex) {}
        }
        if(window.getCheckBoxFrame().getDepGiF().isVisible())
        {
            myFile = new File(projectPath+s2+"\\"+s2+"_Dependance_Graph.dot");
            copy(new File(s1+"_Dependance_Graph.dot"),myFile);
            myFile = new File(projectPath+s2+"\\"+s2+"_Dependance_Graph.svg");
            copy(new File(s1+"_Dependance_Graph.svg"),myFile);
        }
        if(window.getCheckBoxFrame().getRepGiF().isVisible())
        {
            myFile = new File(projectPath+s2+"\\"+s2+"_Representative_Graph.dot");
            copy(new File(s1+"_Representative_Graph.dot"),myFile);
            myFile = new File(projectPath+s2+"\\"+s2+"_Representative_Graph.svg");
            copy(new File(s1+"_Representative_Graph.svg"),myFile);
        }
        if(window.getCheckBoxFrame().getIrrSubGiF().isVisible())
        {
            myFile = new File(projectPath+s2+"\\"+s2+"_Irreducible_Sub_Graph.dot");
            copy(new File(s1+"_Irreducible_Sub_Graph.dot"),myFile);
            myFile = new File(projectPath+s2+"\\"+s2+"_Irreducible_Sub_Graph.svg");
            copy(new File(s1+"_Irreducible_Sub_Graph.svg"),myFile);
        }

    }

    /*
     * function to copy a file in a saving directory
     */
    public static boolean copy( File src, File dest )
    {
        boolean resultat = false;

        // stream declaration
        java.io.FileInputStream sourceFile=null;
        java.io.FileOutputStream destinationFile=null;
        try
        {
            // create a new file
            dest.createNewFile();
            // Open stream
            sourceFile = new java.io.FileInputStream(src);
            destinationFile = new java.io.FileOutputStream(dest);
            // read by 0.5Mo segment
            byte buffer[]=new byte[512*1024];
            int nbReading;
            while( (nbReading = sourceFile.read(buffer)) != -1 ) {
                destinationFile.write(buffer, 0, nbReading);
            }

            // copy success
            resultat = true;
        }
        catch( java.io.FileNotFoundException f ) {}
        catch( java.io.IOException e ) {} 
        finally  // close the stream
        {            
            try { sourceFile.close();      } catch(Exception e) { }
            try { destinationFile.close(); } catch(Exception e) { }
        }
        return( resultat );
    }
}