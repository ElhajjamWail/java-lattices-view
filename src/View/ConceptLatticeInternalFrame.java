/*
 * ConceptLatticeInternalFrame.java
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
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import lattice.ConceptLattice;

/**
 *
 * @author smameri
 */
public class ConceptLatticeInternalFrame extends JInternalFrame {

    private static hmi window;

    private JTextArea cl;

    public ConceptLattice conceptLattice = null;

    public ConceptLatticeInternalFrame(hmi window)
    {
        this.cl = new JTextArea();
        this.window = window;
        buildMenuBar();


        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
        exit();
      }
    });
    }

    public void exit()
    {
        window.checkBoxesFrame.getConceptLiF().dispose(); // frame closed and set visible to false
        window.checkBoxesFrame.getConceptLiF().setVisible(false);
        hmi.desktopPane.remove(window.checkBoxesFrame.getConceptLiF()); // we remove the frame from the container's list
        window.getCheckBoxFrame().windowCount--;
    }

public void buildMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Tools");
        JMenuItem allSetA = new JMenuItem(new ConceptLatticeAction(this.window,1,"Remove all set A"));
        menu.add(allSetA);
        JMenuItem allSetB = new JMenuItem(new ConceptLatticeAction(this.window,2,"Remove all set B"));
        menu.add(allSetB);
        JMenuItem setAForJoin = new JMenuItem(new ConceptLatticeAction(this.window,3,"Initialize set A for join"));
        menu.add(setAForJoin);
        JMenuItem setBForMeet = new JMenuItem(new ConceptLatticeAction(this.window,4,"Initialize set B for meet"));
        menu.add(setBForMeet);
        menu.addSeparator();
        JMenuItem inclusionReduction = new JMenuItem(new ConceptLatticeAction(this.window,5,"Make inclusion reduction"));
        menu.add(inclusionReduction);
        JMenuItem irreduciblesReduction = new JMenuItem(new ConceptLatticeAction(this.window,6,"Make irreducibles reduction"));
        menu.add(irreduciblesReduction);
        JMenuItem edgeValuation = new JMenuItem(new ConceptLatticeAction(this.window,7,"Make edges valuation"));
        menu.add(edgeValuation);
        menu.addSeparator();
        JMenuItem canonicalDirectBasis = new JMenuItem(new ConceptLatticeAction(this.window,11,"Generate canonical direct basis"));
        menu.add(canonicalDirectBasis);
        JMenuItem minimalGenerators = new JMenuItem(new ConceptLatticeAction(this.window,12,"Generate minimal generators"));
        menu.add(minimalGenerators);
        JMenuItem table = new JMenuItem(new ConceptLatticeAction(this.window,15,"Generate the table"));
        menu.add(table);
        menu.addSeparator();
        JMenuItem dependanceGraph = new JMenuItem(new ConceptLatticeAction(this.window,13,"Generate dependance graph"));
        menu.add(dependanceGraph);
        JMenuItem irreducibleSubGraph = new JMenuItem(new ConceptLatticeAction(this.window,14,"Generate irreducible sub graph"));
        menu.add(irreducibleSubGraph);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public JTextArea getTextfield()
    {
        return this.cl;
    }

}
