package info.svitkine.alexei.wage;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


public interface Scene {
	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
	
	public static final int PERIODIC = 0;
	public static final int RANDOM = 1;

	public static class State {
		@Getter @Setter private int worldX;
		@Getter @Setter private int worldY;
		private boolean[] blocked = new boolean[4];
		@Getter @Setter private int soundFrequency; // times a minute, max 3600
		@Getter @Setter private int soundType;
		@Getter @Setter private boolean visited;
		@Getter private List<Obj> objs = new ArrayList<Obj>();
		@Getter private List<Chr> chrs = new ArrayList<Chr>();

		public State(Scene scene) {
			worldX = scene.getWorldX();
			worldY = scene.getWorldY();
			blocked[Scene.NORTH] = scene.isDirBlocked(Scene.NORTH);
			blocked[Scene.SOUTH] = scene.isDirBlocked(Scene.SOUTH);
			blocked[Scene.EAST] = scene.isDirBlocked(Scene.EAST);
			blocked[Scene.WEST] = scene.isDirBlocked(Scene.WEST);
			soundFrequency = scene.getSoundFrequency();
			soundType = scene.getSoundType();
		}

		public void setDirBlocked(int dir, boolean blocked) {
			this.blocked[dir] = blocked;
		}
		
		public boolean isDirBlocked(int dir) {
			return blocked[dir];
		}
	}

	public State getState();
	public void setState(State state);
	
	public int getSoundFrequency();
	public Rectangle getDesignBounds();
	public Rectangle getTextBounds();
	public String getDirMessage(int dir);
	public boolean isDirBlocked(int dir);
	public Design getDesign();
	public int getIndex();
	public String getName();
	public short getResourceID();
	public String getText();
	public Script getScript();
	public int getSoundType();
	public int getWorldX();
	public int getWorldY();
	public String getSoundName();
	public int getFontSize();
	public int getFontType();
	public String getFontName();
}
