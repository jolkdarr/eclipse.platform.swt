/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * Instances of this class represent icons that can be placed on the
 * system tray or task bar status area.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, MenuDetect, Selection</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tray">Tray, TrayItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.0
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TrayItem extends Item {
	Tray parent;
	ToolTip toolTip;
	String toolTipText;
	long /*int*/ imageHandle;
	long /*int*/ tooltipsHandle;
	ImageList imageList;
	Image highlightImage;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tray</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TrayItem (Tray parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (parent.getItemCount ());
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the platform-specific context menu trigger
 * has occurred, by sending it one of the messages defined in
 * the <code>MenuDetectListener</code> interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see MenuDetectListener
 * @see #removeMenuDetectListener
 *
 * @since 3.3
 */
public void addMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MenuDetect, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected
 * <code>widgetDefaultSelected</code> is called when the receiver is double-clicked
 * </p>
 *
 * @param listener the listener which should be notified when the receiver is selected by the user
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	parent.createItem (this, index);
}

@Override
void createHandle (int index) {
	state |= HANDLE;
	handle = GTK.gtk_status_icon_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	imageHandle = GTK.gtk_image_new ();
	GTK.gtk_status_icon_set_visible (handle,true);
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (imageHandle);
}

@Override
void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the receiver's parent, which must be a <code>Tray</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public Tray getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's highlight image if it has one, or null
 * if it does not.
 *
 * @return the receiver's highlight image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public Image getHighlightImage () {
	checkWidget ();
	return highlightImage;
}

/**
 * Returns the receiver's tool tip, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public ToolTip getToolTip () {
	checkWidget ();
	return toolTip;
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}

@Override
long /*int*/ gtk_activate (long /*int*/ widget) {
	sendSelectionEvent (SWT.Selection);
	/*
	* Feature in GTK. GTK will generate a single-click event before sending
	* a double-click event. To know when to send a DefaultSelection, look for
	* the single-click as the current event and for the double-click in the
	* event queue.
	*/
	long /*int*/ nextEvent = GDK.gdk_event_peek ();
	if (nextEvent != 0) {
		int nextEventType = GDK.GDK_EVENT_TYPE (nextEvent);
		long /*int*/ currEvent = GTK.gtk_get_current_event ();
		int currEventType = 0;
		if (currEvent != 0) {
			currEventType = GDK.GDK_EVENT_TYPE (currEvent);
			GDK.gdk_event_free (currEvent);
		}
		GDK.gdk_event_free (nextEvent);
		if (currEventType == GDK.GDK_BUTTON_PRESS && nextEventType == GDK.GDK_2BUTTON_PRESS) {
			sendSelectionEvent (SWT.DefaultSelection);
		}
	}
	return 0;
}

@Override
long /*int*/ gtk_button_press_event (long /*int*/ widget, long /*int*/ eventPtr) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, eventPtr, GdkEventButton.sizeof);
	if (gdkEvent.type == GDK.GDK_3BUTTON_PRESS) return 0;
	if (gdkEvent.button == 3 && gdkEvent.type == GDK.GDK_BUTTON_PRESS) {
		sendEvent (SWT.MenuDetect);
		return 0;
	}
	if (gdkEvent.type == GDK.GDK_2BUTTON_PRESS) {
		sendSelectionEvent (SWT.DefaultSelection);
	} else {
		sendSelectionEvent (SWT.Selection);
	}
	return 0;
}

@Override
long /*int*/ gtk_size_allocate (long /*int*/ widget, long /*int*/ allocation) {
	if (image != null && image.mask != 0) {
		if (GDK.gdk_drawable_get_depth (image.mask) == 1) {
			GtkAllocation widgetAllocation = new GtkAllocation ();
			GTK.gtk_widget_get_allocation (widget, widgetAllocation);
			int xoffset = (int) Math.floor (widgetAllocation.x + ((widgetAllocation.width -GTK.GTK_WIDGET_REQUISITION_WIDTH (widget)) * 0.5) + 0.5);
			int yoffset = (int) Math.floor (widgetAllocation.y + ((widgetAllocation.height - GTK.GTK_WIDGET_REQUISITION_HEIGHT (widget)) * 0.5) + 0.5);
			Rectangle b = image.getBoundsInPixels();
			long /*int*/ gdkImagePtr = GDK.gdk_drawable_get_image (image.mask, 0, 0, b.width, b.height);
			if (gdkImagePtr == 0) error(SWT.ERROR_NO_HANDLES);
			GdkImage gdkImage = new GdkImage();
			OS.memmove (gdkImage, gdkImagePtr);
			byte[] maskData = new byte [gdkImage.bpl * gdkImage.height];
			C.memmove (maskData, gdkImage.mem, maskData.length);
			OS.g_object_unref (gdkImagePtr);
			Region region = new Region (display);
			for (int y = 0; y < b.height; y++) {
				for (int x = 0; x < b.width; x++) {
					int index = (y * gdkImage.bpl) + (x >> 3);
					int theByte = maskData [index] & 0xFF;
					int mask = 1 << (x & 0x7);
					if ((theByte & mask) != 0) {
						Rectangle rect = DPIUtil.autoScaleDown(new Rectangle(xoffset + x, yoffset + y, 1, 1));
						region.add(rect.x, rect.y, rect.width, rect.height);
					}
				}
			}
			GTK.gtk_widget_realize (handle);
			long /*int*/ window = gtk_widget_get_window (handle);
			GDK.gdk_window_shape_combine_region (window, region.handle, 0, 0);
			region.dispose ();
		}
	}
	return 0;
}

