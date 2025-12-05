/* 

import java.util.Arrays;
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
import tareas.Distributor;
import tareas.Task;
import utils.Message;

public class MainSinCorrelator {

    public static void main(String[] args) {
        try {
            System.out.println("\n##########################################################");
            System.out.println("#  SISTEMA CAFE SOL - FLUJO INCORRECTO  #");
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
            distFrioTask.setFilterCondition("tipo", "FRIA");
            Slot distributorFrioSlot = new Slot(distFrioTask);

            Distributor distCalienteTask = (Distributor) distributorFactory.buildTask();
            distCalienteTask.setFilterCondition("tipo", "CALIENTE");
            Slot distributorCalienteSlot = new Slot(distCalienteTask);

            Slot replicatorFrioSlot = new Slot(replicatorFactory.buildTask());
            Slot translatorFrioSlot = new Slot(translatorFactory.buildTask());
            Slot barmanFrioSlot = new Slot(new BarmanTask("FRIAS"));
            
            // NOTA: Creamos el slot pero NO lo conectaremos en la rama fria
            Slot correlatorFrioSlot = new Slot(correlatorFactory.buildTask()); 
            
            Slot enricherFrioSlot = new Slot(enricherFactory.buildTask());

            // Rama caliente (la dejamos funcional para comparar)
            Slot replicatorCalienteSlot = new Slot(replicatorFactory.buildTask());
            Slot translatorCalienteSlot = new Slot(translatorFactory.buildTask());
            Slot barmanCalienteSlot = new Slot(new BarmanTask("CALIENTES"));
            Slot correlatorCalienteSlot = new Slot(correlatorFactory.buildTask());
            Slot enricherCalienteSlot = new Slot(enricherFactory.buildTask());

            Slot mergerSlot = new Slot(mergerFactory.buildTask());
            Slot aggregatorSlot = new Slot(aggregatorFactory.buildTask());

            Slot camareroSlot = new Slot(new Task() {
                @Override
                public List<Message> execute(Message inputMessage) {
                    System.out.println("\n   >>> CAMARERO RECIBE PEDIDO FINAL:");
                    System.out.println("       Items: " + inputMessage.getPayload());
                    System.out.println("       Estado: ADVERTENCIA - Es probable que falten datos en las bebidas frias.");
                    return null;
                }
            });

            // --- 2. CONFIGURACIÓN DE TUBERÍAS ---
            System.out.println("--- [CONFIGURACION] Conectando tuberias... ---");

            splitterSlot.getOutputPort().connect(distributorFrioSlot.getInputPort());
            splitterSlot.getOutputPort().connect(distributorCalienteSlot.getInputPort());

            // RAMA FRIA - CONFIGURACIÓN ROTA (SIN CORRELATOR)
            System.out.println("1. Rama Fria: !! ATENCION !! Conectando SIN Correlator (Directo).");
            System.out.println("   (El mensaje ira: Replicator -> Barman -> Enricher, perdiendo el contexto original)");
            
            distributorFrioSlot.getOutputPort().connect(replicatorFrioSlot.getInputPort());
            
            // CONEXIÓN ERRÓNEA: Saltamos el paso de espera
            replicatorFrioSlot.getOutputPort().connect(translatorFrioSlot.getInputPort());
            translatorFrioSlot.getOutputPort().connect(barmanFrioSlot.getInputPort());
            
            // BYPASS: DEL BARMAN DIRECTO AL ENRICHER (SIN PASAR POR CORRELATOR)
            barmanFrioSlot.getOutputPort().connect(enricherFrioSlot.getInputPort()); 
            
            enricherFrioSlot.getOutputPort().connect(mergerSlot.getInputPort());

            // RAMA CALIENTE (Dejamos esta bien para que sirva de control)
            System.out.println("2. Rama Caliente: Conectada correctamente (con Correlator) para comparación.");
            distributorCalienteSlot.getOutputPort().connect(replicatorCalienteSlot.getInputPort());
            replicatorCalienteSlot.getOutputPort().connect(correlatorCalienteSlot.getInputPort());
            replicatorCalienteSlot.getOutputPort().connect(translatorCalienteSlot.getInputPort());
            translatorCalienteSlot.getOutputPort().connect(barmanCalienteSlot.getInputPort());
            barmanCalienteSlot.getOutputPort().connect(correlatorCalienteSlot.getInputPort());
            correlatorCalienteSlot.getOutputPort().connect(enricherCalienteSlot.getInputPort());
            enricherCalienteSlot.getOutputPort().connect(mergerSlot.getInputPort());

            mergerSlot.getOutputPort().connect(aggregatorSlot.getInputPort());
            aggregatorSlot.getOutputPort().connect(camareroSlot.getInputPort());
            
            System.out.println("--- [CONFIGURACION] Sistema conectado (con errores intencionados). ---\n");

            // --- 3. EJECUCIÓN ---
            // Solo mandamos bebidas frias para que el error sea evidente
            Message bebida1 = new Message("Coca Cola"); 
            bebida1.setHeader("tipo", "FRIA");
            Message bebida3 = new Message("Te Helado"); 
            bebida3.setHeader("tipo", "FRIA");

            List<Message> comanda = Arrays.asList(bebida1, bebida3);
            Message mensajeInicial = new Message(comanda);

            System.out.println(">> INICIANDO COMANDA DE PRUEBA: " + comanda + "\n");
            splitterSlot.getInputPort().receive(mensajeInicial);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/