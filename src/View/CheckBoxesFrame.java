/*
 * CheckBoxesFrame.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package View;

import Controller.ConceptLatticeAction;
import Controller.ClosureSystemAction;
import Controller.CheckBoxesAction;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * @author smameri
 */
public class CheckBoxesFrame extends JInternalFrame
{
    /** reference to the parent window **/
    private hmi window;

    /** initial position, width and height of the checkBox internal frame**/
    private int FRAME_Y =5;
    private int FRAME_WIDTH = 200;
    private int FRAME_HEIGHT = 150;

    /** CheckBoxes to generate the bijectives components by triggering checkBoxesAction **/
    public JCheckBox isOrContext;
    public JCheckBox conceptLattice;
    public JCheckBox canonicalDirectBasis;
    public JCheckBox canonicalBasis;

    /** JInternalFrame to display the several bijective components **/
    private ClosureSystemInternalFrame isOrContextIF;
    private static ConceptLatticeInternalFrame conceptLatticeIF;
    private ClosureSystemInternalFrame reducedContextIF;
    private ClosureSystemInternalFrame canonicalDirectBasisIF;
    private JInternalFrame minimalGeneratorIF;
    private ClosureSystemInternalFrame canonicalBasisIF;
    private JInternalFrame dependanceGraphIF;
    private JInternalFrame representativeGraphIF;
    private JInternalFrame irreducibleSubGraphIF;
    private JInternalFrame precedenceGraphIF;

    public int windowCount=1;
    private JPanel checkPanel;


