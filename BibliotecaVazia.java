package com.example.cadu.synesthesie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BibliotecaVazia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_biblioteca_vazia);
        Button btnVoltar = (Button) findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(voltarOnClick);
    }


    @Override
    public void onBackPressed(){
        Intent main = new Intent(BibliotecaVazia.this,MainActivity.class);
        finish();
        startActivity(main);
    }
    private View.OnClickListener voltarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
}
