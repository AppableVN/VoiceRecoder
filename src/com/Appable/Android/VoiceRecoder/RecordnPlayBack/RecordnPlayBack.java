package com.Appable.Android.VoiceRecoder.RecordnPlayBack;

import java.io.File;
import java.io.IOException;

import com.Appable.Android.VoiceRecoder.R;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RecordnPlayBack extends Activity {
	private static final String PATH_VOICE = "test";
	private static boolean isRecording = false;
	private static boolean isPlaying = false;
	public Button btn_record, btn_playback;
	public ImageView img_icon;
	
	public AudioRecorder mAudioRecorder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_playback);
		
		init();
		initGUI();
		initEvent();
	}

	private void init(){
		btn_record = (Button) findViewById(R.id.btn_record);
		btn_playback = (Button) findViewById(R.id.btn_playback);
		img_icon = (ImageView) findViewById(R.id.imageView1);
		
		mAudioRecorder = new AudioRecorder(PATH_VOICE);
	}
	
	private void initGUI(){
		
	}
	
	private void initEvent(){
		btn_record.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isPlaying){
					if(isRecording){
						isRecording = false;
						btn_record.setText("Record");
						try {
							mAudioRecorder.stopRecord();
						} catch (IOException e) {
							Log.i("Voice Recorder stop","exception: "+ e.getMessage());
						}
					}else{
						isRecording = true;
						btn_record.setText("Stop");
						try {
							mAudioRecorder.startRecord();
						} catch (IOException e) {
							Log.i("Voice Recorder start","exception: "+ e.getMessage());
						}
					}
				}
			}
		});
		
		btn_playback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isRecording){
					if(isPlaying){
						isPlaying = false;
						btn_playback.setText("Play");
						try {
							mAudioRecorder.stoparcoding();
						} catch (IOException e) {
							Log.i("Voice Player stop","exception: "+ e.getMessage());
						}
					}else{
						isPlaying = true;
						btn_playback.setText("Stop");
						try {
							mAudioRecorder.playarcoding();
						} catch (IOException e) {
							Log.i("Voice Player playing","exception: "+ e.getMessage());
						}
					}
				}
			}
		});
		
	}
	
	public void startRecord(){
		
	}
	
	public class AudioRecorder {
		private MediaPlayer player;
	    final MediaRecorder recorder = new MediaRecorder();
	    public final String path;
	    
	    public AudioRecorder(String path) {
	        this.path = sanitizePath(path);
	    }

	    private String sanitizePath(String path) {
	        if (!path.startsWith("/")) {
	            path = "/" + path;
	        }
	        if (!path.contains(".")) {
	            path += ".mp3";
	        }
	        return Environment.getExternalStorageDirectory().getAbsolutePath()
	                + path;
	    }

	    public void startRecord() throws IOException {
	        String state = android.os.Environment.getExternalStorageState();
	        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
	            throw new IOException("SD Card is not mounted.  It is " + state
	                    + ".");
	        }

	        // make sure the directory we plan to store the recording in exists
	        File directory = new File(path).getParentFile();
	        if (!directory.exists() && !directory.mkdirs()) {
	            throw new IOException("Path to file could not be created.");
	        }
	        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        recorder.setOutputFile(path);
	        recorder.prepare();
	        recorder.start();
	    }

	    public void stopRecord() throws IOException {
	        recorder.stop();
	        recorder.reset();
	        recorder.release();
	    }

	    public void playarcoding() throws IOException{
	    	AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	    	int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
	    	if(player == null){
	    		player = new MediaPlayer();
	    	}
	        player.setDataSource(path);
	        player.prepare();
	        player.start();
	        player.setVolume(currentVolume, currentVolume);
	    }
	    
	    public void playarcoding1() throws IOException {
	    	AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	    	int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
	    	if(player == null){
	    		player = new MediaPlayer();
//	    		player = MediaPlayer.c
	    	}
	    	player.setAudioStreamType(AudioManager.STREAM_MUSIC);
	        player.setDataSource(getApplicationContext(), Uri.parse("http://www.virginmegastore.me/Library/Music/CD_001214/Tracks/Track1.mp3"));
	        player.prepare();
	        player.start();
	        player.setVolume(currentVolume, currentVolume);
	    }
	    
	    public void stoparcoding() throws IOException{
	    	if(player!=null){
	    		player.stop();
	    		player.reset();
	    		player.release();
	    		player = null;
	    	}
	    }
	}
}
