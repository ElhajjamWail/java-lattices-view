/*
 * LatticesView.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

import View.hmi;

/**
 * The LatticesView class is the starting class of the application.
 */
public final class LatticesView {
    /**
     * This class is not designed to be instantiated.
     */
    private LatticesView() {
    }

    /**
     * @param   args  the command line arguments
     */
    public static void main(String[] args) {
        hmi fenetre = new hmi();
        fenetre.setVisible(true);
    }
}
