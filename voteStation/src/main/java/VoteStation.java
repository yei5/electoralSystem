
import com.zeroc.Ice.*;
import com.zeroc.IceStorm.*;
import electoralSystem.VoteProcessorPrx;
import redis.clients.jedis.Jedis;

public class VoteStation {

    public static void main(String[] args) {
        try {
            // Inicializar el comunicador de Ice
            com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args);

            ObjectPrx base = communicator.stringToProxy("VoteProcessor");
            VoteProcessorPrx voteProcessor = VoteProcessorPrx.uncheckedCast(base);
            
            // Crear el objeto VoteStation
            VoteStationI voteStation = new VoteStationI(voteProcessor);
            // Registrar el objeto en Ice
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("VoteStationAdapter");
            adapter.add(voteStation, communicator.stringToIdentity("VoteStation"));
            adapter.activate();
            
            System.out.println("Vote Station is running...");
            
            // Ejecutar el bucle de eventos de Ice
            communicator.waitForShutdown();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}