package tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import utils.Message;
import utils.Singleton;
import utils.XmlUtils;

public class Splitter implements Task {

    private String expresionXpath;

    public Splitter(String expresionXpath) {
        this.expresionXpath = expresionXpath;
    }

    @Override
    public List<Message> execute(Message inputMessage) throws Exception {
        System.out.println("Ejecutando Splitter...");
        
        List<Message> mensajesDivididos = new ArrayList<>();
        Document comanda = (Document) inputMessage.getPayload();

        List<Node> items = XmlUtils.NodeGroupSearch(comanda, expresionXpath);

        Singleton singleton = new Singleton(comanda, expresionXpath);
        singleton.quitarNodos();
        
        String idGrupo = UUID.randomUUID().toString();
        int total = items.size();
        int contador = 0;

        for (Object item : items) {
            contador++;
            Message nuevoMensaje;

            if (item instanceof Message) {
                nuevoMensaje = ((Message) item).cloneMessage();
            } else {
                nuevoMensaje = new Message(item);
            }

            nuevoMensaje.setHeader("comandaId", idGrupo);
            nuevoMensaje.setHeader("splitSize", total);
            nuevoMensaje.setHeader("sequenceId", contador);
            
            if (nuevoMensaje.getHeader("correlationId") == null) {
                nuevoMensaje.setHeader("correlationId", UUID.randomUUID().toString());
            }

            mensajesDivididos.add(nuevoMensaje);
        }

        return mensajesDivididos;
    }
}