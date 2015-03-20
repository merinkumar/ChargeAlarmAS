package chargealarm.merin.com.myapplication;


import android.os.Bundle;




import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.merin.chargealarm.R;

import java.io.File;

public class MainActivity extends Activity {

    //
    private static final String TAG = "merin";
    public Button startbutton;
    public Button stopbutton;
    public Button choosebutton;
    public EditText bleveltxt;
    public SeekBar bseek;
    public File file = null;
    Uri uri = null;
    public int sProgress = 100;
    private static final int REQUEST_CHOOSER = 1234;

    //public TextView batteryInfo;
    Intent batteryStatus;
    //public MediaPlayer myMedia;
    Intent SIntent;// = new Intent(MainActivity.this,ChargerService.class);
    //Intent stopSIntent = new Intent(MainActivity.this,ChargerService.class);
    Intent getContentIntent = FileUtils.createGetContentIntent();
    Intent intent = Intent.createChooser(getContentIntent, "Select a file");

    Bundle myBundle = new Bundle();

    protected int blevel = 100;

    public static String B_LEVEL = "Battery Level";
    public static String FILE_NAME = "File Name";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the reference to activity obects
        startbutton = (Button) findViewById(R.id.test);
        stopbutton = (Button) findViewById(R.id.stop);
        choosebutton = (Button) findViewById(R.id.chooseFile);
        bleveltxt = (EditText) findViewById(R.id.bLevel);
        bseek = (SeekBar) findViewById(R.id.bSeek);

        SIntent = new Intent(MainActivity.this,ChargerService.class);





        startbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //startService(new Intent(MainActivity.this,ChargerService.class));
                //myBundle.putParcelable(FILE_NAME, filename);

                blevel = Integer.parseInt(bleveltxt.getText().toString());
                SIntent.putExtra(B_LEVEL, blevel );

                SIntent.putExtra(FILE_NAME,uri );


                startService(SIntent);
            }
        });

        stopbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this,ChargerService.class));

            }
        });

        choosebutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(intent, REQUEST_CHOOSER);

            }
        });



        bseek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(fromUser){
                    blevel = progress;
                    bleveltxt.setText(String.valueOf(blevel));

                }

            }
        });

        bleveltxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = 0;
                try {
                    value = Integer.parseInt(bleveltxt.getText().toString());
                } catch (NumberFormatException e) {

                    bseek.setProgress(1);
                    e.printStackTrace();
                }

                if (value <= 100 && value >= 0){
                    bseek.setProgress(value);
                }else{
                    bseek.setProgress(100);
                }

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    //Toast.makeText(this, "Uri : " + uri, Toast.LENGTH_LONG).show();
                    file = FileUtils.getFile(uri);
                    Toast.makeText(this, "file : " + file.toString(), Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onDestroy() {

        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Log.e(TAG, "onBackPressed");
        this.finish();
    }


}
