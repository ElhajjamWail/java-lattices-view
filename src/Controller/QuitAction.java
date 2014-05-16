/*
 * QuitAction.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
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
