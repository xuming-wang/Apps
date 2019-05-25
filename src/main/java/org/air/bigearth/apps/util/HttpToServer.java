package org.air.bigearth.apps.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * 发送http请求到服务器
 *
 * @author wangxuming
 * @version 1.0
 * @date 2018-12-17
 */
public class HttpToServer {
	private static final Logger log = Logger.getLogger(HttpToServer.class);

	public static String sendGetAutoChange(String url, String param) {
		if (url.contains("https:")) {
			return sendGet(url, param);
		} else {
			return sendGetForHttp(url, param);
		}
	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static byte[] sendGetForHttp(String url, String param, String type) {
		String result = "";
		BufferedReader in = null;
		FileInputStream fis = null;
		byte[] bytes = null;
		try {
			String urlNameString = "";
			if (StringUtils.isNotEmpty(param)) {
				urlNameString = url + "?" + param;
			} else {
				urlNameString = url;
			}
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			///connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9, image/webp,image/apng,*/*;q=0.8");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 设置连接主机超时（单位：毫秒）
			connection.setConnectTimeout(40000);
			// 设置从主机读取数据超时（单位：毫秒）
			connection.setReadTimeout(40000);
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
////            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
			// 定义 BufferedReader输入流来读取URL的响应
			//in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			InputStream fis88 = connection.getInputStream();

			bytes = new byte[fis88.available()];
			fis88.read(bytes, 0, fis88.available());

			/*String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}*/
		} catch (Exception e) {
			log.info("发送GET请求出现异常！" + url + param);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return bytes;

	}
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGetForHttp(String url, String param) {
		String result = "";
        BufferedReader in = null;
        try {
			String urlNameString = "";
			if (StringUtils.isNotEmpty(param)) {
				urlNameString = url + "?" + param;
			} else {
				urlNameString = url;
			}
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 设置连接主机超时（单位：毫秒）
            connection.setConnectTimeout(40000);
			// 设置从主机读取数据超时（单位：毫秒）
            connection.setReadTimeout(40000);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
////            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	log.info("发送GET请求出现异常！" + url + param);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;

	}

	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		StringBuffer sb_res = new StringBuffer();
		BufferedReader in = null;
		URLConnection connection = null;
		try {
			String urlNameString = "";
			if (StringUtils.isNotEmpty(param)) {
				urlNameString = url + "?" + param;
			} else {
				urlNameString = url;
			}
			URL realUrl = new URL(urlNameString);
			
			trustAllHosts();
			
			HttpsURLConnection https = (HttpsURLConnection)realUrl.openConnection();
			if (realUrl.getProtocol().toLowerCase().equals("https")) {
				https.setHostnameVerifier(DO_NOT_VERIFY);
				connection = https;
			} else {
				// 打开和URL之间的连接
				connection = realUrl.openConnection();
			}

			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setConnectTimeout(10000);
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
//			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
//				result += line;
				sb_res.append(line);
			}
		} catch (Exception e) {
			// log.error("运行异常",e);  //可以获取异常的stack
			log.error("发送GET请求出现异常！", e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				log.error("关闭GET请求输入流出现异常！", e2);
			}
		}
		return sb_res.toString();
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(10000);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("发送 POST 请求出现异常", e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				log.error("关闭POST请求输出流、输入流出现异常！", ex);
			}
		}
		return result;
	}
	
	public static String httpGet(String httpUrl) {
		BufferedReader input = null;
		StringBuilder sb = null;
		URL url = null;
		HttpURLConnection con = null;
		try {
			url = new URL(httpUrl);
			try {
				// trust all hosts
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
				if (url.getProtocol().toLowerCase().equals("https")) {
					https.setHostnameVerifier(DO_NOT_VERIFY);
					con = https;
				} else {
					con = (HttpURLConnection)url.openConnection();
				}
				https.setConnectTimeout(10000);
				input = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				sb = new StringBuilder();
				String line;
				while ((line = input.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} finally {
			// close buffered
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 断开连接会释放连接所保留的资源，以便关闭或重用这些资源
			if (con != null) {
				con.disconnect();
			}
		}

		return sb == null ? null : sb.toString();
	}

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * 信任每个服务器-不要检查任何证书
	 */
	private static void trustAllHosts() {
		final String TAG = "trustAllHosts";
		// 创建不验证证书链的信任管理器
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				//Log.i(TAG, "checkClientTrusted");
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				//Log.i(TAG, "checkServerTrusted");
			}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
				// Auto-generated method stub
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
				// Auto-generated method stub
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// 发送 GET 请求
		String s = HttpToServer.sendGetAutoChange("http://www.weather.com.cn/data/sk/101010100.html", "");
		System.out.println("s1:" + s);
//		s = HttpToServer.httpGet("https://www.btc100.com/api/trades?symbol=btc");
//		System.out.println("s2:" + s);
//		s = HttpToServer.sendGet("https://www.btc100.com/api/trades", "symbol=btc");
//		System.out.println("s3:" + s);

		// 发送 POST 请求
//		String sr = HttpToServer.sendPost("https://www.btc100.com/api/trades", "symbol=btc");
//		System.out.println("sr:" + sr);
	}
}