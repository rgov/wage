package info.svitkine.alexei.wage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;


public class World {
	public static final String STORAGE = "STORAGE@";

	@Getter @Setter private String name;
	@Getter @Setter private int signature;
	@Getter @Setter private String creatorCode = "WEDT";
	@Getter @Setter private String aboutMessage;
	@Getter @Setter private String soundLibrary1;
	@Getter @Setter private String soundLibrary2;
	@Getter @Setter private boolean weaponsMenuDisabled;
	@Getter private Script globalScript;
	@Setter private String aboutMenuItemName;
	@Getter @Setter private String weaponsMenuName = "Weapons";
	@Getter @Setter private String commandsMenuName = "Commands";
	@Getter @Setter private String defaultCommandsMenu =
		"North/N;South/S;East/E;West/W;Up/U;Down/D;(-;Look/L;Rest/R;Status/T;Inventory/I;Search/F;(-;Open;Close";
	@Getter @Setter private String saveBeforeCloseMessage = "Save changes before closing?";
	@Getter @Setter private String saveBeforeQuitMessage = "Save changes before quiting?";
	@Getter @Setter private String gameOverMessage = "Game Over!";
	@Getter @Setter private String revertMessage = "Revert to the last saved version?";
	@Getter private Map<String, Scene> scenes;
	@Getter private Map<String, Obj> objs;
	@Getter private Map<String, Chr> chrs;
	@Getter private Map<String, Sound> sounds;
	@Getter private List<Scene> orderedScenes;
	@Getter private List<Obj> orderedObjs;
	@Getter private List<Chr> orderedChrs;
	@Getter private List<Sound> orderedSounds;
	@Getter private List<byte[]> patterns;
	@Getter @Setter private Chr player;
    
	private List<MoveListener> moveListeners;
    private Scene storageScene;

	@Getter @Setter private State currentState;
	
	public World(Script globalScript) {
		this.globalScript = globalScript;
		scenes = new HashMap<String, Scene>();
		objs = new HashMap<String, Obj>();
		chrs = new HashMap<String, Chr>();
		sounds = new HashMap<String, Sound>();
		orderedScenes = new ArrayList<Scene>();
		orderedObjs = new ArrayList<Obj>();
		orderedChrs = new ArrayList<Chr>();
		orderedSounds = new ArrayList<Sound>();
		patterns = new ArrayList<byte[]>();
		SceneImpl storage = new SceneImpl();
		storage.setName(STORAGE);
		storageScene = storage;
		orderedScenes.add(storageScene);
		scenes.put(STORAGE, storageScene);
		moveListeners = new LinkedList<MoveListener>();
	}

	public Scene getStorageScene() {
		return storageScene;
	}
	
	public void addScene(SceneImpl room) {
		if (room.getName() != null)
			scenes.put(room.getName().toLowerCase(), room);
		
		// this is kind of a hack ... having the first scene in orderedScenes be the storage scene throws off
		// my method for calculating a hex offset for the save files
		if (room != getStorageScene())
			room.setIndex(orderedScenes.size() - 1);

		orderedScenes.add(room);
	}

	public void addObj(ObjImpl obj) {
		objs.put(obj.getName().toLowerCase(), obj);
		obj.setIndex(orderedObjs.size());
		orderedObjs.add(obj);
	}

	public void addChr(ChrImpl chr) {
		chrs.put(chr.getName().toLowerCase(), chr);
		chr.setIndex(orderedChrs.size());
		orderedChrs.add(chr);
	}

	public void addSound(Sound sound) {
		sounds.put(sound.getName().toLowerCase(), sound);
		orderedSounds.add(sound);
	}

	public Context getPlayerContext() {
		return player.getContext();
	}

	public Scene getPlayerScene() {
		return player.getState().getCurrentScene();
	}
	
	public Scene getSceneByID(short resourceID) {
		for (Scene scene : getOrderedScenes()) {
			if (scene.getResourceID() == resourceID) {
				return scene;
			}
		}
		return null;
	}
	
	public Scene getRandomScene() {
		// Not including storage:
		return getOrderedScenes().get(1 + (int) (Math.random() * getOrderedScenes().size() - 1));
	}

	public Scene getSceneByHexOffset(int offset) {
		// the save file stores blank info as 0xffff...
		if (offset == -1)
			return null;

		// ...and the storage scene as 0x0000
		if (offset == 0)
			return getStorageScene();

		int index = (offset - State.SCENES_INDEX) / State.SCENE_SIZE;
		return this.orderedScenes.get(index+1);
	}

	public Chr getCharByID(short resourceID) {
		for (Chr chr : getOrderedChrs()) {
			if (chr.getResourceID() == resourceID) {
				return chr;
			}
		}
		return null;
	}

	public Chr getCharByHexOffset(int offset) {
		// a lot of char hex offsets = 0xffff if they are empty (i.e. no character attacking, etc)
		if (offset == -1)
			return null;
		int index = (offset - currentState.getChrsHexOffset()) / State.CHR_SIZE;
		return orderedChrs.get(index);
	}

	public Obj getObjByID(short resourceID) {
		for (Obj obj : getOrderedObjs()) {
			if (obj.getResourceID() == resourceID) {
				return obj;
			}
		}
		return null;
	}

