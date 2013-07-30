package iRadeoDemo.AudioList;

import java.util.List;

import iRadeoDemo.Global;
import iRadeoDemo.HttpService;
import iRadeoDemo.PlayList.PlayListObject;
import iRadeoDemo.PlayList.PlayerObject;

import com.Appable.Android.VoiceRecoder.R;
import com.appable.androidlib.lightwebbrowser.WebBrowserView;
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
import android.widget.ListView;
import android.widget.TextView;

public class AudioFileActivity extends Activity {

	private static final int LAYOUT_ID = R.layout.iradeo_audiolist;
	private HttpService httpService;
	private ListView lv_audiolist;
	private AudioListObject data_result;
	private AudioListAdapter mAudioListAdapter;

	private WebBrowserView webview;
	private String playerid;
	private String playername;
	
	private final String Emberded = 
			"<html>" +
					"<head>"+
				  		"<title>Internal Media Player: Appable</title>"+
			  		"</head>"+
					"<body>" +
						"<script type=\"text/javascript\" src=\"http://www.iradeo.com/livyhoang/player/96667.1.js\"></script>"+
					"</body>" +
				"</html>";
	
	private final String PLAYER = "http://www.iradeo.com/livyhoang/%s";
	
	private String source_player;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(LAYOUT_ID);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			playerid = extras.getString("playerID");
			playername = extras.getString("playerTitle");
			source_player = String.format(PLAYER, "abc");
		}
		init();
		initGUI();
		getAudioList.execute(String.format(Global.AUDIOLIST_API, Global.iRADEO_APIKEY, playerid));
	}

	private void init(){
		lv_audiolist = (ListView)findViewById(R.id.listView1);
		webview = (WebBrowserView) findViewById(R.id.webBrowserView1);
	}
	
	private void initGUI(){
//		webview.loadUrl(source_player);
		webview.loadData(Emberded);
	}
	
	AsyncTask<String, Void, String> getAudioList = new AsyncTask<String, Void, String>(){

		private String result;

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
			data_result = gson.fromJson(result, AudioListObject.class);
			if(data_result!=null){
				if(mAudioListAdapter == null){
					mAudioListAdapter = new AudioListAdapter(getApplicationContext(), data_result.audio);
				}
				lv_audiolist.setAdapter(mAudioListAdapter);
				mAudioListAdapter.notifyDataSetChanged();
			}
		}
	};
	
	private class AudioListAdapter extends BaseAdapter{

		private Context mContext;
		private List<AudioObject> mListData;
		
		public AudioListAdapter(Context context, List<AudioObject> listdata){
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
			holder.playerID.setText(mListData.get(position).audio_id);
			
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

}
