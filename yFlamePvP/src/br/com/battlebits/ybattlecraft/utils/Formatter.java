package br.com.battlebits.ybattlecraft.utils;

public class Formatter {

	public static String getFormattedName(String name) {
		if(name == null)
			return "null";
		if(name.isEmpty())
			return "null";
		char[] stringArray = name.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		name = new String(stringArray);
		return name;
	}

}
