package xinquan.cn.diandian.share;

import java.util.ArrayList;
import java.util.HashMap;

import xinquan.cn.diandian.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.system.text.ShortMessage.ShareParams;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;

public class ShareSDKHelper implements Callback{
	
	private Context context;
	private ArrayList<Platform> list = new ArrayList<Platform>();
	
	public ShareSDKHelper(Context context){
		this.context = context;
			
		list.add(new SinaWeibo(context));
		list.add(new Wechat(context));
		list.add(new TencentWeibo(context));
//		list.add(new Douban(context));
//		list.add(new Renren(context));
	}
	
	PlatformActionListener dummyListener = new PlatformActionListener() {

		public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
			Log.d(getClass().getSimpleName(), res.toString());
			// 在这里添加分享成功的处理代码
		}

		public void onError(Platform plat, int action, Throwable t) {
			t.printStackTrace();
			// 在这里添加分享失败的处理代码
		}

		public void onCancel(Platform plat, int action) {
			// 在这里添加取消分享的处理代码
		}

	};
	
	 public void showShare(String title, String message, String imagePath, String link, PlatformActionListener paListener) {
	        OnekeyShare oks = new OnekeyShare();
	        //关闭sso授权
	        oks.disableSSOWhenAuthorize();
	        
	        // 分享时Notification的图标和文字
	        oks.setNotification(R.drawable.app_icon, context.getString(R.string.app_name));
	        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	        oks.setTitle(title);
	        oks.setTitleUrl(link);
	        // text是分享文本，所有平台都需要这个字段
	        String str = title+message;
//	        if(str.length()>50)
//	        	str = str.substring(0, 50)+"...";
	        
	        oks.setText(str);
	        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//	        oks.setImagePath("/sdcard/test.jpg");
	        
	        if(imagePath != null) {
	        	oks.setImagePath(imagePath);	
	        }
	        
	        
	        	// url仅在微信（包括好友和朋友圈）中使用
	        	oks.setUrl(link);
	        
	        Log.i("test", "title-->"+title+"  message-->"+message);
	        Log.i("test", "link-->"+link);
	        Log.i("test", "imagePath-->"+imagePath);
	        // 启动分享GUI
	        oks.show(context);
	        Log.i("test", "oks.show");
	   }
	 
	 public static void shareShortMessage(String title, String content, String link, PlatformActionListener paListener) {
		 Platform p = ShareSDK.getPlatform(ShortMessage.NAME);
		 p.setPlatformActionListener(paListener);
		 
		 ShareParams sp = new ShareParams();
		 sp.setShareType(ShortMessage.SHARE_TEXT);
		 sp.setTitle(title);
		 sp.setText(content+link);
		 sp.setUrl(link);
		 p.share(sp);
	 }
	
	public void showShare(String title, String content, String link, String imagePath, String platform, PlatformActionListener paListener) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.app_icon, context.getString(R.string.app_name));
		oks.setAddress("12345678901");
		oks.setTitle("点点卖房");
		oks.setTitleUrl(link);
		oks.setText(content + " " + link);
		Log.i("test", "新浪微博--》"+imagePath);
		if(imagePath!=null)
			oks.setImagePath(imagePath);
//		oks.setImageUrl(MainActivity.TEST_IMAGE_URL);
		oks.setUrl(link);
//		oks.setFilePath(MainActivity.TEST_IMAGE);
//		oks.setComment(getContext().getString(R.string.share));
		oks.setSite(context.getString(R.string.app_name));
		oks.setSiteUrl(link);
		oks.setVenueName("点点卖房");
		oks.setVenueDescription("点点卖房");
//		oks.setLatitude(23.056081f);
//		oks.setLongitude(113.385708f);
		oks.setSilent(false);
		oks.setDialogMode();
		if (platform != null) {
			oks.setPlatform(platform);
		}

		// 取消注释，可以实现对具体的View进行截屏分享
//		oks.setViewToShare(getPage());

		// 去除注释，可令编辑页面显示为Dialog模式
//		oks.setDialogMode();

		// 去除注释，在自动授权时可以禁用SSO方式
//		oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		oks.setCallback(paListener);
//		oks.setShareContentCustomizeCallback(paListener);


		oks.show(context);
	}

