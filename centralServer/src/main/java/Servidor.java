package servidor;

import Elecciones.*;
import com.zeroc.Ice.*;

public class Servidor {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("EleccionesAdapter", "default -p 10000");

            ConexionDB conexionDB = new ConexionDB();
            InformarDelegadoImpl delegadoImpl = new InformarDelegadoImpl();

            ServerControllerImpl controllerImpl = new ServerControllerImpl(delegadoImpl, conexionDB);

            adapter.add(controllerImpl, Util.stringToIdentity("serverController"));
            adapter.add(delegadoImpl, Util.stringToIdentity("informarDelegado"));
            adapter.activate();

            System.out.println("Servidor ICE listo...");
            communicator.waitForShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
