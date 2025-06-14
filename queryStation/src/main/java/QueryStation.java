import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueryStation {

    public static void main(String[] args) {
        
        Connection dbConnection = null;
        try {
            // Configuración de Ice
            com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args);
            

            // Crear conexión a la base de datos (PostgreSQL)
            dbConnection = DBConnection.getConnection();

            // Crear el objeto QueryStation
            QueryStationI queryStation = new QueryStationI(dbConnection);

            // Registrar el objeto en Ice
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("QueryStationAdapter");
            adapter.add(queryStation, communicator.stringToIdentity("QueryStation"));
            adapter.activate();

            System.out.println("Query Station is running...");

            // Ejecutar el bucle de eventos de Ice
            communicator.waitForShutdown();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(dbConnection);
        }
    }

}
