package com.example.cadu.synesthesie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Identificar extends AppCompatActivity  {
    private Context context;
    private int [] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificar);
        Button btnVoltar = (Button) findViewById(R.id.btn_cancelar);
        btnVoltar.setOnClickListener(voltarOnClick);
        Button btnMusica = (Button) findViewById(R.id.btn_identificar);
        btnMusica.setOnClickListener(idOnClick);
        context = getApplicationContext();
    }

    private void criarPP(){
        final EditText txtUrl = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Digite o RGB:")
                .setView(txtUrl)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        array = strToRGB(txtUrl.getText().toString());
                        Intent id = new Intent(Identificar.this,CorIdentificada.class);
                        id.putExtra("rgb",array);
                        startActivity(id);
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

    private View.OnClickListener idOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            criarPP();
        }
    };

    @Override
    public void onBackPressed(){
        Intent config = new Intent(Identificar.this,MainActivity.class);
        finish();
        startActivity(config);
    }

    private View.OnClickListener voltarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private int[] strToRGB(String s){
        String [] str = s.split(" ");
        int [] novo = {6,6,6};
        for (int x=0;x<novo.length;x++){
            novo[x]=Integer.parseInt(str[x]);
        }
        return novo;
    }

}