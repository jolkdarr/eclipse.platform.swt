package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import java.util.Locale;
 
/**
 * Instances of this class describe operating system fonts.
 * Only the public API of this type is platform independent.
 * <p>
 * For platform-independent behaviour, use the get and set methods
 * corresponding to the following properties:
 * <dl>
 * <dt>height</dt><dd>the height of the font in points</dd>
 * <dt>name</dt><dd>the face name of the font, which may include the foundry</dd>
 * <dt>style</dt><dd>A bitwise combination of NORMAL, ITALIC and BOLD</dd>
 * </dl>
 * If extra, platform-dependent functionality is required:
 * <ul>
 * <li>On <em>Windows</em>, the data member of the <code>FontData</code>
 * corresponds to a Windows <code>LOGFONT</code> structure whose fields
 * may be retrieved and modified.</li>
 * <li>On <em>X</em>, the fields of the <code>FontData</code> correspond
 * to the entries in the font's XLFD name and may be retrieved and modified.
 * </ul>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 *
 * @see Font
 */
public final class FontData {
	/**
	 * The company that produced the font
	 * Warning: This field is platform dependent.
	 */
	public String foundry;
	/**
	 * The common name of the font
	 * Warning: This field is platform dependent.
	 */
	public String fontFamily;
	/**
	 * The weight ("normal", "bold")
	 * Warning: This field is platform dependent.
	 */
	public String weight;
	/**
	 * The slant ("o" for oblique, "i" for italic)
	 * Warning: This field is platform dependent.
	 */
	public String slant;
	/**
	 * The set width of the font
	 * Warning: This field is platform dependent.
	 */
	public String setWidth;
	/**
	 * Additional font styles
	 * Warning: This field is platform dependent.
	 */
	public String addStyle;
	/**
	 * The height of the font in pixels
	 * Warning: This field is platform dependent.
	 */
	public int pixels;
	/**
	 * The height of the font in tenths of a point
	 * Warning: This field is platform dependent.
	 */
	public int points;
	/**
	 * The horizontal screen resolution for which the font was designed
	 * Warning: This field is platform dependent.
	 */
	public int horizontalResolution;
	/**
	 * The vertical screen resolution for which the font was designed
	 * Warning: This field is platform dependent.
	 */
	public int verticalResolution;
	/**
	 * The font spacing ("m" for monospace, "p" for proportional)
	 * Warning: This field is platform dependent.
	 */
	public String spacing;
	/**
	 * The average character width for the font
	 * Warning: This field is platform dependent.
	 */
	public int averageWidth;
	/**
	 * The ISO character set registry
	 * Warning: This field is platform dependent.
	 */
	public String characterSetRegistry;
	/**
	 * The ISO character set name
	 * Warning: This field is platform dependent.
	 */
	public String characterSetName;

	/**
	 * The locale of the font
	 * (Warning: This field is platform dependent)
	 */
	Locale locale;
/**	 
 * Constructs a new un-initialized font data.
 */
public FontData () {
}
/**
 * Constructs a new FontData given a string representation
 * in the form generated by the <code>FontData.toString</code>
 * method.
 * <p>
 * Note that the representation varies between platforms,
 * and a FontData can only be created from a string that was 
 * generated on the same platform.
 * </p>
 *
 * @param string the string representation of a <code>FontData</code> (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument does not represent a valid description</li>
 * </ul>
 *
 * @see #toString
 */
public FontData(String string) {
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int start = 0;
	int end = string.indexOf('|');
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	String version1 = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	String name = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int height = 0;
	try {
		height = Integer.parseInt(string.substring(start, end));
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int style = 0;
	try {
		style = Integer.parseInt(string.substring(start, end));
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}

	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) {
		setName(name);
		setHeight(height);
		setStyle(style);
		return;
	}
	String platform = string.substring(start, end);

	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) {
		setName(name);
		setHeight(height);
		setStyle(style);
		return;
	}
	String version2 = string.substring(start, end);

