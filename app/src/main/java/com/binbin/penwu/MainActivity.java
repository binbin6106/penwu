package com.binbin.penwu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    private int SECOND = 1;

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            Toast.makeText(MainActivity.this, "当前进度：" + progress, Toast.LENGTH_SHORT).show();
            setSpeed((progress*200));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
//            Toast.makeText(MainActivity.this, "开始：" + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//            Toast.makeText(MainActivity.this, "结束：" + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch mSwitch = (Switch) findViewById(R.id.switch1);
        Switch mSwitch_1 = (Switch) findViewById(R.id.switch3);
        SeekBar sb1 = (SeekBar)findViewById(R.id.seekBar2);
        sb1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        initButton();
//        mText = (TextView) findViewById(R.id.text_);
        // 添加监听
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!buttonView.isPressed()) {
//                    return;
//                }
                if (isChecked){
                    Connect(null);
                }else {
                    DisConnect(null);

                }
            }
        });

        mSwitch_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ServOn(null);
                }else {
                    ServOff(null);
                }
            }
        });
        mTimerTask = new TimerTask( ) {
            @Override
            public void run() {
                readPasic(null);
            }
        };
        mTimer.schedule( mTimerTask, 1000, SECOND * 1000 );


    }


    public void initButton(){
        Button xCWButton = findViewById(R.id.button6); //X轴正向运行/停止按钮
        Button xCWWButton = findViewById(R.id.button7); //X轴反向运行/停止按钮
        Button xAutoRunButton = findViewById(R.id.button4); //X轴自动运行按钮
        Button xStopRunButton = findViewById(R.id.button5); //X轴自动运行停止按钮
        Button yCWButton = findViewById(R.id.button8); //Y轴正向运行/停止按钮
        Button yCWWButton = findViewById(R.id.button9); //Y轴反向运行/停止按钮
        Button zCWButton = findViewById(R.id.button10); //Z轴正向运行/停止按钮
        Button zCWWButton = findViewById(R.id.button11); //Z轴反向运行/停止按钮

        xCWButton.setOnTouchListener(new View.OnTouchListener(){    //X轴正向运行/停止按钮
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    xCWRun(null);
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    xCWStop(null);
                }
                return false;
            }
        });

        xCWWButton.setOnTouchListener(new View.OnTouchListener(){   //X轴反向运行/停止按钮
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    xCWWRun(null);
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    xCWWStop(null);
                }
                return false;
            }
        });

        xAutoRunButton.setOnTouchListener(new View.OnTouchListener(){   //X轴自动运行按钮
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    xAutoRun(null);
                }

//                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
//                    xCWStop(null);
//                }
                return false;
            }
        });

        xStopRunButton.setOnTouchListener(new View.OnTouchListener(){   //X轴自动运行停止按钮
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    xAutoStop(null);
                }

//                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
//                    xCWStop(null);
//                }
                return false;
            }
        });

        yCWButton.setOnTouchListener(new View.OnTouchListener(){   //Y轴正向运行/停止按钮
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    yCWRun(null);
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    yCWStop(null);
                }
                return false;
            }
        });

        yCWWButton.setOnTouchListener(new View.OnTouchListener(){   //Y轴反向运行/停止按钮
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    yCWWRun(null);
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    yCWWStop(null);
                }
                return false;
            }
        });

        zCWButton.setOnTouchListener(new View.OnTouchListener(){   //Z轴正向运行/停止按钮
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    zCWRun(null);
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    zCWStop(null);
                }
                return false;
            }
        });

        zCWWButton.setOnTouchListener(new View.OnTouchListener(){   //Z轴反向运行/停止按钮
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    zCWWRun(null);
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    zCWWStop(null);
                }
                return false;
            }
        });
    }
    public void Connect(View view)
    {
        new Thread(){
            @Override
            public void run() {
                EditText PlcIP = (EditText) findViewById(R.id.editText2);
                ModbusReq.getInstance().setParam(new ModbusParam()
                        .setHost(PlcIP.getText().toString())
                        .setPort(502)
                        .setEncapsulated(false)
                        .setKeepAlive(true)
                        .setTimeout(2000)
                        .setRetries(0))
                        .init(new OnRequestBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                Log.d(TAG, "onSuccess " + s);

//                                Looper.prepare();
//                                Toast.makeText(MainActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();
//                                Looper.loop();
                            }

                            @Override
                            public void onFailed(String msg) {
                                Log.d(TAG, "onFailed " + msg);
//                                Switch mSwitch = (Switch) findViewById(R.id.switch1);
//                                mSwitch.setChecked(false);
//                                Looper.prepare();
//                                Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
//                                Looper.loop();

                            }
                        });
            }
        }.start();
    }

    public void DisConnect(View view){
        ModbusReq.getInstance().destory();
    }
    public void ServOn(View view)
    {
        new Thread(){
            @Override
            public void run(){
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s){
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,8,true);
            }
        }.start();
    }

    public void ServOff(View view)
    {
        new Thread(){
            @Override
            public void run(){
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,8,false);
            }
        }.start();
    }

    public void xCWRun(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,10,true);
                //X轴方向，true代表反向
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,1,true);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,0,true);
            }
        }.start();
    }

    public void xCWStop(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,10,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,0,false);
            }
        }.start();
    }

    public void xCWWRun(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,10,true);
                //X轴方向，true代表反向
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,1,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,0,true);
            }
        }.start();
    }

    public void xCWWStop(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,10,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,0,false);
            }
        }.start();
    }

    public void xAutoRun(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,10,true);
                //X轴方向控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,1,true);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,4,true);
            }
        }.start();
    }

    public void xAutoStop(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,10,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,4,false);
            }
        }.start();
    }

    public void yCWRun(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,11,true);
                //X轴方向控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,3,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,2,true);
            }
        }.start();
    }

    public void yCWStop(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,11,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,2,false);
            }
        }.start();
    }

    public void yCWWRun(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,11,true);
                //X轴方向控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,3,true);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,2,true);
            }
        }.start();
    }

    public void yCWWStop(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,11,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,2,false);
            }
        }.start();
    }

    public void zCWRun(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,12,true);
                //X轴方向控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,7,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,6,true);
            }
        }.start();
    }

    public void zCWStop(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,12,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,6,false);
            }
        }.start();
    }

    public void zCWWRun(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,12,true);
                //X轴方向控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,7,true);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,6,true);
            }
        }.start();
    }

    public void zCWWStop(View view){
        new Thread(){
            @Override
            public void run(){
                //X轴总开关
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,12,false);
                //X轴脉冲输出控制
                ModbusReq.getInstance().writeCoil(new OnRequestBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "writeCoil onSuccess " + s);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, "writeCoil onFailed " + msg);
                    }
                },1,6,false);
            }
        }.start();
    }

    public void setSpeed(final int Speed){
//        new Thread(){
//            @Override
//            public void run(){
//                ModbusReq.getInstance().writeRegister(new OnRequestBack<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        Log.e(TAG, "writeRegister onSuccess " + s);
//                    }
//
//                    @Override
//                    public void onFailed(String msg) {
//                        Log.e(TAG, "writeRegister onFailed " + msg);
//                    }
//                },1,1, 213);
//            }
//        }.start();
        ModbusReq.getInstance().writeRegister(new OnRequestBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "writeRegister onSuccess " + s);
            }

            @Override
            public void onFailed(String msg) {
                Log.e(TAG, "writeRegister onFailed " + msg);
            }
        },1,1, Speed);
    }

    public void readPasic(View view){
//        new Thread(){
//            @Override
//            public void run(){
//
//            }
//        }.start();
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>() {
            @Override
            public void onSuccess(short[] data) {
//                Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
                TextView textView14 = (TextView) findViewById(R.id.textView14);
                TextView textView9 = findViewById(R.id.textView9);
                TextView textView10 = findViewById(R.id.textView10);
                textView14.setText(String.valueOf(data[0]));
                textView9.setText(String.valueOf(data[2]));
                textView10.setText(String.valueOf(data[4]));
            }

            @Override
            public void onFailed(String msg) {
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, 1, 28002, 6);
    }
}
