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
    public void execute() throws Exception {
        replicate(entrada.recibirMensaje());
    }

    public void replicate(Message mensaje) throws Exception {
        for (Slot salida : salidas) {
            Message nuevoMensaje = mensaje.clonar();
            salida.enviarMensaje(nuevoMensaje);
        }
    }
}