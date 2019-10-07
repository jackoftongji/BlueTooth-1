package ych.com.bluetooth.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/15
 *      desc   :
 *      version:
 * </pre>
 */

public class BaseUtils {
    private static BaseUtils INSTANCE = null;

    public static BaseUtils getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (BaseUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaseUtils();
                }
            }
        }
        return INSTANCE;
    }

    public String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public boolean string2File(String data) {
        Log.e("即将保存到文件中的数据--",data);
        //将保存的数据保存在本地文件中
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/bluetooth.txt");
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(file, true));
            ps.println(data);// 往文件里写入字符串
            ps.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getCrc(byte[] data) {
        int high;
        int flag;

        // 16位寄存器，所有数位均为1
        int wcrc = 0xffff;
        for (int i = 0; i < data.length; i++) {
            // 16 位寄存器的高位字节
            high = wcrc >> 8;
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
            wcrc = high ^ data[i];

            for (int j = 0; j < 8; j++) {
                flag = wcrc & 0x0001;
                // 把这个 16 寄存器向右移一位
                wcrc = wcrc >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if (flag == 1)
                    wcrc ^= 0xa001;
            }
        }

        return Integer.toHexString(wcrc);
    }

    public  String CRC16_Check(byte[] Pushdata, int length)
    {
        int Reg_CRC = 0xffff;
        int temp;
        int i, j;
        for (i = 0; i < length; i++) {
            temp = Pushdata[i];
            if (temp < 0) temp += 256;
            temp &= 0xff;
            Reg_CRC ^= temp;
            for (j = 0; j < 8; j++) {
                if ((Reg_CRC & 0x0001) == 0x0001)
                    Reg_CRC = (Reg_CRC >> 1) ^ 0xA001;
                else
                    Reg_CRC >>= 1;
            }
        }
        return Integer.toHexString((Reg_CRC & 0xffff));
    }
    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Integer.toHexString(CRC);
    }

    public static String crc16(byte[] crc_array) {
        int i, j;
        int modbus_crc;
        modbus_crc = 0xffff;
        for (i = 0; i < crc_array.length; i++)
        {
            modbus_crc = (modbus_crc & 0xFF00) | ((modbus_crc & 0x00FF) ^ crc_array[i]);
            for (j = 1; j <= 8; j++)
            {
                if ((modbus_crc & 0x01) == 1)
                {
                    modbus_crc = (modbus_crc >> 1);
                    modbus_crc ^= 0XA001;
                }
                else
                {
                    modbus_crc = (modbus_crc >> 1);
                }
            }
        }
        return Long.toHexString(modbus_crc);
    }

}
