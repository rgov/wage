package info.svitkine.alexei.wage;

public interface Weapon {
	public String getName();
	public String getOperativeVerb();
	public int getType();
	public int getAccuracy();
	public int getDamage();
	public String getSound();
	public void decrementNumberOfUses();
}
