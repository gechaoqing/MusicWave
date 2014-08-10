package com.gecq.musicwave.utils;

import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class NetWork {
	private NetWork(){}
	private static NetWork nw;
	public static NetWork getInstance(){
		if(nw==null){
			nw=new NetWork();
		}
		return nw;
	}
	public String getLrc(String key) {
		StringBuilder sb = new StringBuilder();
		try {
			HttpClient getClient = new DefaultHttpClient();
			HttpGet request = new HttpGet("http://www.baidu.com/s?wd="
					+ URLEncoder.encode("filetype：lrc " + key, "GBK"));
			request.addHeader("Host", "www.baidu.com");
			request.addHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11");
			request.addHeader("Accept",
				"text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			request.addHeader("Accept-Language", "zh-cn,zh;q=0.5");
			request.addHeader("Keep-Alive", "300");
			request.addHeader("Referer", "http://www.baidu.com/");
			request.addHeader("Connection", "keep-alive");
			HttpResponse response = getClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inStrem = response.getEntity().getContent();
				int result = inStrem.read();
				while (result != -1) {
					sb.append((char) result);
					result = inStrem.read();
				}
				inStrem.close();
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
