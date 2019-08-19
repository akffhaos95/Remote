package com.example.remote;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SpeechRecognizeListener {

    private SpeechRecognizerClient client;
    private static final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 0;
    TextView tv_result;
    ImageView iv_mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //주요 권한 사용자에게 다시 체크 받음
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE);
            } else {
                // 유저가 거부하면서 다시 묻지 않기를 클릭.. 권한이 없다고 유저에게 직접 알림.
            }
        } else {
            //startUsingSpeechSDK();
        }


        // SDK 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        // 클라이언트 생성 - 마이이크 아이콘에 동작하도록 하자.
        //SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB);

        iv_mic = (ImageView) findViewById(R.id.iv_mic);
        iv_mic.setOnClickListener(this);
        findViewById(R.id.bt_search).setOnClickListener(this);
        tv_result = (TextView) findViewById(R.id.tv_result);

        setButtonsStatus(true);

    }

    public void onDestroy(){
        super.onDestroy();

        // API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출한다.
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    //상황에 따라 버튼을 사용가능할지 불가능하게 할지 설정한다.
    private void setButtonsStatus(boolean enabled) {
        findViewById(R.id.iv_mic).setEnabled(enabled);
        findViewById(R.id.tv_result).setEnabled(enabled);
        findViewById(R.id.bt_search).setEnabled(!enabled);
    }

    //버튼 클릭들
    @Override
    public void onClick(View v){
        int id = v.getId();
        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;
        Log.i("MainActivity", "ServiceType : " + serviceType);

        //음성인식 시작 버튼-마이크 버튼
        if (id == R.id.iv_mic){
            SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(serviceType);
            client = builder.build();

            client.setSpeechRecognizeListener(this);
            client.startRecording(true);

            Toast.makeText(this, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();

            setButtonsStatus(false);
        } else {
            finish();
        }
    }


    //SpeechRecognizeListener의 여러가지 메소드들...
    @Override
    public void onReady() {//모든 하드웨어및 오디오 서비스가 모두 준비 된 다음 호출
        Log.d("MainActivity", "모든 준비가 완료 되었습니다.");
    }

    @Override
    public void onBeginningOfSpeech() { //사용자가 말하기 시작하는 순간 호출
        Log.d("MainActivity", "말하기 시작 했습니다.");
    }

    @Override
    public void onEndOfSpeech() {//사용자가 말하기를 끝냈다고 판단되면 호출
        Log.d("MainActivity", "말하기가 끝났습니다.");
    }

    @Override
    public void onError(int errorCode, String errorMsg) {

    }

    @Override
    public void onPartialResult(String partialResult) {//인식된 음성 데이터를 문자열로 알려 준다.

    }

    @Override
    public void onResults(Bundle results) {//음성 입력이 종료된것으로 판단하고 서버에 질의를 모두 마치고 나면 호출
        final StringBuilder builder = new StringBuilder();

        final ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        Log.d("MainActivity", "Result: " + texts);

        for (int i = 0; i < texts.size(); i++){
            builder.append(texts.get(i));
            builder.append(" (");
            builder.append(confs.get(i).intValue());
            builder.append(")\n");
        }

        //모든 콜백함수들은 백그라운드에서 돌고 있기 때문에 메인 UI를 변경할려면 runOnUiThread를 사용해야 한다.
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(activity.isFinishing()) return;

                tv_result.setText(texts.get(0));

                setButtonsStatus(true);
            }
        });

    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }
}
