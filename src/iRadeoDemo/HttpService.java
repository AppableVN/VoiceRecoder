package iRadeoDemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore.Files;
import android.util.Log;
import android.widget.Toast;

public class HttpService {
	
	UsernamePasswordCredentials auth = new UsernamePasswordCredentials(Global.USER_NAME, Global.PASS_WORD);
	public interface PostListener {
		public void onSuccess();

		public void onFailure();
	}

	public interface onSaveFavorite2ServerListener{
		public void onSuccess(String favoriteId);
		
		public void onFailure();
	}
	
	public HttpService() {
		
	};

	public String getResulsRespond(String url) throws Exception{
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutSockets = 20000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSockets);
		
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpGet getRequest = new HttpGet(URI.create(url));
		getRequest.addHeader(BasicScheme.authenticate(auth, "UTF-8", false));
		
		if (client != null && getRequest != null){
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine()
					.getStatusCode();
	
			final String statusReason = getResponse.getStatusLine().getReasonPhrase();
			
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusReason
						+ " for URL " + url);
				return null;
			}
	
			HttpEntity getResponseEntity = getResponse.getEntity();
			return EntityUtils.toString(getResponseEntity);
		}
		return null;
	}
	
	public String getResulsRespondURI(String uri) throws Exception{
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutSockets = 20000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSockets);
		
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpGet getRequest = new HttpGet(URI.create(uri));
		getRequest.addHeader(BasicScheme.authenticate(auth, "UTF-8", false));
		
		if (client != null && getRequest != null){
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine()
					.getStatusCode();
	
			final String statusReason = getResponse.getStatusLine().getReasonPhrase();
			
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusReason
						+ " for URL " + uri);
				return null;
			}
	
			HttpEntity getResponseEntity = getResponse.getEntity();
			return EntityUtils.toString(getResponseEntity);
		}
		return null;
	}
	
	public String portRequest(String url, List<NameValuePair> nameValuePairs){
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader(BasicScheme.authenticate(auth, "UTF-8", false));
		
		try {
			
			System.out.println("data post:"+ nameValuePairs.toString());
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String postFileData(String url, List<NameValuePair> nameValuePairs, String fileAddress,
			PostListener listener){
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader(BasicScheme.authenticate(auth, "UTF-8", false));
		MultipartEntity entity = new MultipartEntity(
			    HttpMultipartMode.BROWSER_COMPATIBLE);
		
		File f = new File(fileAddress);
		Log.i("httpservice", "file path:"+ f.getAbsolutePath());
		InputStreamBody fb;
		try {
			System.out.println("data post:"+ nameValuePairs.toString());
			for(NameValuePair tmpvalue: nameValuePairs){
				entity.addPart(tmpvalue.getName(), new StringBody(tmpvalue.getValue(),Charset.forName("UTF-8")));
			}
			fb = new InputStreamBody(new FileInputStream(f), "audio/mpeg", f.getName());
			entity.addPart("file", fb);
			
			httppost.setEntity(entity);
			
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			BufferedReader bufferedReader = 
					new BufferedReader( new InputStreamReader(response.getEntity().getContent()));
			 StringBuffer stringBuffer = new StringBuffer("");
			 String line = "";
			 String LineSeparator = System.getProperty("line.separator");
			 while ((line = bufferedReader.readLine()) != null) {
				 stringBuffer.append(line + LineSeparator);
			 }
			 bufferedReader.close();
			 Log.e("HttpService","response: "+ stringBuffer.toString());
			 return stringBuffer.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}