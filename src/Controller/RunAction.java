package Controller;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import lattice.Context;
import lattice.ImplicationalSystem;
import View.hmi;


/**
 *
 * @author smameri
 * @author Sylvain MORIN
 * This RunAction class provide at the user the possibility to create his own implicational system or context by writing in the JtextArea all the rules
 * needed. The action performed by the class will then generate the IS if the initial closure system is a context, and vice versa. Then will be generated
 * all the bijective components of the closure system
 */
public class RunAction extends AbstractAction{

    private hmi window;

    private OpenFileAction open;

    /*
     * Path of the project/jar directory
     */
    public static final String projectPath = hmi.projectPath;
    public static final String tempFile = projectPath + "\\temp\\temp.txt";
    public static final String tempFile2 = projectPath + "\\temp\\temp";
    
    private File dot,out;
    private boolean isIS;
    
    public RunAction(hmi w, String text,OpenFileAction op, boolean isIS) {
        super(text);
        this.window = w;
        this.open   = op;
        this.isIS   =  isIS;
    }

    public void runAction(ActionEvent e, boolean isIS)
    {
        this.isIS = isIS;
        actionPerformed(e);
    }
    
    public void actionPerformed(ActionEvent e) {
        OpenFileAction.file_name = tempFile;

        //we disable the save button to force the user the use save as to create a directory with the choosen name
        window.getSaveButton().setEnabled(false);
        window.save1.setEnabled(false);
        
        createISandContext(isIS);
        
        //update all the internal frames of the advanced window if they are selected
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
            if(window.getCheckBoxFrame().isOrContext.isSelected() && OpenFileAction.isIS())
            {
                ClosureSystemAction csAction = new ClosureSystemAction(this.window,16,"Generate a dependance graph");
                csAction.actionPerformed(e);
            }
            else if(window.getCheckBoxFrame().conceptLattice.isSelected())
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
            if(window.getCheckBoxFrame().isOrContext.isSelected() && OpenFileAction.isIS())
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
    
    public void createISandContext(boolean isIs)
    {
        if (isIs) {
            FileWriter fw, fw2;
            try {
                fw = new FileWriter(tempFile, false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(window.getISInterface().getIS().toString()); //we write the content of the closure system in a temp file
                output.flush();
                output.close();

                ImplicationalSystem is = new ImplicationalSystem(tempFile);
                window.getISInterface().setIS(is, false);
                window.getContextInterface().setContext(is.closedSetLattice(enabled).getTable());

                fw2 = new FileWriter(tempFile2 + "_to_Context.txt", false);  //we write the converted closure system into another temp file
                BufferedWriter output2 = new BufferedWriter(fw2);
                output2.write(window.getContextInterface().getContext().toString());
                output2.flush();
                output2.close();

                open.createTable(tempFile2+ "_to_Context.txt");
                open.createGraph(tempFile, false);       //generate and display the graph
            } catch (IOException ex) {ex.printStackTrace();}
        }
        else
        {
            try {
                FileWriter fw = new FileWriter(tempFile, false);
                BufferedWriter output = new BufferedWriter(fw);
                output.write(window.getContextInterface().getContext().toString());
                output.flush();
                output.close();

                Context c = new Context(tempFile);
                window.getContextInterface().setContext(c);
                window.getISInterface().setIS(c.conceptLattice(enabled).getCanonicalDirectBasis(), false);
                FileWriter fw2 = new FileWriter(tempFile2 + "_to_IS.txt", false);
                BufferedWriter output2 = new BufferedWriter(fw2);
                output2.write(window.getISInterface().getIS().toString());
                output2.flush();
                output2.close();

                open.createTable(tempFile);
                open.createGraph(tempFile, true);
            } catch (Exception ex) {ex.printStackTrace();}
        }    
    }
}