//	public void shareToWeibo(String message, String imagePath, PlatformActionListener paListener){
//		ShareParams sp = new ShareParams();
//		sp.setText(message);
//		if(imagePath != null)
//			sp.setImagePath(imagePath);
//
//		Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
//		weibo.setPlatformActionListener(paListener); // 设置分享事件回调
//		// 执行图文分享
//		weibo.share(sp);
//	}
//	
//	public void shareToQQSpace(String message, String imagePath, PlatformActionListener paListener){
//		ShareParams sp = new ShareParams();
//		sp.setTitle("测试分享的标题");
//		sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//		sp.setText(message);
//		sp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
//		sp.setSite("发布分享的网站名称");
//		sp.setSiteUrl("发布分享网站的地址");
//
//		Platform qzone = ShareSDK.getPlatform (context, QZone.NAME);
//		qzone. setPlatformActionListener (paListener); // 设置分享事件回调
//		// 执行图文分享
//		qzone.share(sp);
//	}
//	
//	public void shareToDouban(String message, String imagePath){
//		
//	}
//	
//	public void shareToRenren(String message, String imagePath){
//		
//	}
	
	public void authorize(Platform plat, PlatformActionListener paListener) {
        if (plat == null) {
                //popupOthers();
                return;
        }
        
        if(plat.isValid()) {
                String userId = plat.getDb().getUserId();
                if (userId != null) {
                        UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
//                        login(plat.getName(), userId, null);
                        return;
                }
        }
        plat.setPlatformActionListener(paListener);
        // true不使用SSO授权，false使用SSO授权
        plat.SSOSetting(true);
        plat.showUser(null);
	}
	
	public boolean isLogined(Platform plat) {
        if (plat == null) {
                //popupOthers();
                return false;
        }
        
        if(plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (userId != null) {
                    return true;
            }
        }
        return false;
	}
	
	public void updateContext(Context context) {
		this.context = context;
	}
	
//	public void generateTokenFromThirdPartLogin(Platform plat) throws ResponseException{
//		if (plat != null) {
//			User user = null;
//			String flag = getPlatformFlag(plat);
//			String nick_name = plat.getDb().getUserName();
//			String userId = plat.getDb().getUserId();
//			String token = YouYiApplication.getInstance()
//					.getServiceManager().loginThirdParty(flag, userId, nick_name);
//			if (!StringUtils.isEmpty(token)) {
//				user = new User();
//				user.setUserid(plat.getDb().getUserId());
//				user.setAvatarPath(plat.getDb().getUserIcon());
//				user.setNickname(nick_name);
//				user.setFlag(flag);
//				user.setToken(token);
//				Log.e("Roney", user.toString());
//			}
////			user.setToken(plat.getDb().getToken());
//			Preferences.getInstance().setUser(user);
//		}
//	}
	static final String PLATFORM_FLAG_QQ= "qq";
	static final String PLATFORM_FLAG_SINA= "sina";
	static final String PLATFORM_FLAG_RENREN= "renren";
	static final String PLATFORM_FLAG_DOUBAN= "douban";
	
	private String getPlatformFlag (Platform plat) {
		String result = PLATFORM_FLAG_QQ;
		if (plat instanceof SinaWeibo) {
			result = PLATFORM_FLAG_SINA;
		} 
		return result;
	}
	

	
	
	public void logout(){
		for(Platform plat : list){
			 if(plat.isValid()) {
	                plat.getDb().removeAccount();
			 }
		}
	}
	
	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR= 4;
	private static final int MSG_AUTH_COMPLETE = 5;
//	
//	PlatformActionListener loginListener = new PlatformActionListener() {
//		public void onComplete(Platform platform, int action,
//				HashMap<String, Object> res) {
//			if (action == Platform.ACTION_USER_INFOR) {
//				UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, ShareSDKHelper.this);
//				login(platform.getName(), platform.getDb().getUserId(), res);
//			}
//			System.out.println(res);
//		}
//		
//		public void onError(Platform platform, int action, Throwable t) {
//			if (action == Platform.ACTION_USER_INFOR) {
//				UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, ShareSDKHelper.this);
//			}
//			t.printStackTrace();
//		}
//		
//		public void onCancel(Platform platform, int action) {
//			if (action == Platform.ACTION_USER_INFOR) {
//				UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, ShareSDKHelper.this);
//			}
//		}
//	};
//	
//	private void login(String plat, String userId, HashMap<String, Object> userInfo) {
//		Message msg = new Message();
//		msg.what = MSG_LOGIN;
//		msg.obj = plat;
//		UIHandler.sendMessage(msg, this);
//	}
//	
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case MSG_USERID_FOUND: {
				Toast.makeText(context, R.string.userid_found, Toast.LENGTH_SHORT).show();
			}
			break;
			case MSG_LOGIN: {
				String text = context.getString(R.string.logining, msg.obj);
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
				
				Builder builder = new Builder(context);
				builder.setTitle(R.string.if_register_needed);
				builder.setMessage(R.string.after_auth);
				builder.setPositiveButton(R.string.ok, null);
				builder.create().show();
			}
			break;
			case MSG_AUTH_CANCEL: {
				Toast.makeText(context, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
			}
			break;
			case MSG_AUTH_ERROR: {
				Toast.makeText(context, R.string.auth_error, Toast.LENGTH_SHORT).show();
			}
			break;
			case MSG_AUTH_COMPLETE: {
				Toast.makeText(context, R.string.auth_complete, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return false;
	}
}
