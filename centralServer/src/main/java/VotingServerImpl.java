import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zeroc.Ice.Current;

import electoralSystem.VotingServer;
import utils.DBConnection;

public class VotingServerImpl implements VotingServer {

    @Override
    public boolean vote(String votantId, String candidateId, Current current) {
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Para rollback en caso de error

            // Verificar si ya votó
            String check = "SELECT ha_votado FROM votantes WHERE cedula = ?";
            PreparedStatement stmtCheck = conn.prepareStatement(check);
            stmtCheck.setString(1, votantId);
            ResultSet rs = stmtCheck.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false; // Cédula no registrada
            }

            if (rs.getBoolean("ha_votado")) {
                conn.rollback();
                return false; // Ya votó
            }

            // Insertar el voto
            String insert = "INSERT INTO votos (cedula_votante, candidato_id) VALUES (?, ?)";
            PreparedStatement stmtInsert = conn.prepareStatement(insert);
            stmtInsert.setString(1, votantId);
            stmtInsert.setString(2, candidateId);
            stmtInsert.executeUpdate();

            // Marcar como votado
            String update = "UPDATE votantes SET ha_votado = TRUE WHERE cedula = ?";
            PreparedStatement stmtUpdate = conn.prepareStatement(update);
            stmtUpdate.setString(1, votantId);
            stmtUpdate.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error en votación: " + e.getMessage());
            return false;
        }
    
    }
    
}
