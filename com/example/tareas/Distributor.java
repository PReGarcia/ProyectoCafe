package tareas;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import pipeline.Slot;
import utils.Message;
import utils.XmlUtils;

public class Distributor implements Task {

    private Map<String, Slot> salidas;

    public Distributor() {
        this.salidas = new LinkedHashMap<>();
    }

    public void agregarSalida(String xpath, Slot slotDestino) {
        if (xpath != null && slotDestino != null) {
            salidas.put(xpath, slotDestino);
        }
    }

    @Override
    public List<Message> execute(Message mensajeEntrada) throws Exception {
        System.out.println("Ejecutando Distributor...");

        Document documento = mensajeEntrada.getCuerpo();

        if (documento == null) {
            return Collections.emptyList();
        }

        for (Map.Entry<String, Slot> regla : salidas.entrySet()) {
            String xpath = regla.getKey();
            Slot slotDestino = regla.getValue();

            Node resultado = XmlUtils.NodeSearch(documento, xpath);

            if (resultado != null) {
                System.out.println(" -> Condición cumplida (" + xpath + "). Enviando al slot correspondiente.");
                
                Message copia = mensajeEntrada.clonar();
                slotDestino.recibirMensaje(copia);
            }
        }

        return Collections.emptyList();
    }
}