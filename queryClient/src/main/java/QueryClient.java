import com.zeroc.Ice.*;
import electoralSystem.QueryServerPrx;

import java.util.Scanner;

public class QueryClient {
    public static void main(String[] args) {
        Communicator communicator = null;

        try {
            communicator = Util.initialize(args);

            ObjectPrx base = communicator.stringToProxy("Query:default -p 10000");
            QueryServerPrx proxy = QueryServerPrx.checkedCast(base);

            if (proxy == null) {
                throw new Error("❌ No se pudo conectar con el servidor de consulta.");
            }

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("🔍 Ingrese su cédula para consultar su mesa de votación: ");
                String cedula = scanner.nextLine();

                String resultado = proxy.queryVotingTable(cedula);
                System.out.println("🗳️ Resultado: " + resultado);
            }

        } catch (java.lang.Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        } finally {
            if (communicator != null) {
                communicator.destroy();
            }
        }
    }
}


