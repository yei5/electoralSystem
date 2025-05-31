module Elecciones {

    interface ServerController {
        void registrarVoto(string idVotante, int idCandidato);
        void reportarAnomalia(string mensaje);
    };

    interface InformarDelegado {
        void notificar(string mensaje);
    };
};
