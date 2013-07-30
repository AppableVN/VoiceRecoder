package com.Appable.Android.VoiceRecoder.webbrowser;

import com.Appable.Android.VoiceRecoder.R;
import com.appable.androidlib.lightwebbrowser.WebBrowserView;
import com.appable.androidlib.lightwebbrowser.WebBrowserView.OnLoadedURL;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class WebViewLogin extends Activity {
	private EditText txt_Url;
	private Button btn_Go;
	private WebBrowserView browser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginwebview);
		CookieSyncManager.createInstance(WebViewLogin.this);
		init();
		initGUI();
		initEvent();
		
		
		
		if (savedInstanceState != null) {
			browser.restoreState(savedInstanceState);
        } else {
        	
        }
	}

	private void init(){
		browser = (WebBrowserView)findViewById(R.id.lightbrowser);
		btn_Go = (Button)findViewById(R.id.btn_go);
		txt_Url = (EditText)findViewById(R.id.txt_url);
		txt_Url.setText("http://scmerge.levanttech.com/file/districts/1/NCASE_Spring[1].pdf");
	}
	
	private void initGUI(){
		browser.setOnLoadedURL(new OnLoadedURL() {
			
			@Override
			public void receivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loadedURL(WebView view, String url) {
				// TODO Auto-generated method stub
				txt_Url.setText(url);
			}
		});
	}
	
	private void initEvent(){
		btn_Go.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String cur_url = txt_Url.getText().toString();
				browser.loadUrl(cur_url);
			}
		});
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	browser.saveState(outState);
    }
	
	@Override
    public void onStop() {
    	super.onStop();
    	browser.stopLoading();
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		CookieSyncManager.getInstance().stopSync();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CookieSyncManager.getInstance().startSync();
		
	}
	
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);
    }

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (browser.inCustomView()) {
            	browser.hideCustomView();
            	return true;
            }else if(browser.onKeyDown(keyCode, event)){
            	return true;
            }
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
