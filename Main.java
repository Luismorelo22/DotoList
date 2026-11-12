import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * CLASE PRINCIPAL: controla el flujo de la aplicación.
 * Crea el gestor, abre la ventana y revisa cada segundo si hay alertas.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::iniciar);
    }

    private static void iniciar() {
        GestorTareas gestor = new GestorTareas();
        VentanaPrincipal ventana = new VentanaPrincipal(gestor);
        ventana.setVisible(true);

        // Cada 1000 ms (1 segundo) revisa si alguna tarea ya debe avisar
        Timer revisor = new Timer(1000, e -> {
            for (TareaConAlerta t : gestor.tareasPorAlertar()) {
                ventana.mostrarAlerta(t);
            }
        });
        revisor.start();
    }
}
