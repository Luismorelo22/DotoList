/**
 * Clase base: representa una tarea normal (sin alerta).
 * Conceptos POO: atributos privados (encapsulamiento), constructor y métodos.
 */
public class Tarea {

    private final String titulo;
    private boolean completada;

    public Tarea(String titulo) {
        this.titulo = titulo;
        this.completada = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void marcarCompletada() {
        this.completada = true;
    }

    /** Texto que se muestra en la lista. Las subclases pueden sobrescribirlo (polimorfismo). */
    public String getDescripcion() {
        return titulo;
    }

    @Override
    public String toString() {
        return (completada ? "✔  " : "○  ") + getDescripcion();
    }
}
