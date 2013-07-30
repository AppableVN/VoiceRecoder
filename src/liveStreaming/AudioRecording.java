package liveStreaming;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import tokbox.org.codehaus.jackson.map.ser.std.InetAddressSerializer;

import com.Appable.Android.VoiceRecoder.RecordnPlayBack.RecordnPlayBack.AudioRecorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecording {
	//Audio Configuration. 
	private static final int BUFFER_SIZE = 256;
	private static final int sampleRate = 8000;      //How much will be ideal?
	private static final int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	private AudioTrack speaker;
	private MediaPlayer player;
    private AudioRecord recorder;
    private boolean isPlayfile = false;
    private boolean choose_source = false;
    private boolean status = false;
    private int REMOTE_PORT;
    
    Thread streamThread,receiveThread;
    public AudioRecording() {
    }

    
    public void startRecord(final String host,int port) throws IOException{
    	status = true;
    	REMOTE_PORT = port;
    	streamThread =  new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Socket client = null; 
                try {
                	
                    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

                    byte[] buffer = new byte[minBufSize];
                    Log.d("VS","Buffer created of size " + minBufSize);
                	client = SocketFactory.getDefault().createSocket(InetAddress.getByName(host), REMOTE_PORT);
                    Log.d("VS", "Address retrieved");

            		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize);
                    Log.d("VS", "Recorder initialized");


                	OutputStream opClientStream = client.getOutputStream();
                    
                    recorder.startRecording();
                    while(status == true) {
                        //reading data from MIC into buffer
//			                        minBufSize = recorder.read(buffer, 0, buffer.length);
                    	recorder.read(buffer, 0, buffer.length);
                    	opClientStream.write(buffer);
                    	opClientStream.flush();
                    }
                } catch(UnknownHostException e) {
                    Log.e("VS", "UnknownHostException:"+ e.getMessage());
                } catch (Exception e) {
                    Log.e("VS", "IOException:"+ e.getMessage());
                } finally{
	            	try {
//			            	client.shutdownOutput();
						client.close();
						client = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
				
			}
		});
    	streamThread.start();
    }
    
    public void startRecord2(final String url) throws IOException{
    	status = true;
    	streamThread =  new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
                try {
                	URLConnection connection = new URL(url+"uploadstream").openConnection();
                	OutputStream opClientStream = connection.getOutputStream();
                    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

                    byte[] buffer = new byte[minBufSize];
                    Log.d("VS","Buffer created of size " + minBufSize);
                    Log.d("VS", "Address retrieved");

            		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize);
                    Log.d("VS", "Recorder initialized");


                    
                    recorder.startRecording();
                    while(status == true) {
                        //reading data from MIC into buffer
//			                        minBufSize = recorder.read(buffer, 0, buffer.length);
                    	recorder.read(buffer, 0, buffer.length);
                    	opClientStream.write(buffer);
                    	opClientStream.flush();
                    }
					opClientStream.close();
                } catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e) {
                    Log.e("VS", "IOException:"+ e.getMessage());
                } finally{
	            }
				
			}
		});
    	streamThread.start();
    }
    
    public void startRecord1(final String host, int port) throws IOException {
    	status = true;
    	REMOTE_PORT = port;
    	streamThread = new Thread(new Runnable() {

            @Override
            public void run() {
            	DatagramSocket socket = null;
                try {
//	                	int minBufSize = BUFFER_SIZE;
                    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
                    socket = new DatagramSocket();
                    Log.d("VS", "Socket Created");

                    byte[] buffer = new byte[minBufSize];

                    Log.d("VS","Buffer created of size " + minBufSize);
                    DatagramPacket packet;

                    final InetAddress destination = InetAddress.getByName(host);
                    Log.d("VS", "Address retrieved");

                    if(!choose_source){
                		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize);
	                    Log.d("VS", "Recorder initialized");

	                    recorder.startRecording();
	                    while(status == true) {
	                        //reading data from MIC into buffer
//			                        minBufSize = recorder.read(buffer, 0, buffer.length);
	                    	recorder.read(buffer, 0, buffer.length);
	                        //putting buffer in the packet
	                        packet = new DatagramPacket (buffer,buffer.length,destination, REMOTE_PORT);
	                        socket.send(packet);
	                    }
                    }else{
                    	FileInputStream fin = new FileInputStream(sanitizePath("test"));
            	        while(status == true) {
	                        //reading data from MIC into buffer
	                        minBufSize = fin.read(buffer, 0, buffer.length);

	                        //putting buffer in the packet
	                        packet = new DatagramPacket (buffer,buffer.length,destination, REMOTE_PORT);

	                        socket.send(packet);
	                    }
            	        fin.close();
                    }
                } catch(UnknownHostException e) {
                    Log.e("VS", "UnknownHostException:"+ e.getMessage());
                } catch (IOException e) {
                    Log.e("VS", "IOException:"+ e.getMessage());
                } finally{
	            	socket.close();
	            	socket = null;
	            }

            }
        });
        streamThread.start();
    }

    public void stopRecord() throws IOException {
    	status = false;
    	if(!choose_source){
	        recorder.release();
    	}else{
    		
    	}
        streamThread.interrupt();
        streamThread = null;
    }

    public void playarcoding(final String url) throws IOException{
    	status = true;
    	receiveThread = new Thread (new Runnable() {

			@Override
			public void run() {
				try{
		            URLConnection conn = new URL(url+"downloadstream").openConnection();
		            InputStream is = conn.getInputStream();

		            int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		            byte[] buffer = new byte[minBufSize];
		            
		            speaker = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,channelConfig,audioFormat,minBufSize,AudioTrack.MODE_STREAM);
		            speaker.play();
		            while(status == true) {
		            	int avalbyte = is.available();
		            	is.read(buffer, 0, avalbyte);
		            	speaker.write(buffer, 0, avalbyte);
		            }
		            is.close();
		        }
		        catch(Exception e){
		            System.out.print(e);
		        }
			}
    	});
    	
    	receiveThread.start();
    }

    
    public void startPlaying(final String host,int port) throws IOException{
    	status = true;
    	REMOTE_PORT = port;
    	
    	receiveThread = new Thread (new Runnable() {

			@Override
			public void run() {
				Socket client = null; 
				try{
					
					client = SocketFactory.getDefault().createSocket(host, REMOTE_PORT);
                    Log.d("VS", "Address retrieved");
                    
					//minimum buffer size. need to be careful. might cause problems. try setting manually if any problems faced
                    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
	                
	                byte[] buffer = new byte[minBufSize];
	                
	                speaker = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,channelConfig,audioFormat,minBufSize,AudioTrack.MODE_STREAM);
	                speaker.play();
	                
	                InputStream ipClientStream = client.getInputStream();
	                
	                while(status == true) {
	                    try {
	                    	if(ipClientStream.read(buffer)!= -1){
	                    		
	                    	}else{
		                        Log.d("VR", "Packet data read into buffer");
	                    	}
	                    	
	                        speaker.write(buffer, 0, minBufSize);
	                        Log.d("VR", "Writing buffer content to speaker");

	                    } catch(IOException e) {
	                        Log.e("VR","IOException:"+ e.getMessage());
	                    }finally{
	                    }
	                }
					client.close();
				}catch (IOException e) {
					e.printStackTrace();
				}finally{
	            	client = null;
	            }
			}
    	});
    	receiveThread.start();
    }
    
    
    
    public void stoparcoding() throws IOException{
    	status = false;
    	speaker.release();
    	receiveThread.interrupt();
    	receiveThread = null;
        Log.d("VR","Speaker released");
    }
    
    private String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".3gp";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + path;
    }
}