	if (platform.equals("MOTIF") && version2.equals("1")) {
		start = end + 1;
		end = string.length();
		if (end == -1) {
			setName(name);
			setHeight(height);
			setStyle(style);
			return;
		}
		String xlfd = string.substring(start, end);
		setXlfd(xlfd);
		return;
	}
	setName(name);
	setHeight(height);
	setStyle(style);
}
/**	 
 * Constructs a new font data given a font name,
 * the height of the desired font in points, 
 * and a font style.
 *
 * @param name the name of the font (must not be null)
 * @param height the font height in points
 * @param style a bit or combination of NORMAL, BOLD, ITALIC
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 */
public FontData (String name, int height, int style) {
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (name != null) {
		int dash = name.indexOf('-');
		if (dash != -1) {
			foundry = name.substring(0, dash);
			fontFamily = name.substring(dash + 1);
		} else {
			fontFamily = name;
		}
	}
	points = height * 10;
	if ((style & SWT.BOLD) != 0)
		weight = "bold";
	else
		weight = "medium";
	if ((style & SWT.ITALIC) != 0)
		slant = "i";
	else
		slant = "r";
}
/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
public boolean equals (Object object) {
	return (object == this) || ((object instanceof FontData) && 
		getXlfd().equals(((FontData)object).getXlfd()));
}
/**
 * Returns the height of the receiver in points.
 *
 * @return the height of this FontData
 *
 * @see #setHeight
 */
public int getHeight() {
	return points / 10;
}
/**
 * Returns the name of the receiver.
 * On platforms that support font foundries, the return value will
 * be the foundry followed by a dash ("-") followed by the face name.
 *
 * @return the name of this <code>FontData</code>
 *
 * @see #setName
 */
public String getName() {
	StringBuffer buffer = new StringBuffer();
	if (foundry != null) {
		buffer.append(foundry);
		buffer.append("-");
	}
	if (fontFamily != null) buffer.append(fontFamily);
	return buffer.toString();
}
/**
 * Returns the style of the receiver which is a bitwise OR of 
 * one or more of the <code>SWT</code> constants NORMAL, BOLD
 * and ITALIC.
 *
 * @return the style of this <code>FontData</code>
 * 
 * @see #setStyle
 */
public int getStyle() {
	int style = 0;
	if (weight.equals("bold"))
		style |= SWT.BOLD;
	if (slant.equals("i"))
		style |= SWT.ITALIC;
	return style;
}
String getXlfd() {
	String s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14;
	s1 = s2 = s3 = s4 = s5 = s6 = s7 = s8 = s9 = s10 = s11 = s12 = s13 = s14 = "*";
	
	if (foundry != null) s1 = foundry;
	if (fontFamily != null) s2 = fontFamily;
	if (weight != null) s3 = weight;
	if (slant != null) s4 = slant;
	if (setWidth != null) s5 = setWidth;
	if (addStyle != null) s6 = addStyle;
	if (pixels != 0) s7 = Integer.toString(pixels);
	if (points != 0) s8 = Integer.toString(points);
	if (horizontalResolution != 0) s9 = Integer.toString(horizontalResolution);
	if (verticalResolution != 0) s10 = Integer.toString(verticalResolution);
	if (spacing != null) s11 = spacing;
	if (averageWidth != 0) s12 = Integer.toString(averageWidth);
	if (characterSetRegistry != null) s13 = characterSetRegistry;
	if (characterSetName != null) s14 = characterSetName;

	return "-" + s1+ "-" + s2 + "-" + s3 + "-" + s4 + "-" + s5 + "-" + s6 + "-" + s7 + "-" + s8 + "-" 
		+ s9 + "-" + s10 + "-" + s11 + "-" + s12 + "-" + s13 + "-" + s14;
}
/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode () {
	return getXlfd().hashCode();
}
public static FontData motif_new(String xlfd) {
	FontData fontData = new FontData();
	fontData.setXlfd(xlfd);
	return fontData;
}
/**
 * Sets the height of the receiver. The parameter is
 * specified in terms of points, where a point is one
 * seventy-second of an inch.
 *
 * @param height the height of the <code>FontData</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 * 
 * @see #getHeight
 */
