package com.opentok.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.Appable.Android.VoiceRecoder.R;
import com.opentok.android.OpentokException;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.Publisher;

/**
 * This application demonstrates the basic workflow for getting started with the OpenTok Android SDK.
 * Basic hello-world activity shows publishing audio and video and subscribing to an audio and video stream
 */
public class HelloWorldActivity extends Activity implements Publisher.Listener, Subscriber.Listener, Session.Listener {

    private static final String LOGTAG = "demo-hello-world";
    // automatically connect during Activity.onCreate
    private static final boolean AUTO_CONNECT = true;
    // automatically publish during Session.Listener.onSessionConnected
    private static final boolean AUTO_PUBLISH = true;
    // automatically subscribe during Session.Listener.onSessionReceivedStream IFF stream is our own
    private static final boolean SUBSCRIBE_TO_SELF = true;


    /* Fill the following variables using your own Project info from the Dashboard */
    // Replace with your generated Session ID
    private static final String SESSION_ID = 
    		"1_MX4zMDAyNjU1Mn4xMjcuMC4wLjF-VHVlIEp1bCAxNiAyMDoyMTozOCBQRFQgMjAxM34wLjA2MDMxODQ3fg";
    // Replace with your generated Token (use Project Tools or from a server-side library)
    private static final String TOKEN = 
    		"T1==cGFydG5lcl9pZD0zMDAyNjU1MiZzZGtfdmVyc2lvbj10YnJ1YnktdGJyYi12MC45MS4yMDExLTAyLTE3JnNpZz1jNWEyYTg3N2VmZDAzYWI0N2E3MTUyMTE1M2JmODgyODg2ODU3YTk0OnJvbGU9cHVibGlzaGVyJnNlc3Npb25faWQ9MV9NWDR6TURBeU5qVTFNbjR4TWpjdU1DNHdMakYtVkhWbElFcDFiQ0F4TmlBeU1Eb3lNVG96T0NCUVJGUWdNakF4TTM0d0xqQTJNRE14T0RRM2ZnJmNyZWF0ZV90aW1lPTEzNzQwMzEzMDgmbm9uY2U9MC4zMTk2NDQzNzcxNTk3MjU5MyZleHBpcmVfdGltZT0xMzc2NjIzMzA3JmNvbm5lY3Rpb25fZGF0YT0=";
    
    
    private RelativeLayout publisherViewContainer;
    private RelativeLayout subscriberViewContainer;
    private Button btn_Connect;
    private Publisher publisher;
    private Subscriber subscriber;
    private Session session;
    private WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        publisherViewContainer = (RelativeLayout) findViewById(R.id.publisherview);
        subscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriberview);
        btn_Connect = (Button)findViewById(R.id.btn_connect);
        
        btn_Connect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sessionConnect();
			}
		});
        
        // Disable screen dimming
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Full Wake Lock");

    }


    @Override
    public void onStop() {
        super.onStop();

        if (session != null) {
            session.disconnect();
        }

        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
//        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    private void sessionConnect() {
        session = Session.newInstance(HelloWorldActivity.this, SESSION_ID, HelloWorldActivity.this);
        session.connect(TOKEN);
    }

    @Override
    public void onSessionConnected() {

        Log.i(LOGTAG, "session connected");

        // Session is ready to publish.
        if (AUTO_PUBLISH) {
            //Create Publisher instance.
            publisher = Publisher.newInstance(HelloWorldActivity.this);
            publisher.setName("My First Publisher");
            publisher.setListener(HelloWorldActivity.this);

            RelativeLayout.LayoutParams publisherViewParams =
                    new RelativeLayout.LayoutParams(publisher.getView().getLayoutParams().width,
                            publisher.getView().getLayoutParams().height);
            publisherViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            publisherViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            publisherViewParams.bottomMargin = dpToPx(8);
            publisherViewParams.rightMargin = dpToPx(8);
            publisherViewContainer.setLayoutParams(publisherViewParams);
            publisherViewContainer.addView(publisher.getView());
            session.publish(publisher);
        }

    }


    @Override
    public void onSessionDroppedStream(Stream stream) {
        Log.i(LOGTAG, String.format("stream dropped", stream.toString()));
        subscriber = null;
        subscriberViewContainer.removeAllViews();
    }

    @SuppressWarnings("unused")
    @Override
    public void onSessionReceivedStream(final Stream stream) {
        Log.i(LOGTAG, "session received stream");

        boolean isMyStream = session.getConnection().equals(stream.getConnection());
        //If this incoming stream is our own Publisher stream and subscriberToSelf is true let's look in the mirror.
        if ((SUBSCRIBE_TO_SELF && isMyStream) || (!SUBSCRIBE_TO_SELF && !isMyStream)) {
            subscriber = Subscriber.newInstance(HelloWorldActivity.this, stream);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels,
                            getResources().getDisplayMetrics().heightPixels);
            View subscriberView = subscriber.getView();
            subscriberView.setLayoutParams(params);
            subscriberViewContainer.addView(subscriber.getView());
            subscriber.setListener(HelloWorldActivity.this);
            session.subscribe(subscriber);
        }
    }

    @Override
    public void onSubscriberConnected(Subscriber subscriber) {
        Log.i(LOGTAG, "subscriber connected");
    }

    @Override
    public void onSessionDisconnected() {
        Log.i(LOGTAG, "session disconnected");
    }

    @Override
    public void onSessionException(OpentokException exception) {
        Log.e(LOGTAG, "session failed! " + exception.toString());
    }

    @Override
    public void onSubscriberException(Subscriber subscriber, OpentokException exception) {
        Log.i(LOGTAG, "subscriber " + subscriber + " failed! " + exception.toString());
    }

    @Override
    public void onPublisherChangedCamera(int cameraId) {
        Log.i(LOGTAG, "publisher changed camera to cameraId: " + cameraId);
    }

    @Override
    public void onPublisherException(OpentokException exception) {
        Log.i(LOGTAG, "publisher failed! " + exception.toString());
    }

    @Override
    public void onPublisherStreamingStarted() {
        Log.i(LOGTAG, "publisher is streaming!");
    }

    @Override
    public void onPublisherStreamingStopped() {
        Log.i(LOGTAG, "publisher disconnected");
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public Session getSession() {
        return session;
    }

    public RelativeLayout getPublisherView() {
        return publisherViewContainer;
    }

    public RelativeLayout getSubscriberView() {
        return subscriberViewContainer;
    }


    /**
     * Converts dp to real pixels, according to the screen density.
     * @param dp A number of density-independent pixels.
     * @return The equivalent number of real pixels.
     */
    private int dpToPx(int dp) {
        double screenDensity = this.getResources().getDisplayMetrics().density;
        return (int) (screenDensity * (double) dp);
    }
}
