import com.zeroc.Ice.*;

import electoralSystem.VoteStationPrx;

public class VoteClient {

    public static void main(String[] args) {
        try {
            // Inicializar el comunicador de Ice
            Communicator communicator = Util.initialize(args);
            
            // Crear un proxy para el servicio VoteStation
            ObjectPrx base = communicator.stringToProxy("VoteStation:default -h localhost -p 9999");
            VoteStationPrx voteStation = VoteStationPrx.uncheckedCast(base);

            // Realizar una consulta
            String document = "123456789";
            int candidateId = 123; // Candidato a consultar
            boolean result = voteStation.vote(document, candidateId);

            // Mostrar el resultado
            System.out.println("Resultado de la consulta para el documento " + document + ": " + result);
            
            // Finalizar el comunicador
            communicator.destroy();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }

    }
}

