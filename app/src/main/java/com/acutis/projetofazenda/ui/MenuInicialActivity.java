package com.acutis.projetofazenda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.acutis.projetofazenda.R;

public class MenuInicialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        Button btnJogar = findViewById(R.id.btnJogar);
        Button btnOpcoes = findViewById(R.id.btnOpcoes);
        Button btnFechar = findViewById(R.id.btnFechar);

        btnJogar.setOnClickListener(v -> {
            Intent intent = new Intent(MenuInicialActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnOpcoes.setOnClickListener(v -> {
        });

        btnFechar.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}