package com.meitu.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum GrowthState {

	ADD, UPDATE, DEL;

	public static Map<String, GrowthState> s2s = new HashMap<String, GrowthState>();
	static {
		for (GrowthState s : GrowthState.values()) {
			s2s.put(s.name(), s);
		}
	}

	public static GrowthState convert(String s) {
		if (s2s.containsKey(s)) {
			return s2s.get(s);
		}
		return GrowthState.ADD;
	}

}
