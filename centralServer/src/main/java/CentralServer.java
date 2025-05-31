import com.zeroc.Ice.*;

public class CentralServer {
    public static void main(String[] args) {
        Communicator communicator = null;

        try {
            // Inicializa el comunicador ICE
            communicator = Util.initialize(args);

            // Crea el adaptador con el endpoint
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("CentralAdapter", "default -p 10000");

            // Instancia los objetos servant
            QueryServerImpl queryServer = new QueryServerImpl();
            VotingServerImpl votingServer = new VotingServerImpl();

            // Registra los objetos servant con sus identidades
            adapter.add(queryServer, Util.stringToIdentity("Query"));
            adapter.add(votingServer, Util.stringToIdentity("Vote"));

            // Activa el adaptador para que empiece a atender peticiones
            adapter.activate();

            System.out.println("✅ CentralServer ICE corriendo en puerto 10000.");
            System.out.println("🔗 Servants registrados: [Query, Vote]");

            // Espera hasta que el servidor reciba una señal de apagado
            communicator.waitForShutdown();
        } catch (java.lang.Exception e) {
            System.err.println("❌ Error al iniciar CentralServer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (communicator != null) {
                // Libera recursos del comunicador
                try {
                    communicator.destroy();
                } catch (java.lang.Exception e) {
                    System.err.println("⚠️ Error al destruir el comunicador: " + e.getMessage());
                }
            }
        }
    }
}

