package pipeline;

import java.util.LinkedList;
import java.util.List;

import tareas.Task;
import utils.Message;

public class Slot {

    private List<Message> mensajes = new LinkedList<>();
    private Task tareaAsignada;
    
    public Slot(Task tareaAsignada) {
        this.tareaAsignada = tareaAsignada;
    }

    public void enviarMensaje(Message mensaje) throws Exception {
        if (mensaje != null) {
            mensajes.add(mensaje);
        }
        tareaAsignada.execute();
    }

    public Message recibirMensaje() {
        if (mensajes.isEmpty()) {
            return null;
        }
        return mensajes.remove(0);
    }

    public boolean esVacia() {
        return mensajes.isEmpty();
    }
}