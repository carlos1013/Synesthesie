package com.example.cadu.synesthesie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;

public class Configuracao extends AppCompatActivity {
    protected TextToSpeech t2s;
    int progresso_volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
        Button btnSair = (Button) findViewById(R.id.btn_voltar);
        btnSair.setOnClickListener(sairOnClick);
        Button btnVol = (Button) findViewById(R.id.btn_volume);
        btnVol.setOnClickListener(volumeOnClick);
        Button btnVel = (Button) findViewById(R.id.btn_velocidade);
        btnVel.setOnClickListener(velocidadeOnClick);
        Button btnTut = (Button) findViewById(R.id.btn_tutorial);
        btnTut.setOnClickListener(tutorialOnClick);
        t2s = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t2s.setLanguage(Locale.getDefault());
                    SharedPreferences appSet = getSharedPreferences("AppInfo",MODE_PRIVATE);
                    t2s.setSpeechRate(appSet.getFloat("SpeakVel",(float)0.8));
                    t2s.setPitch((float)0.8);
                }
            }
        });
    }

    private View.OnClickListener volumeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private void criarPP(){
        SeekBar seek = new SeekBar(this);
        final Toast torrada = Toast.makeText(this,"Velocidade da fala modificada",Toast.LENGTH_SHORT);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progresso_volume = progress;
            }
            public void onStartTrackingTouch(SeekBar arg0){}
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
        new AlertDialog.Builder(this)
                .setView(seek)
                .setTitle("Modifique a velocidade da fala:")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences appSet = getSharedPreferences("AppInfo",MODE_PRIVATE);
                        SharedPreferences.Editor setEd = appSet.edit();
                        setEd = setEd.putFloat("SpeakVel",(float) (progresso_volume/100.0));
                        setEd.apply();
                        torrada.show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private View.OnClickListener velocidadeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            criarPP();
        }
    };

    private View.OnClickListener tutorialOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InitTutorial it = new InitTutorial();
            String [] tutorial = it.initTutorial();
            for (int x=0;x<tutorial.length;x++) {
                t2s.speak(tutorial[x],TextToSpeech.QUEUE_ADD, null,"id");
            }
        }
    };

    private View.OnClickListener sairOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    public void onBackPressed(){
        t2s.stop();
        Intent voltar = new Intent(Configuracao.this,MainActivity.class);
        finish();
        startActivity(voltar);
    }

}