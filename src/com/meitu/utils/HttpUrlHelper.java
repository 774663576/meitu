package com.meitu.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.meitu.data.enums.RetError;
import com.meitu.utils.Logger.Level;

/**
 * �й�http����������
 * 
 * @author teeker_bin
 * 
 */
public class HttpUrlHelper {
	// 10.6.4.105 ��˾
	// 192.168.1.102��
	public static final int CONNECTION_TIMEOUT = 10 * 1000;
	public static final int SO_TIMEOUT = 10 * 1000;
	public static final String DEFAULT_HOST = "http://192.168.1.102:8080/MeiTu"; // ��������ַ

	/**
	 * get �ύ��ʽ // *
	 * 
	 * @param url
	 *            URL ����
	 * @return
	 * @throws IOException
	 */
	public static String getUrlData(String url) {
		try {
			HttpGet httpRequest = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			// ����ʱ
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							CONNECTION_TIMEOUT);
			// ��ȡ��ʱ
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// �ж��Ƿ�ɹ�
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(httpResponse.getEntity());
			} else {
				Logger.out("HttpUrlHelper.getUrlData", "status code="
						+ httpResponse.getStatusLine().getStatusCode(),
						Level.INFO);
			}
		} catch (Exception e) {
			Logger.out("HttpUrlHelper.getUrlData", e, Level.WARN);
			return httpError(RetError.NETWORK_ERROR);

		}
		return httpError(RetError.INVALID);
	}

	private static String httpError(RetError error) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rt", 0);
		params.put("err", error.name());
		JSONObject jsonObjectFromMap = new JSONObject(params);
		return jsonObjectFromMap.toString();

	}

	/**
	 * POST ����ʽ
	 * 
	 * @param url
	 *            URL ����
	 * @param pairs
	 *            ���ݵĲ���
	 * @return
	 */
	public static String postUrlData(String url, List<NameValuePair> pairs) {
		try {
			HttpPost httpPost = new HttpPost(url);
			HttpClient httpclient = new DefaultHttpClient();
			// ����ʱ
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							CONNECTION_TIMEOUT);
			// ��ȡ��ʱ
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

			if (pairs != null) {
				// �����������
				HttpEntity httpentity = new UrlEncodedFormEntity(pairs, "utf8");
				httpPost.setEntity(httpentity);
			}
			HttpResponse httpResponse = httpclient.execute(httpPost);
			// �ж��Ƿ�ɹ�
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(httpResponse.getEntity());
			} else {
				Logger.out("HttpUrlHelper.postUrlData", "status code="
						+ httpResponse.getStatusLine().getStatusCode()
						+ " url=" + url, Level.INFO);
			}
		} catch (Exception e) {
			Logger.out("HttpUrlHelper.postUrlData", e, Level.WARN);
			return httpError(RetError.NETWORK_ERROR);

		}
		return httpError(RetError.INVALID);
	}

	/**
	 * ͨ��Ĭ�ϵ�host����post����
	 * 
	 * @param map
	 *            ��Ҫ���Ĳ���
	 * @param api
	 *            Ҫ���ʵ�api
	 * @return
	 */
	public static String postData(Map<String, Object> map, String api) {
		return postData(map, api, HttpUrlHelper.DEFAULT_HOST);
	}

	/**
	 * ����post���󣬿��Ե�������host
	 * 
	 * @param map
	 * @param api
	 * @param host
	 * @return
	 */
	public static String postData(Map<String, Object> map, String api,
			String host) {
		for (String key : map.keySet()) {
			Logger.out("RequestMap==", "[param] " + key + ", " + map.get(key)
					+ "        " + api, Level.DEBUG);
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Iterator<?> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry testDemo = (Map.Entry) iterator.next();
			Object key = testDemo.getKey();
			Object value = testDemo.getValue();
			if (key != null && value != null) {
				params.add(new BasicNameValuePair(key.toString(), value
						.toString()));
			}
		}

		return HttpUrlHelper.postUrlData(createUrl(host, api), params);
	}

	private static String createUrl(String host, String api) {
		String url = host;
		if (host.charAt(host.length() - 1) != '/') {
			url = host + "/";
		}
		if (api.charAt(0) == '/') {
			url += api.substring(1);
		} else {
			url += api;
		}
		return url;
	}

	/**
	 * �ϴ�ͼƬ����
	 * 
	 * @param host
	 * @param api
	 * @param map
	 * @param file
	 * @param pkey
	 * @return
	 */
	public static String postDataFile(String api, Map<String, Object> map,
			File file) {
		return postDataWithFile(createUrl(DEFAULT_HOST, api), map, file);
	}

	/**
	 * �ϴ�ͼƬ����
	 * 
	 * @param url
	 * @param map
	 * @param file
	 * @param pkey
	 * @return
	 */
	public static String postDataWithFile(String url, Map<String, Object> map,
			File file) {
		HttpPost post = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		MultipartEntity mpEntity = new MultipartEntity();
		Iterator<?> iterator = map.entrySet().iterator();
		if (file != null && file.exists()) {
			FileBody fileBody = new FileBody(file);
			mpEntity.addPart("image", fileBody);
		}
		try {

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry testDemo = (Map.Entry) iterator.next();
				Object key = testDemo.getKey();
				Object value = testDemo.getValue();
				mpEntity.addPart(
						key.toString(),
						new StringBody(value.toString(), Charset
								.forName("UTF-8")));
			}
			post.setEntity(mpEntity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				Logger.out("HttpUrlHelper.upLoadPic", "status code="
						+ response.getStatusLine().getStatusCode(), Level.INFO);
			}
		} catch (Exception e) {
			Logger.out("HttpUrlHelper.upLoadPic", e, Level.WARN);
			return httpError(RetError.NETWORK_ERROR);

		} finally {
			if (mpEntity != null) {
				try {
					mpEntity.consumeContent();
				} catch (Exception e) {
					Logger.out("HttpUrlHelper.upLoadPic", e, Level.WARN);
				}
			}
			client.getConnectionManager().shutdown();
		}

		return httpError(RetError.INVALID);
	}

	/**
	 * �ϴ�ͼƬ����
	 * 
	 * @param url
	 * @param map
	 * @param file
	 * @param pkey
	 * @return
	 */
	public static String upLoadPicArray(String host, String url,
			Map<String, Object> map, List<File> files) {
		HttpPost post = new HttpPost(createUrl(host, url));
		HttpClient client = new DefaultHttpClient();
		MultipartEntity mpEntity = new MultipartEntity();
		Iterator<?> iterator = map.entrySet().iterator();
		for (int i = 0; i < files.size(); i++) {
			FileBody fileBody = new FileBody(files.get(i), "image/pjpeg");
			// mpEntity.addPart("image" + i, fileBody);
			mpEntity.addPart("image", fileBody);

		}
		try {
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry testDemo = (Map.Entry) iterator.next();
				Object key = testDemo.getKey();
				Object value = testDemo.getValue();
				mpEntity.addPart(
						key.toString(),
						new StringBody(value.toString(), Charset
								.forName("UTF-8")));
			}
			post.setEntity(mpEntity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				Logger.out("HttpUrlHelper.upLoadPic", "status code="
						+ response.getStatusLine().getStatusCode(), Level.INFO);
			}
		} catch (Exception e) {
			Logger.out("HttpUrlHelper.upLoadPic", e, Level.WARN);
			return httpError(RetError.NETWORK_ERROR);

		} finally {
			if (mpEntity != null) {
				try {
					mpEntity.consumeContent();
				} catch (Exception e) {
					Logger.out("HttpUrlHelper.upLoadPic", e, Level.WARN);
				}
			}
			client.getConnectionManager().shutdown();
		}

		return httpError(RetError.INVALID);
	}

}
