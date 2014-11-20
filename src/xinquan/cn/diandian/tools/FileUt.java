package xinquan.cn.diandian.tools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * 文件操作工具类
 * 
 */
public class FileUt {
	public static final String GAME_WORK_IMAGE = Environment
			.getExternalStorageDirectory() + "/diandian";

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Boolean hasSD() { //
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	// url-->InputStream--->byte[]--->bitmap

	public static String uploadFile(String uploadUrl, byte[] bt) { //
		Log.e("uiuiuiuiu", "111111111111");
		String srcPath = FileUt.GAME_WORK_IMAGE + System.currentTimeMillis()
				+ ".jpg";
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		String result = "";
		Log.e("uiuiuiuiu", "222222222222");
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			Log.e("uiuiuiuiu", "2");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			Log.e("uiuiuiuiu", "3");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			Thread.sleep(100);
			Log.e("uiuiuiuiu", "6");
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
					+ srcPath.substring(srcPath.lastIndexOf("/") + 1)
					+ "\""
					+ end);
			dos.writeBytes(end);
			dos.write(bt, 0, bt.length);
			Log.e("uiuiuiuiu", "7" + bt.length);
			dos.writeBytes(end);
			Log.e("uiuiuiuiu", "10");
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			Log.e("uiuiuiuiu", "11");
			dos.flush();
			Log.e("uiuiuiuiu", "8");
			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			Log.e("uiuiuiuiu", "9");
			result = br.readLine();

			Log.e("uiuiuiuiu", "333333333333333");
			dos.close();
			is.close();
		} catch (Exception e) {
			Log.e("uiuiuiuiu", "yichang");
			e.printStackTrace();
		}
		Log.e("uiuiuiuiu", "44444444444444444");
		Log.e("uiuiuiuiu", "------" + result);
		return result;
	}

	public static void saveFileSD(Bitmap bt, String filename)
			throws IOException { // 淇濆瓨鍒癝D鍗?
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				bitmap2Bytes(bt));
		saveDataByWang(inputStream, filename);
	}

	public static void saveDataByWang(InputStream is, String fileName)
			throws IOException {
		File file = new File(fileName);
		FileOutputStream os = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		os.flush();
		os.close();
	}

}