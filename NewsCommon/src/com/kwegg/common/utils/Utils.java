package com.kwegg.common.utils;

import java.util.ArrayList;

public class Utils {

	public static boolean ifArrayContains(ArrayList<String> array,
			String element) {
		for (String str : array) {
			if (str.equals(element))
				return true;
		}
		return false;
	}
}
