package xinquan.cn.diandian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

import cn.sharesdk.framework.ShareSDK;
import xinquan.cn.diandian.bean.RecommendBean;
import xinquan.cn.diandian.tools.MyHttpClient;
import xinquan.cn.diandian.tools.StringUtils;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.widget.Toast;

/*
 * 整个应用程序的枢纽，中转站，里面保存有整个应用程序公用的一些变量，属性，登录状态等等信息。
 */
public class MyApplication extends Application {
	public static String locationCityId;
	public static String locationCity;
	public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;
	public static ProgressDialog mypDialog; // 全局进度条
	public static SharedPreferences sp; // 本地存储SharedPreferences
	public static Editor ed; // 本地存储编辑器Editor
	private List<Activity> mList = new LinkedList<Activity>(); // 本地集合存放Activity引用
	private static MyApplication instance; // Myapplication对象
	public static LayoutInflater lf; // 布局填充器
	public static int with; // 屏幕宽
	public static int height; // 屏幕高
	public static MyHttpClient client; // 网络请求工具类客户端
	public static String seskey = ""; // 登录注册返回的身份秘钥
	public static Boolean login = false; // 登录状态
	public static Boolean addresscreade = false; // 是否更新地址
	public static Boolean fragment4needflash = false; // Fragment4是否需要刷新
	public static Boolean messageneedflash = false; // 消息是否需要刷新
	public static Boolean xuanhao = false; // 城市地区是否选好了
	public static String usernumber = ""; // 用户号
	public static String userName = "";
	public static boolean clearSex=false;
	public static Boolean usernumberneedflush = false; // 用户号是否需要刷新
	public static Boolean clentneedflush = false; // 客户列表是否需要刷新
	public static Boolean setactivityneedflush = false; // 设置Activity是否需要刷新
	public static Boolean fragment2needflush = false;   // 
	public static Boolean costomerDetailneedflush = false;//客户详情刷新
	public CacheManager mcCacheManager;
	
//	public static  ArrayList<RecommendBean> al = new ArrayList<RecommendBean>(); // 推荐列表数据源

	public void onCreate() {
		super.onCreate();
		/*
		 * 初始化SharedPreferences
		 */
		sp = getSharedPreferences("diandian", MODE_PRIVATE);
		ed = sp.edit();
		/*
		 * 初始化LayoutInflater
		 */
		lf = LayoutInflater.from(getApplicationContext());
		/*
		 * 初始化Volley框架的Http工具类
		 */
		client = MyHttpClient.getInstance(MyApplication.this.getApplicationContext());
		
		ShareSDK.initSDK(this);
		
		initEngineManager(this);
		
		mcCacheManager = new CacheManager(getApplicationContext());
		
	}
	
	public CacheManager getCacheManager () {
		if (mcCacheManager == null) {
			mcCacheManager = new CacheManager(getApplicationContext()); 
		}
		
		return mcCacheManager;
	}

	public synchronized static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}
	
	public boolean addRecommand(ArrayList<RecommendBean> al, RecommendBean bean){
		for(Iterator<RecommendBean> it = al.iterator(); it.hasNext();){
			if(StringUtils.isEqual(it.next().getHousesId(), bean.getHousesId()))
				return false;
		}
		return al.add(bean);		
	}

	
	// 全局进度条，在任何一个activity中都可以调用该进度条，传入当前context和需要显示的文字即可
		public static void progressShow(Context context, String title, String msg) {
			mypDialog = new ProgressDialog(context);
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置进度条风格，风格为长形，有刻度的
			if (title != null)
				mypDialog.setTitle(title);
			// 设置ProgressDialog 标题
			mypDialog.setMessage(msg);
			// 设置ProgressDialog 提示信息
			// mypDialog.setIcon(R.drawable.android);
			// //设置ProgressDialog 标题图标
			mypDialog.setProgress(59);
			// 设置ProgressDialog 进度条进度
			// mypDialog.setButton("地狱曙光",this);
			// //设置ProgressDialog 的一个Button
			mypDialog.setCancelable(true);
			// 设置ProgressDialog 是否可以按退回按键取消
			mypDialog.show();
			// 让ProgressDialog显示
		}
		
		// 关闭进度条
		public static void progressClose() {
			mypDialog.dismiss();
		}
	/*
	 * 将Activity加入到管理器中
	 */
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	/*
	 * 整个应用程序退出，循环遍历集合内没有销毁的Activity，全都销毁以后再退出，保证应用程序正常关闭退出
	 */
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		ShareSDK.stopSDK(this);
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static public class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                        "AndroidManifest.xml 文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
                MyApplication.getInstance().m_bKeyRight = false;
            }
            else{
            	MyApplication.getInstance().m_bKeyRight = true;
            	Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                        "key认证成功", Toast.LENGTH_LONG).show();
            }
        }
    }

  //获取拼接出来的请求字符串
  	public static String getUrl(HashMap<String, String> ha)
  	{
  		Iterator iter=ha.entrySet().iterator();
  		String url=UrlPath.baseURL;
  		int count=0;
  		while (iter.hasNext()) {
  			HashMap.Entry entry = (HashMap.Entry) iter.next();
  			Object key = entry.getKey();
  			Object val = entry.getValue();
  			if(count==0)
  				url=url+"?"+key+"="+val;
  			else
  				url=url+"&"+key+"="+val;
  			count++;
  			}
  		return url;
  	}

}
