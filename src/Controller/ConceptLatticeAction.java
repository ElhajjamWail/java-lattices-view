/*
 * ConceptLatticeAction.java
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
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import lattice.ConceptLattice;
import dgraph.DGraph;
import View.GraphPanel;
import View.hmi;

/**
 *
 * @author smameri
 */
public class ConceptLatticeAction extends AbstractAction {


    private static hmi window;

    private int numAction;

    /** The dependance graphe of the implicational system **/
    public DGraph dependanceGraph = null;
    /** The representative graphe of the implicational system **/
    public DGraph representativeGraph = null;

    /** Output files for the dot and the svg files of the closure system **/
    public static File dot,out,dotDp,outDp,irrdot,irrout =null;

    public static JTextArea mg;

    //get the absolute path of the project
    public static String projectPath = hmi.projectPath;
    //location of the temporay file
    public static String tempFile = projectPath+"\\temp\\temp";


    public ConceptLatticeAction(hmi window,int num,String s)
    {
        super(s);
        this.window = window;
        numAction = num;

    }

    public void actionPerformed(ActionEvent e)
    {
        int index = OpenFileAction.file_name.lastIndexOf(".");
        String s = OpenFileAction.file_name.substring(0, index);
        int index2 = s.lastIndexOf("\\");
        String s2 = s.substring(index2+1);
        

        switch(numAction)
        {
            case 1:
                ((ConceptLattice)window.checkBoxesFrame.getConceptLiF().conceptLattice).removeAllSetA();
                window.checkBoxesFrame.getConceptLiF().getTextfield().setText(window.checkBoxesFrame.getConceptLiF().conceptLattice.toString());
                break;
            case 2:
                ((ConceptLattice)window.checkBoxesFrame.getConceptLiF().conceptLattice).removeAllSetB();
                window.checkBoxesFrame.getConceptLiF().getTextfield().setText(window.checkBoxesFrame.getConceptLiF().conceptLattice.toString());
                break;
            case 3:
                ((ConceptLattice)window.checkBoxesFrame.getConceptLiF().conceptLattice).initializeSetAForJoin();
                window.checkBoxesFrame.getConceptLiF().getTextfield().setText(window.checkBoxesFrame.getConceptLiF().conceptLattice.toString());
                break;
            case 4:
               ((ConceptLattice)window.checkBoxesFrame.getConceptLiF().conceptLattice).initializeSetBForMeet();
                window.checkBoxesFrame.getConceptLiF().getTextfield().setText(window.checkBoxesFrame.getConceptLiF().conceptLattice.toString());
                break;
            case 5:
                ((ConceptLattice)window.checkBoxesFrame.getConceptLiF().conceptLattice).makeInclusionReduction();
                window.checkBoxesFrame.getConceptLiF().getTextfield().setText(window.checkBoxesFrame.getConceptLiF().conceptLattice.toString());
               break;
            case 6:
               ((ConceptLattice)window.checkBoxesFrame.getConceptLiF().conceptLattice).makeIrreduciblesReduction();
                window.checkBoxesFrame.getConceptLiF().getTextfield().setText(window.checkBoxesFrame.getConceptLiF().conceptLattice.toString());
               break;
            case 7:
               ((ConceptLattice)window.checkBoxesFrame.getConceptLiF().conceptLattice).makeEdgeValuation();
                window.checkBoxesFrame.getConceptLiF().getTextfield().setText(window.checkBoxesFrame.getConceptLiF().conceptLattice.toString());
               break;
            case 11:
                 if(!window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
                    window.getCheckBoxFrame().canonicalDirectBasis.setSelected(enabled);

                    try {
                        window.getCheckBoxFrame().getCDBiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                    // the parameters of the internal frame for concept lattice
                    window.getCheckBoxFrame().getCDBiF().setClosable(false);
                    window.getCheckBoxFrame().getCDBiF().setMaximizable(true);
                    window.getCheckBoxFrame().getCDBiF().setIconifiable(true);
                    window.getCheckBoxFrame().getCDBiF().setResizable(true);
                    if(window.getCheckBoxFrame().getCDBiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getCDBiF().getBounds());
                    window.getCheckBoxFrame().getCDBiF().setBounds(r);
                    }
                    else //calculate and set the position of the internal frame
                        window.getCheckBoxFrame().getCDBiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 150);

                    //create the menu bar for the cdb internal frame
                    window.getCheckBoxFrame().getCDBiF().buildMenuBarCDB();
                    //we create the canonical direct basis from the concept lattice
                    window.checkBoxesFrame.getCDBiF().coris = window.checkBoxesFrame.getConceptLiF().conceptLattice.getCanonicalDirectBasis();

                    window.checkBoxesFrame.getCDBiF().getTextfield().setBackground(Color.white);
                    window.checkBoxesFrame.getCDBiF().getTextfield().setEditable(false);
                    window.checkBoxesFrame.getCDBiF().getTextfield().setText(window.checkBoxesFrame.getCDBiF().coris.toString()); //create the direct canonical basis of the concept lattice
                    window.getCheckBoxFrame().getCDBiF().setTitle(s2+"_Canonical_Direct_Basis");
                    window.getCheckBoxFrame().getCDBiF().setContentPane(new JScrollPane(window.checkBoxesFrame.getCDBiF().getTextfield()));
                    try{ hmi.desktopPane.remove(window.checkBoxesFrame.getCDBiF()); // we remove the frame from the container's list if component exist
                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.getCheckBoxFrame().getCDBiF(),new Integer(2));
                    window.getCheckBoxFrame().getCDBiF().setVisible(true);
                     window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed

                if(!window.getCheckBoxFrame().canonicalDirectBasis.isSelected())
                {
                    window.getCheckBoxFrame().getCDBiF().setVisible(false); // frame closed and set visible to false
                    window.getCheckBoxFrame().getCDBiF().dispose();
                    hmi.desktopPane.remove(window.checkBoxesFrame.getCDBiF()); // we remove the frame from the container's list
                    window.getCheckBoxFrame().windowCount--;
                }
                break;
            case 12:
                try {
                        window.getCheckBoxFrame().getMinGeniF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getMinGeniF().setClosable(true);
                window.getCheckBoxFrame().getMinGeniF().setMaximizable(true);
                window.getCheckBoxFrame().getMinGeniF().setIconifiable(true);
                window.getCheckBoxFrame().getMinGeniF().setResizable(true);
                if(window.getCheckBoxFrame().getMinGeniF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getMinGeniF().getBounds());
                    window.getCheckBoxFrame().getMinGeniF().setBounds(r);
                    }
                    else //calculate and set the position of the internal frame
                        window.getCheckBoxFrame().getMinGeniF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 150);



                    mg = new JTextArea();
                    mg.setBackground(Color.white);
                    mg.setEditable(false);
                    mg.setText(window.checkBoxesFrame.getConceptLiF().conceptLattice.getMinimalGenerators().toString()); //create the minimal generators of the lattice concept
                    window.getCheckBoxFrame().getMinGeniF().setTitle(s2+"_Minimal_generators");
                    window.getCheckBoxFrame().getMinGeniF().setContentPane(new JScrollPane(mg));
                    try{ hmi.desktopPane.remove(window.checkBoxesFrame.getMinGeniF()); // we remove the frame from the container's list if component exist
                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.getCheckBoxFrame().getMinGeniF(),new Integer(2));
                    window.getCheckBoxFrame().getMinGeniF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                break;
            case 13:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getDepGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getDepGiF().setClosable(true);
                window.getCheckBoxFrame().getDepGiF().setMaximizable(true);
                window.getCheckBoxFrame().getDepGiF().setIconifiable(true);
                window.getCheckBoxFrame().getDepGiF().setResizable(true);

                if(window.getCheckBoxFrame().getDepGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getDepGiF().getBounds());
                    window.getCheckBoxFrame().getDepGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getDepGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.dependanceGraph = window.checkBoxesFrame.getConceptLiF().conceptLattice.getDependencyGraph();
                    this.dependanceGraph.save(s+"_Dependance_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_Dependance_Graph.dot");
                    dotDp = new File(s+"_Dependance_Graph.dot");
                    outDp = new File(s+"_Dependance_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), outDp );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_Dependance_Graph.svg"),new File(tempFile+"_Dependance_Graph.svg"));
                        SaveAction.copy(new File(s+"_Dependance_Graph.dot"),new File(tempFile+"_Dependance_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_Dependance_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getDepGiF().setTitle(s2+"_Dependance_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getDepGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getDepGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getDepGiF(),new Integer(2));
                    window.getCheckBoxFrame().getDepGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}

