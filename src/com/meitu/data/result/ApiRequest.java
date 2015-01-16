package com.meitu.data.result;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.meitu.data.enums.RetError;
import com.meitu.data.enums.RetStatus;
import com.meitu.parser.IParser;
import com.meitu.utils.HttpUrlHelper;
import com.meitu.utils.Logger;
import com.meitu.utils.Logger.Level;
import com.meitu.utils.SharedUtils;

public class ApiRequest {
	private static Result parse(IParser parser, String httpResult,
			Map<String, Object> params) {
		if (httpResult == null || httpResult.equals("")) {
			return new Result(RetStatus.FAIL, RetError.NETWORK_ERROR);
		}
		try {
			JSONObject jsonObj = new JSONObject(httpResult);
			String rt = jsonObj.getString("rt");
			if (!rt.equals("1")) {
				String err = jsonObj.getString("err");
				Result ret = new Result();
				ret.setStatus(RetStatus.FAIL);
				ret.setErr(err);
				return ret;
			}

			Result ret = parser.parse(jsonObj);
			return ret;
		} catch (Exception e) {
			Logger.out("ApiRequest.parse", e, Level.WARN);
		}
		return Result.defContentErrorResult();
	}

	public static Result<?> request(String url, Map<String, Object> params,
			IParser parser) {
		params.put("user_id", SharedUtils.getUid());
		for (String key : params.keySet()) {
			Logger.out("ApiRequest.request",
					"[param] " + key + ", " + params.get(key), Level.DEBUG);
		}
		String result = HttpUrlHelper.postData(params, url);
		Logger.out("ApiRequest.request.result", result, Level.DEBUG);
		return parse(parser, result, params);
	}

	public static Result<?> requestWithFile(String url,
			Map<String, Object> params, File file, IParser parser) {
		params.put("user_id", SharedUtils.getUid());
		for (String key : params.keySet()) {
			Logger.out("ApiRequest.request",
					"[param] " + key + ", " + params.get(key), Level.DEBUG);
		}
		String result = HttpUrlHelper.postDataFile(url, params, file);
		Logger.out("ApiRequest.request.result", result, Level.DEBUG);
		return parse(parser, result, params);
	}

	public static Result uploadFileArrayWithToken(String url,
			Map<String, Object> params, List<File> files, IParser parser) {
		params.put("user_id", SharedUtils.getUid());

		for (String key : params.keySet()) {
			Logger.out("ApiRequest.request",
					"[param] " + key + ", " + params.get(key), Level.DEBUG);
		}
		Logger.out("ApiRequest.request", "[url] " + url, Level.DEBUG);

		String result = HttpUrlHelper.upLoadPicArray(
				HttpUrlHelper.DEFAULT_HOST, url, params, files);
		Logger.out("ApiRequest.request", "[result] " + result, Level.DEBUG);

		return parse(parser, result, params);
	}
}
