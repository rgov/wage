package info.svitkine.alexei.wage;

import java.awt.Rectangle;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;


public class ChrImpl implements Chr {
	@Getter @Setter private int index;
	@Getter @Setter private String name;
	@Getter @Setter private short resourceID;
	@Getter @Setter private Design design;
	@Getter @Setter private String initialScene;
	@Getter @Setter private int gender;
	@Getter @Setter private boolean nameProperNoun;
	@Getter @Setter private boolean playerCharacter;
	@Getter @Setter private int maximumCarriedObjects;
	@Getter @Setter private int returnTo;
	private Rectangle designBounds;
    
	@Getter @Setter private int physicalStrength;
	@Getter @Setter private int physicalHp;
	@Getter @Setter private int naturalArmor;
	@Getter @Setter private int physicalAccuracy;
	@Getter @Setter private int spiritualStrength;
	@Getter @Setter private int spiritualHp;
	@Getter @Setter private int resistanceToMagic;
	@Getter @Setter private int spiritualAccuracy;
	@Getter @Setter private int runningSpeed;
	@Getter @Setter private int rejectsOffers;
	@Getter @Setter private int followsOpponent;
	
	@Getter @Setter private String initialSound;
	@Getter @Setter private String scoresHitSound;
	@Getter @Setter private String receivesHitSound;
	@Getter @Setter private String dyingSound;

	@Getter @Setter private String nativeWeapon1;
	@Getter @Setter private String operativeVerb1;
	@Getter @Setter private int weaponDamage1;
	@Getter @Setter private String weaponSound1;
	
	@Getter @Setter private String nativeWeapon2;
	@Getter @Setter private String operativeVerb2;
	@Getter @Setter private int weaponDamage2;
	@Getter @Setter private String weaponSound2;
	
	@Getter @Setter private int winningWeapons;
	@Getter @Setter private int winningMagic;
	@Getter @Setter private int winningRun;
	@Getter @Setter private int winningOffer;
	@Getter @Setter private int losingWeapons;
	@Getter @Setter private int losingMagic;
	@Getter @Setter private int losingRun;
	@Getter @Setter private int losingOffer;
	
	@Getter @Setter private String initialComment;
	@Getter @Setter private String scoresHitComment;
	@Getter @Setter private String receivesHitComment;
	@Getter @Setter private String makesOfferComment;
	@Getter @Setter private String rejectsOfferComment;
	@Getter @Setter private String acceptsOfferComment;
	@Getter @Setter private String dyingWords;

	@Setter private State state;

	@Getter private Context context = new Context();
	
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

	public Weapon[] getWeapons(boolean includeMagic) {
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();
		if (hasNativeWeapon1()) {
			weapons.add(new Weapon() {
				public String getName() {
					return getNativeWeapon1();
				}
				public String getOperativeVerb() {
					return getOperativeVerb1();
				}
				public int getType() {
					return Obj.REGULAR_WEAPON;
				}
				public int getAccuracy() {
					return 0;
				}
				public int getDamage() {
					return getWeaponDamage1();
				}
				public String getSound() {
					return getWeaponSound1();
				}
				public String getFailureMessage() {
					return null;
				}
			});
		}
		if (hasNativeWeapon2()) {
			weapons.add(new Weapon() {
				public String getName() {
					return getNativeWeapon2();
				}
				public String getOperativeVerb() {
					return getOperativeVerb2();
				}
				public int getType() {
					return Obj.REGULAR_WEAPON;
				}
				public int getAccuracy() {
					return 0;
				}
				public int getDamage() {
					return getWeaponDamage2();
				}
				public String getSound() {
					return getWeaponSound2();
				}
				public String getFailureMessage() {
					return null;
				}
			});
		}
		for (Obj o : state.getInventory()) {
			switch (o.getType()) {
				case Obj.REGULAR_WEAPON:
				case Obj.THROW_WEAPON:
					weapons.add(o);
					break;
				case Obj.MAGICAL_OBJECT:
					if (includeMagic) {
						weapons.add(o);
					}
			}
		}
		return (Weapon[]) weapons.toArray(new Weapon[0]);
	}

	public Obj[] getMagicalObjects() {
		ArrayList<Obj> magic = new ArrayList<Obj>();
		for (Obj obj : state.getInventory()) {
			if (obj.getType() == Obj.MAGICAL_OBJECT) {
				magic.add(obj);
			}
		}
		return magic.toArray(new Obj[0]);
	}

	public boolean hasNativeWeapon1() {
		return nativeWeapon1 != null && operativeVerb1 != null &&
			nativeWeapon1.length() > 0 && operativeVerb1.length() > 0;
	}

	public boolean hasNativeWeapon2() {
		return nativeWeapon2 != null && operativeVerb2 != null &&
			nativeWeapon2.length() > 0 && operativeVerb2.length() > 0;
	}
}
