package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.DataBrowserCallbacks;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.DataBrowserListViewColumnDesc;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class Table extends Composite {
	TableItem [] items;
	TableColumn [] columns;
	int itemCount, columnCount, idCount, anchorFirst, anchorLast, headerHeight;
	boolean ignoreSelect;
	static final int CHECK_COLUMN_ID = 1024;
	static final int COLUMN_ID = 1025;

public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
}

public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a table that does not have scroll bars.  Therefore,
	* no matter what style bits are specified, set the
	* H_SCROLL and V_SCROLL bits so that the SWT style
	* will match the widget that Windows creates.
	*/
	style |= SWT.H_SCROLL | SWT.V_SCROLL;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	//NOT DONE
	return new Point (100, 100);
}

void createHandle () {
	Display display = getDisplay ();
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateDataBrowserControl (window, null, OS.kDataBrowserListView, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	int selectionFlags = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserSelectOnlyOne : OS.kDataBrowserCmdTogglesSelection;
	OS.SetDataBrowserSelectionFlags (handle, selectionFlags);
	short [] height = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
	headerHeight = height [0];
	OS.SetDataBrowserListViewHeaderBtnHeight (handle, (short) 0);
	OS.SetDataBrowserHasScrollBars (handle, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0);
	//NOT DONE
	if ((style & SWT.H_SCROLL) == 0) OS.AutoSizeDataBrowserListViewColumns (handle);
	int position = 0;
	if ((style & SWT.CHECK) != 0) {
		DataBrowserListViewColumnDesc checkColumn = new DataBrowserListViewColumnDesc ();
		checkColumn.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
		checkColumn.propertyDesc_propertyID = CHECK_COLUMN_ID;
		checkColumn.propertyDesc_propertyType = OS.kDataBrowserCheckboxType;
		checkColumn.propertyDesc_propertyFlags = OS.kDataBrowserPropertyIsMutable;
		//NOT DONE
		checkColumn.headerBtnDesc_minimumWidth = 40;
		checkColumn.headerBtnDesc_maximumWidth = 40;
		checkColumn.headerBtnDesc_initialOrder = OS.kDataBrowserOrderIncreasing;
		OS.AddDataBrowserListViewColumn (handle, checkColumn, position++);
	}
	DataBrowserListViewColumnDesc column = new DataBrowserListViewColumnDesc ();
	column.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
	column.propertyDesc_propertyID = COLUMN_ID;
	column.propertyDesc_propertyType = OS.kDataBrowserTextType; // OS.kDataBrowserIconAndTextType
	column.propertyDesc_propertyFlags = OS.kDataBrowserListViewSelectionColumn | OS.kDataBrowserDefaultPropertyFlags;
	//NOT DONE
//	column.headerBtnDesc_minimumWidth = 80;
	column.headerBtnDesc_maximumWidth = 0x7FFF;
	column.headerBtnDesc_initialOrder = OS.kDataBrowserOrderIncreasing;
	OS.AddDataBrowserListViewColumn (handle, column, position);
	//NOT DONE
	OS.SetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, (short)300);
	OS.HIViewAddSubview (parent.handle, handle);
	OS.HIViewSetZOrder (handle, OS.kHIViewZOrderBelow, 0);
}

