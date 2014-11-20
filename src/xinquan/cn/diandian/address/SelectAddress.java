package xinquan.cn.diandian.address;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import xinquan.cn.diandian.MyApplication;
import xinquan.cn.diandian.R;
import xinquan.cn.diandian.UrlPath;
import xinquan.cn.diandian.address.SideBar.OnTouchingLetterChangedListener;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 地区选择
 */
public class SelectAddress extends Activity {
	private ListView sortListView; // 城市列表ListView
	private SideBar sideBar; // 右侧拼音排序列表
	private TextView dialog;
	private SortAdapter adapter; // listView 适配器
	private ClearEditText mClearEditText; // 自定义EditText
	private CharacterParser characterParser; // 汉字拼音类
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectaddress);
		initViews();
		initData();
		initlistener();
	}

	private void initlistener() {
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				MyApplication.ed.putString("cityname",
						((SortModel) adapter.getItem(position)).getName());
				MyApplication.ed.putString("cityid",
						((SortModel) adapter.getItem(position)).getId());
				MyApplication.ed.commit();
				MyApplication.addresscreade = true;
				SelectAddress.this.finish();
			}
		});
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void initData() {
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
								SourceDateList = filledData(ja);
								Log.e("mmmmm", "" + SourceDateList.size());
								Collections.sort(SourceDateList,
										pinyinComparator);
								adapter.updateListView(SourceDateList);
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

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		if (SourceDateList == null) {
			SourceDateList = new ArrayList<SortModel>();
		}
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	/*
	 * private List<SortModel> filledData(String[] date) { List<SortModel>
	 * mSortList = new ArrayList<SortModel>(); for (int i = 0; i < date.length;
	 * i++) { SortModel sortModel = new SortModel(); sortModel.setName(date[i]);
	 * // 汉字转换成拼音 String pinyin = characterParser.getSelling(date[i]); String
	 * sortString = pinyin.substring(0, 1).toUpperCase(); // 正则表达式，判断首字母是否是英文字母
	 * if (sortString.matches("[A-Z]")) {
	 * sortModel.setSortLetters(sortString.toUpperCase()); } else {
	 * sortModel.setSortLetters("#"); }
	 * 
	 * mSortList.add(sortModel); } return mSortList;
	 * 
	 * }
	 */

	private List<SortModel> filledData(JSONArray ja) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < ja.length(); i++) {

			try {
				JSONObject jb = ja.getJSONObject(i);
				SortModel sortModel = new SortModel();
				sortModel.setName(jb.getString("name"));
				sortModel.setId(jb.getString("cityId"));
				// 汉字转换成拼音
				String pinyin = characterParser
						.getSelling(jb.getString("name"));
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}

				mSortList.add(sortModel);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
