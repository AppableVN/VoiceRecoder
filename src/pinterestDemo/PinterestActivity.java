package pinterestDemo;

import java.net.URI;

import iRadeoDemo.HttpService;

import com.Appable.Android.VoiceRecoder.R;
import com.pinterest.pinit.PinItButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PinterestActivity extends Activity {

	private HttpService httpService;
	
	private final int LAYOUT_ID = R.layout.pinterestlayout;

	private final String PARTNER_ID = "1432322";
	
	private PinItButton pinIt;
	private Button btn_getPinterest; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(LAYOUT_ID);
		
		init();
		initGUI();
		initEvent();
	}
	
	private void init(){
		pinIt = (PinItButton) findViewById(R.id.pin_it);
		btn_getPinterest = (Button)findViewById(R.id.btn_getPinterest);
	}
	
	private void initGUI(){
		pinIt.setImageUrl("http://placekitten.com/400/300");
	    pinIt.setUrl("http://placekitten.com"); // optional
	    pinIt.setDescription("A place kitten!"); // optional
	}
	
	private void initEvent(){
		PinItButton.setPartnerId(PARTNER_ID); // required
	    PinItButton.setDebugMode(true); // optional
	    
	    btn_getPinterest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetPinterestInfo getPinterest = new GetPinterestInfo();
				getPinterest.execute(Global.PIN_API);
			}
		});
	    
	    
	}

	private class GetPinterestInfo extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			if(httpService == null){
				httpService = new HttpService();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String result = params[0];//= httpService.getResulsRespondURI(params[0]);
				return result;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Intent callIntent = new Intent(Intent.ACTION_GET_CONTENT, Uri.parse(Global.PIN_API));
			PinterestActivity.this.startActivityForResult(callIntent, 1);
		}
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
			Log.e("pinterest","data:"+data.getDataString());
		}else{
			Log.e("pinterest","no data");
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
	
	
}
