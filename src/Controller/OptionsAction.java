/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.Options;
import View.hmi;
import View.hmi;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author JunXcola
 */
public class OptionsAction extends AbstractAction
{
    public static hmi window;
    private Options OpWindow;
    
    
    public OptionsAction(hmi window, String text)
    {
        super(text);

        OptionsAction.window = window;
    }

    public void actionPerformed(ActionEvent ae) 
    {
        OpWindow = new Options(OptionsAction.window);
        OpWindow.setVisible(true);
    }
    
}
