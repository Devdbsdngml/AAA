package com.example.aaa;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    // Debugging
    private static final String TAG = "MainActivity";
    private static final boolean D = true;


    private static final int REQUEST_ENABLE_BT = 2;
    public static Context context;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private ImageButton btn;

    private Animation animRotate;
    private Animation animClick;

    private StringBuffer mOutStringBuffer;
    private BluetoothAdapter bluetoothAdapter = null;
    public BluetoothService bluetoothService = null;

//    BluetoothThread bluetoothThread;
//    Handler writeHandler;
    private final String address = "98:D3:41:FD:91:8D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        init();
        infiRotate();

        btnClick();
    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "...In onStart()...");

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (bluetoothService == null) setup();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        Log.d(TAG, "...In onResume()...");

        infiRotate();
        btnClick();

        if (bluetoothService != null) {
            if (bluetoothService.getState() == BluetoothService.STATE_NONE) {
                bluetoothService.start();
            }
        }
    }

    private void setup() {

        Log.d(TAG, "setup()");

        bluetoothService = new BluetoothService(this, mHandler);
        mOutStringBuffer = new StringBuffer("");

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        bluetoothService.connect(device);

//        bluetoothThread = new BluetoothThread(address, new Handler() {
//            @Override
//            public void handleMessage(Message message) {
//                String s = (String) message.obj;
//
//                // Do something with the message
//                switch (s) {
//                    case "CONNECTED":
//                        errorMessage("CONECTED", "ListActivity");
//                        break;
//                    case "DISCONNECTED":
//                        errorMessage("DISCONNECTED", "ListActivity");
//                        break;
//                    case "CONNECTION FAILED":
//                        errorMessage("CONNECTION FAILED", "ListActivity");
//                        break;
//                    default:
//                        errorMessage("FAILED", "ListActivity");
//                        break;
//                }
//            }
//        });
//        bluetoothThread.start();
//        writeHandler = bluetoothThread.getWriteHandler();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "...In onStop()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothService != null) bluetoothService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void init() {
        btn = findViewById(R.id.btn);
        animRotate = AnimationUtils.loadAnimation(this, R.anim.btnrotate);
        animClick = AnimationUtils.loadAnimation(this, R.anim.btnclick);
    }

    private void infiRotate() {
        btn.setAnimation(animRotate);
    }

    private void btnClick() {
        btn.setOnClickListener(new OnSingleClickLinstener() {
            @Override
            public void onSingleClick(View v) {
                btn.startAnimation(animClick);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, ExplainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }, 2900);
            }
        });
    }

    private static void Toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    // Handler
    private final myHandler mHandler = new myHandler(this);

    private static class myHandler extends Handler {
        private final WeakReference<MainActivity> weakReference;

        public myHandler(MainActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast("클릭하세요");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast("잠시만 기다려주세요");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Toast("관리자에게 문의하세요");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.d(TAG, "Sent " + writeMessage);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    if (readMessage.equals("a")) {

                    }

                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    break;
                case MESSAGE_TOAST:
//                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
//                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
