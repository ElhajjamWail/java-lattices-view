/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author smameri
 */
public class QuitAction extends AbstractAction {
    public QuitAction(String text){
		super(text);
	}

	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
