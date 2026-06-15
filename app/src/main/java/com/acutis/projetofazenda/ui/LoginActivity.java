package com.acutis.projetofazenda.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.acutis.projetofazenda.R;
import com.acutis.projetofazenda.AppDatabase;
import com.acutis.projetofazenda.entity.Usuario;

public class LoginActivity extends AppCompatActivity {

    private ImageView imgFoto;
    private EditText editEmail, editSenha;
    private AppDatabase database;
    private SharedPreferences loginPrefs;
    private String caminhoFotoSalva = "";

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imgFoto.setImageBitmap(imageBitmap);
                    caminhoFotoSalva = "foto_provisoria_cache";

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    String fotoBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    loginPrefs.edit().putString("ULTIMA_FOTO", fotoBase64).apply();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = AppDatabase.getDatabase(getApplicationContext());
        loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        imgFoto = findViewById(R.id.imgFotoUsuario);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        AppCompatButton btnEntrar = findViewById(R.id.btnEntrar);
        AppCompatButton btnVoltar = findViewById(R.id.btnVoltar);

        String ultimoEmail = loginPrefs.getString("ULTIMO_EMAIL", "");
        String ultimaFoto = loginPrefs.getString("ULTIMA_FOTO", "");

        if (!ultimoEmail.isEmpty()) {
            editEmail.setText(ultimoEmail);
        }
        if (!ultimaFoto.isEmpty()) {
            byte[] imageAsBytes = Base64.decode(ultimaFoto.getBytes(), Base64.DEFAULT);
            imgFoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

        imgFoto.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                cameraLauncher.launch(takePictureIntent);
            }
        });

        btnVoltar.setOnClickListener(v -> finish());

        btnEntrar.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String senha = editSenha.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                Toast.makeText(this, "Erro: Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Erro: Insira um e-mail válido!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (senha.length() < 4) {
                Toast.makeText(this, "Erro: A senha precisa de pelo menos 4 dígitos!", Toast.LENGTH_SHORT).show();
                return;
            }

            String senhaCriptografada = gerarHashSHA256(senha);
            Usuario usuarioExistente = database.usuarioDao().buscarPorEmail(email);

            if (usuarioExistente == null) {
                Usuario novoUsuario = new Usuario();
                novoUsuario.nome = email.split("@")[0];
                novoUsuario.email = email;
                novoUsuario.senhaHash = senhaCriptografada;
                novoUsuario.caminhoFoto = caminhoFotoSalva;
                novoUsuario.tituloReino = "Fazendeiro de Campo Grande";

                database.usuarioDao().cadastrarUsuario(novoUsuario);
                loginPrefs.edit().putString("ULTIMO_EMAIL", email).apply();
                Toast.makeText(this, "Conta criada com sucesso na Guilda!", Toast.LENGTH_SHORT).show();
                irParaFazenda(novoUsuario.nome);
            } else {
                if (usuarioExistente.senhaHash.equals(senhaCriptografada)) {
                    loginPrefs.edit().putString("ULTIMO_EMAIL", email).apply();
                    Toast.makeText(this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show();
                    irParaFazenda(usuarioExistente.nome);
                } else {
                    Toast.makeText(this, "Erro: Senha incorreta!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void irParaFazenda(String nomeUsuario) {
        Intent intent = new Intent(LoginActivity.this, FazendaActivity.class);
        intent.putExtra("NOME_JOGADOR", nomeUsuario);
        startActivity(intent);
        finish();
    }

    private String gerarHashSHA256(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(senha.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return senha;
        }
    }
}