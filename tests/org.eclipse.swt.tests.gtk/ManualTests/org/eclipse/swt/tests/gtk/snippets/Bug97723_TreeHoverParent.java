/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class Bug97723_TreeHoverParent {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setBounds(10,10,200,200);
		final Tree tree = new Tree(shell, SWT.NONE);
		tree.setBounds(10,10,150,150);
		new TreeColumn(tree, SWT.NONE).setWidth(100);
		new TreeColumn(tree, SWT.NONE).setWidth(100);
		shell.open();
		shell.addListener(SWT.MouseHover, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("hover");
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
