import com.zeroc.Ice.*;

import electoralSystem.QueryStationPrx;

public class QueryClient {

    public static void main(String[] args) {
        try {
            // Inicializar el comunicador de Ice
            Communicator communicator = Util.initialize(args);
            
            // Crear un proxy para el servicio QueryStation
            ObjectPrx base = communicator.stringToProxy("QueryStation");
            QueryStationPrx queryStation = QueryStationPrx.checkedCast(base);
            
            // Realizar una consulta
            String document = "123456789"; // Documento a consultar
            String result = queryStation.query(document);
            
            // Mostrar el resultado
            System.out.println("Resultado de la consulta para el documento " + document + ": " + result);
            
            // Finalizar el comunicador
            communicator.destroy();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }

    }
}
