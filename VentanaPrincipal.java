import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * La interfaz gráfica (GUI) hecha con Swing.
 * Conceptos POO: herencia (extends JFrame) y eventos (listeners).
 */
public class VentanaPrincipal extends JFrame {

    private final GestorTareas gestor;

    private final DefaultListModel<Tarea> modelo = new DefaultListModel<>();
    private final JList<Tarea> lista = new JList<>(modelo);
    private final JTextField campoTitulo = new JTextField(18);
    private final JCheckBox chkAlerta = new JCheckBox("Avisarme en");
    private final JSpinner minutos = new JSpinner(new SpinnerNumberModel(1, 1, 1440, 1));

    public VentanaPrincipal(GestorTareas gestor) {
        super("Doto List");
        this.gestor = gestor;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // --- Parte de arriba: escribir la tarea y (opcional) la alerta ---
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila1.add(new JLabel("Nueva tarea:"));
        fila1.add(campoTitulo);
        JButton btnAgregar = new JButton("Agregar");
        fila1.add(btnAgregar);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila2.add(chkAlerta);
        fila2.add(minutos);
        fila2.add(new JLabel("minutos"));

        JPanel arriba = new JPanel(new GridLayout(2, 1));
        arriba.add(fila1);
        arriba.add(fila2);
        add(arriba, BorderLayout.NORTH);

        // --- Centro: la lista de tareas ---
        lista.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(new JScrollPane(lista), BorderLayout.CENTER);

        // --- Abajo: botón para completar ---
        JButton btnCompletar = new JButton("Marcar como completada");
        JPanel abajo = new JPanel();
        abajo.add(btnCompletar);
        add(abajo, BorderLayout.SOUTH);

        // --- Eventos ---
        btnAgregar.addActionListener(e -> agregarTarea());
        campoTitulo.addActionListener(e -> agregarTarea()); // Enter también agrega
        btnCompletar.addActionListener(e -> completarSeleccionada());
    }

    private void agregarTarea() {
        String texto = campoTitulo.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nombre de la tarea.",
                    "Falta información", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Tarea nueva; // variable del tipo padre: puede guardar cualquiera de las dos
        if (chkAlerta.isSelected()) {
            int min = (Integer) minutos.getValue();
            nueva = new TareaConAlerta(texto, LocalDateTime.now().plusMinutes(min));
        } else {
            nueva = new Tarea(texto);
        }

        gestor.agregar(nueva);
        campoTitulo.setText("");
        campoTitulo.requestFocus();
        refrescar();
    }

    private void completarSeleccionada() {
        Tarea seleccionada = lista.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Primero selecciona una tarea de la lista.",
                    "Nada seleccionado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        seleccionada.marcarCompletada();
        refrescar();
    }

    /** Vuelve a dibujar la lista con lo que hay en el gestor. */
    public void refrescar() {
        modelo.clear();
        for (Tarea t : gestor.getTareas()) {
            modelo.addElement(t);
        }
    }

    /** Muestra la alerta de una tarea. */
    public void mostrarAlerta(TareaConAlerta tarea) {
        tarea.marcarAlertaMostrada(); // primero marcamos, para que no se repita
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(this,
                "¡Es hora de: " + tarea.getTitulo() + "!",
                "⏰ Alerta", JOptionPane.INFORMATION_MESSAGE);
    }
}
