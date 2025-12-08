package tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import pipeline.Slot;
import utils.Message;
import utils.Arbol;
import utils.XmlUtils;

public class Splitter implements Task {

    private String expresionXpath;
    private String idGrupo;
    private Slot entrada;
    private Slot salida;
    private static Arbol arbolInstancia;
    private boolean esConGrupo = false;

    public Splitter(String expresionXpath, String idGrupo, Slot entrada, Slot salida) {
        this.expresionXpath = expresionXpath;
        this.idGrupo = idGrupo;
        this.entrada = entrada;
        this.salida = salida;
        arbolInstancia = Arbol.getInstancia();
        esConGrupo = true;
    }

    public Splitter(String expresionXpath, Slot entrada, Slot salida) {
        this.expresionXpath = expresionXpath;
        this.entrada = entrada;
        this.salida = salida;
        arbolInstancia = Arbol.getInstancia();
    }



    @Override
    public void execute() throws Exception {
        split(entrada.recibirMensaje());
    }
    
    public void split(Message mensaje) throws Exception {
        Message recibido = mensaje;
        Document comanda = mensaje.getCuerpo();

        if(!esConGrupo){
            recibido.setComandaId(UUID.randomUUID().toString());
        }else{
            recibido.setComandaId(XmlUtils.NodeSearch(comanda, idGrupo).getTextContent());
        }

        List<Node> items = XmlUtils.NodeGroupSearch(comanda, expresionXpath);
        int total = items.size();
        recibido.setTamSecuencia(total);

        Message mensajeOriginal = recibido.clonar();
        
        arbolInstancia.agregarArbol(recibido.getComandaId(), comanda, items);
        int contador = 0;

        for (Node item : items) {
            Message nuevoMensaje = mensajeOriginal.clonar();
            Document nuevoCuerpo = XmlUtils.createNewDocument();
            Node importado = nuevoCuerpo.importNode(item, true);
            nuevoCuerpo.appendChild(importado);

            nuevoMensaje.setCuerpo(nuevoCuerpo);
            nuevoMensaje.setOrdenSecuencia(contador);
            contador++; 

            nuevoMensaje.setCorrelationId(UUID.randomUUID().toString());
            salida.enviarMensaje(nuevoMensaje);
        }
    }
}