package tareas;

import pipeline.Slot;
import utils.Message;

public class Merger implements Task {

    private Slot[] entradas;
    private Slot salida;

    public Merger(Slot[] entradas, Slot salida) {
        this.entradas = entradas;
        this.salida = salida;
    }

    @Override
    public void execute() throws Exception {
        for (Slot slot : entradas) {
            if (!slot.esVacia()) {
                merge(slot.recibirMensaje());
            }
        }
    }

    public void merge(Message mensaje) throws Exception {
        salida.enviarMensaje(mensaje);
    }
}