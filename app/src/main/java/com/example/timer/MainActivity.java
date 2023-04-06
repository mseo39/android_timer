package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer; //CountDownTimer 참조변수 선언
    
    //사용자로 부터 입력 받은 값을 저장할 변수선언
    //0으로 초기화
    Integer minute=0;
    Integer second=0;

    //전역변수로 textview, progressBar 참조변수를 선언
    TextView timerText;
    ProgressBar timeProgressBar;

    //MediaPlayer 참조 변수 선언, 타이머가 끝나고 음악이 나오게 하기 위해서이다
    private MediaPlayer player;

    //타이머에 사용될 변수를 지정_ 처음에는 0으로 초기화해준다
    //milli는 사용자로부터 입력 받은 초기 millisecond값을 저장하는 변수이다
    //변하지 않는다
    Integer milli=0;
    //milli_long 변수는 초기값은 사용자로부터 입력받은 것이고
    //타이머가 시작하면 남은 millisecond값으로 계속 바뀐다
    //왜? 타이머를 취소하고 다시 타이머를 시작할 때 취소했을 때 남은 시간에서 다시 시작해야하기 때문에
    //남은 시간을 계속 저장해줘야한다
    //재시작하고 싶을 때는 변수에 사용자가 지정한 초기값을 지정해주면 된다
    Long milli_long=(long)milli;

    //========== 레이아웃 생성, 초기화 컴포넌트를 불러옴
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //프로그램 Activity가 생성할 때 실행
        setContentView(R.layout.activity_main); //화면 디자인을 xml로 정의해놓은 파일을 불러와 지정

        //=====전달받은 인텐트를 가져온다
        Intent intent = getIntent();
        //전달받은 데이터를 가져옴 키-값 형식으로 오기 때문에 키를 이용해서 값을 가져오면 된다
        minute = Integer.valueOf(intent.getStringExtra("minute"));
        second = Integer.valueOf(intent.getStringExtra("second"));

        //인텐트로 전달된 값(사용자로부터 받은 분과 초)을 밀리세컨드로 바꿔주고 milli 변수에 저장
        milli=(minute * 60000)+(second * 1000);
        //사용자로부터 입력받은 값을 저장
        milli_long=(long)milli;

        //===== 뷰들의 참조변수를 선언하고 저장
        //재시작 버튼
        //Button 참조변수 선언
        //id가 buttonreplay인 Button을 찾아서 변수에 저장
        Button replayButton = (Button) findViewById(R.id.buttonreplay);
        //시작/일시정지 버튼
        //ToggleButton 참조변수 선언
        //id가 toggleButton인 ToggleButton을 찾아서 변수에 저장
        ToggleButton toggleButton= (ToggleButton) findViewById(R.id.toggleButton);

        //남은 시간을 보여줄 텍스트
        //id가 textViewTime인 TextView을 찾아서 변수에 저장
        timerText= (TextView) findViewById(R.id.textViewTime);
        //사용자가 입력한 시간으로 지정해준다
        //%02d는 10진수 두자리 정수이며 남은 부분은 0으로 채운다는 의미이다
        timerText.setText(String.format("%02d:%02d", minute, second));

        //남은 시간을 시각적으로 보여둘 프로그레스 바
        //id가 progressbar인 ProgressBar을 찾아서 변수에 저장
        timeProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        //토글버튼이 클릭했을 때(체크가 변했을 때)
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){//토글버튼이 true 상태라면(텍스트가 일시정지일 때 누름)
                    countDownTimer.cancel(); //타이머를 멈춘다
                }else{
                    //토글버튼이 flase 상태라면(텍스트가 시작일 때 누름)
                    //남은 시간을 보여주는 텍스트를 가져와서 사용자로부터 입력받은 값과 같다면
                    //타이머가 아직 시작 안했었다는 것이기 때문에 타이머 시작함수를 호출한다
                    if(timerText.getText().toString().equals(String.format("%02d:%02d", minute, second))) {
                        timerStart();
                    }else{//만약 다르다면 일시정지했기 때문이므로 timer를 호출하고
                        //타이머를 시작한다
                        Timer();
                        countDownTimer.start();
                    }
                }
            }
        });

        
        //재시작 이미지를 눌렀을 때, 익명 클래스로 작성
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //타이머를 취소한다
                countDownTimer.cancel();
                //처음값으로 변경해준다
                milli_long=(long)milli;
                //타이머 시작함수를 호출한다
                timerStart();
                //텍스트도 처음값으로 지정해준다
                timerText.setText(String.format("%02d:%02d", minute, second));
                //토글은 true로 지정해줘서 타이머가 바로 시작하지않고 조기화된 상태에서 멈추게 한다
                toggleButton.setChecked(true);

            }
        });

    }
    
    //타이머를 새로 시작할 때
    public void timerStart(){
        //Timer함수 호출
        Timer();
        //카운트다운 시작
        countDownTimer.start();

        //프로세스 바의 최대값을 지정(단위를 초로 바꾸어 지정)
        timeProgressBar.setMax((int) milli / 1000);
        //현제의 프로그레스 바를 지정(최대값과 같은 값으로 지정하기 때문에 프로세스 바가 채워진 상태에서 시작한다)
        timeProgressBar.setProgress((int) milli / 1000);
    }
    
    //타이머의 기능을 수행하는 함수
    public void Timer(){
        //CountDownTimer 객체 생성, 매개변수는 총 시간, 시간 간격이다
        countDownTimer = new CountDownTimer(milli_long, 1000) {
            //1초에 한번씩 onTick이 실행됨(매개변수로 1000밀리세컨드를 보냈기 때문)
            //millisUntilFinished에는 남은 밀리초가 저장되어 있음
            public void onTick(long millisUntilFinished) {
                //남은 시간을 보여주는 텍스트뷰에 남은 시간으로 지정해준다
                //1분은 60000밀리초이고 1초는 1000밀리초인것을 이용한다
                //분은 millisUntilFinished/60000이고
                //초는 millisUntilFinished/1000을 계산해서 초로 바꿔주고 이미 앞에서 계산한
                //millisUntilFinished/60000을 빼줘야 하는데 millisUntilFinished/60000은 단위가 분이므로 초로 단위를 바꾸어 빼줘야하기 때문에
                //(millisUntilFinished/60000)*60을 해준 뒤 빼준 것이다
                //%02d: 총 2자리수인 10진수이며 비어진 부분은 0으로 채우겠다는 것이다
                timerText.setText(
                String.format("%02d:%02d",
                        millisUntilFinished/60000,
                        millisUntilFinished/1000 - (millisUntilFinished/60000)*60));
                //현재 초를 계산하여 프로그레스 바에 지정해준다
                timeProgressBar.setProgress((int)(millisUntilFinished/1000));

                //milli_long에 현재 남은 시간을 저장해준다
                milli_long=millisUntilFinished;

            }//총시간이 지나고 실행되는 메소드이다
            public void onFinish() {
                //res/raw/alarm.mp3을 가진 미디어 플레이어를 생성한다
                player=MediaPlayer.create(MainActivity.this, R.raw.alarm);
                //소리 크기를 지정한다, 왼쪽 100, 오른쪽 100으로 지정해주었다
                player.setVolume(100,100);
                //미디어플레이어 설정이 다 끝났으니 실행을 해준다
                player.start();

            }
        };
    }

    //countdowntimer는 현재 스레드의 상태를 확인하는 부분이 없기 때문에
    //카운트다운이 돌아가지 않는데 cancel()을 하면 에러가 나는 상황이 발생하기 때문에
    //에러가 발생하면 countdowntimer를 초기화하는 방식으로 해결이 가능함
    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }
}