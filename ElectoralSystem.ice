
module electoralSystem {

    ["java:getset"]
    struct VoteEvent {
        string document;
        int candidateId;
        long timestamp;
    };

    interface QueryStation {
        idempotent string query(string document);
    };


    interface VoteStation {
        bool vote(string document, int candidateId);
    };

    
    interface VoteProcessor {
        
        bool newVote(VoteEvent event);
    };
};