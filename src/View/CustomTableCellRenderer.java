/*
 * CustomTableCellRenderer.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */


package View;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

 public class CustomTableCellRenderer extends DefaultTableCellRenderer
 {
    @Override
      public Component getTableCellRendererComponent (JTable table,
    Object obj, boolean isSelected, boolean hasFocus, int row, int column)
      {
          Component cell = super.getTableCellRendererComponent(
           table, obj, isSelected, hasFocus, row, column);
          if (isSelected) {
            cell.setBackground(Color.green);
          }
          else {
          if (row % 2 == 0) {
            cell.setBackground(Color.cyan);
          }
          else {
            cell.setBackground(Color.lightGray);
          }
          }
          return cell;
      }
  }

