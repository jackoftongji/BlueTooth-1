package ych.com.bluetooth.test;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ych.com.bluetooth.R;
import ych.com.bluetooth.bluetooth.BlueToothTool;

public class FirstActivity extends AppCompatActivity {
    private EditText editText01;
    private EditText editText02;
    private EditText editText03;
    private EditText editText04;
    private EditText editText05;
    private EditText editText06;
    private EditText editText07;
    private EditText editText08;
    private EditText editText09;

    private Button button1;
    private Button button2;

    private BluetoothDevice device;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String data = (String)msg.obj;
            editText02.setText(data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();

    }

    private void initListener() {

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string01 = editText01.getText().toString();
                long id01 = Long.parseLong(string01);
                String string02 = editText02.getText().toString();
                String string03 = editText03.getText().toString();
                String string04 = editText04.getText().toString();
                String string05 = editText05.getText().toString();
                String string06 = editText06.getText().toString();
                String string07 = editText07.getText().toString();
                String string08 = editText08.getText().toString();
                Retrofit retrofit;
                //Ultrasound ultrasound = new Ultrasound(20,"0.00 0.01 1.12 1.34","上海","一号段","400km","肖","2019年8月29号","2019年8月29号");
                Ultrasound ultrasound = new Ultrasound(id01, string02, string03, string04, string05, string06, string07, string08);
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
                String string09 = editText09.getText().toString();
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

    }

    private void initView() {
        button1 = (Button) findViewById(R.id.Button_1);
        button2 = (Button) findViewById(R.id.Button_2);
        editText01 = (EditText) findViewById(R.id.id);
        editText02 = (EditText) findViewById(R.id.waveform);
        editText03 = (EditText) findViewById(R.id.construction_site);
        editText04 = (EditText) findViewById(R.id.section);
        editText05 = (EditText) findViewById(R.id.mileage);
        editText06 = (EditText) findViewById(R.id.author);
        editText07 = (EditText) findViewById(R.id.upload_data);
        editText08 = (EditText) findViewById(R.id.found_data);
        editText09 = (EditText) findViewById(R.id.query_data);
    }

    private void initLayout() {
        setContentView(R.layout.first_layout);
    }
}
