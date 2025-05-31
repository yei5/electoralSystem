import com.zeroc.Ice.Current;

import electoralSystem.VotingServer;
import utils.MockDatabase;

public class VotingServerImpl implements VotingServer {

    @Override
    public boolean vote(String votantId, String candidateId, Current current) {
        
        System.out.println("Intentando votar: " + votantId + " por " + candidateId);
        return MockDatabase.registerVote(votantId);
    
    }
    
}
