/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class LVITEM {
	public int mask;
	public int iItem;
	public int iSubItem;
	public int state;
	public int stateMask;
	/** @field cast=(LPTSTR) */
	public long /*int*/ pszText;
	public int cchTextMax;
	public int iImage;
	public long /*int*/ lParam;
	public int iIndent;
	public int iGroupId;
	public int cColumns;
	/** @field cast=(PUINT) */
	public long /*int*/ puColumns;
	public static final int sizeof = OS.LVITEM_sizeof ();
}
