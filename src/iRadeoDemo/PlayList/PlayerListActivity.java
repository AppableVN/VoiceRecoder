package iRadeoDemo.PlayList;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import iRadeoDemo.FileUtils;
import iRadeoDemo.Global;
import iRadeoDemo.HttpService;
import iRadeoDemo.AudioList.AudioFileActivity;

import com.Appable.Android.VoiceRecoder.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerListActivity extends Activity {
	private static final int LAYOUT_ID = R.layout.iradeo_playlist;
	private HttpService httpService;
	private ListView lv_playlist;
	private PlayListObject data_result;
	private PlayListAdapter mPlayListAdapter;
	private Button btn_upload;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(LAYOUT_ID);
		
		init();
		initGUI();
		initEvent();
		GetPlayerList getPlayerList = new GetPlayerList();
		getPlayerList.execute(String.format(Global.PLAYERLIST_API, Global.iRADEO_APIKEY));
	}
	
	

	private void init(){
		lv_playlist = (ListView)findViewById(R.id.listView1);
		btn_upload = (Button)findViewById(R.id.button1);
	}
	
	private void initGUI(){
		
	}
	
	private void initEvent(){
		btn_upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String path = "/mnt/sdcard/ZingMp3/Radioactive_Imagine Dragons_-1074330215.mp3";
				postUploadFileTask postUploadFile = new postUploadFileTask();
//				postUploadFile.execute(path);

				showFileChooser();
			}
		});
	}
	
	
	private static final int FILE_SELECT_CODE = 0;

	private void showFileChooser() {
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
	    intent.setType("audio/mpeg3"); 
	    intent.addCategory(Intent.CATEGORY_OPENABLE);

	    try {
	        startActivityForResult(
	                Intent.createChooser(intent, "Select a File to Upload"),
	                FILE_SELECT_CODE);
	    } catch (android.content.ActivityNotFoundException ex) {
	        // Potentially direct the user to the Market with a Dialog
	        Toast.makeText(this, "Please install a File Manager.", 
	                Toast.LENGTH_SHORT).show();
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	        case FILE_SELECT_CODE:
	        if (resultCode == RESULT_OK) {
	            // Get the Uri of the selected file 
	            Uri uri = data.getData();
	            Log.e("Upload File", "File Uri: " + uri.toString());
	            // Get the path
	            String path;
				try {
					path = FileUtils.getPath(this, uri);
		            Log.d("Upload File", "File Path: " + path);
		            postUploadFileTask postUploadFile = new postUploadFileTask();
		            postUploadFile.execute(path);
		            
		            
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
	            // Get the file instance
	            // File file = new File(path);
	            // Initiate the upload
	        }
	        break;
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	private class postUploadFileTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			if(httpService == null)
				httpService  = new HttpService();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			if(params[0]!=null && params[0].length() > 0){
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("player", "96667"));
				nameValuePairs.add(new BasicNameValuePair("title", "test upload android"));
				nameValuePairs.add(new BasicNameValuePair("artist", "file mp3"));
				nameValuePairs.add(new BasicNameValuePair("api_key", Global.iRADEO_APIKEY));
				String url = Global.AUDIOUPLOAD_API;
				String response = httpService.postFileData(url, nameValuePairs, params[0], null);
				return response;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result!=null ) Toast.makeText(PlayerListActivity.this, result, Toast.LENGTH_SHORT).show();
		}
	}
	
	private class GetPlayerList extends AsyncTask<String, Void, String>{


		@Override
		protected void onPreExecute() {
			if(httpService == null)
				httpService  = new HttpService();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e("PlayListActivity","url: "+ params[0]);
			try {
				String result = httpService.getResulsRespond(params[0]);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			Gson gson = new Gson();
			data_result = gson.fromJson(result, PlayListObject.class);
			if(mPlayListAdapter == null){
				mPlayListAdapter = new PlayListAdapter(PlayerListActivity.this, data_result.players);
			}
			lv_playlist.setAdapter(mPlayListAdapter);
			mPlayListAdapter.notifyDataSetChanged();
		}
		
	}
	
	private class PlayListAdapter extends BaseAdapter{

		private Context mContext;
		private List<PlayerObject> mListData;
		
		public PlayListAdapter(Context context, List<PlayerObject> listdata){
			mContext = context;
			mListData = listdata;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return mListData.get(position).hashCode();
		}

		private class ViewHolder{
			TextView playerName;
			TextView playerID;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
				holder.playerName = (TextView)convertView.findViewById(R.id.txt_name);
				holder.playerID = (TextView)convertView.findViewById(R.id.txt_id);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			holder.playerName.setText(mListData.get(position).title);
			holder.playerName.setTextColor(Color.parseColor("#"+mListData.get(position).text_color));
			holder.playerID.setText(mListData.get(position).player_id);
			holder.playerID.setTextColor(Color.parseColor("#"+mListData.get(position).link_color));
			convertView.setBackgroundColor(Color.parseColor("#"+mListData.get(position).background_color));
			
			final int mPosition = position;
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext, AudioFileActivity.class);
					intent.putExtra("playerID", mListData.get(mPosition).player_id);
					intent.putExtra("playerTitle", mListData.get(mPosition).title);
					mContext.startActivity(intent);
				}
			});
			
			return convertView;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
