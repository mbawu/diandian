package xinquan.cn.diandian.tools;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 
 * 文件上传工具类
 * 
 */
public class FileUpTheard implements Runnable {
	private Handler ha;
	private String url;
	private byte[] bt;

	public FileUpTheard(Handler ha, String url, byte[] bt) {
		this.ha = ha;
		this.url = url;
		this.bt = bt;
	}

	public void run() {
		Log.e("uiuiuiuiu", "开始FileUt.uploadFile");
		String m = FileUt.uploadFile(url, bt);
		Message ms = new Message();
		ms.obj = m;
		ha.sendMessage(ms);
	}

}
