package com.Appable.Android.VoiceRecoder;

import iRadeoDemo.UploadFileActivity;
import iRadeoDemo.AudioList.AudioFileActivity;
import iRadeoDemo.PlayList.PlayerListActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pinterestDemo.PinterestActivity;

import liveStreaming.AppableLiveStream;
import liveStreaming.BroadCastStream;

import com.Appable.Android.VoiceRecoder.RecordnPlayBack.RecordnPlayBack;
import com.Appable.Android.VoiceRecoder.webbrowser.WebViewLogin;
import com.opentok.OpenTokDemoActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class VoiceMain extends Activity {
	public Button btn_record_playback;
	public Button btn_broadCast;
	public Button btn_pinterest;
	public Button btn_liveStream;
	public Button btn_weblogin;

	public Button btn_playlist;
	public Button btn_OpenTok;
	public Button btn_upload;
	public Button btn_delete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_main);
		
		init();
		initGUI();
		initEvent();
		
//		final PackageManager packageManager = getPackageManager();
//		List<ApplicationInfo> installedApplications = 
//		   packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//
//		for (ApplicationInfo appInfo : installedApplications)
//		{
//		    Log.d("OUTPUT", "Package name : " + appInfo.packageName);
//		    Log.d("OUTPUT", "Name: " + appInfo.loadLabel(packageManager));
//		} 
	}

	private void init(){
		btn_record_playback = (Button) findViewById(R.id.btn_record_playback);
		btn_broadCast = (Button) findViewById(R.id.btn_broadcast);
		btn_pinterest = (Button) findViewById(R.id.btn_pinterest);
		btn_liveStream = (Button) findViewById(R.id.btn_livestream);
		btn_weblogin = (Button) findViewById(R.id.btn_weblogin);
		btn_playlist = (Button) findViewById(R.id.btn_iradeoplaylist);
		btn_OpenTok = (Button) findViewById(R.id.btn_opentok);
		btn_upload = (Button) findViewById(R.id.btn_iradeoupload);
		btn_delete = (Button) findViewById(R.id.btn_iradeodelete);
	}
	
	private void initGUI(){
		
	}
	
	private void initEvent(){
		btn_record_playback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","record_playback");
				Intent intent = new Intent(VoiceMain.this, RecordnPlayBack.class);
				startActivity(intent);
			}
		});
		
		btn_broadCast.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","BroadCastStream");
				Intent intent = new Intent(getApplicationContext(), BroadCastStream.class);
				startActivity(intent);
			}
		});
		
		btn_pinterest.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","printerest");
				Intent intent = new Intent(getApplicationContext(), PinterestActivity.class);
				startActivity(intent);
			}
		});
		
		btn_liveStream.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","liveStream");
				Intent intent = new Intent(getApplicationContext(), AppableLiveStream.class);
				startActivity(intent);
			}
		});
		btn_weblogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","web login");
				Intent intent = new Intent(getApplicationContext(), WebViewLogin.class);
				startActivity(intent);
			}
		});

		btn_playlist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","btn_playlist");
				Intent intent = new Intent(getApplicationContext(), PlayerListActivity.class);
				startActivity(intent);
			}
		});
		
		btn_OpenTok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","OpenTok Demo");
				Intent intent = new Intent(getApplicationContext(), OpenTokDemoActivity.class);
				startActivity(intent);
			}
		});
		
		btn_upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","btn_upload");
				Intent intent = new Intent(getApplicationContext(), UploadFileActivity.class);
//				startActivity(intent);
			}
		});
		
		btn_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i("Voice main","btn_delete");
//				Intent intent = new Intent(getApplicationContext(), WebViewLogin.class);
//				startActivity(intent);
			}
		});
	}
}
