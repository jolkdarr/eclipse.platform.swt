/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSButtonCell extends NSActionCell {

public NSButtonCell() {
	super();
}

public NSButtonCell(long /*int*/ id) {
	super(id);
}

public NSButtonCell(id id) {
	super(id);
}

public NSColor backgroundColor() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public void drawBezelWithFrame(NSRect frame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawBezelWithFrame_inView_, frame, controlView != null ? controlView.id : 0);
}

public void drawImage(NSImage image, NSRect frame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawImage_withFrame_inView_, image != null ? image.id : 0, frame, controlView != null ? controlView.id : 0);
}

public NSRect drawTitle(NSAttributedString title, NSRect frame, NSView controlView) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_drawTitle_withFrame_inView_, title != null ? title.id : 0, frame, controlView != null ? controlView.id : 0);
	return result;
}

public void setBackgroundColor(NSColor backgroundColor) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, backgroundColor != null ? backgroundColor.id : 0);
}

public void setButtonType(long /*int*/ aType) {
	OS.objc_msgSend(this.id, OS.sel_setButtonType_, aType);
}

public void setHighlightsBy(long /*int*/ highlightsBy) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightsBy_, highlightsBy);
}

public void setImagePosition(long /*int*/ imagePosition) {
	OS.objc_msgSend(this.id, OS.sel_setImagePosition_, imagePosition);
}

public NSString title() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}
