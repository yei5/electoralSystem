import electoralSystem.VoteEvent;
import electoralSystem.VoteStation;
import electoralSystem.VoteProcessorPrx;
import com.zeroc.Ice.Current;
import com.zeroc.IceStorm.TopicManagerPrx;
import com.zeroc.Ice.ObjectPrx;


public class VoteStationI implements VoteStation {
    private final VoteProcessorPrx voteProcessor; // Proxy al procesador

    public VoteStationI(VoteProcessorPrx voteProcessor) throws com.zeroc.IceStorm.TopicExists {
        this.voteProcessor = voteProcessor;
    }

    @Override
    public boolean vote(String document, int candidateId, Current current) {
        System.out.println("Recibido intento de voto para: " + document);

        VoteEvent event = new VoteEvent(document, candidateId, System.currentTimeMillis());

        // 1. Lógica principal: llamar al VoteProcessor directamente
        boolean procesado = false;
        try {
            procesado = voteProcessor.newVote(event);
        } catch (Exception e) {
            System.err.println("Error al contactar con VoteProcessor: " + e.getMessage());
            System.out.println("Processor: " + voteProcessor);
            e.printStackTrace();
            return false;
        }

        if (!procesado) {
            System.out.println("VoteProcessor rechazó el voto.");
            return false;
        }

        return true;
    }
}
