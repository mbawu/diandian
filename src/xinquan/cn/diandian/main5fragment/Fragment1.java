package xinquan.cn.diandian.main5fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinquan.cn.diandian.MainActivity;
import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.TitleBarContainer;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.address.SelectAddress;
import xinquan.cn.diandian.bean.AreaBean;
import xinquan.cn.diandian.bean.Fragment1houseBean;
import xinquan.cn.diandian.bean.TypeBean;
import xinquan.cn.diandian.no1activitys.GroupbuyingActivity;
import xinquan.cn.diandian.no1activitys.HouseDetails;
import xinquan.cn.diandian.no1activitys.MessageActivity;
import xinquan.cn.diandian.no1activitys.TravelingActivity;
import xinquan.cn.diandian.tools.StringUtils;
import xinquan.cn.diandian.widget.BadgeView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/*
 * 第一个Fragment   楼盘Fragment，对应楼盘第一级界面
 */
public class Fragment1 extends Fragment implements OnClickListener {
	private static final String tag = "Fragment1";
	private View v; // Fragment整体View
	private LinearLayout ly1; // 特惠团购
	private LinearLayout ly2; // 旅游地产
	private Spinner sp1; // 区域spinner
	private Spinner sp2; // 类别spinner
	private Spad1 spad1; // 区域spinner适配器
	private Spad2 spad2; // 类别spinner适配器
	private BadgeView mBadgeView;
	private String loadCityName;
	/*
	 * 区域以及类型的Spinner数据源
	 */
	private List<AreaBean> splist1 = new ArrayList<AreaBean>();
	private List<TypeBean> splist2 = new ArrayList<TypeBean>();
	/*
	 * 区域以及类型的Spinner数据源中转集合（由于Spinner只可接收String类型的一组数据，此方法在于重新中转一下数据并且做上标记）
	 */
	private ArrayList<String> al1 = new ArrayList<String>();
	private ArrayList<String> al2 = new ArrayList<String>();
	private ListView lv;
	private Myad ad;
	private List<Fragment1houseBean> li = new ArrayList<Fragment1houseBean>();
	private LinearLayout pricesort;
	private TextView pricesorttv;
	private ImageView pricesortim;
	private int page = 1;
	private int totalpage = 1;
	private int lastitem;
	private Boolean jiazai = true;
	private Button intercept;
	private View footView;
	private String typee = "";
	private String areaa = "";
	private String pricesortt = "-1";
	private Boolean onece2 = true;
	private Boolean onece1 = true;
	private TitleBarContainer mTitleBar;
	private String cityName;
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	private boolean getCityId=false;
	
