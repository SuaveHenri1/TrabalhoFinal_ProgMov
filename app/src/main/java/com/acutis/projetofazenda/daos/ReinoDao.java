package com.acutis.projetofazenda.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.acutis.projetofazenda.entity.Reino;

@Dao
public interface ReinoDao {

    @Insert
    void inserirReino(Reino reino);

    @Query("SELECT * FROM tabela_reinos WHERE estadoReal = :estado LIMIT 1")
    Reino buscarReinoPorEstado(String estado);
}