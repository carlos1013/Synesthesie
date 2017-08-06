package com.example.cadu.synesthesie;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.ListIterator;

public class Musica extends Biblioteca {
    private LinkedList<int[]> musica = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);
        Button btnCmRep = (Button) findViewById(R.id.btn_cmReproduzir);
        btnCmRep.setOnClickListener(repOnClick);
        Button btnRsa = (Button) findViewById(R.id.btn_rsa);
        btnRsa.setOnClickListener(repSeq);
        Button btnAas = (Button) findViewById(R.id.btn_aas);
        btnAas.setOnClickListener(adcSeq);
        Button right = (Button) findViewById(R.id.btn_rightM);
        right.setOnClickListener(rightOnClick);
        Button left = (Button) findViewById(R.id.btn_leftM);
        left.setOnClickListener(leftOnClick);
        mudarCor();
    }

    private void setText(Button btn){
        if ((map[atual][0]<=95) && (map[atual][1]<=95) && (map[atual][2]<=95)){
            btn.setTextColor(Color.rgb(240,240,240));
        }
        else{
            btn.setTextColor(Color.rgb(10,10,10));
        }
    }

    protected View.OnClickListener repOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!t2s.isSpeaking())
                rodando = false;
            if (!rodando) {
                rodando = true;
                int[] rgb = {map[atual][0], map[atual][1], map[atual][2]};
                ga.rodar(rgb);
                t2s.speak(nomes[atual], TextToSpeech.QUEUE_FLUSH, null, "id");
            }
        }
    };


    private void mudarCor(){
        View mudar = findViewById(R.id.btn_cmReproduzir);
        mudar.setBackgroundColor(Color.rgb(map[atual][0],map[atual][1],map[atual][2]));
        Button btn = (Button) mudar;
        setText(btn);
        View esq = findViewById(R.id.btn_leftM);
        esq.setBackgroundColor(Color.rgb(map[atual][0],map[atual][1],map[atual][2]));
        btn = (Button) esq;
        setText(btn);
        View dir = findViewById(R.id.btn_rightM);
        dir.setBackgroundColor(Color.rgb(map[atual][0],map[atual][1],map[atual][2]));
        btn = (Button) dir;
        setText(btn);
    }

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


    private View.OnClickListener adcSeq = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast torrada = Toast.makeText(context,"Cor adicionada à sequencia", Toast.LENGTH_SHORT);
            torrada.show();
            musica.add(map[atual]);
        }
    };

    private View.OnClickListener repSeq = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ListIterator<int[]> iterador = musica.listIterator();
            if(iterador.hasNext()){
                while(iterador.hasNext())
                    ga.rodar(iterador.next());
            }
            else{
                Toast torrada = Toast.makeText(context,"Não é possivel reproduzir\nNenhuma cor adicionada à sequência", Toast.LENGTH_SHORT);
                torrada.show();
            }
        }
    };
}