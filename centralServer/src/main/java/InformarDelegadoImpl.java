package servidor;

import Elecciones.*;
import com.zeroc.Ice.Current;

public class InformarDelegadoImpl implements InformarDelegado {
    @Override
    public void notificar(String mensaje, Current current) {
        System.out.println("ALERTA AL DELEGADO: " + mensaje);
    }
}
