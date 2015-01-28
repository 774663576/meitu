package com.meitu.data.enums;

import java.util.HashMap;
import java.util.Map;

public enum ArticleState {

	ADD, UPDATE, DEL;

	public static Map<String, ArticleState> s2s = new HashMap<String, ArticleState>();
	static {
		for (ArticleState s : ArticleState.values()) {
			s2s.put(s.name(), s);
		}
	}

	public static ArticleState convert(String s) {
		if (s2s.containsKey(s)) {
			return s2s.get(s);
		}
		return ArticleState.ADD;
	}

}
