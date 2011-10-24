package info.svitkine.alexei.wage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import lombok.Getter;
import lombok.Setter;


public class State {

	public static final int VARS_INDEX = 0x005E;
	public static final int SCENES_INDEX = 0x0232;
	
	public static final int SCENE_SIZE = 0x0010;
	public static final int CHR_SIZE = 0x0016;
	public static final int OBJ_SIZE = 0x0010;
	
	// important global info
	@Getter @Setter private short numScenes;
	@Getter @Setter private short numChars;
	@Getter @Setter private short numObjs;
	
	// unique world id (int)
	@Getter @Setter private int worldSignature;
	
	// global status vars
	@Getter @Setter private int visitNum;
	@Getter @Setter private int loopNum;
	@Getter @Setter private int killNum;
	@Getter @Setter private int exp;
	@Getter @Setter private int aim;
	@Getter @Setter private int opponentAim;

	// information about player character
	@Getter @Setter private int basePhysStr;
	@Getter @Setter private int basePhysHp;
	@Getter @Setter private int basePhysArm;
	@Getter @Setter private int basePhysAcc;
	@Getter @Setter private int baseSprtStr;
	@Getter @Setter private int baseSprtHp;
	@Getter @Setter private int baseSprtArm;
	@Getter @Setter private int baseSprtAcc;
	@Getter @Setter private int baseRunSpeed;
	
	// hex offsets within the save file
	@Getter @Setter private int chrsHexOffset;
	@Getter @Setter private int objsHexOffset;
	@Getter @Setter private int playerHexOffset;	
	@Getter @Setter private int curSceneHexOffset = 0;
	
	// info about non-player characters related to the current scene
	@Getter @Setter private int presCharHexOffset = -1;		// resource id of character present in current scene
	@Getter @Setter private int runCharHexOffset = -1;		// hex index to character who just ran away
	
	// are we wearing anything?
	@Getter @Setter private int helmetHexOffset = -1;
	@Getter @Setter private int shieldHexOffset = -1;
	@Getter @Setter private int chestArmHexOffset = -1;
	@Getter @Setter private int sprtArmHexOffset = -1;
	
	@Getter @Setter private short[] userVars = new short[26 * 9];
	
	@Getter @Setter private byte[] sceneData;
	@Getter @Setter private byte[] chrData;
	@Getter @Setter private byte[] objData;
	
	@Getter @Setter private boolean valid = false;
	
    
	public int getHexOffsetForObj(Obj obj) {
		if (obj == null)
			return -1;
		return ((obj.getIndex() * State.OBJ_SIZE) + getObjsHexOffset());
	}

	public int getHexOffsetForChr(Chr chr) {
		if (chr == null)
			return -1;
		return ((chr.getIndex() * State.CHR_SIZE) + getChrsHexOffset());
	}

	public int getHexOffsetForScene(Scene scene) {
		if (scene == null)
			return -1;
		return ((scene.getIndex() * State.SCENE_SIZE) + State.SCENES_INDEX);
	}

