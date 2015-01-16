package com.meitu.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum CircleMemberState {

	ADD, UPDATE, DEL;

	public static Map<String, CircleMemberState> s2s = new HashMap<String, CircleMemberState>();
	static {
		for (CircleMemberState s : CircleMemberState.values()) {
			s2s.put(s.name(), s);
		}
	}

	public static CircleMemberState convert(String s) {
		if (s2s.containsKey(s)) {
			return s2s.get(s);
		}
		return CircleMemberState.ADD;
	}

}
