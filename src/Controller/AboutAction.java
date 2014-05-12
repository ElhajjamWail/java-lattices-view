/*
 * AboutAction.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package Controller;

import View.hmi;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
 
/**
 *
 * @author smameri
 */
public class AboutAction extends AbstractAction {
private hmi window;

/** Path of the project/jar directory */
    String projectPath = hmi.projectPath;

	public AboutAction(hmi window, String text){
		super(text);

		this.window = window;
	}

	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(window,"Lattice View is a HMI based on the lattice librairy \n"
                        + " developped at the L3i labs. of the University of La Rochelle \n"
                        + " This app. is under the terms of the GNU Lesser General Public\n"
                        + " License as published by the Free Software Foundation", "About Lattice View", 1, new ImageIcon(projectPath+"\\icons\\logo_l3i.png"));
	}
}
