package com.example.cadu.synesthesie;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;


public class Biblioteca extends AppCompatActivity {
    protected Context context;
    protected int [][] map;
    protected String [] nomes;
    protected int atual;
    protected GerenciadorBiblioteca gb;
    protected TextToSpeech t2s;
    protected GeradorAudio ga;
    protected boolean rodando;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        gb = new GerenciadorBiblioteca(context);
        map = gb.inicializar(context);
        nomes = gb.inicializarNomes(context);
        if (map.length==0){
            Intent vazia = new Intent(Biblioteca.this,BibliotecaVazia.class);
            startActivity(vazia);
        }
        else {
            setContentView(R.layout.activity_biblioteca);
            Button btnRep = (Button) findViewById(R.id.btn_reproduzir);
            btnRep.setOnClickListener(reproduzirOnClick);
            Button btnRem = (Button) findViewById(R.id.btn_remover);
            btnRem.setOnClickListener(removerOnClick);
            Button btnEd = (Button) findViewById(R.id.btn_editar);
            btnEd.setOnClickListener(editarOnClick);
            Button btnLeft = (Button) findViewById(R.id.btn_left);
            btnLeft.setOnClickListener(leftOnClick);
            Button btnRight = (Button) findViewById(R.id.btn_right);
            btnRight.setOnClickListener(rightOnClick);
            atual = 0;
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
            ga = new GeradorAudio();
            rodando = false;
            mudarCor();
        }
    }

    private void setText(Button btn){
        if ((map[atual][0]<=95) && (map[atual][1]<=95) && (map[atual][2]<=95)){
            btn.setTextColor(Color.rgb(240,240,240));
        }
        else{
            btn.setTextColor(Color.rgb(10,10,10));
        }
    }

    private void mudarCor(){
        View mudar = findViewById(R.id.btn_reproduzir);
        mudar.setBackgroundColor(Color.rgb(map[atual][0],map[atual][1],map[atual][2]));
        Button btn = (Button) mudar;
        setText(btn);
        View esq = findViewById(R.id.btn_left);
        esq.setBackgroundColor(Color.rgb(map[atual][0],map[atual][1],map[atual][2]));
        btn = (Button) esq;
        setText(btn);
        View dir = findViewById(R.id.btn_right);
        dir.setBackgroundColor(Color.rgb(map[atual][0],map[atual][1],map[atual][2]));
        btn = (Button) dir;
        setText(btn);
    }

    protected View.OnClickListener reproduzirOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!t2s.isSpeaking())
                rodando = false;
            if (!rodando) {
                rodando = true;
                int[] rgb = {map[atual][0], map[atual][1], map[atual][2]};
                ga.rodar(rgb);
                gb.imprimirArq(context);
                t2s.speak(nomes[atual], TextToSpeech.QUEUE_FLUSH, null, "id");
            }
        }
    };

    private View.OnClickListener leftOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            atual--;
            if (atual < 0) atual = map.length-1;
            mudarCor();
        }
    };

    private View.OnClickListener rightOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            atual++;
            if(atual == map.length) atual = 0;
            mudarCor();
        }
    };

    private void criarPPedita(){
        final EditText txtUrl = new EditText(this);
        final Toast torrada = Toast.makeText(this, "Alteração bem sucessida",Toast.LENGTH_SHORT);
        new AlertDialog.Builder(this)
                .setTitle("Digite o novo nova para a cor:")
                .setView(txtUrl)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        torrada.show();
                        gb.editar(map[atual],txtUrl.getText().toString(),context);
                        nomes = gb.inicializarNomes(context);
                        gb.imprimirArq(context);
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

    private View.OnClickListener editarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            criarPPedita();
        }
    };

    private void criarPPremove(){
        final Toast torrada = Toast.makeText(this, "Cor removida", Toast.LENGTH_SHORT);
        final TextView txtUrl = new TextView(this);
        txtUrl.setText("Tem certeza que deseja remover a cor?");
        txtUrl.setPadding(0,10,0,0);
        txtUrl.setGravity(Gravity.CENTER);
        new AlertDialog.Builder(this)
                .setTitle(" ")
                .setView(txtUrl)
                .setPositiveButton("Sim",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        torrada.show();
                        gb.remover(map[atual], context);
                        map = gb.inicializar(context);
                        nomes = gb.inicializarNomes(context);
                        if (map.length==0){
                            Intent vazia = new Intent(Biblioteca.this,BibliotecaVazia.class);
                            startActivity(vazia);
                        }
                        else {
                            atual++;
                            if (atual < 0) {
                                atual = map.length - 1;
                            } else if (atual >= map.length) {
                                atual = 0;
                            }
                            mudarCor();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private View.OnClickListener removerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            criarPPremove();
        }
    };
}