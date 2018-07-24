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

public class MENUITEMINFO {
	public int cbSize;
	public int fMask;
	public int fType;
	public int fState;
	public int wID;
	/** @field cast=(HMENU) */
	public long /*int*/ hSubMenu;
	/** @field cast=(HBITMAP) */
	public long /*int*/ hbmpChecked;
	/** @field cast=(HBITMAP) */
	public long /*int*/ hbmpUnchecked;
	public long /*int*/ dwItemData;
	/** @field cast=(LPTSTR) */
	public long /*int*/ dwTypeData;
	public int cch;
	/** @field cast=(HBITMAP) */
	public long /*int*/ hbmpItem;
	public static final int sizeof = OS.MENUITEMINFO_sizeof ();
}
