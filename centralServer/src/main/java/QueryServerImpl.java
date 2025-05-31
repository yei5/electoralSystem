import electoralSystem.QueryServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import com.zeroc.Ice.Current;
import utils.DBConnection;

public class QueryServerImpl implements QueryServer {
    @Override
    public String queryVotingTable(String votantId, Current current) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT lugar_votacion FROM votantes WHERE cedula = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, votantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return "Lugar de votación: " + rs.getString("lugar_votacion");
            } else {
                return "⚠️ No se encontró ningún votante con esa cédula.";
            }

        } catch (SQLException e) {
            return "❌ Error al consultar: " + e.getMessage();
        }
    }
}

