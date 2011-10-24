package info.svitkine.alexei.wage;

import lombok.Getter;
import lombok.Setter;


public class Context {
	/** The base physical accuracy of the player. */
	public static final int PHYS_ACC_BAS = 0;
	/** The current physical accuracy of the player. */
	public static final int PHYS_ACC_CUR = 1;
	/** The base physical armor of the player. */
	public static final int PHYS_ARM_BAS = 2;
	/** The current physical armor of the player. */
	public static final int PHYS_ARM_CUR = 3;
	/** The base physical hit points of the player. */
	public static final int PHYS_HIT_BAS = 4;
	/** The current physical hit points of the player. */
	public static final int PHYS_HIT_CUR = 5;
	/** The base physical speed of the player. */
	public static final int PHYS_SPE_BAS = 6;
	/** The current physical speed of the player. */
	public static final int PHYS_SPE_CUR = 7;
	/** The base physical strength of the player. */
	public static final int PHYS_STR_BAS = 8;
	/** The current physical strength of the player. */
	public static final int PHYS_STR_CUR = 9;
	/** The base spiritual accuracy of the player. */
	public static final int SPIR_ACC_BAS = 10;
	/** The current spiritual accuracy of the player. */
	public static final int SPIR_ACC_CUR = 11;
	/** The base spiritual armor of the player. */
	public static final int SPIR_ARM_BAS = 12;
	/** The current spiritual armor of the player. */
	public static final int SPIR_ARM_CUR = 13;
	/** The base spiritual hit points of the player. */
	public static final int SPIR_HIT_BAS = 14;
	/** The current spiritual hit points of the player. */
	public static final int SPIR_HIT_CUR = 15;
	/** The base spiritual strength of the player. */
	public static final int SPIR_STR_BAS = 16;
	/** The current spiritual strength of the player. */
	public static final int SPIR_STR_CUR = 17;

    // XXX visits, kills, experiences were shorts, does it matter?
    @Getter @Setter private int visits; // Number of scenes visited, including repeated visits
	@Getter @Setter private int kills;  // Number of characters killed
	@Getter @Setter private int experience;
	@Getter @Setter private boolean frozen;
	@Getter @Setter private short[] userVariables = new short[26 * 9];
    
	public short getUserVariable(int index) {
		return userVariables[index];
	}

	public void setUserVariable(int index, short value) {
		userVariables[index] = value;
	}
}
