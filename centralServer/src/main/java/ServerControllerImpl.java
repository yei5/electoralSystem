
import Elecciones.InformarDelegado;
import Elecciones.ServerController;
// import Elecciones.*; // Removed because the package cannot be resolved
import com.zeroc.Ice.Current;

public class ServerControllerImpl implements ServerController {
    private InformarDelegado delegado;
    private ConexionDB conexionDB;

    public ServerControllerImpl(InformarDelegado delegado, ConexionDB conexionDB) {
        this.delegado = delegado;
        this.conexionDB = conexionDB;
    }

    @Override
    public void registrarVoto(String idVotante, int idCandidato, Current current) {
        try {
            System.out.println("Registrando voto de: " + idVotante);
            conexionDB.insertarVoto(idVotante, idCandidato);
        } catch (Exception e) {
            System.out.println("Fallo al registrar voto, guardando localmente.");
            conexionDB.guardarTemporalmente(idVotante, idCandidato);
        }
    }

    @Override
    public void reportarAnomalia(String mensaje, Current current) {
        try {
            delegado.notificar(mensaje);
        } catch (Exception e) {
            System.out.println("No se pudo contactar al delegado. Se guarda la anomalía localmente.");
            conexionDB.guardarAnomalia(mensaje);
        }
    }
}
