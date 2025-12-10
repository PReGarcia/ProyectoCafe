package tareas;

import pipeline.Slot;
import utils.Message;

public class Replicator implements Task {
    private Slot[] salidas;
    private Slot entrada;

    public Replicator(Slot entrada, Slot... salidas) {
        this.entrada = entrada;
        this.salidas = salidas;
    }

    @Override
    public void execute() {
        int contador = 0;
        while (!entrada.esVacia()) {
            replicate(entrada.recibirMensaje());
            contador++;
        }
        System.out.println("Replicator: Tarea finalizada. " + contador + " mensajes replicados.");
    }

    public void replicate(Message mensaje) {
        for (Slot salida : salidas) {
            Message nuevoMensaje = mensaje.clonar();
            salida.enviarMensaje(nuevoMensaje);
        }
    }
}