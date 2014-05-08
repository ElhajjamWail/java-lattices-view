/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.ClosureSystemAction;
import javax.swing.*;
import lattice.ClosureSystem;

/**
 *
 * @author smameri
 */
public class ClosureSystemInternalFrame extends JInternalFrame {


    private static hmi window;

    private JTextArea cs;

    public ClosureSystem coris=null;

    public ClosureSystemInternalFrame(hmi window)
    {
        this.cs = new JTextArea();
        this.window = window;


    }




public void buildMenuBarContext()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Tools");
        JMenuItem precedenceGraph = new JMenuItem(new ClosureSystemAction(this.window,50,"Generate the precedence Graph"));
        menu.add(precedenceGraph);
        menu.addSeparator();
        JMenuItem conceptLattice = new JMenuItem(new ClosureSystemAction(this.window,1,"Generate Concept Lattice"));
        menu.add(conceptLattice);
        menu.addSeparator();
        JMenuItem reverse = new JMenuItem(new ClosureSystemAction(this.window,2,"Reverse component"));
        menu.add(reverse);
        JMenuItem reduction = new JMenuItem(new ClosureSystemAction(this.window,3,"Reduce component"));
        menu.add(reduction);
        JMenuItem attReduction = new JMenuItem(new ClosureSystemAction(this.window,4,"Make attributes reduction"));
        menu.add(attReduction);
        JMenuItem obsReduction = new JMenuItem(new ClosureSystemAction(this.window,5,"Make observations reduction"));
        menu.add(obsReduction);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }


public void buildMenuBarIS()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Tools");
        JMenuItem proper = new JMenuItem(new ClosureSystemAction(this.window,6,"Make a proper IS"));
        menu.add(proper);
        JMenuItem unary = new JMenuItem(new ClosureSystemAction(this.window,7,"Make a unary component"));
        menu.add(unary);
        JMenuItem compact = new JMenuItem(new ClosureSystemAction(this.window,8,"Make a compact component"));
        menu.add(compact);
        JMenuItem righMaximal = new JMenuItem(new ClosureSystemAction(this.window,9,"Make right maximal"));
        menu.add(righMaximal);
        JMenuItem leftMinimal = new JMenuItem(new ClosureSystemAction(this.window,10,"Make left minimal"));
        menu.add(leftMinimal);
        JMenuItem direct = new JMenuItem(new ClosureSystemAction(this.window,11,"Make a compact and direct component"));
        menu.add(direct);
        JMenuItem minimum = new JMenuItem(new ClosureSystemAction(this.window,12,"Make a minimum and proper IS"));
        menu.add(minimum);
        JMenuItem cdb = new JMenuItem(new ClosureSystemAction(this.window,13,"Make canonical direct basis"));
        menu.add(cdb);
        JMenuItem cb = new JMenuItem(new ClosureSystemAction(this.window,14,"Make canonical basis"));
        menu.add(cb);
        menu.addSeparator();
        JMenuItem representativeGraph = new JMenuItem(new ClosureSystemAction(this.window,15,"Generate the representative graph"));
        menu.add(representativeGraph);
        JMenuItem dependanceGraph = new JMenuItem(new ClosureSystemAction(this.window,16,"Generate the dependance graph"));
        menu.add(dependanceGraph);
        menu.addSeparator();
        JMenuItem reduction = new JMenuItem(new ClosureSystemAction(this.window,17,"Reduce component"));
        menu.add(reduction);
        JMenuItem closedSetLattice = new JMenuItem(new ClosureSystemAction(this.window,18,"Generate closed set lattice"));
        menu.add(closedSetLattice);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

