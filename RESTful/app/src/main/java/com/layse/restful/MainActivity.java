package com.layse.restful;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MainActivity extends AppCompatActivity {

  private EditText txt;
  private Button btSend;
  private TextView txtNumber;

  private static Socket socketOnRequest;

  private Handler mHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    txt = findViewById(R.id.editTxt);
    txtNumber = findViewById(R.id.txtNumber);


    mHandler = new Handler();

    findViewById(R.id.btSend).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if(txt.getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "Nenhuma palavra digitada!", Toast.LENGTH_SHORT).show();
        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    postHttp(txt.getText().toString());
                }
            }).start();

        }
        //new MessageSender().execute(c);

      }
    });

  }

  public void postHttp(String txt) {

    HttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost("http://192.168.43.171:2001");

    try {
      ArrayList<NameValuePair> valores = new ArrayList<>();
      valores.add(new BasicNameValuePair("text", txt));

      httpPost.setEntity(new UrlEncodedFormEntity(valores));
      final HttpResponse resposta = httpClient.execute(httpPost);

      String res = EntityUtils.toString(resposta.getEntity());
      final JSONObject response = new JSONObject(res);

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                try {
                    txtNumber.setText(response.getString("size"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });



    } catch (ClientProtocolException e) {

    } catch (IOException e) {

    } catch (JSONException e) {
        e.printStackTrace();
    }

  }

}
