package info.svitkine.alexei.wage;

import java.awt.Rectangle;
import lombok.Getter;
import lombok.Setter;


public class SceneImpl implements Scene {

	@Getter @Setter private int index;
	@Getter @Setter private String name;
	@Getter @Setter private short resourceID;
	@Getter @Setter private Script script;
	@Getter @Setter private Design design;
	@Getter @Setter private String text;
	@Getter @Setter private int fontSize;
	@Getter @Setter private int fontType; // 3 => Geneva, 22 => Courier, param to TextFont() function
	@Getter @Setter private int soundFrequency; // times a minute, max 3600
	@Getter @Setter private int soundType;
	@Getter @Setter private String soundName;
	@Getter @Setter private int worldX;
	@Getter @Setter private int worldY;
	private Rectangle designBounds = new Rectangle();
    private Rectangle textBounds = new Rectangle();
    private boolean[] blocked = new boolean[4];
	private String[] messages = new String[4];

	@Setter private State state;

	public State getState() {
		if (state == null)
			state = new State(this);
		return state;
	}

	public Rectangle getDesignBounds() {
		return designBounds == null ? null : new Rectangle(designBounds);
	}

	public void setDesignBounds(Rectangle bounds) {
		this.designBounds = new Rectangle(bounds);
	}

	public Rectangle getTextBounds() {
		return textBounds == null ? null : new Rectangle(textBounds);
	}

	public void setTextBounds(Rectangle bounds) {
		this.textBounds = new Rectangle(bounds);
	}
	
	public void setDirMessage(int dir, String message) {
		messages[dir] = message;
	}
	
	public String getDirMessage(int dir) {
		return messages[dir];
	}

	public void setDirBlocked(int dir, boolean blocked) {
		this.blocked[dir] = blocked;
	}
	
	public boolean isDirBlocked(int dir) {
		return blocked[dir];
	}

	public String toString() {
		return getName();
	}

	public String getFontName() {
		return FontNames.getFontName(fontType);
	}
}
