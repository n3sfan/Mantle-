package io.lethinh.github.mantle.nbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Credit goes to Mojang.
 */
public final class NBTHelper {

	private NBTHelper() {

	}

	/**
	 * Load the gzipped compound from the inputstream.
	 */
	public static NBTTagCompound readCompressed(InputStream is) throws IOException {
		DataInputStream in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(is)));
		NBTTagCompound nbt;

		try {
			nbt = read(in, NBTSizeTracker.INFINITE);
		} finally {
			in.close();
		}

		return nbt;
	}

	/**
	 * Write the compound, gzipped, to the outputstream.
	 */
	public static void writeCompressed(NBTTagCompound compound, OutputStream outputStream) throws IOException {
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputStream)));

		try {
			write(compound, out);
		} finally {
			out.close();
		}
	}

	public static void safeWrite(NBTTagCompound compound, File fileIn) throws IOException {
		File file1 = new File(fileIn.getAbsolutePath() + "_tmp");

		if (file1.exists()) {
			file1.delete();
		}

		write(compound, file1);

		if (fileIn.exists()) {
			fileIn.delete();
		}

		if (fileIn.exists()) {
			throw new IOException("Failed to delete " + fileIn);
		} else {
			file1.renameTo(fileIn);
		}
	}

	/**
	 * Reads from a CompressedStream.
	 */
	public static NBTTagCompound read(DataInputStream inputStream) throws IOException {
		return read(inputStream, NBTSizeTracker.INFINITE);
	}

	/**
	 * Reads the given DataInput, constructs, and returns an NBTTagCompound with the
	 * data from the DataInput
	 */
	public static NBTTagCompound read(DataInput input, NBTSizeTracker accounter) throws IOException {
		NBTBase nbtbase = read(input, 0, accounter);

		if (nbtbase instanceof NBTTagCompound) {
			return (NBTTagCompound) nbtbase;
		} else {
			throw new IOException("Root tag must be a named compound tag");
		}
	}

	public static void write(NBTTagCompound compound, DataOutput output) throws IOException {
		writeTag(compound, output);
	}

	private static void writeTag(NBTBase tag, DataOutput output) throws IOException {
		output.writeByte(tag.getId());

		if (tag.getId() != 0) {
			output.writeUTF("");
			tag.write(output);
		}
	}

	private static NBTBase read(DataInput input, int depth, NBTSizeTracker accounter) throws IOException {
		byte b0 = input.readByte();
		accounter.read(8); // Forge: Count everything!

		if (b0 == 0) {
			return new NBTTagEnd();
		} else {
			NBTSizeTracker.readUTF(accounter, input.readUTF()); // Forge: Count this string.
			accounter.read(32); // Forge: 4 extra bytes for the object allocation.
			NBTBase nbtbase = NBTBase.createNewByType(b0);

			try {
				nbtbase.read(input, depth, accounter);
				return nbtbase;
			} catch (IOException ioexception) {
				throw ioexception;
			}
		}
	}

	public static void write(NBTTagCompound compound, File fileIn) throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream(fileIn));

		try {
			write(compound, out);
		} finally {
			out.close();
		}
	}

	public static NBTTagCompound read(File fileIn) throws IOException {
		if (!fileIn.exists()) {
			return null;
		} else {
			DataInputStream in = new DataInputStream(new FileInputStream(fileIn));
			NBTTagCompound nbt;

			try {
				nbt = read(in, NBTSizeTracker.INFINITE);
			} finally {
				in.close();
			}

			return nbt;
		}
	}

}
