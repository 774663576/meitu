package com.meitu.parser;

import org.json.JSONObject;

import com.meitu.data.result.Result;
import com.meitu.data.result.StringResult;

public class StringParser implements IParser {

	private String specialKey = "";

	public StringParser(String specialKey) {
		this.specialKey = specialKey;
	}

	public String getSpecialKey() {
		return specialKey;
	}

	public void setSpecialKey(String specialKey) {
		this.specialKey = specialKey;
	}

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}

		String value = jsonObj.getString(specialKey);
		if (value == null) {
			return Result.defContentErrorResult();
		}
		return new StringResult(value);
	}
}