void createItem (TableColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	column.id = COLUMN_ID + idCount++;
	int position = index + ((style & SWT.CHECK) != 0 ? 1 : 0);
	if (columnCount != 0) {
		DataBrowserListViewColumnDesc desc = new DataBrowserListViewColumnDesc ();
		desc.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
		desc.propertyDesc_propertyID = column.id;
		desc.propertyDesc_propertyType = OS.kDataBrowserTextType; // OS.kDataBrowserIconAndTextType
		desc.propertyDesc_propertyFlags = OS.kDataBrowserDefaultPropertyFlags;
		//NOT DONE
//		desc.headerBtnDesc_minimumWidth = 80;
		desc.headerBtnDesc_maximumWidth = 0x7FFF;
		desc.headerBtnDesc_initialOrder = OS.kDataBrowserOrderIncreasing;
		OS.AddDataBrowserListViewColumn (handle, desc, position);
		//NOT DONE
		OS.SetDataBrowserTableViewNamedColumnWidth (handle, column.id, (short)0);
	} 
	if (columnCount == columns.length) {
		TableColumn [] newColumns = new TableColumn [columnCount + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	//NOT DONE - OPTIMIZE
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		for (int j=columnCount-1; j>index; --j) {
			item.setText (j, item.getText (j - 1));
			item.setImage (j, item.getImage (j - 1));
		}
		item.setText (index, "");
		item.setImage (index, null);
	}
}

void createItem (TableItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int [] id = new int [] {itemCount + 1};
	if (OS.AddDataBrowserItems (handle, OS.kDataBrowserNoItem, 1, id, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	if (itemCount == items.length) {
		TableItem [] newItems = new TableItem [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

void createWidget () {
	super.createWidget ();
	items = new TableItem [4];
	columns = new TableColumn [4];
}

public void deselect (int index) {
	checkWidget();
	ignoreSelect = true;
	int [] id = new int [] {index + 1};
	OS.SetDataBrowserSelectedItems (handle, id.length, id, OS.kDataBrowserItemsRemove);
	ignoreSelect = false;
}

public void deselect (int start, int end) {
	checkWidget();
	int length = end - start + 1;
	if (length <= 0) return;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = end - i + 1;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, length, ids, OS.kDataBrowserItemsRemove);
	ignoreSelect = false;
}

public void deselect (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = indices [length - i - 1] + 1;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, length, ids, OS.kDataBrowserItemsRemove);
	ignoreSelect = false;
}

public void deselectAll () {
	checkWidget ();
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, 0, null, OS.kDataBrowserItemsRemove);
	ignoreSelect = false;
}

void destroyItem (TableColumn column) {
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	//NOT DONE - OPTIMIZE
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		for (int j=index; j<columnCount-1; j++) {
			item.setText (j, item.getText (j + 1));
			item.setImage (j, item.getImage (j + 1));
		}
		if (columnCount > 1) {
			item.setText (columnCount - 1, "");
			item.setImage (columnCount - 1, null);
		}
	}
	if (columnCount > 1) {
		if (OS.RemoveDataBrowserTableViewColumn (handle, column.id) != OS.noErr) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
}

void destroyItem (TableItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	int [] id = new int [] {itemCount};
	if (OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, id.length, id, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

public TableColumn getColumn (int index) {
	checkWidget ();
	if (!(0 <=index && index < columnCount)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}

public int getColumnCount () {
	checkWidget ();
	return columnCount;
}

public TableColumn [] getColumns () {
	checkWidget ();
	TableColumn [] result = new TableColumn [columnCount];
	System.arraycopy (columns, 0, result, 0, columnCount);
	return result;
}

public int getGridLineWidth () {
	checkWidget ();
	return 0;
}

public int getHeaderHeight () {
	checkWidget ();
	short [] height = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
	return height [0];
}

public boolean getHeaderVisible () {
	checkWidget ();
	short [] height = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
	return height [0] != 0;
}

public TableItem getItem (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}

public TableItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	//NOT DONE - OPTIMIZE
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	OS.SetPt (pt, (short) point.x, (short) point.y);
	Rect rect = new Rect ();
	int [] id = new int [1];
	for (int i=0; i<itemCount; i++) {
		OS.GetDataBrowserItemPartBounds (handle, i, COLUMN_ID, OS.kDataBrowserPropertyEnclosingPart, rect);
		if (OS.PtInRect (pt, rect)) return items [i];
	}
	return null;
}

public int getItemCount () {
	checkWidget ();
	return itemCount;
}

public int getItemHeight () {
	checkWidget ();
	short [] height = new short [1];
	if (OS.GetDataBrowserTableViewRowHeight (handle, height) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	}
	return height [0];
}

public TableItem [] getItems () {
	checkWidget ();
	TableItem [] result = new TableItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

public boolean getLinesVisible () {
	checkWidget ();
	return false;
}

public TableItem [] getSelection () {
	checkWidget ();
	int ptr = OS.NewHandle (0);
	if (OS.GetDataBrowserItems (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, ptr) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	int count = OS.GetHandleSize (ptr) / 4;
	TableItem [] result = new TableItem [count];
	OS.HLock (ptr);
	int [] start = new int [1];
	OS.memcpy (start, ptr, 4);
	int [] id = new int [1];
	for (int i=0; i<count; i++) {
		OS.memcpy (id, start [0] + (i * 4), 4);
		result [i] = items [id [0] - 1];
	}
	OS.HUnlock (ptr);
	OS.DisposeHandle (ptr);
	return result;
}

public int getSelectionCount () {
	checkWidget ();
	int [] count = new int [1];
	if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, count) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_COUNT);
	}
	return count [0];
}

public int getSelectionIndex () {
	checkWidget();
	int [] first = new int [1], last = new int [1];
	OS.GetDataBrowserSelectionAnchor (handle, first, last);
	if (OS.GetDataBrowserTableViewItemID (handle, first [0], first) != OS.noErr) return -1;
    return first [0] - 1;
}

public int [] getSelectionIndices () {
	checkWidget ();
	int ptr = OS.NewHandle (0);
	if (OS.GetDataBrowserItems (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, ptr) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	int count = OS.GetHandleSize (ptr) / 4;
	int [] result = new int [count];
	OS.HLock (ptr);
	int [] start = new int [1];
	OS.memcpy (start, ptr, 4);
	int [] id = new int [1];
	for (int i=0; i<count; i++) {
		OS.memcpy (id, start [0] + (i * 4), 4);
		result [i] = id [0] - 1;
	}
	OS.HUnlock (ptr);
	OS.DisposeHandle (ptr);
	return result;
}

public int getTopIndex () {
	checkWidget();
    int[] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    return top [0] / getItemHeight ();
}

void hookEvents () {
	super.hookEvents ();
	Display display= getDisplay();
	DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
	callbacks.version = OS.kDataBrowserLatestCallbacks;
	OS.InitDataBrowserCallbacks (callbacks);
	callbacks.v1_itemDataCallback = display.itemDataProc;
	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
	OS.SetDataBrowserCallbacks (handle, callbacks);
}

public int indexOf (TableColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<columnCount; i++) {
		if (columns [i] == column) return i;
	}
	return -1;
}

public int indexOf (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

public boolean isSelected (int index) {
	checkWidget();
	return OS.IsDataBrowserItemSelected (handle, index + 1);
}

int itemDataProc (int browser, int id, int property, int itemData, int setValue) {
	int row = id - 1;
	if (!(0 <= row && row < items.length)) return OS.noErr;
	TableItem item = items [row];
	switch (property) {
		case CHECK_COLUMN_ID: {
			if (setValue != 0) {
				short [] theData = new short [1];
				OS.GetDataBrowserItemDataButtonValue (itemData, theData);
				item.checked = theData [0] == OS.kThemeButtonOn;
				Event event = new Event ();
				event.item = item;
				event.detail = SWT.CHECK;
				postEvent (SWT.Selection, event);
			} else {
				short theData = (short)(item.checked ? OS.kThemeButtonOn : OS.kThemeButtonOff);
				OS.SetDataBrowserItemDataButtonValue (itemData, theData);
			}
			break;
		}
	}
	if (property >= COLUMN_ID) {
		int column = 0;
		while (column < columnCount) {
			if (columns [column].id == property) break;
			column++;
		}
		String text = item.getText (column);
		char [] buffer = new char [text.length ()];
		text.getChars (0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
		OS.SetDataBrowserItemDataText (itemData, ptr);
		OS.CFRelease (ptr);
	}
	return OS.noErr;
}

int itemNotificationProc (int browser, int id, int message) {
	int index = id - 1;
	if (!(0 <= index && index < items.length)) return OS.noErr;
	TableItem item = items [index];
	switch (message) {
		case OS.kDataBrowserItemSelected:
		case OS.kDataBrowserItemDeselected: {
			if (ignoreSelect) break;
			int [] first = new int [1], last = new int [1];
			OS.GetDataBrowserSelectionAnchor (handle, first, last);
			boolean selected = false;
			if ((style & SWT.MULTI) != 0) {
				int modifiers = OS.GetCurrentEventKeyModifiers ();
				if ((modifiers & OS.shiftKey) != 0) {
					if (message == OS.kDataBrowserItemSelected) {
						selected = first [0] == id || last [0] == id;
					} else {
						selected = id == anchorFirst || id == anchorLast;
					}
				} else {
					if ((modifiers & OS.cmdKey) != 0) {
						selected = true;
					} else {
						selected = first [0] == last [0];
					}
				}
			} else {
				selected = message == OS.kDataBrowserItemSelected;
			}
			if (selected) {
				anchorFirst = first [0];
				anchorLast = last [0];
				Event event = new Event ();
				event.item = item;
				postEvent (SWT.Selection, event);
			}
			break;
		}	
		case OS.kDataBrowserItemDoubleClicked: {
			Event event = new Event ();
			event.item = item;
			postEvent (SWT.DefaultSelection, event);
			break;
		}
	}
	return OS.noErr;
}

void releaseWidget () {
	for (int i=0; i<columnCount; i++) {
		TableColumn column = columns [i];
		if (!column.isDisposed ()) column.releaseResources ();
	}
	columns = null;
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	items = null;
	super.releaseWidget ();
}

public void remove (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int [] id = new int [] {itemCount};
	if (OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, id.length, id, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	TableItem item = items [index];
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.releaseResources ();
	OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

public void remove (int start, int end) {
	checkWidget();
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int length = end - start + 1;
	for (int i=0; i<length; i++) remove (start);
}

public void remove (int [] indices) {
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int last = -1;
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last || i == 0) remove (index);
		last = index;
	}
}

public void removeAll () {
	checkWidget();
	OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, 0, null, 0);
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	items = new TableItem [4];
	itemCount = columnCount = idCount = anchorFirst = anchorLast = 0;
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void select (int index) {
	checkWidget();
	int [] id = new int [] {index + 1};
	ignoreSelect = true;
	int operation = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserItemsAssign: OS.kDataBrowserItemsAdd;
	OS.SetDataBrowserSelectedItems (handle, id.length, id, operation);
	ignoreSelect = false;
}

public void select (int start, int end) {
	checkWidget();
	int length = end - start + 1;
	if (length <= 0) return;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = end - i + 1;
	ignoreSelect = true;
	int operation = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserItemsAssign: OS.kDataBrowserItemsAdd;
	OS.SetDataBrowserSelectedItems (handle, length, ids, operation);
	ignoreSelect = false;
}

public void select (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = indices [length - i - 1] + 1;
	ignoreSelect = true;
	int operation = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserItemsAssign: OS.kDataBrowserItemsAdd;
	OS.SetDataBrowserSelectedItems (handle, ids.length, ids, operation);
	ignoreSelect = false;
}

public void selectAll () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, 0, null, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
}

public void setHeaderVisible (boolean show) {
	checkWidget ();
	int height = show ? headerHeight : 0;
	OS.SetDataBrowserListViewHeaderBtnHeight (handle, (short)height);
}

public void setLinesVisible (boolean show) {
	checkWidget ();
}

public void setSelection (int index) {
	checkWidget();
	int [] id = new int [] {index + 1};
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, id.length, id, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
	OS.RevealDataBrowserItem (handle, id [0], COLUMN_ID, (byte) OS.kDataBrowserRevealOnly);
}

public void setSelection (int start, int end) {
	checkWidget ();
	int length = end - start + 1;
	if (length <= 0) return;
	int count = length;
	int [] ids = new int [length];
	for (int i=start; i<=end; i++) {
		if (0 <= i && i < itemCount) ids [--count] = i + 1;
	}
	if (count != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
	OS.RevealDataBrowserItem (handle, ids [0], COLUMN_ID, (byte) OS.kDataBrowserRevealOnly);
}

public void setSelection (int [] indices) {
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	int count = length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) {
		int index = indices [i];
		if (0 <= index && index < itemCount) ids [--count] = index + 1;
	}
	if (count != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
	OS.RevealDataBrowserItem (handle, ids [0], COLUMN_ID, (byte) OS.kDataBrowserRevealOnly);
}

public void setSelection (TableItem [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = items.length;
	int count = length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) {
		int index = indexOf (items [i]);
		if (0 <= index && index < itemCount) ids [--count] = index + 1;
	}
	if (count != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
	OS.RevealDataBrowserItem (handle, ids [0], COLUMN_ID, (byte) OS.kDataBrowserRevealOnly);
}

public void setTopIndex (int index) {
	checkWidget();
    int [] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    top [0] = index * getItemHeight ();
    OS.SetDataBrowserScrollPosition (handle, top [0], left [0]);
}

public void showItem (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int index = indexOf (item);
	if (index != -1) {
		int [] id = new int [] {index + 1};
		OS.RevealDataBrowserItem (handle, id [0], COLUMN_ID, (byte) OS.kDataBrowserRevealOnly);
	}
}

public void showSelection () {
	checkWidget();
	int index = getSelectionIndex ();
	if (index >= 0) {
		int [] id = new int [] {index + 1};
		OS.RevealDataBrowserItem (handle, id [0], COLUMN_ID, (byte) OS.kDataBrowserRevealOnly);
	}
}

}
