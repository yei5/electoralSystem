import electoralSystem.QueryServer;
import com.zeroc.Ice.Current;
import utils.MockDatabase;

public class QueryServerImpl implements QueryServer {
    @Override
    public String queryVotingTable(String votantId, Current current) {
        System.out.println("Consulta recibida para ID: " + votantId);
        return MockDatabase.getVotingTable(votantId);
    }
}

