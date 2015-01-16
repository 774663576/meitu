package com.meitu.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.meitu.data.enums.CircleMemberState;

import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractData implements IData, Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public static enum Status {
		NEW, OLD, UPDATE, DEL
	};

	protected Status status = Status.NEW;

	@Override
	public void read(SQLiteDatabase db) {
		return;
	}

	@Override
	public void write(SQLiteDatabase db) {
		return;
	}

	@Override
	public void update(IData data) {
		return;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public static Map<String, Status> s2s = new HashMap<String, Status>();
	static {
		for (Status s : Status.values()) {
			s2s.put(s.name(), s);
		}
	}

	public static Status convert(String s) {
		if (s2s.containsKey(s)) {
			return s2s.get(s);
		}
		return Status.NEW;
	}
}
