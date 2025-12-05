package pipeline;

import org.w3c.dom.Document;
import utils.Message;
import utils.XmlUtils;

public class InputPort {

    private Slot slot;

    public InputPort(Slot slot) {
        this.slot = slot;
    }

    public void leerArchivo(String nombreArchivo) {
        Document documento = XmlUtils.parseXml(nombreArchivo);

        if (documento != null) {
            Message mensaje = new Message(null, null, 0, 0, documento);
            
            slot.recibirMensaje(mensaje);
            
            System.out.println("InputPort: Archivo '" + nombreArchivo + "' leído y enviado al Slot.");
        } else {
            System.err.println("InputPort: Error al leer el archivo " + nombreArchivo);
        }
    }
}