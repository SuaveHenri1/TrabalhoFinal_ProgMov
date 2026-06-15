package com.acutis.projetofazenda.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabela_reinos")
public class Reino {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String estadoReal;

    public String nomeFantasia;

    public String climaComum;

    public Reino() {}
}