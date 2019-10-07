package ych.com.bluetooth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import ych.com.bluetooth.base.BaseActivity;
import ych.com.bluetooth.base.BaseGestureActivity;
import ych.com.bluetooth.base.OnGestureListener;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/18
 *      desc   :
 *      version:
 * </pre>
 */

public class MailActivity extends BaseGestureActivity {
    private ImageView iv_send;
    private ImageView iv_back;
    private EditText et_recipients;
    private EditText et_sender;
    private TextView tv_importance;
    private Spinner snr_importance;
    private EditText et_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_mail);
    }

    @Override
    public void initView() {
        iv_send = findViewById(R.id.iv_send);
        iv_back = findViewById(R.id.iv_back);
        et_recipients = findViewById(R.id.et_recipients);
        et_sender = findViewById(R.id.et_sender);
        tv_importance = findViewById(R.id.tv_importance);
        et_theme = findViewById(R.id.et_theme);
        snr_importance = findViewById(R.id.snr_importance);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.setOnBackListener(new OnGestureListener() {
            @Override
            public void onBack(boolean onBack) {
                finish();
            }

            @Override
            public void onLeft(boolean onLeft) {

            }

            @Override
            public void onClickTwice(boolean onClickTwice) {

            }
        });
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_recipients.getText().toString() != null && !"".equals(et_recipients.getText().toString().trim())) {
                    if (et_sender.getText().toString() != null && !"".equals(et_sender.getText().toString().trim())) {
                        if (et_theme.getText().toString() != null && !"".equals(et_theme.getText().toString().trim())) {
                            // 必须明确使用mailto前缀来修饰邮件地址,如果使用
                            // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
                            Uri uri = Uri.parse("mailto:" + et_sender.getText().toString());
                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                            int index = et_recipients.getText().toString().trim().indexOf(",");
                            if (index == 0) {
                                String[] email = {et_recipients.getText().toString().trim()};
                                intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                            } else {
                                String[] email = et_recipients.getText().toString().trim().split(",");
                                intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                            }
                            intent.putExtra(Intent.EXTRA_SUBJECT, et_theme.getText().toString()); // 主题
                            intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文

                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/bluetooth.txt")));
                            startActivity(Intent.createChooser(intent, "请选择邮件类应用"));

                        } else {
                            Snackbar.make(iv_send, "请输入邮件主题", Snackbar.LENGTH_SHORT).show();
                        }

                    } else {
                        Snackbar.make(iv_send, "请输入发件人", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(iv_send, "请输入收件人", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        snr_importance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String importance=(String) snr_importance.getSelectedItem();
                tv_importance.setText(importance);;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void sendMail() {
        //耗时操作要起子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EmailSender sender = new EmailSender();
                    //设置服务器地址和端口，可以查询网络
                    sender.setProperties("smtp.163.com", "25");
                    //分别设置发件人，邮件标题和文本内容
                    sender.setMessage("×××××××××@163.com", "title", "content");
                    //设置收件人
                    sender.setReceiver(new String[]{"×××××××@163.com"});
                    //添加附件换成你手机里正确的路径
                    // sender.addAttachment("/sdcard/emil/emil.txt");
                    //发送邮件
                    //sender.setMessage("你的163邮箱账号", "EmailS//ender", "Java Mail ！");这里面两个邮箱账号要一致
                    sender.sendEmail("smtp.163.com", "*****@163.com", "password");
                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
