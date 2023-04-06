package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;

//사용자가 카운트다운 초기값을 설정해주는 기능
public class SettingActivity extends AppCompatActivity {
    //========== 레이아웃 생성, 초기화 컴포넌트를 불러옴
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //프로그램 Activity가 생성할 때 실행
        setContentView(R.layout.activity_setting); //화면 디자인을 xml로 정의해놓은 파일을 불러와 지정

        // ======== NumberPicker 초기화 =========
        
        //== 분을 지정하는 NumberPicker
        NumberPicker numberMinute; //NumberPicker 참조변수 선언
        numberMinute=findViewById(R.id.minute); //id가 minute인 NumberPicker를 찾아서 변수에 저장

        numberMinute.setMaxValue(60); //사용자가 선택할 수 있는 최대값 지정
        numberMinute.setMinValue(0); //사용자가 선택할 수 있는 최소값 지정
        numberMinute.setValue(0); //NumberPicker 초기값 지정

        //== 초를 지정하는 NumberPicker
        NumberPicker numberSecond; //NumberPicker 참조변수 선언
        numberSecond =findViewById(R.id.second); //id가 second인 NumberPicker를 찾아서 변수에 저장

        numberSecond.setMaxValue(59); //사용자가 선택할 수 있는 최대값 지정
        numberSecond.setMinValue(0); //사용자가 선택할 수 있는 최소값 지정
        numberSecond.setValue(10); //NumberPicker 초기값 지정

        // ========== 시작버튼 ==========

        //== ImageButton 참조변수를 선언하고 id가 imageButtonStart인 것을 찾아서 변수에 저장
        ImageButton startButton = findViewById(R.id.imageButtonStart);

        //== 시작 버튼을 눌렀을 때, 익명 클래스로 작성
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent는 메시징 객체로, 다른 앱 구성 요소로부터 작업을 요청하는 데 사용
                // 인텐트가 구성 요소 사이의 통신을 촉진
                Intent timer_intent = new Intent(SettingActivity.this, MainActivity.class);
                //putExtra: 요청된 작업을 수행하는데 필요한 추가 정보가 담긴 키-쌍
                //minute:(사용자가 지정한 분)을 MainActivity로 넘겨줌
                timer_intent.putExtra("minute",String.valueOf(numberMinute.getValue()));
                //second:(사용자가 지정한 초)을 MainActivity로 넘겨줌
                timer_intent.putExtra("second", String.valueOf(numberSecond.getValue()));
                //액티비티를 실행
                startActivity(timer_intent);

            }
        });

    }
}