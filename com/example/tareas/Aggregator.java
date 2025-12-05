package tareas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import utils.Message;

public class Aggregator implements Task {

    private final Map<String, List<Message>> pendingComandas = new ConcurrentHashMap<>();
    private static final String AGGREGATE_ID_HEADER = "comandaId";
    private static final String AGGREGATE_SIZE_HEADER = "splitSize";

    @Override
    public List<Message> execute(Message inputMessage) throws Exception {
        String comandaId = (String) inputMessage.getHeader(AGGREGATE_ID_HEADER);
        Integer comandaSize = (Integer) inputMessage.getHeader(AGGREGATE_SIZE_HEADER);

        if (comandaId == null || comandaSize == null) return null;

        List<Message> comandaList = pendingComandas.computeIfAbsent(comandaId, k -> new ArrayList<>());
        List<Message> completedMessage = null;

        synchronized (comandaList) {
            comandaList.add(inputMessage);
            int itemsRecibidos = comandaList.size();

            System.out.println("   (Aggregator) Recibido plato " + itemsRecibidos + " de " + comandaSize + " para la mesa.");

            if (itemsRecibidos == comandaSize) {
                System.out.println("\n   >> ORDEN COMPLETADA (" + comandaId + "). Enviando todo al Camarero.\n");
                
                pendingComandas.remove(comandaId);
                completedMessage = buildAggregatedMessage(new ArrayList<>(comandaList));
            }
        }
        return completedMessage;
    }

    private List<Message> buildAggregatedMessage(List<Message> messages) {
        List<Object> aggregatedPayloads = messages.stream()
                .map(Message::getPayload)
                .collect(Collectors.toList());

        Message finalMessage = new Message(aggregatedPayloads);
        Message firstMessage = messages.get(0);
        finalMessage.setHeader(AGGREGATE_ID_HEADER, firstMessage.getHeader(AGGREGATE_ID_HEADER));
        finalMessage.setHeader(AGGREGATE_SIZE_HEADER, firstMessage.getHeader(AGGREGATE_SIZE_HEADER));
        finalMessage.setHeader("status", "COMPLETADA");

        return Collections.singletonList(finalMessage);
    }
}