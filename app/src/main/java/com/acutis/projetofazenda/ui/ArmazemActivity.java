package com.acutis.projetofazenda.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.acutis.projetofazenda.R;

public class ArmazemActivity extends AppCompatActivity {

    private TextView txtSaldoArmazem, txtSementesEstoque, txtCenourasEstoque;
    private SharedPreferences prefs;
    private int moedas, sementes, cenouras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armazem);

        txtSaldoArmazem = findViewById(R.id.txtSaldoArmazem);
        txtSementesEstoque = findViewById(R.id.txtSementesEstoque);
        txtCenourasEstoque = findViewById(R.id.txtCenourasEstoque);
        AppCompatButton btnComprarSemente = findViewById(R.id.btnComprarSemente);
        AppCompatButton btnVenderCenoura = findViewById(R.id.btnVenderCenoura);
        AppCompatButton btnVoltarFazenda = findViewById(R.id.btnVoltarFazenda);

        prefs = getSharedPreferences("FazendaPrefs", MODE_PRIVATE);
        carregarInventario();

        btnComprarSemente.setOnClickListener(v -> {
            if (moedas >= 5) {
                moedas -= 5;
                sementes++;
                salvarInventario();
                Toast.makeText(this, "Semente comprada!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Moedas insuficientes!", Toast.LENGTH_SHORT).show();
            }
        });

        btnVenderCenoura.setOnClickListener(v -> {
            if (cenouras > 0) {
                cenouras--;
                moedas += 10;
                salvarInventario();
                Toast.makeText(this, "Cenoura vendida!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Estoque vazio!", Toast.LENGTH_SHORT).show();
            }
        });

        btnVoltarFazenda.setOnClickListener(v -> finish());
    }

    private void carregarInventario() {
        moedas = prefs.getInt("MOEDAS", 0);
        sementes = prefs.getInt("SEMENTES", 1);
        cenouras = prefs.getInt("CENOURAS", 0);
        atualizarInterface();
    }

    private void salvarInventario() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("MOEDAS", moedas);
        editor.putInt("SEMENTES", sementes);
        editor.putInt("CENOURAS", cenouras);
        editor.apply();
        atualizarInterface();
    }

    private void atualizarInterface() {
        txtSaldoArmazem.setText("Moedas: " + moedas);
        txtSementesEstoque.setText("Sementes de Cenoura: " + sementes);
        txtCenourasEstoque.setText("Cenouras Colhidas: " + cenouras);
    }
}