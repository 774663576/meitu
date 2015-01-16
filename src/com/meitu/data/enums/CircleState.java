package com.meitu.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum CircleState {

	ADD, DEL, UPDATE;

	public static Map<String, CircleState> s2s = new HashMap<String, CircleState>();
	static {
		for (CircleState s : CircleState.values()) {
			s2s.put(s.name(), s);
		}
	}

	public static CircleState convert(String s) {
		if (s2s.containsKey(s)) {
			return s2s.get(s);
		}
		return CircleState.ADD;
	}

}
