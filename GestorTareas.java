import java.util.ArrayList;
import java.util.List;

/**
 * Guarda y administra la lista de tareas (la "lógica" de la app, sin interfaz).
 * Conceptos POO: composición (tiene una lista de Tarea) y polimorfismo
 * (la lista guarda Tarea y TareaConAlerta al mismo tiempo).
 */
public class GestorTareas {

    private final List<Tarea> tareas = new ArrayList<>();

    public void agregar(Tarea tarea) {
        tareas.add(tarea);
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    /** Devuelve las tareas con alerta cuya hora ya llegó. */
    public List<TareaConAlerta> tareasPorAlertar() {
        List<TareaConAlerta> resultado = new ArrayList<>();
        for (Tarea t : tareas) {
            if (t instanceof TareaConAlerta conAlerta && conAlerta.debeAlertar()) {
                resultado.add(conAlerta);
            }
        }
        return resultado;
    }
}
