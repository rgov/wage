package info.svitkine.alexei.wage;

import java.awt.Font;

import lombok.Getter;


public class MenuItem {
	public static final int BOLD = 1;
	public static final int ITALIC = 2;
	public static final int UNDERLINE = 4;
	public static final int OUTLINE = 8;
	public static final int SHADOW = 16;
	public static final int CONDENSED = 32;
	public static final int EXTENDED = 64;
	
	@Getter private String text;
	@Getter private int style;
	@Getter private char shortcut;
	@Getter private boolean enabled;

	public MenuItem(String text, int style, char shortcut, boolean enabled) {
		this.text = text;
		this.style = style;
		this.shortcut = shortcut;
		this.enabled = enabled;
	}
	
	public MenuItem(String text, int style, char shortcut) {
		this(text, style, shortcut, true);
	}
	
	public MenuItem(String text, int style) {
		this(text, style, (char) 0);
	}
	
	public MenuItem(String text) {
		this(text, 0, (char) 0);
	}
	
	public boolean hasShortcut() {
		return shortcut != 0;
	}
	
	public void performAction() {
	}

	@Deprecated
	public int getFontStyle() {
		int fontStyle = 0;
		if ((style & BOLD) != 0)
			fontStyle |= Font.BOLD;
		if ((style & ITALIC) != 0)
			fontStyle |= Font.ITALIC;
		return fontStyle;
	}
}
