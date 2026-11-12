import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Subclase de Tarea: es una tarea que además tiene una hora de alerta.
 * Conceptos POO: HERENCIA (extends) y POLIMORFISMO (@Override de getDescripcion).
 */
public class TareaConAlerta extends Tarea {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalDateTime fechaAlerta;
    private boolean alertaMostrada;

    public TareaConAlerta(String titulo, LocalDateTime fechaAlerta) {
        super(titulo); // llama al constructor de Tarea
        this.fechaAlerta = fechaAlerta;
        this.alertaMostrada = false;
    }

    /** True si ya llegó la hora, la tarea no está completada y aún no se avisó. */
    public boolean debeAlertar() {
        return !isCompletada()
                && !alertaMostrada
                && !LocalDateTime.now().isBefore(fechaAlerta);
    }

    public void marcarAlertaMostrada() {
        this.alertaMostrada = true;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + "  (⏰ " + fechaAlerta.format(FORMATO) + ")";
    }
}