	// For Debugging Purposes:
	public void printState(World world, String filePath) throws IOException {
		PrintWriter stream;
		
		if (filePath != null)
			stream = new PrintWriter(new FileWriter(filePath));
		else
			stream = new PrintWriter(System.out, true);
		
		stream.println("Number of Scenes: " + this.numScenes);
		stream.println("Number of Characters: " + this.numChars);
		stream.println("Number of Object: " + this.numObjs);
		stream.println("==============================================");
		stream.println("Hex Offset to start of Characters: " + Integer.toHexString(this.chrsHexOffset));
		stream.println("Hex Offset to start of Objects: " + Integer.toHexString(this.objsHexOffset));
		stream.println("==============================================");
		stream.println("World Signature: " + Integer.toHexString(this.worldSignature));
		stream.println("==============================================");
		stream.println("Visit# (Total scenes visited including repeats): " + this.visitNum);
		stream.println("Loop# (Commands executed in the current scene): " + this.loopNum);
		stream.println("Monster# (Total monsters killed): " + this.killNum);
		stream.println("==============================================");
		stream.println("Hex Offset to Player: " + Integer.toHexString(this.playerHexOffset));
		stream.println("Player: " + world.getCharByHexOffset(this.playerHexOffset));
		stream.println("==============================================");
		stream.println("Hex Offset to character in current scene: " + Integer.toHexString(this.presCharHexOffset));
		stream.println("Name of Character in current scene: " + world.getCharByHexOffset((short)this.presCharHexOffset));
		stream.println("==============================================");
		stream.println("Hex Offset to Current Scene: " + Integer.toHexString(this.curSceneHexOffset));
		stream.println("Current Scene: " + world.getSceneByHexOffset(this.curSceneHexOffset));
		stream.println("==============================================");
		stream.println("Hex Offset to Worn Helmet: " + Integer.toHexString(this.helmetHexOffset));
		if (helmetHexOffset != 0xffff)
			stream.println("Helmet: " + world.getObjByHexOffset((short)this.helmetHexOffset));
		stream.println("Hex Offset to Worn Shield: " + Integer.toHexString(this.shieldHexOffset));
		if (shieldHexOffset != 0xffff)
			stream.println("Shield: " + world.getObjByHexOffset((short)this.shieldHexOffset));
		stream.println("Hex Offset to Worn Chest Armor: " + Integer.toHexString(this.chestArmHexOffset));
		if (chestArmHexOffset != 0xffff)
			stream.println("Chest Armor: " + world.getObjByHexOffset((short)this.chestArmHexOffset));
		stream.println("Hex Offset to Worn Spiritual Armor: " + Integer.toHexString(this.sprtArmHexOffset));
		if (sprtArmHexOffset != 0xffff)
			stream.println("Spiritual Armor: " + world.getObjByHexOffset((short)this.sprtArmHexOffset));
		stream.println("==============================================");
		stream.println("Hex Offset to Running Character: " + Integer.toHexString(this.runCharHexOffset));
		if (runCharHexOffset != 0xffff)
			stream.println("Running Character: " + world.getCharByHexOffset((short)this.runCharHexOffset));
		stream.println("==============================================");
		stream.println("Base Physical Strength Value: " + this.basePhysStr );
		stream.println("Base Physical Hit Point Value: " + this.basePhysHp);
		stream.println("Base Physical Armor Value: " + this.basePhysArm);
		stream.println("Base Physical Accuracy Value: " + this.basePhysAcc);
		stream.println("Base Spiritual Strength Value: " + this.baseSprtStr);
		stream.println("Base Spiritual Hit Point Value: " + this.baseSprtHp);
		stream.println("Base Spiritual Armor Value:"+ this.baseSprtArm);
		stream.println("Base Spiritual Accuracy Value: " + this.baseSprtAcc);
		stream.println("Base Run Speed Value: " + this.baseRunSpeed);
		stream.println("==============================================");
		stream.println("Player Experience: " + this.exp);
		stream.println("==============================================");

		// print variables
		int varCount = 0;
		
		for (char letter = 'A'; letter <= 'Z'; letter++) {
			for (int num = 1; num <= 9; num++) {
				stream.print(letter);
				stream.print(num + "#:" + this.userVars[varCount] + " | ");
				varCount++;
			}
			stream.println();
		}
		
		// print scenes
		printScenes(world, stream);
		
		// print characters
		printCharacters(world, stream);
		
		// print objects
		printObjects(world, stream);
		
		stream.close();
	}
	