              break;
            case 14:
                try {
                    try
                    {
                        window.getCheckBoxFrame().getIrrSubGiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}

                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getIrrSubGiF().setClosable(true);
                window.getCheckBoxFrame().getIrrSubGiF().setMaximizable(true);
                window.getCheckBoxFrame().getIrrSubGiF().setIconifiable(true);
                window.getCheckBoxFrame().getIrrSubGiF().setResizable(true);

                if(window.getCheckBoxFrame().getIrrSubGiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getIrrSubGiF().getBounds());
                    window.getCheckBoxFrame().getIrrSubGiF().setBounds(r);
                    }
                     else //calculate and set the position of the internal frame
                    window.getCheckBoxFrame().getIrrSubGiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);



                    // create the dependance graph from the initial implicational system and its dot file
                    this.dependanceGraph = window.checkBoxesFrame.getConceptLiF().conceptLattice.irreduciblesSubgraph();
                    this.dependanceGraph.save(s+"_Irreducible_Sub_Graph.dot");
                    GraphViz gv = new GraphViz();
                    gv.readSource(s+"_Irreducible_Sub_Graph.dot");
                    irrdot = new File(s+"_Irreducible_Sub_Graph.dot");
                    irrout = new File(s+"_Irreducible_Sub_Graph.svg");
                    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), "svg" ), irrout );

                    if(!s.equals(tempFile))
                    {
                        SaveAction.copy(new File(s+"_Irreducible_Sub_Graph.svg"),new File(tempFile+"_Irreducible_Sub_Graph.svg"));
                        SaveAction.copy(new File(s+"_Irreducible_Sub_Graph.dot"),new File(tempFile+"_Irreducible_Sub_Graph.dot"));
                    }

                    final GraphPanel g = new GraphPanel();
                    SVGUniverse svgUniverse = new SVGUniverse();

                    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(new File(s + "_Irreducible_Sub_Graph.svg").toURI().toURL()));
                    g.setD(diagram);
                    g.setDiagram(g.getD());
                    window.getCheckBoxFrame().getIrrSubGiF().setTitle(s2+"_Irreducible_Sub_Graph");
                    final JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    final JPanel p = new JPanel();
                    p.add(g);
                    p.setBackground(Color.white);
                    sp.setViewportView(p);
                    window.getCheckBoxFrame().getIrrSubGiF().setContentPane(sp);
                    p.addMouseWheelListener(new MouseWheelListener() {
                    public void mouseWheelMoved(final MouseWheelEvent e)
                    {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
                                                    if (e.getWheelRotation() > 0) {
                                                            g.zoom_out();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    } else {
                                                            g.zoom_in();
                                                                  p.scrollRectToVisible(new Rectangle(e.getX()-(sp.getWidth()/2),e.getY()-(sp.getHeight()/2),sp.getWidth(),sp.getHeight()));
                                                    }
                                            }
                        }
                    });

                     hmi.desktopPane.remove(window.checkBoxesFrame.getIrrSubGiF()); // we remove the frame from the container's list if component exist


                    hmi.desktopPane.add(window.getCheckBoxFrame().getIrrSubGiF(),new Integer(2));
                    window.getCheckBoxFrame().getIrrSubGiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                    }catch(IOException ex){}
                break;
            case 15:
                try {
                        window.getCheckBoxFrame().getRedCiF().setIcon(false);
                    } catch (PropertyVetoException ex) {}
                // the parameters of the internal frame for concept lattice
                window.getCheckBoxFrame().getRedCiF().setClosable(true);
                window.getCheckBoxFrame().getRedCiF().setMaximizable(true);
                window.getCheckBoxFrame().getRedCiF().setIconifiable(true);
                window.getCheckBoxFrame().getRedCiF().setResizable(true);
                window.getCheckBoxFrame().getRedCiF().buildMenuBarTable();
                if(window.getCheckBoxFrame().getRedCiF().isShowing())
                    {
                    Rectangle r = new Rectangle(window.getCheckBoxFrame().getRedCiF().getBounds());
                    window.getCheckBoxFrame().getRedCiF().setBounds(r);
                    }
                    else //calculate and set the position of the internal frame
                        window.getCheckBoxFrame().getRedCiF().setBounds(20*(window.getCheckBoxFrame().windowCount%10), 20*(window.getCheckBoxFrame().windowCount%10), 400, 300);

                    window.checkBoxesFrame.getRedCiF().coris = window.checkBoxesFrame.getConceptLiF().conceptLattice.getTable();
                    
                    window.checkBoxesFrame.getRedCiF().getTextfield().setBackground(Color.white);
                    window.checkBoxesFrame.getRedCiF().getTextfield().setEditable(false);
                    window.checkBoxesFrame.getRedCiF().getTextfield().setText(window.checkBoxesFrame.getRedCiF().coris.toString()); //create the table of the lattice
                    window.getCheckBoxFrame().getRedCiF().setTitle(s2+"_table");
                    window.getCheckBoxFrame().getRedCiF().setContentPane(new JScrollPane(window.checkBoxesFrame.getRedCiF().getTextfield()));
                    try{ hmi.desktopPane.remove(window.checkBoxesFrame.getRedCiF()); // we remove the frame from the container's list if component exist
                    }catch(Exception ex){}
                    hmi.desktopPane.add(window.getCheckBoxFrame().getRedCiF(),new Integer(2));
                    window.getCheckBoxFrame().getRedCiF().setVisible(true);
                    window.getCheckBoxFrame().windowCount++; //windoCount is incremented to determin the position of the next JInternalFrame to be displayed
                break;
        }
    }

}
