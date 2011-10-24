package info.svitkine.alexei.wage;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


public interface Chr {
	public static final int RETURN_TO_STORAGE = 0;
	public static final int RETURN_TO_RANDOM_SCENE = 1;
	public static final int RETURN_TO_INITIAL_SCENE = 2;

	public static final int HEAD = 1;
	public static final int CHEST = 2;
	public static final int SIDE = 3;

	public static final int HEAD_ARMOR = 0;
	public static final int BODY_ARMOR = 1;
	public static final int SHIELD_ARMOR = 2;
	public static final int MAGIC_ARMOR = 3;
	public static final int NUMBER_OF_ARMOR_TYPES = 4;
	
	public static class State {
		@Getter @Setter private int basePhysicalStrength;
		@Getter @Setter private int currentPhysicalStrength;
		@Getter @Setter private int basePhysicalHp;
		@Getter @Setter private int currentPhysicalHp;
		@Getter @Setter private int baseNaturalArmor; // aka physicalArmor
		@Getter @Setter private int currentNaturalArmor; // aka physicalArmor
		@Getter @Setter private int basePhysicalAccuracy;
		@Getter @Setter private int currentPhysicalAccuracy;
		@Getter @Setter private int baseSpiritualStrength;
		@Getter @Setter private int currentSpiritualStrength;
		@Getter @Setter private int baseSpiritualHp;
		@Getter @Setter private int currentSpiritualHp;
		@Getter @Setter private int baseResistanceToMagic; // aka spiritualArmor
		@Getter @Setter private int currentResistanceToMagic; // aka spiritualArmor
		@Getter @Setter private int baseSpiritualAccuracy;
		@Getter @Setter private int currentSpiritualAccuracy;
		@Getter @Setter private int baseRunningSpeed;
		@Getter @Setter private int currentRunningSpeed;

		@Getter @Setter private int rejectsOffers;
		@Getter @Setter private int followsOpponent;

		@Getter @Setter private int weaponDamage1;
		@Getter @Setter private int weaponDamage2;

		@Getter @Setter private Scene currentScene;
		@Getter @Setter private List<Obj> inventory = new ArrayList<Obj>();
		private Obj[] armor = new Obj[4];
		
		public State(Chr chr) {
			basePhysicalStrength = currentPhysicalStrength = chr.getPhysicalStrength();
			basePhysicalHp = currentPhysicalHp = chr.getPhysicalHp();
			baseNaturalArmor = currentNaturalArmor = chr.getNaturalArmor();
			basePhysicalAccuracy = currentPhysicalAccuracy = chr.getPhysicalAccuracy();
			baseSpiritualStrength = currentSpiritualStrength = chr.getSpiritualStrength();
			baseSpiritualHp = currentSpiritualHp = chr.getSpiritualHp();
			baseResistanceToMagic = currentResistanceToMagic = chr.getResistanceToMagic();
			baseSpiritualAccuracy = currentSpiritualAccuracy = chr.getSpiritualAccuracy();
			baseRunningSpeed = currentRunningSpeed = chr.getRunningSpeed();
			rejectsOffers = chr.getRejectsOffers();
			followsOpponent = chr.getFollowsOpponent();
			weaponDamage1 = chr.getWeaponDamage1();
			weaponDamage2 = chr.getWeaponDamage2();
		}

		public Obj getArmor(int type) {
			return armor[type];
		}

		public void setArmor(int type, Obj obj) {
			armor[type] = obj;
		}
	}

	public Context getContext();
	public State getState();
	public void setState(State state);
	public Rectangle getDesignBounds();
	public String getAcceptsOfferComment();
	public String getDyingSound();
	public String getDyingWords();
	public int getFollowsOpponent();
	public String getInitialComment();
	public String getInitialSound();
	public int getLosingMagic();
	public int getLosingOffer();
	public int getLosingRun();
	public int getLosingWeapons();
	public String getMakesOfferComment();
	public Weapon[] getWeapons(boolean includeMagic);
	public Obj[] getMagicalObjects();
	public boolean hasNativeWeapon1();
	public boolean hasNativeWeapon2();
	public String getNativeWeapon1();
	public String getNativeWeapon2();
	public int getNaturalArmor();
	public String getOperativeVerb1();
	public String getOperativeVerb2();
	public int getPhysicalAccuracy();
	public int getPhysicalHp();
	public int getPhysicalStrength();
	public void setPhysicalStrength(int physicalStrength);
	public String getReceivesHitComment();
	public String getReceivesHitSound();
	public String getRejectsOfferComment();
	public int getRejectsOffers();
	public int getResistanceToMagic();
	public int getRunningSpeed();
	public String getScoresHitComment();
	public String getScoresHitSound();
	public int getSpiritualHp();
	public int getSpiritualAccuracy();
	public int getSpiritualStrength();
	public int getWeaponDamage1();
	public int getWeaponDamage2();
	public String getWeaponSound1();
	public String getWeaponSound2();
	public int getWinningMagic();
	public int getWinningOffer();
	public int getWinningRun();
	public int getWinningWeapons();
	public Design getDesign();
	public String getName();
	public short getResourceID();
	public int getGender();
	public String getInitialScene();
	public int getMaximumCarriedObjects();
	public boolean isNameProperNoun();
	public void setNameProperNoun(boolean nameProperNoun);
	public boolean isPlayerCharacter();
	public int getReturnTo();
	public int getIndex();
}
