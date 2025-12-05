package tareas;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import utils.Message;

public class Translator implements Task {

    private String rutaXslt;

    public Translator(String rutaXslt) {
        this.rutaXslt = rutaXslt;
    }

    @Override
    public List<Message> execute(Message mensajeEntrada) throws Exception {
        System.out.println("Ejecutando Translator (Aplicando XSLT: " + rutaXslt + ")...");

        Document xmlOriginal = mensajeEntrada.getCuerpo();

        if (xmlOriginal == null) {
            System.err.println("Translator: El mensaje no tiene cuerpo XML.");
            return Collections.singletonList(mensajeEntrada);
        }

        File archivoXslt = new File(rutaXslt);
        TransformerFactory fabrica = TransformerFactory.newInstance();
        Transformer transformer = fabrica.newTransformer(new StreamSource(archivoXslt));

        DOMSource origen = new DOMSource(xmlOriginal);
        DOMResult resultado = new DOMResult();
        
        transformer.transform(origen, resultado);

        Document xmlTransformado = (Document) resultado.getNode();

        Message mensajeSalida = mensajeEntrada.clonar();
        mensajeSalida.setCuerpo(xmlTransformado);

        return Collections.singletonList(mensajeSalida);
    }
}