import java.util.List;

import factory.AggregatorFactory;
import factory.ContentEnricherFactory;
import factory.CorrelatorFactory;
import factory.DistributorFactory;
import factory.MergerFactory;
import factory.ReplicatorFactory;
import factory.SplitterFactory;
import factory.TaskFactory;
import factory.TranslatorFactory;
import pipeline.Slot;
import tareas.BarmanTask;
import tareas.DatabaseQueryTask;
import tareas.DatabaseXmlSink;
import tareas.Distributor;
import tareas.Task;
import utils.Message;
import utils.XmlUtils;
import org.w3c.dom.Document;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("\n##########################################################");
            System.out.println("#      SISTEMA CAFE SOL      #");
            System.out.println("##########################################################");

            TaskFactory splitterFactory = new SplitterFactory();
            TaskFactory distributorFactory = new DistributorFactory();
            TaskFactory replicatorFactory = new ReplicatorFactory();
            TaskFactory translatorFactory = new TranslatorFactory();
            TaskFactory correlatorFactory = new CorrelatorFactory();
            TaskFactory enricherFactory = new ContentEnricherFactory();
            TaskFactory mergerFactory = new MergerFactory();
            TaskFactory aggregatorFactory = new AggregatorFactory();

            Slot splitterSlot = new Slot(splitterFactory.buildTask());
            
            Distributor distFrioTask = (Distributor) distributorFactory.buildTask();
            distFrioTask.setFilterCondition("tipo", "cold");
            Slot distributorFrioSlot = new Slot(distFrioTask);

            Distributor distCalienteTask = (Distributor) distributorFactory.buildTask();
            distCalienteTask.setFilterCondition("tipo", "hot");
            Slot distributorCalienteSlot = new Slot(distCalienteTask);

            Slot replicatorFrioSlot = new Slot(replicatorFactory.buildTask());
            Slot translatorFrioSlot = new Slot(translatorFactory.buildTask());
            Slot barmanFrioSlot = new Slot(new BarmanTask("FRIAS"));
            Slot correlatorFrioSlot = new Slot(correlatorFactory.buildTask());
            Slot enricherFrioSlot = new Slot(enricherFactory.buildTask());

            Slot replicatorCalienteSlot = new Slot(replicatorFactory.buildTask());
            Slot translatorCalienteSlot = new Slot(translatorFactory.buildTask());
            Slot barmanCalienteSlot = new Slot(new BarmanTask("CALIENTES"));
            Slot correlatorCalienteSlot = new Slot(correlatorFactory.buildTask());
            Slot enricherCalienteSlot = new Slot(enricherFactory.buildTask());

            Slot mergerSlot = new Slot(mergerFactory.buildTask());
            Slot aggregatorSlot = new Slot(aggregatorFactory.buildTask());

            Slot camareroSlot = new Slot(new Task() {
                @Override
                public List<Message> execute(Message inputMessage) throws Exception {
                    System.out.println("\n   >>> CAMARERO ENTREGANDO PEDIDO:");
                    System.out.println("       Contenido: " + inputMessage.getPayload());
                    return null;
                }
            });

            Slot dbQuerySlot = new Slot(new DatabaseQueryTask());
            Slot dbSinkSlot = new Slot(new DatabaseXmlSink());
            
            Slot monitorDbSlot = new Slot(new Task() {
                @Override
                public List<Message> execute(Message inputMessage) throws Exception {
                    System.out.println("   [MONITOR DB] Respuesta recibida: " + inputMessage.getPayload());
                    return null; 
                }
            });

            System.out.println("--- [CONFIGURACION] Conectando tuberias... ---");

            splitterSlot.getOutputPort().connect(distributorFrioSlot.getInputPort());
            splitterSlot.getOutputPort().connect(distributorCalienteSlot.getInputPort());

            distributorFrioSlot.getOutputPort().connect(replicatorFrioSlot.getInputPort());
            replicatorFrioSlot.getOutputPort().connect(translatorFrioSlot.getInputPort());
            translatorFrioSlot.getOutputPort().connect(barmanFrioSlot.getInputPort());
            barmanFrioSlot.getOutputPort().connect(correlatorFrioSlot.getInputPort());
            correlatorFrioSlot.getOutputPort().connect(enricherFrioSlot.getInputPort());
            enricherFrioSlot.getOutputPort().connect(mergerSlot.getInputPort());

            distributorCalienteSlot.getOutputPort().connect(replicatorCalienteSlot.getInputPort());
            replicatorCalienteSlot.getOutputPort().connect(translatorCalienteSlot.getInputPort());
            translatorCalienteSlot.getOutputPort().connect(barmanCalienteSlot.getInputPort());
            barmanCalienteSlot.getOutputPort().connect(correlatorCalienteSlot.getInputPort());
            correlatorCalienteSlot.getOutputPort().connect(enricherCalienteSlot.getInputPort());
            enricherCalienteSlot.getOutputPort().connect(mergerSlot.getInputPort());

            mergerSlot.getOutputPort().connect(aggregatorSlot.getInputPort());
            aggregatorSlot.getOutputPort().connect(camareroSlot.getInputPort());

            dbQuerySlot.getOutputPort().connect(monitorDbSlot.getInputPort());
            dbSinkSlot.getOutputPort().connect(monitorDbSlot.getInputPort());

            System.out.println("--- [CONFIGURACION] Sistema listo. ---\n");


            System.out.println(">> 1. INICIANDO PROCESO DE COMANDA (Cafe): Leemos order1.xml");
            String rutaXml = "com/example/comandas/order1.xml";
            Document docComanda = XmlUtils.parseXml(rutaXml);

            if (docComanda != null) {
                Message mensajeInicial = new Message(docComanda);
                splitterSlot.getInputPort().receive(mensajeInicial);
            } else {
                System.err.println("Error: No se pudo leer el archivo XML en: " + rutaXml);
            }

            System.out.println("\n>> 2. INICIANDO PRUEBA DE CONSULTA SQL:");
            Message solicitudDb = new Message("SELECT * FROM inventario"); 
            dbQuerySlot.getInputPort().receive(solicitudDb);

            System.out.println("\n>> 3. INICIANDO PRUEBA DE GUARDADO XML:");
            if (docComanda != null) {
                Message mensajeXml = new Message(docComanda);
                dbSinkSlot.getInputPort().receive(mensajeXml);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}