    public CheckBoxesFrame(hmi window)
    {
        //initiate all the internal frames
        isOrContextIF = new ClosureSystemInternalFrame(window);
        conceptLatticeIF = new ConceptLatticeInternalFrame(window);
        reducedContextIF = new ClosureSystemInternalFrame(window);
        reducedContextIF.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        reducedContextIF.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
        exitRD();}});
        canonicalDirectBasisIF = new ClosureSystemInternalFrame(window);
        canonicalBasisIF = new ClosureSystemInternalFrame(window);
        minimalGeneratorIF = new JInternalFrame();
        minimalGeneratorIF.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        minimalGeneratorIF.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
        exitMG();}});

        dependanceGraphIF = new JInternalFrame();
        dependanceGraphIF.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dependanceGraphIF.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
        exitDGraph();}});

        representativeGraphIF = new JInternalFrame();
        representativeGraphIF.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        representativeGraphIF.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
        exitRGraph();}});
        
        irreducibleSubGraphIF = new JInternalFrame();
        irreducibleSubGraphIF.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        irreducibleSubGraphIF.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
        exitIRRGraph();}});
        
        precedenceGraphIF = new JInternalFrame();
        precedenceGraphIF.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        precedenceGraphIF.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
        exitPGraph();}});

        this.window = window;
        this.setClosable(false);
        this.setMaximizable(false);
        this.setIconifiable(false);
        this.setResizable(false);
        this.setFrameIcon(null);

        this.setBounds(hmi.desktopPane.getWidth()-220, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);

        createCheckBoxes();
        this.setContentPane(checkPanel);
        this.setVisible(true);
    }

    private void createCheckBoxes()
    {
        isOrContext = new JCheckBox("IS or Context");
        isOrContext.addActionListener(new CheckBoxesAction(this.window,0));
        conceptLattice= new JCheckBox("Concept lattice");
        conceptLattice.addActionListener(new CheckBoxesAction(this.window,1));
        canonicalDirectBasis= new JCheckBox("Canonical Direct Basis");
        canonicalDirectBasis.addActionListener(new CheckBoxesAction(this.window,4));
        canonicalBasis= new JCheckBox(" Canonical basis");
        canonicalBasis.addActionListener(new CheckBoxesAction(this.window,6));
        
        checkPanel = new JPanel(new GridLayout(0, 1));
        checkPanel.add(isOrContext);
        checkPanel.add(conceptLattice);
        checkPanel.add(canonicalDirectBasis);
        checkPanel.add(canonicalBasis);
    }

    /**
     * Get function to access private JInternalFrames
     * @return
     */

    public ClosureSystemInternalFrame getIsOrContextiF(){return this.isOrContextIF;}

    public ConceptLatticeInternalFrame getConceptLiF(){return this.conceptLatticeIF;}

    public ClosureSystemInternalFrame getRedCiF(){ return this.reducedContextIF;}

    public ClosureSystemInternalFrame getCDBiF(){return this.canonicalDirectBasisIF;}

    public JInternalFrame getMinGeniF(){return this.minimalGeneratorIF;}

    public ClosureSystemInternalFrame getCBLiF(){return this.canonicalBasisIF;}

    public JInternalFrame getDepGiF(){return this.dependanceGraphIF;}

    public JInternalFrame getRepGiF(){return this.representativeGraphIF;}

    public JInternalFrame getIrrSubGiF(){return this.irreducibleSubGraphIF;}
    
    public JInternalFrame getPGiF(){return this.precedenceGraphIF;}

    public void exitDGraph()
    {
        try{
        ClosureSystemAction.dotDp.delete();
        ClosureSystemAction.outDp.delete();
        ConceptLatticeAction.dotDp.delete();
        ConceptLatticeAction.outDp.delete();
        }catch(Exception e){}
        window.getCheckBoxFrame().getDepGiF().setVisible(false); // frame closed and set visible to false
        window.getCheckBoxFrame().getDepGiF().dispose();
        hmi.desktopPane.remove(window.checkBoxesFrame.getDepGiF()); // we remove the frame from the container's list
        window.getCheckBoxFrame().windowCount--;
    }

    public void exitRGraph()
    {
        try{
        ClosureSystemAction.dot.delete();
        ClosureSystemAction.out.delete();
        ConceptLatticeAction.dot.delete();
        ConceptLatticeAction.out.delete();
        }catch(Exception e){}
        window.getCheckBoxFrame().getRepGiF().setVisible(false); // frame closed and set visible to false
        window.getCheckBoxFrame().getRepGiF().dispose();
        hmi.desktopPane.remove(window.checkBoxesFrame.getRepGiF()); // we remove the frame from the container's list
        window.getCheckBoxFrame().windowCount--;
    }

    public void exitMG(){
        window.getCheckBoxFrame().getMinGeniF().setVisible(false); // frame closed and set visible to false
        window.getCheckBoxFrame().getMinGeniF().dispose();
        hmi.desktopPane.remove(window.checkBoxesFrame.getMinGeniF()); // we remove the frame from the container's list
        window.getCheckBoxFrame().windowCount--;
    }

    public void exitIRRGraph()
    {
        ConceptLatticeAction.irrdot.delete();
        ConceptLatticeAction.irrout.delete();
        window.getCheckBoxFrame().getIrrSubGiF().setVisible(false); // frame closed and set visible to false
        window.getCheckBoxFrame().getIrrSubGiF().dispose();
        hmi.desktopPane.remove(window.checkBoxesFrame.getIrrSubGiF()); // we remove the frame from the container's list
        window.getCheckBoxFrame().windowCount--;
    }
    
    public void exitPGraph()
    {
        ClosureSystemAction.dotPG.delete();
        ClosureSystemAction.outPG.delete();
        window.getCheckBoxFrame().getPGiF().setVisible(false); // frame closed and set visible to false
        window.getCheckBoxFrame().getPGiF().dispose();
        hmi.desktopPane.remove(window.checkBoxesFrame.getPGiF()); // we remove the frame from the container's list
        window.getCheckBoxFrame().windowCount--;
    }

    public void exitRD(){
        window.getCheckBoxFrame().getRedCiF().setVisible(false); // frame closed and set visible to false
        window.getCheckBoxFrame().getRedCiF().dispose();
        hmi.desktopPane.remove(window.checkBoxesFrame.getRedCiF()); // we remove the frame from the container's list
        window.getCheckBoxFrame().windowCount--;
    }
}