	//设置相关参数
	private void setLocationOption(){
	LocationClientOption option = new LocationClientOption();
	option.setOpenGps(true); //打开gps
	option.setServiceName("com.baidu.location.service_v2.9");
	option.setPoiExtraInfo(true);
	option.setAddrType("all");
	option.setPriority(LocationClientOption.NetWorkFirst);
	option.setPriority(LocationClientOption.GpsFirst);       //gps 
	option.setPoiNumber(10);
	option.disableCache(true);
	mLocationClient.setLocOption(option);
	}


	
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			loadCityName=location.getCity();
			Log.i("test", loadCityName);
			Toast.makeText(getActivity(), loadCityName, 2000).show();
			MyApplication.locationCity=loadCityName;
			getCityId();
			
//			MyApplication.addresscreade=true;
//			refreshList(true);
		}


		@Override
		public void onReceivePoi(BDLocation arg0) {
		}
		}
		@Override
		public void onDestroy() {
		mLocationClient.stop();
		super.onDestroy();
		}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment1, container, false);
			initview();
			initmessage();
			initdata();
			initlistener();
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}
	
    public void notifyBadgeChanged(String count) {
    	try {
    		if (mBadgeView == null) {
        		return;
        	}

    		if (StringUtils.isEqual(count, "0")) {
        		mBadgeView.hide();
        		return;
        	}
    		
        	mBadgeView.show();
        	mBadgeView.setText(count);
//        	mBadgeView.invalidate();
		} catch (Exception e) {
			Log.e("notifyBadgeChanged", e.getMessage(), e);
		}
    }

	/*
	 * 初始化未读消息数量
	 */
	private void initmessage() {
		if (MyApplication.login) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "user");
			ha.put("c", "user");
			ha.put("a", "get_count_msg");
			ha.put("user_key", MyApplication.seskey);
			ha.put("userId", MyApplication.sp.getString("userid", "-1"));
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("code") == 1) {
									notifyBadgeChanged(response.getString("number"));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
						}
					});
		}
	}
	String baiduURL = "http://api.map.baidu.com/geocoder?output=json&location=%f,%f&key=640955aaf38699ed0422529dab964593";
	/*
	 * 判断用户选择了城市没有，如果没有选择城市，直接跳转到城市选择列表
	 * 
	 *  "city":"北京市",
            "district":"丰台区",
            "province":"北京市",
            "street":"樊羊路",
            "street_number":""
	*/
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try{
			Location location = getLocation();
			HashMap<String, String> ha = new HashMap<String, String>();
			String url = String.format(baiduURL, location.getLatitude(), location.getLongitude());
			MyApplication.client.postWithURL(url, ha,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if ("OK".equals(response.getString("status"))) {
									JSONObject result = response.getJSONObject("result");
									if(result!=null){
										JSONObject addressComponent = result.getJSONObject("addressComponent");
										if(addressComponent!=null){
											cityName = addressComponent.getString("city");
											if (MyApplication.sp.getString("cityName", "-1").equals("-1")) {
//												startActivity(new Intent(getActivity(), SelectAddress.class));
												MyApplication.ed.putString("cityName", cityName);
												MyApplication.ed.commit();
											}
											Log.i(tag, "current location city=" + cityName);
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
						}
					});
		}catch(Exception e){
			e.printStackTrace();
		}
//		if (MyApplication.sp.getString("cityid", "-1").equals("-1")) {
////			startActivity(new Intent(getActivity(), SelectAddress.class));
//			MyApplication.ed.putString("cityid", "5");
//			MyApplication.ed.commit();
//		}
		//add data
	}
	
	private void getCityId() {
		// 根据a-z进行排序源数据
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "city");
		ha.put("c", "city");
		ha.put("a", "get_town");
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							Log.i("test", "response-->"+response.toString());
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								String city=MyApplication.locationCity.substring(0, MyApplication.locationCity.indexOf("市"));
								for (int i = 0; i < ja.length(); i++) {
									JSONObject ob= ja.getJSONObject(i);
									String name=ob.getString("name").toString().trim();
									if(name.equals(city))
									{
										MyApplication.locationCityId=ob.getString("cityId");
										Log.i("test",""+"locationCityId-->"+ MyApplication.locationCityId);
										MyApplication.ed.putString("cityid", MyApplication.locationCityId);
										MyApplication.ed.putString("cityname", city);
										Log.i("test", "loadCityName"+loadCityName);
										MyApplication.ed.commit();
										Log.i("test", "cityName"+MyApplication.sp.getString("cityname",city));
										getCityId=true;
										cityName=city;
										mTitleBar.setLeftTextResource(city, true);
										initSp1data(true);
										initSp2data();
//										initlvdata();
									}
								}
								//如果没有在城市列表找到该城市的话
								if(!getCityId)
								{
									JSONObject ob= ja.getJSONObject(0);
									String name=ob.getString("name").toString().trim();
									MyApplication.locationCityId=ob.getString("cityId");
									MyApplication.ed.putString("cityid", MyApplication.locationCityId);
									MyApplication.ed.putString("cityname", city);
									MyApplication.ed.commit();
									cityName=name;
									mTitleBar.setLeftTextResource(cityName, true);
									initSp1data(true);
									initSp2data();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("mmmmm", "cuowu");
					}
				});

	}
	
	private Location getLocation()
    {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getActivity().getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
//        updateToNewLocation(location);
        // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
//        locationManager.requestLocationUpdates(provider, 100 * 1000, 500, locationListener);    
        return location;
    }


	/*
	 * 初始化监听器
	 */
	private void initlistener() {
		/*
		 * 区域Spinner设置选中监听器，只要选中了 就要通知界面刷新楼盘列表信息
		 */
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					areaa = "";
					page = 1;
					totalpage = 1;
					li.clear();
					Log.e("dsfssgsaw", "55");
					if (onece1) {
						onece1 = false;
						initlvdata();
					} else {
						initlvdata();
					}
				} else {
					AreaBean ab = splist1.get(arg2);
					areaa = (String) ab.getCityid();
					page = 1;
					totalpage = 1;
					li.clear();
					Log.e("city id:", ab.getCityid());
					initlvdata();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		/*
		 * 类型Spinner设置选中监听器，只要选中了 就要通知界面刷新楼盘列表信息
		 */
		sp2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					typee = "";
					page = 1;
					totalpage = 1;
					li.clear();
					if (onece2) {
						onece2 = false;
						initSp1data(false);
					} else {
						initlvdata();
					}
				} else {
					TypeBean tb = splist2.get(arg2);
					typee = (String) tb.getTypeid();
					page = 1;
					totalpage = 1;
					li.clear();
					Log.e("dsfssgsaw", "88");
					initlvdata();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		/*
		 * 特惠团购和旅游地产设置监听器
		 */
		ly1.setOnClickListener(this);
		ly2.setOnClickListener(this);
		/*
		 * 选择城市设置监听器
		 */
		mTitleBar.setLeftOnClickListener(this).setRightOnClickListener(this);
		pricesort.setOnClickListener(this);
		/*
		 * 楼盘列表ListView设置OnItemClickListener监听器
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String tag = (String) arg1.findViewById(R.id.house_im).getTag();
				Intent in = new Intent(getActivity(), HouseDetails.class);
				in.putExtra("id", tag);
				startActivity(in);
			}
		});
		/*
		 * ListView设置界面滑动时候的监听器
		 */
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.e("test", "frag1-->"+jiazai);
				if (lastitem == (page - 1) * 5 + 1 && jiazai) {
					Log.e("test", "frag1-->if"+page);
					jiazai = false;
					intercept.setVisibility(View.GONE);
					footView.setVisibility(View.VISIBLE);
					Log.e("dsfssgsaw", "99");
					initlvdata();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				lastitem = totalItemCount;
			}
		});

	}

	/*
	 * 楼盘列表ListView数据刷新加载
	 */
	private void initlvdata() {
		/*
		 * 如果服务器还有数据 接着请求
		 */
		if (page <= totalpage) {
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "houses");
			ha.put("c", "houses");
			ha.put("a", "houses");
			String cityId = MyApplication.sp.getString("cityid", "5");
			String cityName = MyApplication.sp.getString("cityname", loadCityName);
			String areaId = StringUtils.isEmpty(areaa)? "2": areaa;
			if(StringUtils.isEmpty(areaa)){
				ha.put("cityId", cityId);
				if(!StringUtils.isEmpty(cityName)){
					ha.put("city_name",  cityName);
				}
			}else{
				ha.put("area_cityId", areaId);
			}
			ha.put("page", "" + page);
			ha.put("price", pricesortt);
			ha.put("type", typee);
			ha.put("rowCount", "5");
			Log.i("test", "frg1-list-->"+MyApplication.getUrl(ha));
//			StringBuffer sb = new StringBuffer();
//			sb.append(UrlPath.baseURL).append("?");
//			for(Iterator<String> it = ha.keySet().iterator(); it.hasNext();){
//				String key = it.next();
//				sb.append(key).append("=").append(ha.get(key)).append("&");
//			}
//			Log.i(tag, "gethouses" + sb.toString());
			/*
			 * 发送请求，从服务器获取ListView数据
			 */
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("code") == 1) {
									page = page + 1;
									totalpage = Integer.parseInt(response
											.getString("totlepage"));
									JSONArray ja = response
											.getJSONArray("list");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										Fragment1houseBean fh = new Fragment1houseBean();
										fh.setHousesid(jb.getString("housesId"));
										fh.setHousesname(jb
												.getString("housesName"));
										fh.setDiscount_note(jb
												.getString("discount_note"));
										fh.setPicture(jb.getString("picture"));
										fh.setPrice(StringUtils.convertIntString(jb.getString("price")));
										fh.setArea_name(jb
												.getString("area_name"));
										li.add(fh);
									}
									/*
									 * 通知ListView刷新，隐藏footView,隐藏intercept
									 */
									ad.notifyDataSetChanged();
									footView.setVisibility(View.GONE);
									intercept.setVisibility(View.GONE);
								} else {
									ad.notifyDataSetChanged();
									footView.setVisibility(View.GONE);
									intercept.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								footView.setVisibility(View.GONE);
								intercept.setVisibility(View.GONE);
								e.printStackTrace();
							}
							jiazai = true;
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							footView.setVisibility(View.GONE);
							intercept.setVisibility(View.GONE);
							jiazai = true;
						}
					});
		}

	}

	/*
	 * 初始化Fragment1数据
	 */
	private void initdata() {
		
		setLocationOption();
		mLocationClient.start();

		
	}

	/*
	 * 初始化类型Spinner数据
	 */
	private void initSp2data() {
		HashMap<String, String> ha = new HashMap<String, String>();
		ha.put("m", "houses");
		ha.put("c", "houses");
		ha.put("a", "get_houses_type");
		MyApplication.client.postWithURL(UrlPath.baseURL, ha,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getInt("code") == 1) {
								JSONArray ja = response.getJSONArray("list");
								if (splist2 == null) {
									splist2 = new ArrayList<TypeBean>();
								} else {
									splist2.clear();
									al2.clear();
								}
								splist2.add(new TypeBean());
								al2.add("类型");
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									TypeBean tb = new TypeBean();
									tb.setType_name(jb.getString("type_name"));
									tb.setTypeid(jb.getString("typeId"));
									splist2.add(tb);
									al2.add(jb.getString("type_name"));
								}
								spad2.notifyDataSetChanged();
								sp2.setSelection(0);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
	}

	/*
	 * 初始化区域Spinner数据
	 */
	private void initSp1data(boolean needRefresh) {
		if (!MyApplication.sp.getString("cityid", "5").equals("-1") || needRefresh) {
			al1.clear();
			al1.add("区域");
			splist1.clear();
			spad1.notifyDataSetChanged();
			HashMap<String, String> ha = new HashMap<String, String>();
			ha.put("m", "city");
			ha.put("c", "city");
			ha.put("a", "city");
			ha.put("parentId", MyApplication.sp.getString("cityid", "-1"));
			MyApplication.client.postWithURL(UrlPath.baseURL, ha,
					new Listener<JSONObject>() {
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("code") == 1) {
									JSONArray ja = response
											.getJSONArray("list");
									if (splist1 == null) {
										splist1 = new ArrayList<AreaBean>();
									} else {
										splist1.clear();
										al1.clear();
									}
									splist1.add(new AreaBean());
									al1.add("区域");
									for (int i = 0; i < ja.length(); i++) {
										JSONObject jb = ja.getJSONObject(i);
										AreaBean ab = new AreaBean();
										ab.setCityid(jb.getString("cityId"));
										ab.setName(jb.getString("name"));
										splist1.add(ab);
										al1.add(jb.getString("name"));
									}
									spad1.notifyDataSetChanged();
									sp1.setSelection(0);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {

						}
					});

		} else {
			Log.e("sdsgsgsg", "sp1城市ID为空");
		}
	}

	/*
	 * 初始化View的引用
	 */
	private void initview() {
		mTitleBar = new TitleBarContainer(v, R.string.title_diandian);
		mTitleBar.setLeftMenuResource(R.drawable.adress_im).setRightTextResource(R.string.message, false);
		mLocationClient = new LocationClient( v.getContext() );
		mLocationClient.registerLocationListener( myListener );
		footView = MyApplication.lf.inflate(R.layout.footview, null);
		intercept = (Button) v.findViewById(R.id.intercept);
		ly1 = (LinearLayout) v.findViewById(R.id.ly1);
		ly2 = (LinearLayout) v.findViewById(R.id.ly2);
		sp1 = (Spinner) v.findViewById(R.id.sp1);
		sp2 = (Spinner) v.findViewById(R.id.sp2);
		spad1 = new Spad1(getActivity(), R.layout.sp_tv, R.id.txtvwSpinner, al1);
		spad2 = new Spad2(getActivity(), R.layout.sp_tv, R.id.txtvwSpinner, al2);
		sp1.setAdapter(spad1);
		sp2.setAdapter(spad2);
		spad1.setDropDownViewResource(R.layout.sp_tv_item);
		spad2.setDropDownViewResource(R.layout.sp_tv_item);
		lv = (ListView) v.findViewById(R.id.lv);
		ad = new Myad();
		/*
		 * ListView初始化之前添加footView 否则会报错
		 */
		lv.addFooterView(footView);
		lv.setAdapter(ad);
		pricesort = (LinearLayout) v.findViewById(R.id.pricesort);
		pricesortim = (ImageView) v.findViewById(R.id.pricesort_im);
		pricesorttv = (TextView) v.findViewById(R.id.pricesort_tv);
		
		Context context = getActivity();
		mBadgeView = new BadgeView(context, mTitleBar.getBadgeView(false));
		mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		mBadgeView.setBadgeMargin(20);
		notifyBadgeChanged("0");
	}

	/*
	 * 楼盘ListView适配器
	 */
	private class Myad extends BaseAdapter {
		@Override
		public int getCount() {
			if (li == null) {
				return 0;
			} else {
				return li.size();
			}

		}

		@Override
		public Object getItem(int position) {
			
			return li.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Fragment1houseBean hb = li.get(position);
			if (convertView == null) {
				convertView = MyApplication.lf.inflate(
						R.layout.fragment1_house_item, null);
			}
			NetworkImageView house_im = (NetworkImageView) convertView
					.findViewById(R.id.house_im);
			TextView house_name = (TextView) convertView
					.findViewById(R.id.house_name);
			TextView house_price = (TextView) convertView
					.findViewById(R.id.house_price);
			TextView discount_note = (TextView) convertView
					.findViewById(R.id.discount_note);
			house_name.setText("【"+hb.getArea_name()+"】"+hb.getHousesname());
			house_price.setText(StringUtils.getPriceString(getActivity(), hb.getPrice()));
			if(discount_note != null)
				discount_note.setText(hb.getDiscount_note());
			
			MyApplication.client.getImageForNetImageView(hb.getPicture(),
					house_im, R.drawable.ic_launcher);
			house_im.setTag(hb.getHousesid());
			return convertView;
		}
	}

	/*
	 * 当Fragment可获取焦点的时候判断用户是否重新选择了城市，如果选择了 表示数据需要刷新
	 * 当Fragment可获取焦点的时候判断消息是否需要刷新，如果需要刷新就重新刷新右上角的未读消息FSSS
	 */
	public void onResume() {
		super.onResume();
		
	}
	
	public void refreshList(boolean init){
		if (MyApplication.addresscreade || init) {
			mTitleBar.setLeftTextResource(MyApplication.sp.getString("cityname", loadCityName), true);
			page = 1;
			totalpage = 1;
			initSp2data();
			initSp1data(true);
			li.clear();
			Log.e("dsfssgsaw", "22");
			initlvdata();
			MyApplication.addresscreade = false;
		}
//		if (MyApplication.messageneedflash) {
			initmessage();
//			MyApplication.messageneedflash = false;
//		}
	}

	/*
	 * 所有点击事件枢纽
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * 地址切换触发
		 */
		case R.id.title_icon_left_layout:
			getActivity().startActivityForResult(
					new Intent(getActivity(), SelectAddress.class), MainActivity.chooseAddress);
			break;
		/*
		 * 右上角消息按钮触发
		 */
		case R.id.title_icon_right_layout:
			getActivity().startActivity(
					new Intent(getActivity(), MessageActivity.class));
			break;
		/*
		 * 特惠团购触发
		 */
		case R.id.ly1:
			startActivity(new Intent(getActivity(), GroupbuyingActivity.class));
			break;
		/*
		 * 旅游地产触发
		 */
		case R.id.ly2:
			startActivity(new Intent(getActivity(), TravelingActivity.class));
			break;
		/*
		 * 按价格排序触发
		 */
		case R.id.pricesort:
			if (pricesorttv.getText().toString().equals("升序")) {
				pricesorttv.setText("降序");
				page = 1;
				totalpage = 1;
				li.clear();
				if (pricesortt.equals("1")) {
					pricesortt = "2";
				} else {
					pricesortt = "1";
				}
				Log.e("dsfssgsaw", "33");
				initlvdata();
				
			} else {
				pricesorttv.setText("升序");
				page = 1;
				totalpage = 1;
				li.clear();
				if (pricesortt.equals("1")) {
					pricesortt = "2";
				} else {
					pricesortt = "1";
				}
				Log.e("dsfssgsaw", "44");
				initlvdata();
			}

			break;
		default:
			break;
		}
		

	}

	/*
	 * 区域Spinner适配器
	 */
	private class Spad1 extends ArrayAdapter<String> {

		public Spad1(Context context, int textViewResourceId, int viewId,
				List<String> objects) {
			super(context, textViewResourceId, viewId, objects);
		}

	}

	/*
	 * 类型Spinner适配器
	 */
	private class Spad2 extends ArrayAdapter<String> {

		public Spad2(Context context, int textViewResourceId, int viewId,
				List<String> objects) {
			super(context, textViewResourceId, viewId, objects);
		}

	}
}
