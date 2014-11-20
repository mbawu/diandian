package xinquan.cn.diandian.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class EulaWebView extends WebView {

	private float downX = 0;
	private float upY = 0;
	private float xPrecision = 0;
	private boolean allowHorizontalScroll = false;

	public EulaWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public EulaWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EulaWebView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setHorizontalScrollBarEnabled(false);
		setVerticalScrollBarEnabled(false);
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
	}
	
	public void setAllowHorizontalScroll(boolean allow) {
		allowHorizontalScroll = allow;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		if (oldl != l && !isAllowHorizontalScroll()) {
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					EulaWebView.this.scrollTo(0, (int) upY);
				}
			});
		}
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		MotionEvent moveEvent = null;
		switch (evt.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = evt.getX(0);
			xPrecision = evt.getXPrecision();
			break;
		case MotionEvent.ACTION_MOVE:
			// MotionEvent.obtain(downTime, eventTime, action, x, y, pressure,
			// size, metaState, xPrecision, yPrecision, deviceId,
			// edgeFlags);
			if (evt.getPointerCount() > 1) {
				return true;
			}
			moveEvent = MotionEvent.obtain(evt.getDownTime(),
					evt.getEventTime(), evt.getAction(), downX, evt.getY(),
					evt.getPressure(), evt.getSize(), evt.getMetaState(),
					xPrecision, evt.getYPrecision(), evt.getDeviceId(),
					evt.getEdgeFlags());
			break;
		case MotionEvent.ACTION_UP:
			upY = evt.getY();
			// moveEvent = MotionEvent.obtain(evt.getDownTime(),
			// evt.getEventTime(), evt.getAction(), downX, evt.getY(),
			// evt.getPressure(), evt.getSize(), evt.getMetaState(),
			// xPrecision, evt.getYPrecision(), evt.getDeviceId(),
			// evt.getEdgeFlags());
			// moveEvent.setSource(evt.getSource());
			break;
		}
		// Log.i("CC", "EulaWebView.onTouchEvent " + evt.getAction() + " X:"
		// + downX);
		if (moveEvent != null && !isAllowHorizontalScroll()) {
			return super.onTouchEvent(moveEvent);
		} else {
			return super.onTouchEvent(evt);
		}
	}
	
	protected boolean isAllowHorizontalScroll() {
		return allowHorizontalScroll;
	}

}
