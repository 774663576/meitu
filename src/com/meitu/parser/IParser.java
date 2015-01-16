package com.meitu.parser;

import org.json.JSONObject;

import com.meitu.data.result.Result;

public interface IParser {
	public Result parse(JSONObject jsonObj) throws Exception;
}
