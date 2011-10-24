package info.svitkine.alexei.wage;

import java.awt.Rectangle;

import lombok.Getter;
import lombok.Setter;


public interface Obj extends Weapon {
	// object types:
	public static final int REGULAR_WEAPON = 1;
	public static final int THROW_WEAPON = 2;
	public static final int MAGICAL_OBJECT = 3;
	public static final int HELMET = 4;
	public static final int SHIELD = 5;
	public static final int CHEST_ARMOR = 6;
	public static final int SPIRITUAL_ARMOR = 7;
	public static final int MOBILE_OBJECT = 8;
	public static final int IMMOBILE_OBJECT = 9;

	// attack types:
	public static final int CAUSES_PHYSICAL_DAMAGE = 0;
	public static final int CAUSES_SPIRITUAL_DAMAGE = 1;
	public static final int CAUSES_PHYSICAL_AND_SPIRITUAL_DAMAGE = 2;
	public static final int HEALS_PHYSICAL_DAMAGE = 3;
	public static final int HEALS_SPIRITUAL_DAMAGE = 4;
	public static final int HEALS_PHYSICAL_AND_SPIRITUAL_DAMAGE = 5;
	public static final int FREEZES_OPPONENT = 6;

	public static class State {
		@Getter private Scene currentScene;
		@Getter private Chr currentOwner;
		@Getter @Setter private int accuracy;
		@Getter @Setter private int value;
		@Getter @Setter private int type;
		@Getter @Setter private int damage;
		@Getter @Setter private int attackType;
		@Getter @Setter private int numberOfUses;
		
		public State(Obj obj) {
			this(obj, null, null);
		}

		public State(Obj obj, Chr currentOwner, Scene currentScene) {
			this.accuracy = obj.getAccuracy();
			this.value = obj.getValue();
			this.type = obj.getType();
			this.damage = obj.getDamage();
			this.attackType = obj.getAttackType();
			this.numberOfUses = obj.getNumberOfUses();
			this.currentOwner = currentOwner;
			this.currentScene = currentScene;
		}

		public void setCurrentScene(Scene currentScene) {
			this.currentScene = currentScene;
			if (currentScene != null)
				currentOwner = null;
		}
        
		public void setCurrentOwner(Chr currentOwner) {
			this.currentOwner = currentOwner;
			if (currentOwner != null)
				currentScene = null;
		}
    }

	public State getState();
	public void setState(State state);
	
	public Design getDesign();
	public Rectangle getDesignBounds();
	public String getName();
	public short getResourceID();
	public int getAccuracy();
	public int getAttackType();
	public String getClickMessage();
	public int getDamage();
	public String getFailureMessage();
	public int getNumberOfUses();
	public int getType();
	public String getOperativeVerb();
	public boolean isReturnToRandomScene();
	public String getSceneOrOwner();
	public void setSceneOrOwner(String sceneOrOwner);
	public String getSound();
	public String getUseMessage();
	public int getValue();
	public boolean isNamePlural();
	public int getIndex();
}
