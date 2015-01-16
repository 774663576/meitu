package com.meitu.parser;

import org.json.JSONObject;

import com.meitu.data.result.Result;
import com.meitu.data.result.SimpleResult;

public class SimpleParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		return new SimpleResult();
	}

}