public void buildMenuBarCDB()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Tools");
        JMenuItem proper = new JMenuItem(new ClosureSystemAction(this.window,19,"Make a proper IS"));
        menu.add(proper);
        JMenuItem unary = new JMenuItem(new ClosureSystemAction(this.window,20,"Make a unary component"));
        menu.add(unary);
        JMenuItem compact = new JMenuItem(new ClosureSystemAction(this.window,21,"Make a compact component"));
        menu.add(compact);
        JMenuItem righMaximal = new JMenuItem(new ClosureSystemAction(this.window,22,"Make right maximal"));
        menu.add(righMaximal);
        JMenuItem leftMinimal = new JMenuItem(new ClosureSystemAction(this.window,23,"Make left minimal"));
        menu.add(leftMinimal);
        JMenuItem direct = new JMenuItem(new ClosureSystemAction(this.window,24,"Make a compact and direct component"));
        menu.add(direct);
        JMenuItem minimum = new JMenuItem(new ClosureSystemAction(this.window,25,"Make a minimum and proper IS"));
        menu.add(minimum);
        JMenuItem cdb = new JMenuItem(new ClosureSystemAction(this.window,26,"Make canonical direct basis"));
        menu.add(cdb);
        JMenuItem cb = new JMenuItem(new ClosureSystemAction(this.window,27,"Make canonical basis"));
        menu.add(cb);
        menu.addSeparator();
        JMenuItem representativeGraph = new JMenuItem(new ClosureSystemAction(this.window,28,"Generate the representative graph"));
        menu.add(representativeGraph);
        JMenuItem dependanceGraph = new JMenuItem(new ClosureSystemAction(this.window,29,"Generate the dependance graph"));
        menu.add(dependanceGraph);
        menu.addSeparator();
        JMenuItem reduction = new JMenuItem(new ClosureSystemAction(this.window,30,"Reduce component"));
        menu.add(reduction);
        JMenuItem closedSetLattice = new JMenuItem(new ClosureSystemAction(this.window,31,"Generate closed set lattice"));
        menu.add(closedSetLattice);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

public void buildMenuBarCB()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Tools");
        JMenuItem proper = new JMenuItem(new ClosureSystemAction(this.window,32,"Make a proper IS"));
        menu.add(proper);
        JMenuItem unary = new JMenuItem(new ClosureSystemAction(this.window,33,"Make a unary component"));
        menu.add(unary);
        JMenuItem compact = new JMenuItem(new ClosureSystemAction(this.window,34,"Make a compact component"));
        menu.add(compact);
        JMenuItem righMaximal = new JMenuItem(new ClosureSystemAction(this.window,35,"Make right maximal"));
        menu.add(righMaximal);
        JMenuItem leftMinimal = new JMenuItem(new ClosureSystemAction(this.window,36,"Make left minimal"));
        menu.add(leftMinimal);
        JMenuItem direct = new JMenuItem(new ClosureSystemAction(this.window,37,"Make a compact and direct component"));
        menu.add(direct);
        JMenuItem minimum = new JMenuItem(new ClosureSystemAction(this.window,38,"Make a minimum and proper IS"));
        menu.add(minimum);
        JMenuItem cdb = new JMenuItem(new ClosureSystemAction(this.window,39,"Make canonical direct basis"));
        menu.add(cdb);
        JMenuItem cb = new JMenuItem(new ClosureSystemAction(this.window,40,"Make canonical basis"));
        menu.add(cb);
        menu.addSeparator();
        JMenuItem representativeGraph = new JMenuItem(new ClosureSystemAction(this.window,41,"Generate the representative graph"));
        menu.add(representativeGraph);
        JMenuItem dependanceGraph = new JMenuItem(new ClosureSystemAction(this.window,42,"Generate the dependance graph"));
        menu.add(dependanceGraph);
        menu.addSeparator();
        JMenuItem reduction = new JMenuItem(new ClosureSystemAction(this.window,43,"Reduce component"));
        menu.add(reduction);
        JMenuItem closedSetLattice = new JMenuItem(new ClosureSystemAction(this.window,44,"Generate closed set lattice"));
        menu.add(closedSetLattice);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

public void buildMenuBarTable()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Tools");
        JMenuItem precedenceGraph = new JMenuItem(new ClosureSystemAction(this.window,51,"Generate the precedence Graph"));
        menu.add(precedenceGraph);
        menu.addSeparator();
        JMenuItem conceptLattice = new JMenuItem(new ClosureSystemAction(this.window,45,"Generate Concept Lattice"));
        menu.add(conceptLattice);
        menu.addSeparator();
        JMenuItem reverse = new JMenuItem(new ClosureSystemAction(this.window,46,"Reverse component"));
        menu.add(reverse);
        JMenuItem reduction = new JMenuItem(new ClosureSystemAction(this.window,47,"Reduce component"));
        menu.add(reduction);
        JMenuItem attReduction = new JMenuItem(new ClosureSystemAction(this.window,48,"Make attributes reduction"));
        menu.add(attReduction);
        JMenuItem obsReduction = new JMenuItem(new ClosureSystemAction(this.window,49,"Make observations reduction"));
        menu.add(obsReduction);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public JTextArea getTextfield()
    {
        return this.cs;
    }
}


