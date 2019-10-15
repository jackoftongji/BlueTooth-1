package ych.com.bluetooth.test;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.Toast;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ych.com.bluetooth.R;
import ych.com.bluetooth.bluetooth.BlueToothTool;

public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "FirstActivity";

    private EditText editTextId;
    private EditText editTextWaveform;
    private EditText editTextEquipment;
    private EditText editTextConstructionSite;
    private EditText editTextSection;
    private EditText editTextAuthor;
//    private EditText editTextUploadDate;
//    private EditText editTextFoundDate;

    private TextClock editTextUploadDate;
    private TextClock editTextFoundDate;

    private EditText editTextQueryData;

    private Button button1;
    private Button button2;

    private BluetoothDevice device;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox rememberInfo;



    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String data = (String)msg.obj;
            editTextWaveform.setText(data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
        //setTime();
    }

    private void initListener() {

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();
                long id01 = Long.parseLong(id);
                String waveform = editTextWaveform.getText().toString();
                String equipment = editTextEquipment.getText().toString();
                String constructionSite = editTextConstructionSite.getText().toString();
                String section = editTextSection.getText().toString();
                String author = editTextAuthor.getText().toString();
                String uploadDate = editTextUploadDate.getText().toString();
                String foundDate = editTextFoundDate.getText().toString();

                putDataInSharedPreferences();

                //setTime();

                Retrofit retrofit;
                //Ultrasound ultrasound = new Ultrasound(20,"0.00 0.01 1.12 1.34","上海","一号段","400km","肖","2019年8月29号","2019年8月29号");
                Ultrasound ultrasound = new Ultrasound(id01, waveform, equipment, constructionSite, section, author, uploadDate, foundDate);
                Gson gson = new Gson();
                String obj = gson.toJson(ultrasound);
                retrofit = new Retrofit.Builder().baseUrl("http://homily.cn:8001/").build();
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
                BookService bookService = retrofit.create(BookService.class);
                retrofit2.Call<ResponseBody> data = bookService.getBook(body);
                data.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("firstActivity", "onResponse: --ok--" + response.body());
                        Toast.makeText(FirstActivity.this, "success", Toast.LENGTH_LONG).show();
                        try {
                            Log.d("firstActivity", "onResponse: --ok--" + response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        Log.d("firstActivity", "onResponse: --err--" + t.toString());
                    }
                });

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string09 = editTextQueryData.getText().toString();
                Long id02 = Long.parseLong(string09);
                Retrofit retrofit02 = new Retrofit.Builder().baseUrl("http://homily.cn:8001/").build();
                BookService bookService02 = retrofit02.create(BookService.class);
                Call<ResponseBody> call =  bookService02.getUltrasound(id02);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {

                            String responseData = response.body().string();
                            parseJSONWithJSONObject(responseData);
                            Log.d("firstActivity", "从数据库拿到数据:"+responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void parseJSONWithJSONObject(String jsonData) throws JSONException {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String data = jsonObject.getString("waveform");
                        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                        intent.putExtra("data", data);
                        startActivity(intent);
                        Log.d("firstActivity", "数据带去活动二了");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("firstActivity", t.toString());
                    }
                });
            }
        });

    }

    private void initData() {
        Intent intent = this.getIntent();
        device = (BluetoothDevice) intent.getParcelableExtra("device");
        BlueToothTool.getINSTANCE(this).connect(device);

        BlueToothTool.getINSTANCE(this).setFirstListener(new FirstListener() {
            @Override
            public void sendData(String data) {
                Message message = new Message();
                message.obj = data;
                handler.sendMessage(message);

            }
        });

        //记住所输信息
        getDataFromSharedPreferences();

    }

    private void initView() {
        button1 = (Button) findViewById(R.id.Button_1);
        button2 = (Button) findViewById(R.id.Button_2);
        editTextId = (EditText) findViewById(R.id.id);
        editTextWaveform = (EditText) findViewById(R.id.waveform);
        editTextEquipment = (EditText) findViewById(R.id.equipment);
        editTextConstructionSite = (EditText) findViewById(R.id.construction_site);
        editTextSection = (EditText) findViewById(R.id.section);
        editTextAuthor = (EditText) findViewById(R.id.author);
//        editTextUploadDate = (EditText) findViewById(R.id.upload_date);
//        editTextFoundDate = (EditText) findViewById(R.id.found_date);

        editTextUploadDate = (TextClock) findViewById(R.id.upload_date);
        editTextFoundDate = (TextClock) findViewById(R.id.found_date);

        editTextQueryData = (EditText) findViewById(R.id.query_data);

        rememberInfo=(CheckBox)findViewById(R.id.remember_info);
    }

    private void initLayout() {
        setContentView(R.layout.first_layout);
    }

    public void collectDataAgain(View view){

        recreate();

//        setTime();
        putDataInSharedPreferences();
        Toast.makeText(this, "collectDataAgain", Toast.LENGTH_SHORT).show();
    }



    public void setTime(){
        Calendar calendar=Calendar.getInstance();

        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        editTextUploadDate.setText(year+"年"+month+"月"+day+"日  "+hour+":"+minute);
        editTextFoundDate.setText(year+"年"+month+"月"+day+"日  "+hour+":"+minute);
    }


    //记住所输信息

    public void getDataFromSharedPreferences(){

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember=sharedPreferences.getBoolean("remember_info",false);

        if (isRemember){
            String equipment1=sharedPreferences.getString("equipment","");
            String constructionSite1=sharedPreferences.getString("constructionSite","");
            String section1=sharedPreferences.getString("section","");
            String author1=sharedPreferences.getString("author","");
            String uploadDate1=sharedPreferences.getString("uploadDate","");
            String foundDate1=sharedPreferences.getString("foundDate","");

            editTextEquipment.setText(equipment1);
            editTextConstructionSite.setText(constructionSite1);
            editTextSection.setText(section1);
            editTextAuthor.setText(author1);
            editTextUploadDate.setText(uploadDate1);
            editTextFoundDate.setText(foundDate1);

            rememberInfo.setChecked(true);
        }

    }

    public void putDataInSharedPreferences(){
        String equipment0 = editTextEquipment.getText().toString();
        String constructionSite0 = editTextConstructionSite.getText().toString();
        String section0 = editTextSection.getText().toString();
        String author0 = editTextAuthor.getText().toString();
        String uploadDate0 = editTextUploadDate.getText().toString();
        String foundDate0 = editTextFoundDate.getText().toString();

        editor=sharedPreferences.edit();
        if (rememberInfo.isChecked()){
            editor.putBoolean("remember_info",true);

            editor.putString("equipment",equipment0);
            editor.putString("constructionSite",constructionSite0);
            editor.putString("section",section0);
            editor.putString("author",author0);
            editor.putString("uploadDate",uploadDate0);
            editor.putString("foundDate",foundDate0);
        }else {
            editor.clear();
        }
        editor.apply();
    }




}
