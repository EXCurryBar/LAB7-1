package com.example.myapplication;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.annotation.SuppressLint;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.view.View;
        import android.widget.Button;
        import android.widget.SeekBar;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int rabprogress = 0, turporgress = 0;

    private SeekBar seekBar1,seekBar2;
    private Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar1 = findViewById(R.id.seekBar);
        seekBar2 = findViewById(R.id.seekBar2);
        btn_start = findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_start.setEnabled(false);
                rabprogress = 0;
                turporgress = 0;

                seekBar1.setProgress(0);
                seekBar2.setProgress(0);

                runThread();
                runAsyncTask();
            }
        });
    }
    private void runThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (rabprogress<=100 && turporgress <= 100){
                    try{
                        Thread.sleep(100);
                        rabprogress+=(int)(Math.random()*3.1);
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    seekBar1.setProgress(rabprogress);
                    break;
            }
            if (rabprogress>=100&&turporgress<100){
                Toast.makeText(MainActivity.this,"兔子勝利",Toast.LENGTH_SHORT).show();
                btn_start.setEnabled(true);
            }
            return false;
        }
    });
    private void runAsyncTask(){
        new AsyncTask<Void, Integer, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids){
                while (turporgress<=100 && rabprogress<100){
                    try{
                        Thread.sleep(100);
                        turporgress+=(int)(Math.random()*3);
                        publishProgress(turporgress);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                return true;
            }
            @Override
            protected void onProgressUpdate(Integer... values){
                super.onProgressUpdate(values);
                seekBar2.setProgress(values[0]);
            }
            @Override
            protected void onPostExecute(Boolean aBoolean){
                super.onPostExecute(aBoolean);
                if(turporgress>=100&&rabprogress<100){
                    Toast.makeText(MainActivity.this,"烏龜勝利",Toast.LENGTH_SHORT).show();
                    btn_start.setEnabled(true);
                }
            }
        }.execute();
    }
}
