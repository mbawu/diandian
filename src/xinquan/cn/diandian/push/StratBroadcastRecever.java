package xinquan.cn.diandian.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 开机事件广播接受者
 */
public class StratBroadcastRecever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, MyServer.class));
	}

}
