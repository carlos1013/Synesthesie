package com.example.cadu.synesthesie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnId = (Button) findViewById(R.id.btn_id_cor);
        btnId.setOnClickListener(idOnClick);
        Button btnLib = (Button) findViewById(R.id.btn_lib_cor);
        btnLib.setOnClickListener(libOnClick);
        Button btnMusica = (Button) findViewById(R.id.btn_musica);
        btnMusica.setOnClickListener(musicaOnClick);
        Button btnConfig = (Button) findViewById(R.id.btn_config);
        btnConfig.setOnClickListener(configOnClick);
    }


    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
        finish();
    }

    private OnClickListener idOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent config = new Intent(MainActivity.this,Identificar.class);
            startActivity(config);
        }
    };

    private OnClickListener libOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent lib = new Intent(MainActivity.this,Biblioteca.class);
            startActivity(lib);
        }
    };

    private  OnClickListener musicaOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent musica = new Intent(MainActivity.this,Musica.class);
            startActivity(musica);
        }
    };

    private OnClickListener configOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent config = new Intent(MainActivity.this,Configuracao.class);
            startActivity(config);
        }
    };
}
