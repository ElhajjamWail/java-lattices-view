/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author Sylvain MORIN
 */
public abstract class DropDownButton extends JButton implements ChangeListener, PopupMenuListener, ActionListener, PropertyChangeListener{ 
    private final JButton mainButton = this; 
    private final JButton arrowButton = new JButton(new ImageIcon(hmi.projectPath+"\\icons\\arrow.png")); 
 
    private boolean popupVisible = false; 
 
    public DropDownButton()
    { 
        mainButton.getModel().addChangeListener(this); 
        mainButton.getModel().addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {actionListener(ae);}});
        arrowButton.getModel().addChangeListener(this); 
        arrowButton.addActionListener(this); 
        arrowButton.setMargin(new Insets(0, 0, 3, 0)); 
        mainButton.addPropertyChangeListener("enabled", this);
    } 
 
    public void propertyChange(PropertyChangeEvent evt){ arrowButton.setEnabled(mainButton.isEnabled());} 
 
    public void stateChanged(ChangeEvent e)
    { 
        if(e.getSource()==mainButton.getModel())
        { 
            if(popupVisible && !mainButton.getModel().isRollover())
            { 
                mainButton.getModel().setRollover(true); 
                return; 
            } 
            
            arrowButton.getModel().setRollover(mainButton.getModel().isRollover()); 
            arrowButton.setSelected(mainButton.getModel().isArmed() && mainButton.getModel().isPressed()); 
        }
        else
        { 
            if(popupVisible && !arrowButton.getModel().isSelected())
            { 
                arrowButton.getModel().setSelected(true); 
                return; 
            } 
            mainButton.getModel().setRollover(arrowButton.getModel().isRollover()); 
        } 
    } 
 
    public void actionPerformed(ActionEvent ae)
    { 
         JPopupMenu popup = getPopupMenu(); 
         popup.addPopupMenuListener(this); 
         popup.show(mainButton, 0, mainButton.getHeight()); 
     } 
 
    public void popupMenuWillBecomeVisible(PopupMenuEvent e)
    { 
        popupVisible = true; 
        mainButton.getModel().setRollover(true); 
        arrowButton.getModel().setSelected(true); 
    } 
 
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
    { 
        popupVisible = false; 
 
        mainButton.getModel().setRollover(false); 
        arrowButton.getModel().setSelected(false); 
        ((JPopupMenu)e.getSource()).removePopupMenuListener(this);
    } 
 
    public void popupMenuCanceled(PopupMenuEvent e){ popupVisible = false;} 
 
   
    protected abstract JPopupMenu getPopupMenu(); 
    protected abstract void actionListener(ActionEvent ae);
    
    public JButton addToToolBar(JToolBar toolbar)
    { 
        toolbar.add(mainButton); 
        toolbar.add(arrowButton); 
        return mainButton; 
    } 
    
    public void setArrowToolTipText(String text){ arrowButton.setToolTipText(text);}
} 