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

public class NSDatePicker extends NSControl {

public NSDatePicker() {
	super();
}

public NSDatePicker(long /*int*/ id) {
	super(id);
}

public NSDatePicker(id id) {
	super(id);
}

public NSDate dateValue() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_dateValue);
	return result != 0 ? new NSDate(result) : null;
}

public void setBackgroundColor(NSColor backgroundColor) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, backgroundColor != null ? backgroundColor.id : 0);
}

public void setBezeled(boolean bezeled) {
	OS.objc_msgSend(this.id, OS.sel_setBezeled_, bezeled);
}

public void setBordered(boolean bordered) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_, bordered);
}

public void setDatePickerElements(long /*int*/ datePickerElements) {
	OS.objc_msgSend(this.id, OS.sel_setDatePickerElements_, datePickerElements);
}

public void setDatePickerStyle(long /*int*/ datePickerStyle) {
	OS.objc_msgSend(this.id, OS.sel_setDatePickerStyle_, datePickerStyle);
}

public void setDateValue(NSDate dateValue) {
	OS.objc_msgSend(this.id, OS.sel_setDateValue_, dateValue != null ? dateValue.id : 0);
}

public void setDrawsBackground(boolean drawsBackground) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, drawsBackground);
}

public void setTextColor(NSColor textColor) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_, textColor != null ? textColor.id : 0);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSDatePicker, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSDatePicker, OS.sel_setCellClass_, factoryId);
}

}
