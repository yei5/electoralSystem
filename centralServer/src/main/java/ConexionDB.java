package servidor;

import java.sql.*;
import java.util.*;

public class ConexionDB {
    private Connection conn;

    public ConexionDB() throws SQLException {
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/elecciones", "usuario", "contraseña");
    }

    public void insertarVoto(String idVotante, int idCandidato) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO votos (votante, candidato) VALUES (?, ?)");
        stmt.setString(1, idVotante);
        stmt.setInt(2, idCandidato);
        stmt.executeUpdate();
    }

    public void guardarTemporalmente(String idVotante, int idCandidato) {
        // Aquí guardar en archivo local o tabla auxiliar para reintentos
        System.out.println("Guardado en buffer local: " + idVotante);
    }

    public void guardarAnomalia(String mensaje) {
        System.out.println("Anomalía guardada localmente: " + mensaje);
    }

    public void reenviarPendientes() {
        // Cargar de almacenamiento local y reenviar a la DB
        System.out.println("Reenviando pendientes...");
    }
}
