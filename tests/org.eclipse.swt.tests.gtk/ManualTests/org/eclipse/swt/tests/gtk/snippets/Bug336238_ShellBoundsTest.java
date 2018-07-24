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


//import static org.junit.Assert.*;

import org.eclipse.swt.widgets.Shell;
//import org.junit.Test;

public class Bug336238_ShellBoundsTest {

  static int cycles = 5;
 	
//  @Test
  public void testSetBounds() {
		
    int x;
    int y;
    int width = 100;
    int height = 100;
		
    for (int i=0; i < cycles; i++) {
			
      x = (new Double(Math.random()*1000)).intValue();
      y = (new Double(Math.random()*1000)).intValue();
			
      Shell testShell = new Shell();
      testShell.open();
			
      testShell.setBounds(x, y, width, height);
			
//      assertEquals(x, testShell.getLocation().x);
//      assertEquals(y, testShell.getLocation().y);
      testShell.close();
    }		
  }	
}