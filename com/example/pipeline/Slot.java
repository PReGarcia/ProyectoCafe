package pipeline;

import java.util.LinkedList;
import java.util.List;
import utils.Message;

public class Slot {

    private List<Message> mensajes = new LinkedList<>();

    public void recibirMensaje(Message mensaje) {
        if (mensaje != null) {
            mensajes.add(mensaje);
        }
    }

    public Message leerMensaje() {
        if (mensajes.isEmpty()) {
            return null;
        }
        return mensajes.remove(0);
    }
}