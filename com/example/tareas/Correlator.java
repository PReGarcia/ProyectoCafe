package tareas;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utils.Message;

//Hay que hacerlo mas generico esta hecho muy pensado para cafe??
public class Correlator implements Task {

    private final Map<String, Message> pendingMessages = new ConcurrentHashMap<>();

    private String RESPONSE_ROLE_VALUE;
    private String DATA_HEADER;
    private String ROLE_HEADER;
    private static final String CORRELATION_ID_HEADER = "correlationId";

    public Correlator(){}

    public Correlator(String roleString, String dataString, String responseString){
        this.DATA_HEADER = dataString;
        this.RESPONSE_ROLE_VALUE = responseString;
        this.ROLE_HEADER = roleString;
    }

    @Override
    public List<Message> execute(Message inputMessage) throws Exception {

        String correlationId = (String) inputMessage.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null) {
            System.out.println("ERROR: Mensaje llego al Correlator sin '" + CORRELATION_ID_HEADER + "'. Descartando.");
            return null; 
        }

        Message pendingMessage = pendingMessages.remove(correlationId);

        if (pendingMessage == null) {
            pendingMessages.put(correlationId, inputMessage);
            
            return null; 
        
        } else {

            Message originalMessage;
            Message responseMessage;

            if (RESPONSE_ROLE_VALUE.equals(inputMessage.getHeader(ROLE_HEADER))) {
                responseMessage = inputMessage;
                originalMessage = pendingMessage;
            } else {
                originalMessage = inputMessage;
                responseMessage = pendingMessage;
            }

            Object responseData = responseMessage.getPayload();
            originalMessage.setHeader(DATA_HEADER, responseData); //

            return Collections.singletonList(originalMessage);
        }
    }
}