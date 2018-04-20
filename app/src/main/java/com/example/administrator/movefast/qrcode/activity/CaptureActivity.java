package com.example.administrator.movefast.qrcode.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.qrcode.camera.CameraManager;
import com.example.administrator.movefast.qrcode.decoding.CaptureActivityHandler;
import com.example.administrator.movefast.qrcode.decoding.InactivityTimer;
import com.example.administrator.movefast.qrcode.view.ViewfinderView;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.utils.logger.Logger;
import com.example.administrator.movefast.widget.TopBar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;


import java.io.IOException;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback {
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private SurfaceView surfaceView;
	private TopBar topBar;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		topBar = findViewById(R.id.topbar);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		CameraManager.init(getApplication());
		initEvent();
	}

	private void initEvent() {
		topBar.setOnTopClickListener(new TopBar.OnTopClickListener() {
			@Override
			public void onLeftClick() {
				finish();
			}

			@Override
			public void onRightClick() {

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			//surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 *
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		String resultString = result.getText();
		// FIXME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			resultIntent.putExtras(bundle);
			Log.d("qrcode", "result:" + resultString);
			this.setResult(RESULT_OK, resultIntent);
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				CaptureActivity.this.finish();
			}
		}, 500);
	}

	private boolean initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder, new CameraManager.onCameraConfigChangedListener() {
				@Override
				public void onChanged(Point point) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
					Logger.d("photo point -->" + point.y + "  " + point.x);
					double sacle = ((double) point.x / point.y);
					double defaultSacle = ((double) Helper.getDisplayHeight(CaptureActivity.this) / Helper.getDisplayWidth(CaptureActivity.this));
					if (sacle < defaultSacle) {
						layoutParams.width = Helper.getDisplayWidth(CaptureActivity.this);
						layoutParams.height = (int) (sacle * layoutParams.width);
					} else {
						layoutParams.height = Helper.getDisplayHeight(CaptureActivity.this);
						layoutParams.width = (int) (layoutParams.height / sacle);
					}
					Logger.d("photo screen-->" + Helper.getDisplayWidth(CaptureActivity.this) + "  " + Helper.getDisplayHeight(CaptureActivity.this));
					Logger.d("photo surface-->" + layoutParams.width + "  " + layoutParams.height);

					surfaceView.setLayoutParams(layoutParams);
				}
			});
		} catch (IOException ioe) {
			return false;
		} catch (RuntimeException e) {
			return false;
		}

		try {
			if (handler == null) {
				handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, characterSet);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			if (!initCamera(holder)) {
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}


}