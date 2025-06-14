import electoralSystem.VoteEvent;
import electoralSystem.VoteProcessor;
import com.zeroc.Ice.Current;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class VoteProcessorI implements VoteProcessor {
    private final Connection dbConnection;

    public VoteProcessorI(Connection dbConnection) {
        this.dbConnection = dbConnection;
        ensureTableExists(); // Verifica o crea la tabla si no existe
    }

    private void ensureTableExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS votos_registrados (" +
                "id SERIAL PRIMARY KEY," +
                "documento_ciudadano VARCHAR(20) NOT NULL," +
                "candidato_id INTEGER NOT NULL," +
                "timestamp TIMESTAMP NOT NULL" +
            ");";

        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Tabla 'votos_registrados' verificada/creada.");
        } catch (Exception e) {
            System.err.println("Error al verificar/crear la tabla votos_registrados: " + e.getMessage());
        }
    }

    @Override
    public boolean newVote(VoteEvent event, Current current) {
        System.out.println("VoteProcessor: Procesando voto de " + event.document);

        try {
            if (!canVote(event.document)) {
                System.err.println("RECHAZADO: " + event.document + " ya votó.");
                return false;
            }

            String insertSql = "INSERT INTO votos_registrados (documento_ciudadano, candidato_id, timestamp) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = dbConnection.prepareStatement(insertSql)) {
                stmt.setString(1, event.document);
                stmt.setInt(2, event.candidateId);
                stmt.setTimestamp(3, new java.sql.Timestamp(event.timestamp));
                stmt.executeUpdate();
            }

            String updateSql = "UPDATE mesa_votacion SET total_votos = total_votos + 1 WHERE id = (SELECT mesa_id FROM ciudadano WHERE documento = ?)";
            try (PreparedStatement stmt = dbConnection.prepareStatement(updateSql)) {
                stmt.setString(1, event.document);
                stmt.executeUpdate();
            }

            String citizenUpdateSql = "UPDATE ciudadano SET ha_votado = TRUE WHERE documento = ?";
            try (PreparedStatement stmt = dbConnection.prepareStatement(citizenUpdateSql)) {
                stmt.setString(1, event.document);
                stmt.executeUpdate();
            }

            System.out.println("Voto procesado correctamente para: " + event.document);
            return true;
        } catch (Exception e) {
            System.err.println("Error al procesar voto: " + e.getMessage());
            return false;
        }
    }

    private boolean canVote(String document) throws java.sql.SQLException {
        String checkSql = "SELECT ha_votado FROM ciudadano WHERE documento = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(checkSql)) {
            stmt.setString(1, document);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Verificando si el ciudadano ha votado: " + !rs.getBoolean("ha_votado"));
                return !rs.getBoolean("ha_votado");
            }
        }
        return false;
    }
}

