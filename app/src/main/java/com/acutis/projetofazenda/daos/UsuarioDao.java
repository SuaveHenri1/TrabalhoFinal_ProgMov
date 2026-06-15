package com.acutis.projetofazenda.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.acutis.projetofazenda.entity.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    void cadastrarUsuario(Usuario usuario);

    @Query("SELECT * FROM tabela_usuarios WHERE email = :email AND senhaHash = :senhaHash LIMIT 1")
    Usuario fazerLogin(String email, String senhaHash);

    @Query("SELECT * FROM tabela_usuarios WHERE email = :email LIMIT 1")
    Usuario buscarPorEmail(String email);
}