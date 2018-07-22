package io.lethinh.github.mantle.nbt;

/**
 * Credit goes to Mojang.
 */
public class NBTException extends Exception {

	private static final long serialVersionUID = 1L;

	public NBTException(String message, String json, int p_i47523_3_) {
		super(message + " at: " + slice(json, p_i47523_3_));
	}

	private static String slice(String json, int p_193592_1_) {
		StringBuilder stringbuilder = new StringBuilder();
		int i = Math.min(json.length(), p_193592_1_);

		if (i > 35) {
			stringbuilder.append("...");
		}

		stringbuilder.append(json.substring(Math.max(0, i - 35), i));
		stringbuilder.append("<--[HERE]");
		return stringbuilder.toString();
	}

}