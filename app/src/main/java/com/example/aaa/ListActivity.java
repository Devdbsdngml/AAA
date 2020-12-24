package com.example.aaa;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
//    public static Context context;

    private ImageButton forestBtn;
    private ImageButton cloudBtn;
    private ImageButton oceanBtn;
    private ImageButton businessBtn;

    private ImageView secLogo;
    private TextView tv;

    private Animation animFade;
    private Animation animRotate;

    public BluetoothService bluetoothService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        init();
        fadeIn();
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
        fadeIn();

        bluetoothService = ((MainActivity) MainActivity.context).bluetoothService;

        forestClick();
        cloudClick();
        oceanClick();
        businessClick();

//        bluetoothThread = ((MainActivity)MainActivity.context).getBluetoothThread();
//        mHandler = bluetoothThread.getWriteHandler();


//      BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

//        try {
//            btSocket = createBluetoothSocket(device);
//        } catch (IOException e1) {
//            errorMessage("Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
//        }
//
//        // Discovery is resource intensive.  Make sure it isn't going on
//        // when you attempt to connect and pass your message.
//        btAdapter.cancelDiscovery();
//
//        // Establish the connection.  This will block until it connects.
//        Log.d(TAG, "...Connecting...");
//        try {
//            btSocket.connect();
//            Log.d(TAG, "...Connection ok...");
//        } catch (IOException e) {
//            try {
//                btSocket.close();
//            } catch (IOException e2) {
//                errorMessage("Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
//            }
//        }
//
//        // Create a data stream so we can talk to server.
//        Log.d(TAG, "...Create Socket...");
//
//        try {
//            outStream = btSocket.getOutputStream();
//        } catch (IOException e) {
//            errorMessage("Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

//        if (outStream != null) {
//            try {
//                outStream.flush();
//            } catch (IOException e) {
//                errorMessage("Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
//            }
//        }

//        try {
//            btSocket.close();
//        } catch (IOException e2) {
//            errorExit("Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
//        }
    }

    private void init() {
        forestBtn = findViewById(R.id.forest);
        cloudBtn = findViewById(R.id.cloud);
        oceanBtn = findViewById(R.id.ocean);
        businessBtn = findViewById(R.id.business);
        secLogo = findViewById(R.id.secLogo);
        tv = findViewById(R.id.tv);

        animFade = AnimationUtils.loadAnimation(this, R.anim.btnfade);
        animRotate = AnimationUtils.loadAnimation(this, R.anim.listlogo);

    }

    private void fadeIn() {
        forestBtn.setAnimation(animFade);
        cloudBtn.setAnimation(animFade);
        oceanBtn.setAnimation(animFade);
        businessBtn.setAnimation(animFade);
        tv.setAnimation(animFade);

        secLogo.setAnimation(animRotate);
    }

    private void forestClick() {
        forestBtn.setOnClickListener(new OnSingleClickLinstener() {
            @Override
            public void onSingleClick(View v) {
                sendMessage("4");
                sendMessage("1");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ListActivity.this, ForestActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }, 8000);
            }
        });
    }

    private void cloudClick() {
        cloudBtn.setOnClickListener(new OnSingleClickLinstener() {
            @Override
            public void onSingleClick(View v) {
                sendMessage("1");
                sendMessage("4");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ListActivity.this, CloudActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }, 8000);
            }
        });
    }

    private void oceanClick() {
        oceanBtn.setOnClickListener(new OnSingleClickLinstener() {
            @Override
            public void onSingleClick(View v) {
                sendMessage("1");
                sendMessage("4");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ListActivity.this, OceanActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }, 8000);
            }
        });
    }

    private void businessClick() {
        businessBtn.setOnClickListener(new OnSingleClickLinstener() {
            @Override
            public void onSingleClick(View v) {
                sendMessage("1");
                sendMessage("4");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ListActivity.this, BusinessActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }, 8000);
            }
        });
    }

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Log.d(TAG, "sendMessage Error");
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            bluetoothService.write(send);
            Log.d(TAG, "ListActivity Send");
        }
    }
}


