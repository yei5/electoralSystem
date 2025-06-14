import com.zeroc.Ice.*;
import com.zeroc.IceStorm.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class VoteProcessor {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {
    // 3. Conectar a la base de datos
    Connection dbConnection = DBConnection.getConnection();

    // 4. Crear instancia de la implementación
    VoteProcessorI voteProcessor = new VoteProcessorI(dbConnection);

    // 5. Crear adapter para exponer servicios y eventos
    ObjectAdapter adapter = communicator.createObjectAdapter("VoteProcessorAdapter");

    // 6. Registrar el objeto para RPC (VoteStation lo invoca así)
    adapter.add(voteProcessor, Util.stringToIdentity("VoteProcessor"));

    adapter.activate();
    System.out.println("Vote Processor is running...");

        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}
