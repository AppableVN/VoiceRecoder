package liveStreaming;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.Appable.Android.VoiceRecoder.R;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BroadCastStream extends Activity {
	private static final String PATH_VOICE = "myvoice";
	private static boolean isRecording = false;
	private static boolean isPlaying = false;
	public Button btn_record, btn_playback;
	public int PORT = 12345;
	public EditText edit_port;
	public String HOST = "http://192.168.1.65:8080/webservice/";
	public AudioRecording mAudioRecorder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.broadcast_voice);
		
		init();
		initGUI();
		initEvent();
	}

	private void init(){
		btn_record = (Button) findViewById(R.id.btn_record);
		btn_playback = (Button) findViewById(R.id.btn_playback);
		edit_port = (EditText)findViewById(R.id.txt_target);
		mAudioRecorder = new AudioRecording();
	}
	
	private void initGUI(){
		edit_port.setText("12345");
	}
	
	private void initEvent(){
		btn_record.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PORT = Integer.valueOf(edit_port.getText().toString());
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
							mAudioRecorder.startRecord2(HOST);
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
				PORT = Integer.valueOf(edit_port.getText().toString());
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
							mAudioRecorder.playarcoding(HOST);
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
}
