/*
 * AutoComplete.java
 *
 * Copyright: 2013-2014 Karell Bertet, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices-view, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

package View;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Sylvain MORIN
 */
public class AutoComplete extends PlainDocument {
 
	private final TreeSet<String> dictionary = new TreeSet<String>();
 
	private final JTextComponent _textField;
 
	public AutoComplete(JTextComponent field, String[] aDictionary) {
		_textField = field;
		dictionary.addAll(Arrays.asList(aDictionary));
	}
 
	public void addDictionaryEntry(String item) {
		dictionary.add(item);
	}
 
	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		super.insertString(offs, str, a);
		String word = autoComplete(getText(0, getLength()));
		if (word != null) {
			super.insertString(offs + str.length(), word, a);
			_textField.setCaretPosition(offs + str.length());
			_textField.moveCaretPosition(getLength());
			// _textField.setCaretPosition(getLength());
			// _textField.moveCaretPosition(offs + str.length());
		}
	}
 
	public String autoComplete(String text) {
		for (Iterator<String> i = dictionary.iterator(); i.hasNext();) {
			String word = i.next();
			if (word.startsWith(text)) {
				return word.substring(text.length());
			}
		}
		return null;
	}
}