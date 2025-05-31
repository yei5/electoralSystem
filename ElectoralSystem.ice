module electoralSystem{

    interface QueryServer{
        string queryVotingTable(string votantId);
    }

    interface VotingServer{
        bool vote (string votantId, string candidateId);
    }    
    
};
