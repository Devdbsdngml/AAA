package com.example.aaa;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

public class BusinessActivity extends AppCompatActivity {

    private static final String TAG = "BusinessActivity";

    private PlayerView playerView;
    private SimpleExoPlayer player;
    // video uri
    // you have to change buildRawResourceUri(R.drawable.forest) to your video
    private Uri uri = Uri.parse(RawResourceDataSource.buildRawResourceUri(R.raw.business).toString());

    public BluetoothService bluetoothService;
    private StringBuffer stringBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //가로 화면으로 고정
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_exoplayer);

        init();
    }

    private void init() {
        playerView = findViewById(R.id.pv_forest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "...In onStart()...");
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        Log.d(TAG, "...In onResume()...");

        bluetoothService = ((MainActivity) MainActivity.context).bluetoothService;
        stringBuffer = new StringBuffer("");

        sendMessage("2");
        initPlayer();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        playerView.setPlayer(null);
        player.release();
        player = null;
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "...In onStop()...");
    }

    private long time = 0;
    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            sendMessage("3");
            sendMessage("5");
            finish();
        }
    }

    private void initPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);

        DataSource.Factory factory= new DefaultDataSourceFactory(this, Util.getUserAgent(this, "AAA"));
        MediaSource mediaSource= new ExtractorMediaSource.Factory(factory).createMediaSource(uri);

//        DefaultDataSourceFactory factory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "AAA"));
//        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//        MediaSource mediaSource = new ExtractorMediaSource(uri, factory, extractorsFactory, null, null);

        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
//        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_IDLE: // 1
                        //재생 실패
                        break;
                    case Player.STATE_BUFFERING: // 2
                        // 재생 준비
                        break;
                    case Player.STATE_READY: // 3
                        // 재생 준비 완료
                        break;
                    case Player.STATE_ENDED: // 4
                        // 재생 마침
                        sendMessage("3");
                        sendMessage("5");
                        Toast.makeText(getBaseContext(), "영상이 끝났습니다", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Log.d(TAG,"sendMessage Failed");
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            bluetoothService.write(send);

            stringBuffer.setLength(0);
        }
    }
}