	private void printScenes(World world, PrintWriter stream) {
		int offset = 0;
		
		for (Scene scn : world.getOrderedScenes()) {
			if (scn != world.getStorageScene()) {
				
				short id = bytesToShort(sceneData[offset], sceneData[offset+1]);

				if (scn.getResourceID() != id)
					return;
				
				stream.println("Scene: " + scn.getName());
				stream.println("ID: " + id );
				stream.println("World Y: " + bytesToShort(sceneData[offset+2],sceneData[offset+3]));
				stream.println("World X: " + bytesToShort(sceneData[offset+4],sceneData[offset+5]));
				stream.println("Blocked North: " + sceneData[offset+6]);
				stream.println("Blocked South: " + sceneData[offset+7]);
				stream.println("Blocked East: " + sceneData[offset+8]);
				stream.println("Blocked West: " + sceneData[offset+9]);
				
				stream.println("Sound Frequency: " + bytesToShort(sceneData[offset+10],sceneData[offset+11]));
				stream.println("Sound Type: " + sceneData[offset+12]);
				
				// rest of the scene data (bytes 14-16) are unknown
				stream.println("UNKNOWN: " + sceneData[offset+13]);
				stream.println("UNKNOWN: " + sceneData[offset+14]);
				stream.println("Visited: " + sceneData[offset+15]);
				
				offset += State.SCENE_SIZE;
			}
		}
	}
	
	private void printCharacters(World world, PrintWriter stream) {
		ByteArrayInputStream bin = new ByteArrayInputStream(chrData);
		DataInputStream in = new DataInputStream(bin);
		try {
			for (Chr chr : world.getOrderedChrs()) {			
				short id = in.readShort();

				if (chr.getResourceID() != id)
					return;

				short sceneLoc = in.readShort();

				stream.println("Character: " + chr.getName());
				stream.println("ID: " + id );
				stream.println("Location: " + sceneLoc);

				stream.println("Current Physical Strength: " + in.readByte());
				stream.println("Current Physical Hit: " + in.readByte());
				stream.println("Current Physical Armor: " + in.readByte());
				stream.println("Current Physical Accuracy: " + in.readByte());
				stream.println("Current Spiritual Strength: " + in.readByte());
				stream.println("Current Spiritual Hit: " + in.readByte());
				stream.println("Current Spiritual Armor: " + in.readByte());
				stream.println("Current Spiritual Accuracy: " + in.readByte());
				stream.println("Current Physical Speed: " + in.readByte());

				stream.println("Rejects Offers: " + in.readByte());
				stream.println("Follows Opponents: " + in.readByte());

				// bytes 16-20 are unknown
				stream.println("UNKNOWN: " + in.readByte());
				stream.println("UNKNOWN: " + in.readByte());
				stream.println("UNKNOWN: " + in.readByte());
				stream.println("UNKNOWN: " + in.readByte());
				stream.println("UNKNOWN: " + in.readByte());

				stream.println("Weapon Damage 1: "+ in.readByte());
				stream.println("Weapon Damage 2: " + in.readByte());
			}
		} catch (IOException e) { }
	}
	
	private void printObjects(World world, PrintWriter stream){
		int offset = 0;
		
		for (Obj obj : world.getOrderedObjs()) {
			short id = bytesToShort(objData[offset],objData[offset+1]);

			if (obj.getResourceID() != id)
				return;
			
			short sceneLoc = bytesToShort(objData[offset+2],objData[offset+3]);
			short charLoc = bytesToShort(objData[offset+4],objData[offset+5]);
			
			stream.println("Object: " + obj.getName());
			stream.println("ID: " + id);
			stream.println("Scene Location: " + sceneLoc);
			stream.println("Char Location: " + charLoc);
			
			stream.println("UNKNOWN: " + objData[offset+6]);
			stream.println("UNKNOWN: " + objData[offset+7]);
			stream.println("UNKNOWN: " + objData[offset+8]);
			
			// bytes 7-9 are unknown (always = 0)
			
			// update object stats
			stream.println("Accuracy: " + objData[offset+9]);
			stream.println("Value: " + objData[offset+10]);
			stream.println("Type: " + objData[offset+11]);
			stream.println("Damage: " + objData[offset+12]);
			stream.println("Attack Type: " + objData[offset+13]);
			stream.println("Number of Uses: " + bytesToShort(objData[offset+14],objData[offset+15]));
			
			offset += State.OBJ_SIZE;
		}
	}
	
	private static short bytesToShort(byte low, byte high) {
		return (short)((0xff & high) | (0xff & low) << 8 );
	}
}