public void setHeight(int height) {
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	points = height * 10;
}
/**
 * Sets the name of the receiver.
 * <p>
 * Some platforms support font foundries. On these platforms, the name
 * of the font specified in setName() may have one of the following forms:
 * <ol>
 * <li>a face name (for example, "courier")</li>
 * <li>a foundry followed by a dash ("-") followed by a face name (for example, "adobe-courier")</li>
 * </ol>
 * In either case, the name returned from getName() will include the
 * foundry.
 * </p>
 * <p>
 * On platforms that do not support font foundries, only the face name
 * (for example, "courier") is used in <code>setName()</code> and 
 * <code>getName()</code>.
 * </p>
 *
 * @param name the name of the font data
 *
 * @see #getName
 */
public void setName(String name) {
	if (name == null) {
		foundry = fontFamily = null;
		return;
	}
	int dash = name.indexOf('-');
	if (dash != -1) {
		foundry = name.substring(0, dash);
		fontFamily = name.substring(dash + 1);
	} else {
		fontFamily = name;
	}
}
/**
 * Sets the locale of the receiver.
 * <p>
 * The locale determines which platform character set this
 * font is going to use. Widgets and graphics operations that
 * use this font will convert UNICODE strings to the platform
 * character set of the specified locale.
 * </p>
 * <p>
 * On platforms which there are multiple character sets for a
 * given language/country locale, the variant portion of the
 * locale will determine the character set.
 * </p>
 * 
 * @param locale the Locale of the <code>FontData</code>
 */
public void setLocale(Locale locale) {
	this.locale = locale;
}
/**
 * Sets the style of the receiver to the argument which must
 * be a bitwise OR of one or more of the <code>SWT</code> 
 * constants NORMAL, BOLD and ITALIC.
 *
 * @param style the new style for this <code>FontData</code>
 *
 * @see #getStyle
 */
public void setStyle(int style) {
	if ((style & SWT.BOLD) == SWT.BOLD)
		weight = "bold";
	else
		weight = "normal";
	if ((style & SWT.ITALIC) == SWT.ITALIC)
		slant = "i";
	else
		slant = "r";
}
void setXlfd(String xlfd) {
	int start, stop;
	start = 1;
	stop = xlfd.indexOf ("-", start);
	foundry = xlfd.substring(start, stop);
	if (foundry.equals("*")) foundry = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	fontFamily = xlfd.substring(start, stop);
	if (fontFamily.equals("*")) fontFamily = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
 	weight = xlfd.substring(start, stop);
 	if (weight.equals("*")) weight = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	slant = xlfd.substring(start, stop);
	if (slant.equals("*")) slant = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	setWidth = xlfd.substring(start, stop);
	if (setWidth.equals("*")) setWidth = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	addStyle = xlfd.substring(start, stop);
	if (addStyle.equals("*")) addStyle = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	String s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		pixels = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		points = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		horizontalResolution = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		verticalResolution = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	spacing  = xlfd.substring(start, stop);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		averageWidth = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
 	characterSetRegistry = xlfd.substring(start, stop);
 	if (characterSetRegistry.equals("*")) characterSetRegistry = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
 	characterSetName = xlfd.substring(start);
 	if (characterSetName.equals("*")) characterSetName = null;
}
/**
 * Returns a string representation of the receiver which is suitable
 * for constructing an equivalent instance using the 
 * <code>FontData(String)</code> constructor.
 *
 * @return a string representation of the FontData
 *
 * @see FontData
 */
public String toString() {
	return "1|" + fontFamily + "|" + getHeight() + "|" + getStyle() + "|" +
		"MOTIF|1|" + getXlfd();
}
}
