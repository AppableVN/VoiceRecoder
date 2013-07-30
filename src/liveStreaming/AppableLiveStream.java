package liveStreaming;

import iRadeoDemo.AudioList.AudioFileActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Appable.Android.VoiceRecoder.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppableLiveStream extends Activity {

	private String userName;
	private int userid, streamid;
	private HttpService httpService;
	private EditText edit_userName;
	private Button btn_login;
	private Button btn_refresh;
	private ListView lvUser;
	
	private Boolean isRecording = false;
	private Boolean isListenning = false;
	
	private ArrayList<UserInfo> listUser = new ArrayList<UserInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appable_livestream);
		
		init();
		initEvent();
		initGUI();
		
	}

	private void init(){
		edit_userName = (EditText)findViewById(R.id.edit_username); 
		btn_login = (Button)findViewById(R.id.btn_login); 
		btn_refresh = (Button)findViewById(R.id.btn_refresh); 
		lvUser = (ListView)findViewById(R.id.ls_listUser); 
	}
	
	private void initEvent(){
		btn_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String label = btn_login.getText().toString();
				if(label.compareTo("Login") == 0){
					userName = edit_userName.getText().toString();
					if(userName.compareTo("") == 0) return;
					LoginTask loginTask = new LoginTask();
					loginTask.execute(LiveStreamUtils.SIGNIN_API);
					btn_login.setText("Logout");
				}else{
					LogoutTask logoutTask = new LogoutTask();
					logoutTask.execute(LiveStreamUtils.SIGNOUT_API);
					btn_login.setText("Login");
				}
				
			}
		});
		
		btn_refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetUserTask getuser = new GetUserTask();
				getuser.execute(LiveStreamUtils.GETUSER_API);
			}
		});
	}
	
	private void initGUI(){
		userAdapter = new listUserAdapter(this);
		lvUser.setAdapter(userAdapter);
	}
	
	private class LoginTask extends AsyncTask<String, Void, String>{
		@Override
		protected void onPreExecute() {
			if(httpService == null)
				httpService  = new HttpService();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("AppableLiveStream","url: "+ params[0]);
			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", userName));
				String result = httpService.portRequest(params[0], nameValuePairs);
				Log.e("AppableLiveStream","result: "+ result);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			listUser.clear();
			if(result == null || result.compareTo("0") == 0){
				return;
			}
			
			try {
				JSONArray jsarray = new JSONArray(result);
				for(int i=0; i< jsarray.length(); i++){
					JSONObject jsobject = jsarray.getJSONObject(i);
					UserInfo user = new UserInfo();
					user.userid = jsobject.getInt("userid");
					user.username = jsobject.getString("username");
					user.state = jsobject.getBoolean("state");
					if(user.username.compareTo(userName) == 0) userid = user.userid;
					listUser.add(user);
				}
				userAdapter.notifyDataSetChanged();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	private class LogoutTask extends AsyncTask<String, Void, String>{
		@Override
		protected void onPreExecute() {
			if(httpService == null)
				httpService  = new HttpService();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("AppableLiveStream","url: "+ params[0]);
			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("userid", ""+userid));
				String result = httpService.portRequest(params[0], nameValuePairs);
				
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			listUser.clear();
			userAdapter.notifyDataSetChanged();
		}
		
	}
	
	private class GetUserTask extends AsyncTask<String, Void, String>{


		@Override
		protected void onPreExecute() {
			if(httpService == null)
				httpService  = new HttpService();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("AppableLiveStream","url: "+ params[0]);
			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", userName));
				String result = httpService.portRequest(params[0], nameValuePairs);
				
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			listUser.clear();
			if(result == null || result.compareTo("0") == 0){
				return;
			}
			
			try {
				JSONArray jsarray = new JSONArray(result);
				for(int i=0; i< jsarray.length(); i++){
					JSONObject jsobject = jsarray.getJSONObject(i);
					UserInfo user = new UserInfo();
					user.userid = jsobject.getInt("userid");
					user.username = jsobject.getString("username");
					user.state = jsobject.getBoolean("state");
					if(user.username.compareTo(userName) == 0) userid = user.userid;
					listUser.add(user);
				}
				userAdapter.notifyDataSetChanged();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private class StopBroadCastTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.compareTo("0") == 0) return;
			stopRecording();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(httpService == null)
				httpService  = new HttpService();
		}

		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("AppableLiveStream","url: "+ params[0]);
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("streamid", ""+userid));
				String result = httpService.portRequest(params[0], nameValuePairs);
				
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	private class BroadCastTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.compareTo("0") == 0) return;
			record(Integer.valueOf(result));
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(httpService == null)
				httpService  = new HttpService();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("AppableLiveStream","url: "+ params[0]);
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("userid", ""+userid));
				String result = httpService.portRequest(params[0], nameValuePairs);
				
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	AudioRecording mAudioRecorder = new AudioRecording();
	
	private void stopRecording(){
		isRecording = false;
		try {
			mAudioRecorder.stopRecord();
		} catch (IOException e) {
			Log.i("Voice Recorder stop","exception: "+ e.getMessage());
		}
	}
	
	private void record(int port){
		isRecording = true;
		try {
			mAudioRecorder.startRecord(LiveStreamUtils.REMOTE_HOST, port);
		} catch (IOException e) {
			Log.i("Voice Recorder start","exception: "+ e.getMessage());
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private listUserAdapter userAdapter;
	
	private class listUserAdapter extends BaseAdapter{
		private Context mContext;
		
		public listUserAdapter(Context context){
			mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listUser.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listUser.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return listUser.get(position).hashCode();
		}

		private class ViewHolder{
			TextView username;
			TextView userID;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
				holder.username = (TextView)convertView.findViewById(R.id.txt_name);
				holder.userID = (TextView)convertView.findViewById(R.id.txt_id);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.username.setText(listUser.get(position).username);
//			holder.userID.setText(listUser.get(position).userid);
			
			final int tempPosition = position;
			
			if(userName.compareTo(listUser.get(position).username) == 0){
				convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, listUser.get(tempPosition).username + ": broadcase voice", Toast.LENGTH_SHORT).show();
					}
				});
			}else{
				convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, listUser.get(tempPosition).username + ": listen from user", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
			return convertView;
		}
	}

}
