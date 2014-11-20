package xinquan.cn.diandian.no1activitys;

import java.util.HashMap;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.share.ShareSDKHelper;
import xinquan.cn.diandian.tools.StringUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.friends.Wechat.ShareParams;
import cn.sharesdk.wechat.moments.WechatMoments;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public class ShareWindow implements OnClickListener{
	private Activity activity;
	private Button cancelBtn;
	private Dialog dialog;
	
	private String share_url;

	private ShareWindow(Activity activity) {
		this.activity = activity;
		initMusicDialog();
	}
	
	public static ShareWindow getInstance(Activity activity) {
		return new ShareWindow(activity);
	}
 
	private void initMusicDialog() {
		View view = activity.getLayoutInflater().inflate(R.layout.share_window, null);
		cancelBtn = (Button) view.findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(this);
		(view.findViewById(R.id.weixin)).setOnClickListener(this);
		(view.findViewById(R.id.xinlangweibo)).setOnClickListener(this);
		(view.findViewById(R.id.TcentWeibo)).setOnClickListener(this);
		(view.findViewById(R.id.pengyouquan)).setOnClickListener(this);
		(view.findViewById(R.id.shortSMS)).setOnClickListener(this);
		
		dialog = new Dialog(activity, R.style.ShareDialog);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
	    window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
	}
	public void show(String share_url) {
		this.share_url = share_url;
		if (dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			dismiss();
			switch (v.getId()) {
			case R.id.weixin:
				shareToWeiXin();
				break;
			case R.id.TcentWeibo:
			case R.id.xinlangweibo:
				new ShareSDKHelper(activity).showShare("分享", "点点买房", (StringUtils.isEmpty(share_url)?"点点买房":share_url), null, "weibo", paListener);
				break;
				
			case R.id.shortSMS:
				shareToShortMessage();
				break;
				
			case R.id.pengyouquan:
				shareToPengYou();
				break;
			case R.id.cancel:
				
				break;

			default:
				break;
			}
			
		} catch (Exception e) {
			Log.e("close_click", e.getMessage(), e);
		}
	}
		PlatformActionListener paListener = new PlatformActionListener() {

		public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
			
			Log.d(getClass().getSimpleName(), res.toString());
			handler.sendEmptyMessage(1);
		}

		public void onError(Platform plat, int action, Throwable t) {
			t.printStackTrace();
			handler.sendEmptyMessage(2);
		}

		public void onCancel(Platform plat, int action) {
			// 在这里添加取消分享的处理代码
		}

	};

	
	private void shareToWeiXin() {
		ShareParams sp = new ShareParams();
		sp.setTitle("微信分享");
		sp.setText("点点卖房");
		sp.setShareType(Platform.SHARE_TEXT);
		
		Platform pl = ShareSDK.getPlatform(Wechat.NAME);
		pl.setPlatformActionListener(paListener);
		pl.share(sp);
	}
	
	private void shareToPengYou() {
		ShareParams sp = new ShareParams();
		sp.setTitle("微信分享");
		sp.setText("点点卖房, 推荐房源得佣金");
		sp.setShareType(Platform.SHARE_TEXT);
		
		Platform pl = ShareSDK.getPlatform(WechatMoments.NAME);
		pl.setPlatformActionListener(paListener);
		pl.share(sp);
	}
	
	private void shareToShortMessage() {
		ShareParams sp = new ShareParams();
		sp.setTitle("微信分享");
		sp.setText("点点卖房, 推荐房源得佣金");
		sp.setShareType(Platform.SHARE_TEXT);
		
		Platform pl = ShareSDK.getPlatform("WechatMoments");
		pl.setPlatformActionListener(paListener);
		pl.share(sp);
	}
	
/*	微信
	AppID：wxe91f0cf7ad6a72b9
	AppSecret：af8e1ac0bcbd4c0e596c8f6dcd4a640f*/
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
			}else if(msg.what==2){
				Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
}
