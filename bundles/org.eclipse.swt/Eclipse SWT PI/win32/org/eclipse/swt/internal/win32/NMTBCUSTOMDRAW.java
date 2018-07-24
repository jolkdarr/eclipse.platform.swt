/*******************************************************************************
 * Copyright (c) 2017, 2017 Conrad Groth and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Conrad Groth - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMTBCUSTOMDRAW extends NMCUSTOMDRAW {
	public NMCUSTOMDRAW nmcd;
	/** @field cast=(HBRUSH) */
	public long /*int*/ hbrMonoDither;
	/** @field cast=(HBRUSH) */
	public long /*int*/ hbrLines;
	/** @field cast=(HPEN) */
	public long /*int*/ hpenLines;
	public int clrText;
	public int clrMark;
	public int clrTextHighlight;
	public int clrBtnFace;
	public int clrBtnHighlight;
	public int clrHighlightHotTrack;
	// RECT rcText;
	/** @field accessor=rcText.left */
	public int rcText_left;
	/** @field accessor=rcText.top */
	public int rcText_top;
	/** @field accessor=rcText.right */
	public int rcText_right;
	/** @field accessor=rcText.bottom */
	public int rcText_bottom;
	public int nStringBkMode;
	public int nHLStringBkMode;
	public int iListGap;
	public static final int sizeof = OS.NMTBCUSTOMDRAW_sizeof ();
}
