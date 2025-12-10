package tareas;

import org.w3c.dom.Node;

import pipeline.Slot;
import utils.Message;
import utils.XmlUtils;

public class ContentEnricher implements Task {

    private int counter = 0;

    private String xpath;
    private Slot[] entradas;
    private Slot salida;

    public ContentEnricher(String xpath, Slot salida, Slot... entradas) {
        this.xpath = xpath;
        this.entradas = entradas;
        this.salida = salida;
    }

    @Override
    public void execute() throws Exception {
        while(!entradas[0].esVacia() || !entradas[1].esVacia()){
            enriquecer(entradas[0].recibirMensaje(), entradas[1].recibirMensaje());
        }
    }

    private void enriquecer(Message mensaje, Message respuesta) throws Exception {
        counter++;
        Node nodoDestino = XmlUtils.NodeSearch(mensaje.getCuerpo(), xpath);
        Node nodoImportado = mensaje.getCuerpo().importNode(respuesta.getCuerpo(), true);
        nodoDestino.appendChild(nodoImportado);
        salida.enviarMensaje(mensaje);
        if (counter == 1) {
            System.out.println("ContentEnricher: Se han procesado " + counter + " mensajes.");
        }
    }
}