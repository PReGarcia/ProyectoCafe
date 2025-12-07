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
        while (!entrada.esVacia()) {
            replicate(entrada.recibirMensaje());
        }
    }

    public void replicate(Message mensaje) {
        for (Slot salida : salidas) {
            Message nuevoMensaje = mensaje.clonar();
            salida.enviarMensaje(nuevoMensaje);
        }
    }
}