package io.lethinh.github.mantle.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Credit goes to Mojang.
 */
public class NBTTagByte extends NBTPrimitive {

	/** The byte value for the tag. */
	private byte data;

	NBTTagByte() {
	}

	public NBTTagByte(byte data) {
		this.data = data;
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension
	 * classes
	 */
	@Override
	void write(DataOutput output) throws IOException {
		output.writeByte(this.data);
	}

	@Override
	void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
		sizeTracker.read(72L);
		this.data = input.readByte();
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 1;
	}

	@Override
	public String toString() {
		return this.data + "b";
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public NBTTagByte copy() {
		return new NBTTagByte(this.data);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return super.equals(p_equals_1_) && this.data == ((NBTTagByte) p_equals_1_).data;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.data;
	}

	@Override
	public long getLong() {
		return this.data;
	}

	@Override
	public int getInt() {
		return this.data;
	}

	@Override
	public short getShort() {
		return this.data;
	}

	@Override
	public byte getByte() {
		return this.data;
	}

	@Override
	public double getDouble() {
		return this.data;
	}

	@Override
	public float getFloat() {
		return this.data;
	}

}