	public Obj getObjByHexOffset(int offset) {
		// a lot of obj hex offsets = 0xffff if they are empty (i.e. not wearing spirtual armor, etc.)
		if (offset == -1)
			return null;
		int index = (offset - currentState.getObjsHexOffset()) / State.OBJ_SIZE;
		return orderedObjs.get(index);
	}
	
	public Scene getSceneAt(int x, int y) {
		for (Scene scene : scenes.values()) {
			if (scene != storageScene && scene.getWorldX() == x && scene.getWorldY() == y) {
				return scene;
			}
		}
		return null;
	}

	public Set<Scene> getAdjacentScenes(Scene scene) {
		Set<Scene> scenes = new HashSet<Scene>();
		if (scene != null) {
			int x = scene.getWorldX();
			int y = scene.getWorldY();
			int dx[] = new int[] { 0, 0, 1, -1 };
			int dy[] = new int[] { -1, 1, 0, 0 };
			for (int dir : new int[] { Scene.NORTH, Scene.EAST, Scene.SOUTH, Scene.WEST }) {
				if (!scene.isDirBlocked(dir)) {
					scenes.add(getSceneAt(x + dx[dir], y + dy[dir]));
				}
			}
		}
		return scenes;
	}
	
	public class MoveEvent {
		private Object what;
		private Object from;
		private Object to;
		public MoveEvent(Object what, Object from, Object to) {
			this.what = what;
			this.from = from;
			this.to = to;
		}
		public Object getWhat() {
			return what;
		}
		public Object getFrom() {
			return from;
		}
		public Object getTo() {
			return to;
		}
	}
	
	public interface MoveListener {
		public void onMove(MoveEvent event);
	}
	
	private void fireMoveEvent(MoveEvent event) {
		for (MoveListener ml : moveListeners)
			ml.onMove(event);
	}
	
	public void addMoveListener(MoveListener ml) {
		moveListeners.add(ml);
	}
	
	public void removeMoveListener(MoveListener ml) {
		moveListeners.remove(ml);
	}

	private Chr removeFromChr(Obj obj) {
		Chr owner = obj.getState().getCurrentOwner();
		if (owner != null) {
			owner.getState().getInventory().remove(obj);
			for (int i = 0; i < Chr.NUMBER_OF_ARMOR_TYPES; i++) {
				if (owner.getState().getArmor(i) == obj) {
					owner.getState().setArmor(i, null);
				}
			}
		}
		return owner;
	}

	public void move(Obj obj, Chr chr) {
		if (obj == null)
			return;
		Object from = removeFromChr(obj);
		if (obj.getState().getCurrentScene() != null) {
			obj.getState().getCurrentScene().getState().getObjs().remove(obj);
			from = obj.getState().getCurrentScene();
		}
		obj.getState().setCurrentOwner(chr);
		chr.getState().getInventory().add(obj);
		Collections.sort(chr.getState().getInventory(), new Comparator<Obj>() {
			public int compare(Obj o1, Obj o2) {
				return o1.getIndex() - o2.getIndex();
			}
		});
		fireMoveEvent(new MoveEvent(obj, from, chr));
	}

	public void move(Obj obj, Scene scene) {
		if (obj == null)
			return;
		Object from = removeFromChr(obj);
		if (obj.getState().getCurrentScene() != null) {
			obj.getState().getCurrentScene().getState().getObjs().remove(obj);
			from = obj.getState().getCurrentScene();
		}
		obj.getState().setCurrentScene(scene);
		scene.getState().getObjs().add(obj);
		Collections.sort(scene.getState().getObjs(), new Comparator<Obj>() {
			public int compare(Obj o1, Obj o2) {
				boolean o1Immobile = (o1.getType() == Obj.IMMOBILE_OBJECT);
				boolean o2Immobile = (o2.getType() == Obj.IMMOBILE_OBJECT);
				if (o1Immobile == o2Immobile) {
					return o1.getIndex() - o2.getIndex();					
				}
				return (o1Immobile ? -1 : 1);
			}
		});
		fireMoveEvent(new MoveEvent(obj, from, scene));
	}

	public void move(Chr chr, Scene scene) {
		if (chr == null)
			return;
		Scene from = chr.getState().getCurrentScene();
		if (from != scene) {
			if (from != null)
				from.getState().getChrs().remove(chr);
			scene.getState().getChrs().add(chr);
			sortChrs(scene.getState().getChrs());
			if (scene == storageScene) {
				chr.setState(new Chr.State(chr));
			} else if (chr.isPlayerCharacter()) {
				scene.getState().setVisited(true);
				Context context = getPlayerContext();
				context.setVisits(context.getVisits() + 1);
			}
			chr.getState().setCurrentScene(scene);
			fireMoveEvent(new MoveEvent(chr, from, scene));
		}
	}

	private void sortChrs(List<Chr> chrs) {
		Collections.sort(chrs, new Comparator<Chr>() {
			public int compare(Chr c1, Chr c2) {
				return c1.getIndex() - c2.getIndex();
			}
		});
	}

	public String getAboutMenuItemName() {
		if (aboutMenuItemName == null) {
			return "About " + getName() + "...";
		}
		return aboutMenuItemName.replaceAll("@", getName());
	}

}
