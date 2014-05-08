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
import javax.swing.JFileChooser;

/**
 *
 * @author smameri
 */
public class SaveAsAction extends AbstractAction
{
    private hmi window;

    //get the absolute path of the project
    public static String projectPath = hmi.projectPath;
    //location of the temporay file
    public static String tempFile = projectPath+"\\temp\\temp";

    //Create a file chooser
    final JFileChooser fc = new JFileChooser("/");

    //Declaration of files and paths
    private File file;
    public static String file_name;

    public SaveAsAction(hmi window,String text)
    {
        super(text);
        this.window = window;
    }
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       int returnVal = fc.showDialog(window,"Save As");
          if (returnVal == JFileChooser.APPROVE_OPTION) {

            file_name = fc.getSelectedFile().toString();
        int index = file_name.lastIndexOf("\\");
        File myFile = new File(file_name+"\\"+file_name.substring(index+1)+".txt");
        SaveAction.copy(new File(tempFile+".txt"),myFile);
        myFile = new File(file_name+"\\"+file_name.substring(index+1)+".dot");
        SaveAction.copy(new File(tempFile+".dot"),myFile);
        myFile = new File(file_name+"\\"+file_name.substring(index+1)+".svg");
        SaveAction.copy(new File(tempFile+".svg"),myFile);
        if(OpenFileAction.isContext())
        {
            myFile = new File(file_name+"\\"+file_name.substring(index+1)+"_to_IS.txt");
            SaveAction.copy(new File(tempFile+"_to_IS.txt"),myFile);
            
        }
        else if(OpenFileAction.isIS())
        {
            myFile = new File(file_name+"\\"+file_name.substring(index+1)+"_to_Context.txt");
            SaveAction.copy(new File(tempFile+"_to_Context.txt"),myFile);
        }
        ExcelExporter.fillData(window.getDisplayTable().getJTable(),new File(file_name+"\\"+file_name.substring(index+1)+".xls"));

        if(window.getCheckBoxFrame().conceptLattice.isSelected())
        {
            FileWriter fw;
            try {
                fw = new FileWriter(file_name+"\\"+file_name.substring(index+1)+"_concept_lattice.txt", false);
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
                fw = new FileWriter(file_name+"\\"+file_name.substring(index+1)+"_table.txt", false);
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
                fw = new FileWriter(file_name+"\\"+file_name.substring(index+1)+"_CDB.txt", false);
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
                fw = new FileWriter(file_name+"\\"+file_name.substring(index+1)+"_minimal_generators.txt", false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(ConceptLatticeAction.mg.getText().toString());
                output.flush();
                output.close();
            } catch (IOException ex) {}
        }
        if(window.getCheckBoxFrame().canonicalBasis.isVisible())
        {
            FileWriter fw;
            try {
                fw = new FileWriter(file_name+"\\"+file_name.substring(index+1)+"_CB.txt", false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(window.checkBoxesFrame.getCBLiF().getTextfield().getText());
                output.flush();
                output.close();
            } catch (IOException ex) {}
        }
        if(window.getCheckBoxFrame().getDepGiF().isVisible())
        {
            myFile = new File(file_name+"\\"+file_name.substring(index+1)+"_Dependance_Graph.dot");
            SaveAction.copy(new File(tempFile+"_Dependance_Graph.dot"),myFile);
            myFile = new File(file_name+"\\"+file_name.substring(index+1)+"_Dependance_Graph.svg");
            SaveAction.copy(new File(tempFile+"_Dependance_Graph.svg"),myFile);
        }
        if(window.getCheckBoxFrame().getRepGiF().isVisible())
        {
            myFile = new File(file_name+"\\"+file_name.substring(index+1)+"_Representative_Graph.dot");
            SaveAction.copy(new File(tempFile+"_Representative_Graph.dot"),myFile);
            myFile = new File(file_name+"\\"+file_name.substring(index+1)+"_Representative_Graph.svg");
            SaveAction.copy(new File(tempFile+"_Representative_Graph.svg"),myFile);
        }
        if(window.getCheckBoxFrame().getIrrSubGiF().isVisible())
        {
            myFile = new File(file_name+"\\"+file_name.substring(index+1)+"_Irreducible_Sub_Graph.dot");
            SaveAction.copy(new File(tempFile+"_Irreducible_Sub_Graph.dot"),myFile);
            myFile = new File(file_name+"\\"+file_name.substring(index+1)+"_Irreducible_Sub_Graph.svg");
            SaveAction.copy(new File(tempFile+"_Irreducible_Sub_Graph.svg"),myFile);
        }
    }
    }

}
