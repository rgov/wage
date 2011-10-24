package info.svitkine.alexei.wage;

import java.awt.Rectangle;

import lombok.Getter;
import lombok.Setter;


public class ObjImpl implements Obj {
	@Getter @Setter private int index;
	@Getter @Setter private String name;
	@Getter @Setter private short resourceID;
	@Getter @Setter private boolean namePlural;
	@Getter @Setter private Design design;
	@Getter @Setter private int type;
	@Getter @Setter private int value;
	@Getter @Setter private int damage; // or protection, if armor / helmet. etc
	@Getter @Setter private int accuracy;
	@Getter @Setter private int attackType;
	@Getter @Setter private int numberOfUses;
	@Getter @Setter private boolean returnToRandomScene;
	@Getter @Setter private String sceneOrOwner;
	@Getter @Setter private String clickMessage;
	@Getter @Setter private String operativeVerb;
	@Getter @Setter private String failureMessage;
	@Getter @Setter private String useMessage;
	@Getter @Setter private String sound;
    private Rectangle designBounds;

	@Setter private State state = new State(this);

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

	public String toString() {
		return name;
	}
}
