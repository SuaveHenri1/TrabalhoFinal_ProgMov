package com.acutis.projetofazenda.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.acutis.projetofazenda.R;

public class FazendaActivity extends AppCompatActivity {

    private ImageView loteTerra1, loteTerra2;
    private TextView txtBoasVindas, txtDinheiro, txtTimer1, txtTimer2;

    private SharedPreferences prefs;
    private int moedas;
    private int sementesCenoura;
    private int cenourasColhidas;

    private static final int ESTADO_VAZIO = 0;
    private static final int ESTADO_PLANTADO_SECO = 1;
    private static final int ESTADO_REGADO = 2;
    private static final int ESTADO_PRONTO = 3;

    private int estadoLote1 = ESTADO_VAZIO;
    private int estadoLote2 = ESTADO_VAZIO;

    private static final int COR_VAZIO = Color.parseColor("#8B4513");
    private static final int COR_SECO = Color.parseColor("#DEB887");
    private static final int COR_REGADO = Color.parseColor("#228B22");
    private static final int COR_PRONTO = Color.parseColor("#FFA500");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazenda);

        txtBoasVindas = findViewById(R.id.txtBoasVindas);
        txtDinheiro = findViewById(R.id.txtDinheiro);
        loteTerra1 = findViewById(R.id.loteTerra1);
        loteTerra2 = findViewById(R.id.loteTerra2);
        txtTimer1 = findViewById(R.id.txtTimer1);
        txtTimer2 = findViewById(R.id.txtTimer2);
        AppCompatButton btnArmazem = findViewById(R.id.btnArmazem);

        prefs = getSharedPreferences("FazendaPrefs", MODE_PRIVATE);

        String nomeJogador = getIntent().getStringExtra("NOME_JOGADOR");
        if (nomeJogador != null) {
            txtBoasVindas.setText("Fazenda de " + nomeJogador);
        }

        loteTerra1.setOnClickListener(v -> tratarCliqueLote(1, loteTerra1));
        loteTerra2.setOnClickListener(v -> tratarCliqueLote(2, loteTerra2));

        btnArmazem.setOnClickListener(v -> {
            Intent intent = new Intent(FazendaActivity.this, ArmazemActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        moedas = prefs.getInt("MOEDAS", 0);
        sementesCenoura = prefs.getInt("SEMENTES", 1);
        cenourasColhidas = prefs.getInt("CENOURAS", 0);
        atualizarHUD();
    }

    private void atualizarHUD() {
        txtDinheiro.setText("Moedas: " + moedas + " | Sementes: " + sementesCenoura);
    }

    private void guardarAlteracoes() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("SEMENTES", sementesCenoura);
        editor.putInt("CENOURAS", cenourasColhidas);
        editor.apply();
    }

    private void tratarCliqueLote(int numeroLote, ImageView visualLote) {
        int estadoAtual = (numeroLote == 1) ? estadoLote1 : estadoLote2;
        TextView txtTimer = (numeroLote == 1) ? txtTimer1 : txtTimer2;

        switch (estadoAtual) {
            case ESTADO_VAZIO:
                if (sementesCenoura > 0) {
                    sementesCenoura--;
                    guardarAlteracoes();
                    atualizarEstado(numeroLote, ESTADO_PLANTADO_SECO, visualLote);
                    txtTimer.setText("Sede!");
                    atualizarHUD();
                    Toast.makeText(this, "Semente plantada! Precisa regar.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Você não tem sementes!", Toast.LENGTH_SHORT).show();
                }
                break;

            case ESTADO_PLANTADO_SECO:
                atualizarEstado(numeroLote, ESTADO_REGADO, visualLote);
                Toast.makeText(this, "Regado! Crescendo...", Toast.LENGTH_SHORT).show();

                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        txtTimer.setText(millisUntilFinished / 1000 + "s");
                    }
                    public void onFinish() {
                        txtTimer.setText("Colher!");
                        atualizarEstado(numeroLote, ESTADO_PRONTO, visualLote);
                    }
                }.start();
                break;

            case ESTADO_REGADO:
                Toast.makeText(this, "Aguarde o crescimento...", Toast.LENGTH_SHORT).show();
                break;

            case ESTADO_PRONTO:
                cenourasColhidas++;
                guardarAlteracoes();
                atualizarEstado(numeroLote, ESTADO_VAZIO, visualLote);
                txtTimer.setText("Vazio");
                Toast.makeText(this, "Colhido com sucesso!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void atualizarEstado(int numeroLote, int novoEstado, ImageView visualLote) {
        if (numeroLote == 1) {
            estadoLote1 = novoEstado;
        } else {
            estadoLote2 = novoEstado;
        }

        switch (novoEstado) {
            case ESTADO_VAZIO: visualLote.setBackgroundColor(COR_VAZIO); break;
            case ESTADO_PLANTADO_SECO: visualLote.setBackgroundColor(COR_SECO); break;
            case ESTADO_REGADO: visualLote.setBackgroundColor(COR_REGADO); break;
            case ESTADO_PRONTO: visualLote.setBackgroundColor(COR_PRONTO); break;
        }
    }
}