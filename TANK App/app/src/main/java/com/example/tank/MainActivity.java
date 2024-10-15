package com.example.tank;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
public class MainActivity extends AppCompatActivity {

    RelativeLayout layout_joystick;
    RelativeLayout layout_joystick2;
    ImageView image_joystick, image_border;
    TextView textView1, textView2, textView3, textView4, textView5;
    TextView textView6, textView7, textView8, textView9, textView0;
    JoyStickClass js;
    JoyStickClass js2;
    int MY_PERMISSION_ACCESS_COARSE_LOCATION = 100;
    BluetoothAdapter bltA;
    int Request_Enable_Bt = 1;
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    OutputStream out = null;
    static InputStream input = null;
    InputStreamReader aReader = null;
    private BufferedReader mBufferedReader = null;
    BluetoothDevice device;
    int read;
    int aux;
    int aux1;
    String array;
    Runnable runing;
    MediaPlayer shoot;
    MediaPlayer start;
    MediaPlayer stop;
    MediaPlayer stationary;
    MediaPlayer gear1;
    MediaPlayer gear2;
    MediaPlayer gear3;
    boolean moving=false;
    int flag;
    Button btnSpeed;
    Button fire;
    Button fire2;
    int viteza=0;
    Set<BluetoothDevice> pairedDevices;
    String NumesiAdresa;
    Spinner spiner;
    ArrayList<String> listaPaired=new ArrayList<String>();




    @SuppressLint({"MissingInflatedId", "MissingPermission", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSpeed=findViewById(R.id.button2);
        fire=findViewById(R.id.button);
        fire2=findViewById(R.id.button3);


          shoot=MediaPlayer.create(this, R.raw.shoot);
          start=MediaPlayer.create(this, R.raw.start);
          stop=MediaPlayer.create(this, R.raw.stop);
          stationary=MediaPlayer.create(this, R.raw.relanti);
        gear1=MediaPlayer.create(this,R.raw.speed1);
        gear2=MediaPlayer.create(this, R.raw.speed2);
        gear3= MediaPlayer.create(this, R.raw.speed3);


        bltA = BluetoothAdapter.getDefaultAdapter();

        if (bltA == null) {
            finish();
        }
        if (!bltA.isEnabled()) {
            Intent enableBlt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlt, Request_Enable_Bt);
        }

//        pairedDevices=bltA.getBondedDevices();
//
//        if(pairedDevices.size()>0)
//        {             for (BluetoothDevice device : pairedDevices) {
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            // get the device name
//            String deviceName = device.getName();
//
//            // get the mac address
//            String macAddress = device.getAddress();
//
//            // append in the two separate views
//            String total= deviceName+ " " + macAddress;
//
//
//            listaPaired.add(total);
//
//        }
//        }
        final UUID SerialUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        String mac = "16:C4:01:00:81:82";
//        String mac = "98:D3:B1:FD:64:0F";

        device = bltA.getRemoteDevice(mac);
        try {
//            mBTSocket = device.createRfcommSocketToServiceRecord(SerialUUID);
            mBTSocket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        InputStream INPUT = null;
        try {
            if (mBTSocket != null) {
                mBTSocket.connect();
            }
            out = mBTSocket.getOutputStream();
            input = mBTSocket.getInputStream();
            aReader = new InputStreamReader(input);
            mBufferedReader = new BufferedReader(aReader);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }


        start.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stationary.start();
                stationary.setLooping(true);
            }
        });
        btnSpeed.setText("0");

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writing("]");
            }
        });

        fire2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoot.start();
                writing("g");

            }
        });

        btnSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viteza=viteza+1;
                if(viteza==4)
                {
                    viteza=0;
                    stationary.pause();
                    stop.start();
                }

                btnSpeed.setText(String.valueOf(viteza));
                writing(String.valueOf(viteza));

                if(viteza==1)
                {
                    start.start();
//                    stationary.start();
//                    stationary.setLooping(true);
                }
//                else if(viteza==2)
//                {
//                    media.release();
//                    treapta2.start();
//                    treapta2.setLooping(true);
//                }
//                else if(viteza==3)
//                {
//                    media.release();
//                    treapta3.start();
//                    treapta3.setLooping(true);
//                }

            }
        });



//
//        ArrayAdapter<String> adapterForSpiner=new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listaPaired);
//        adapterForSpiner.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//        spiner.setAdapter(adapterForSpiner);



        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.button4);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {

                js.drawStick(arg1);

                int direction = js.get8Direction();
                aux1=aux;
                aux=direction;
                if(aux!=aux1){
                    soundEffects(direction);
                }


                if(viteza==0)
                {
                    writing("S");
                }
                else if(viteza!=0) {

                    if (direction == JoyStickClass.STICK_UP) {
                        writing("Q");
                        moving=true;
                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        writing("W");
                        moving=true;

                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        writing("E");
                        moving=true;

                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        writing("R");
                        moving=true;

                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        writing("T");
                        moving=true;

                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        writing("Y");
                        moving=true;

                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        writing("U");
                        moving=true;

                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        writing("M");
                        moving=true;

                    } else if (direction == JoyStickClass.STICK_NONE)
                        writing("S");

                }
                return true;
            }
        });

        layout_joystick2 = (RelativeLayout) findViewById(R.id.layout_joystick2);

        js2 = new JoyStickClass(getApplicationContext(), layout_joystick2, R.drawable.button4);
        js2.setStickSize(150, 150);
        js2.setLayoutSize(500, 500);
        js2.setLayoutAlpha(150);
        js2.setStickAlpha(100);
        js2.setOffset(90);
        js2.setMinimumDistance(50);

        layout_joystick2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js2.drawStick(arg1);
//                    if (arg1.getAction() == MotionEvent.ACTION_DOWN
//                            || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                        int direction = js2.get8Direction();
                        if (direction == JoyStickClass.STICK_UP) {
                            writing("V");
                        } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                            writing("A");
                        } else if (direction == JoyStickClass.STICK_RIGHT) {
                            writing("L");
                        } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                            writing("A");
                        } else if (direction == JoyStickClass.STICK_DOWN) {
                            writing("C");
                        } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                            writing("A");
                        } else if (direction == JoyStickClass.STICK_LEFT) {
                            writing("P");
                        } else if (direction == JoyStickClass.STICK_UPLEFT) {
                            writing("A");
                        } else if (direction == JoyStickClass.STICK_NONE) {
                            writing("A");
                        }

                return true;
            }
        });

    }


    public void writing(String s) {
        try {
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            out.write(bytes);

        } catch (IOException e) {
        }
    }

    public void soundEffects(int direction) {
        if (direction == 0 && viteza != 0) {
            stationary.start();
            stationary.setLooping(true);
            if(gear1.isPlaying()) {
                gear1.pause();
            }
            if(gear2.isPlaying()) {
                gear2.pause();
            }
            if(gear3.isPlaying()) {
                gear3.pause();
            }
        } else if (direction != 0 && viteza != 0) {
            if (viteza == 1) {
                stationary.pause();
                gear1.start();
                gear1.setLooping(true);
            }
            if (viteza == 2) {
                stationary.pause();
                gear2.start();
                gear2.setLooping(true);
            }
            if (viteza == 3) {
                stationary.pause();
                gear3.start();
                gear3.setLooping(true);
            }
        }

    }

}