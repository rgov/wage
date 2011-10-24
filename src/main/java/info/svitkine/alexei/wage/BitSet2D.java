package info.svitkine.alexei.wage;

import java.util.BitSet;

import lombok.Getter;


public class BitSet2D {
	private BitSet bitSet;
	@Getter private int width;
	@Getter private int height;

	public BitSet2D(int width, int height, boolean initialValue) {
		this.bitSet = new BitSet(width * height);
		this.width = width;
		this.height = height;
		if (initialValue == true)
			bitSet.set(0, width * height); 
	}

	public boolean get(int x, int y) {
		return bitSet.get(y*height + x);
	}

	public void set(int x, int y, boolean value) {
		bitSet.set(y*height + x, value);
	}
}
