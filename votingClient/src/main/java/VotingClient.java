import com.zeroc.Ice.*;
import electoralSystem.VotingServerPrx;

import java.util.Scanner;

public class VotingClient {
    public static void main(String[] args) {
        Communicator communicator = null;

        try {
            communicator = Util.initialize(args);

            ObjectPrx base = communicator.stringToProxy("Vote:default -p 10000");
            VotingServerPrx proxy = VotingServerPrx.checkedCast(base);

            if (proxy == null) {
                throw new Error("❌ No se pudo conectar con el servidor de votación.");
            }

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("🆔 Ingrese su cédula: ");
                String cedula = scanner.nextLine();

                System.out.print("🗳️ Ingrese el ID del candidato por el que desea votar: ");
                String candidato = scanner.nextLine();

                boolean exito = proxy.vote(cedula, candidato);
                if (exito) {
                    System.out.println("✅ Voto registrado exitosamente.");
                } else {
                    System.out.println("❌ No se pudo registrar el voto. Es posible que ya haya votado.");
                }
            }

        } catch (java.lang.Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        } finally {
            if (communicator != null) {
                communicator.destroy();
            }
        }
    }
}
