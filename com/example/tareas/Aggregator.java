package tareas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import pipeline.Slot;
import utils.Arbol;
import utils.Message;
import utils.XmlUtils;

public class Aggregator implements Task {

    private final Map<String, List<Message>> comandasPendientes = new ConcurrentHashMap<>();
    private Slot entrada;
    private Slot salida;
    private String xpath;
    Arbol arbol = Arbol.getInstancia();

    public Aggregator(String xpath, Slot entrada, Slot salida) {
        this.entrada = entrada;
        this.salida = salida;
    }

    @Override
    public void execute() throws Exception {
        Message mensaje = entrada.recibirMensaje();
        String idComanda = mensaje.getComandaId();

        List<Message> lista = comandasPendientes.computeIfAbsent(idComanda, k -> new ArrayList<>());
        lista.add(mensaje);

        if (mensaje.getTamSecuencia() == lista.size()) {
            lista.sort(Comparator.comparingInt(Message::getOrdenSecuencia));
            aggregate(mensaje.getComandaId(), lista);
            comandasPendientes.remove(idComanda);
        }
    }

    private void aggregate(String idComanda, List<Message> messages) throws Exception {
        Document comanda = arbol.getArbol(idComanda);

        Document doc = XmlUtils.createNewDocument();
        Node root = doc.importNode(comanda, true);
        doc.appendChild(root);

        Node nodoDestino = XmlUtils.NodeSearch(comanda, xpath);
        Document documentoNodos;

        Message mensajeSalida = null;
        for (Message msg : messages) {
            if (mensajeSalida == null) {
                mensajeSalida = msg.clonar();
            }
            documentoNodos = XmlUtils.createNewDocument();
            Node bebidaNode = documentoNodos.importNode(msg.getCuerpo(), true);
            nodoDestino.appendChild(bebidaNode);
        }

        mensajeSalida.setCuerpo(doc);
        salida.enviarMensaje(mensajeSalida);
    }
}