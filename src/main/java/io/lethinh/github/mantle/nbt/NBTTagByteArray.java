package io.lethinh.github.mantle.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Credit goes to Mojang.
 */
public class NBTTagByteArray extends NBTBase {

	/** The byte array stored in the tag. */
	private byte[] data;

	NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] data) {
		this.data = data;
	}

	public NBTTagByteArray(List<Byte> p_i47529_1_) {
		this(toArray(p_i47529_1_));
	}

	private static byte[] toArray(List<Byte> p_193589_0_) {
		byte[] abyte = new byte[p_193589_0_.size()];

		for (int i = 0; i < p_193589_0_.size(); ++i) {
			Byte obyte = p_193589_0_.get(i);
			abyte[i] = obyte == null ? 0 : obyte.byteValue();
		}

		return abyte;
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension
	 * classes
	 */
	@Override
	void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);
		output.write(this.data);
	}

	@Override
	void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
		sizeTracker.read(192L);
		int i = input.readInt();
		sizeTracker.read(8 * i);
		this.data = new byte[i];
		input.readFully(this.data);
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 7;
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("[B;");

		for (int i = 0; i < this.data.length; ++i) {
			if (i != 0) {
				stringbuilder.append(',');
			}

			stringbuilder.append(this.data[i]).append('B');
		}

		return stringbuilder.append(']').toString();
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public NBTBase copy() {
		byte[] abyte = new byte[this.data.length];
		System.arraycopy(this.data, 0, abyte, 0, this.data.length);
		return new NBTTagByteArray(abyte);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return super.equals(p_equals_1_) && Arrays.equals(this.data, ((NBTTagByteArray) p_equals_1_).data);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.data);
	}

	public byte[] getByteArray() {
		return this.data;
	}

}