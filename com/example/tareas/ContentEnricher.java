package tareas;

import java.util.Collections;
import java.util.List;

import utils.Message;

public class ContentEnricher implements Task {

    private static final String CORRELATOR_DATA_HEADER = "datosBarman";
    
    private static final String ENRICHMENT_HEADER = "infoBebida";

    @Override
    public List<Message> execute(Message inputMessage) throws Exception {
        System.out.println("Ejecutando tarea de tipo ContentEnricher (Logica Cafe Sol)");

        Message outputMessage = inputMessage.cloneMessage();

        Object datosExternos = outputMessage.getHeader(CORRELATOR_DATA_HEADER);

        if (datosExternos != null) {
            outputMessage.setHeader(ENRICHMENT_HEADER, datosExternos);
            

            System.out.println("Mensaje enriquecido con '" + ENRICHMENT_HEADER + "': " + datosExternos);
            

        } else {
            System.out.println("ContentEnricher: No se encontraron datos en el header '" + CORRELATOR_DATA_HEADER + "'."
                               + " El Correlator (5 o 9) no adjunto la informacion.");
            outputMessage.setHeader(ENRICHMENT_HEADER, "DATOS_NO_ENCONTRADOS");
        }
        return Collections.singletonList(outputMessage);
    }
}