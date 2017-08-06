package com.example.cadu.synesthesie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class CorIdentificada extends AppCompatActivity {
    private int [] array;
    private Context context;
    private GeradorAudio ga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cor_identificada);
        Button btnVoltar = (Button) findViewById(R.id.btn_retornar);
        btnVoltar.setOnClickListener(voltarOnClick);
        Button btnSalvar = (Button) findViewById(R.id.btn_add);
        btnSalvar.setOnClickListener(salvarOnClick);
        View mudar = findViewById(R.id.btn_rs);
        Button btnReproduzir = (Button) mudar;
        btnReproduzir.setOnClickListener(reproduzirOnClick);
        context = getApplicationContext();
        array = getIntent().getIntArrayExtra("rgb");
        ga = new GeradorAudio();
        mudar.setBackgroundColor(Color.rgb(array[0],array[1],array[2]));
        if ((array[0]<=95) && (array[1]<=95) && (array[2]<=95)){
            btnReproduzir.setTextColor(Color.rgb(240,240,240));
        }
        else{
            btnReproduzir.setTextColor(Color.rgb(10,10,10));
        }
    }
    @Override
    protected void onStart(){
        super.onResume();
        ga.rodar(array);
    }

    @Override
    public void onBackPressed(){
        Intent id = new Intent(CorIdentificada.this,Identificar.class);
        finish();
        startActivity(id);
    }
    private void criarPP(){
        final EditText txtUrl = new EditText(this);
        final Toast torrada = Toast.makeText(this, "Cor adicionada com sucesso", Toast.LENGTH_SHORT);
        new AlertDialog.Builder(this)
                .setTitle("Digite o nome da cor:")
                .setView(txtUrl)
                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String txt = txtUrl.getText().toString();
                        if (txt.equals("")){
                            txt = "sem nome";
                        }
                        GerenciadorBiblioteca gb = new GerenciadorBiblioteca(context);
                        gb.adicionar(array,txt,context);
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


    private View.OnClickListener voltarOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            onBackPressed();

        }
    };

    private View.OnClickListener salvarOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            criarPP();
        }
    };

    private View.OnClickListener reproduzirOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            ga.rodar(array);
        }
    };
}