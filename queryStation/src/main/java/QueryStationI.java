import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zeroc.Ice.*;
import electoralSystem.QueryStation;
import utils.DBConnection;
import redis.clients.jedis.Jedis;


public class QueryStationI implements QueryStation {
    private final Jedis cache; // Conexión a la caché (Redis)
    private final Connection dbConnection; // Conexión a la BD (PostgreSQL)

    public QueryStationI(Jedis cache, Connection dbConnection) {
        this.cache = cache;
        this.dbConnection = dbConnection;
    }

    @Override
    public String query(String document, Current current) {
        System.out.println("Recibida consulta para el documento: " + document);
        String cacheKey = "query:" + document;

        // 1. Patrón Cache-Aside: Intentar obtener de la caché primero
        try {
            String cachedResult = cache.get(cacheKey);
            if (cachedResult != null) {
                System.out.println("Cache HIT para " + document);
                return cachedResult;
            }
        } catch (java.lang.Exception e) {
            System.err.println("Error al acceder a la caché: " + e.getMessage());
            // Continuar a la BD si la caché falla
        }

        // 2. Cache Miss: Consultar la base de datos
        System.out.println("Cache MISS para " + document + ". Consultando la base de datos.");
        String dbResult = queryDatabase(document);

        // 3. Almacenar el resultado en la caché para futuras consultas
        if (dbResult != null && !dbResult.isEmpty()) {
            try {
                 // Cache por 1 hora (3600 segundos)
                cache.setex(cacheKey, 3600, dbResult);
            } catch (java.lang.Exception e) {
                System.err.println("Error al escribir en la caché: " + e.getMessage());
            }
        }
        return dbResult;
    }

    private String queryDatabase(String document) {
        // Lógica para consultar la información del puesto de votación en PostgreSQL
        String sql = "SELECT p.nombre, m.consecutive, d.nombre as depto, mu.nombre as mun " +
                     "FROM ciudadano c " +
                     "JOIN mesa_votacion m ON c.mesa_id = m.id " +
                     "JOIN puesto_votacion p ON m.puesto_id = p.id " +
                     "JOIN municipio mu ON p.municipio_id = mu.id " +
                     "JOIN departamento d ON mu.departamento_id = d.id " +
                     "WHERE c.documento = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, document);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return String.format("Puesto: %s, Mesa: %d, Departamento: %s, Municipio: %s",
                    rs.getString("nombre"),
                    rs.getInt("consecutive"),
                    rs.getString("depto"),
                    rs.getString("mun")
                );
            }
        } catch (java.lang.Exception e) {
            System.err.println("Error en la consulta a la BD: " + e.getMessage());
        }
        return "Ciudadano no encontrado.";
    }
}