@Override
long /*int*/ gtk_status_icon_popup_menu (long /*int*/ widget, long /*int*/ button, long /*int*/ activate_time) {
	/*
	* GTK provides a MenuPositionFunc for GtkStatusIcon in order
	* to set the popup-menu aligned to the tray.
	*/
	Display display = this.display;
	display.currentTrayItem = this;
	sendEvent (SWT.MenuDetect);
	if (!isDisposed ()) display.runPopups();
	display.currentTrayItem = null;
	return 0;
}

@Override
void hookEvents () {
	OS.g_signal_connect_closure (handle, OS.activate, display.getClosure (ACTIVATE), false);
	OS.g_signal_connect_closure (handle, OS.popup_menu, display.getClosure (STATUS_ICON_POPUP_MENU), false);
}

/**
 * Returns <code>true</code> if the receiver is visible and
 * <code>false</code> otherwise.
 *
 * @return the receiver's visibility
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
	return GTK.gtk_status_icon_get_visible (handle);
}

@Override
void register () {
	super.register ();
	display.addWidget (imageHandle, this);
}

@Override
void releaseHandle () {
	if (handle != 0) {
		OS.g_object_unref (handle);
	}
	handle = imageHandle = 0;
	super.releaseHandle ();
	parent = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (tooltipsHandle != 0) OS.g_object_unref (tooltipsHandle);
	tooltipsHandle = 0;
	if (imageList != null) imageList.dispose ();
	imageList = null;
	toolTipText = null;
	highlightImage = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the platform-specific context menu trigger has
 * occurred.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see MenuDetectListener
 * @see #addMenuDetectListener
 *
 * @since 3.3
 */
public void removeMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MenuDetect, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver is selected by the user.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);
}

/**
 * Sets the receiver's highlight image.
 *
 * @param image the new highlight image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public void setHighlightImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	highlightImage = image;
}

/**
 * Sets the receiver's image.
 *
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.image = image;
	if (image != null) {
		if (imageList == null) imageList = new ImageList ();
		int imageIndex = imageList.indexOf (image);
		if (imageIndex == -1) {
			imageIndex = imageList.add (image);
		} else {
			imageList.put (imageIndex, image);
		}
		long /*int*/ pixbuf = imageList.getPixbuf (imageIndex);
		GTK.gtk_status_icon_set_from_pixbuf (handle, pixbuf);
		GTK.gtk_status_icon_set_visible (handle, true);
	} else {
		GTK.gtk_widget_set_size_request (handle, 1, 1);
		GTK.gtk_status_icon_set_from_pixbuf (handle, 0);
		GTK.gtk_status_icon_set_visible (handle, false);
	}
}

/**
 * Sets the receiver's tool tip to the argument, which
 * may be null indicating that no tool tip should be shown.
 *
 * @param toolTip the new tool tip (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setToolTip (ToolTip toolTip) {
	checkWidget ();
	ToolTip oldTip = this.toolTip, newTip = toolTip;
	if (oldTip != null) oldTip.item = null;
	this.toolTip = newTip;
	if (newTip != null) newTip.item = this;
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be
 * escaped by doubling it in the string.
 * </p>
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&C)" at the end of the tooltip text
 * are not shown in tooltip.
 * </p>
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget ();
	toolTipText = string;
	byte [] buffer = null;
	if (string != null && string.length () > 0) {
		buffer = Converter.wcsToMbcs (string, true);
	}
	GTK.gtk_status_icon_set_tooltip_text (handle, buffer);
}

/**
 * Makes the receiver visible if the argument is <code>true</code>,
 * and makes it invisible otherwise.
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget ();
	if(GTK.gtk_status_icon_get_visible (handle) == visible) return;
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		GTK.gtk_status_icon_set_visible (handle, visible);
	} else {
		GTK.gtk_status_icon_set_visible (handle, visible);
		sendEvent (SWT.Hide);
	}
}
}