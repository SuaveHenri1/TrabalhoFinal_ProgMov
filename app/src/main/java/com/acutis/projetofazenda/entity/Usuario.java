package com.acutis.projetofazenda.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "tabela_usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;

    public String email;

    public String senhaHash;

    public String caminhoFoto;

    public String tituloReino;

    public Usuario() {}
}
