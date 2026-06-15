package com.acutis.projetofazenda;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.acutis.projetofazenda.daos.ReinoDao;
import com.acutis.projetofazenda.daos.UsuarioDao;
import com.acutis.projetofazenda.entity.Reino;
import com.acutis.projetofazenda.entity.Usuario;

@Database(entities = {Usuario.class, Reino.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();

    public abstract ReinoDao reinoDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "fazenda_medieval_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}