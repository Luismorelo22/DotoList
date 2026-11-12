import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

/**
 * La interfaz gráfica (GUI) hecha con Swing, con colores y diseño más amigable.
 * Conceptos POO: herencia (extends JFrame), clases internas, eventos (listeners).
 */
public class VentanaPrincipal extends JFrame {

    // Paleta de colores (puedes cambiarlos a tu gusto)
    private static final Color AZUL = new Color(0x4F6BED);
    private static final Color FONDO = new Color(0xF5F6FA);
    private static final Color TEXTO = new Color(0x2D3436);
    private static final Color GRIS = new Color(0x95A5A6);
    private static final Color VERDE = new Color(0x27AE60);
    private static final Color BORDE = new Color(0xD5DAE5);

    private final GestorTareas gestor;

    private final DefaultListModel<Tarea> modelo = new DefaultListModel<>();
    private final JList<Tarea> lista = new JList<>(modelo);
    private final CampoConHint campoTitulo = new CampoConHint("¿Qué tienes que hacer?", 20);
    private final JCheckBox chkAlerta = new JCheckBox("Avisarme en");
    private final JSpinner minutos = new JSpinner(new SpinnerNumberModel(1, 1, 1440, 1));
    private final JLabel lblResumen = new JLabel(" ");

    public VentanaPrincipal(GestorTareas gestor) {
        super("Doto List");
        this.gestor = gestor;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(560, 640);
        setMinimumSize(new Dimension(480, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());

        // Arriba: encabezado azul + zona para escribir la tarea
        JPanel norte = new JPanel(new BorderLayout());
        norte.add(crearEncabezado(), BorderLayout.NORTH);
        norte.add(crearPanelEntrada(), BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        // Centro: lista de tareas
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setCellRenderer(new RenderizadorTarea());
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(Color.WHITE);
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(FONDO);
        centro.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // Abajo: botón para completar
        add(crearPanelBotones(), BorderLayout.SOUTH);

        // Doble clic sobre una tarea = marcarla como completada
        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    completarSeleccionada();
                }
            }
        });

        refrescar();
    }

    private JPanel crearEncabezado() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AZUL);
        p.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel titulo = new JLabel("Doto List");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        lblResumen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblResumen.setForeground(new Color(0xDDE4FF));

        p.add(titulo, BorderLayout.NORTH);
        p.add(lblResumen, BorderLayout.SOUTH);
        return p;
    }

    private JPanel crearPanelEntrada() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));

        campoTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoTitulo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        JButton btnAgregar = crearBoton("Agregar", AZUL);

        JPanel fila1 = new JPanel(new BorderLayout(10, 0));
        fila1.setOpaque(false);
        fila1.add(campoTitulo, BorderLayout.CENTER);
        fila1.add(btnAgregar, BorderLayout.EAST);

        Font fuenteAlerta = new Font("Segoe UI", Font.PLAIN, 14);
        chkAlerta.setFont(fuenteAlerta);
        chkAlerta.setOpaque(false);
        chkAlerta.setFocusPainted(false);
        JLabel lblMin = new JLabel("minutos");
        lblMin.setFont(fuenteAlerta);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        fila2.setOpaque(false);
        fila2.add(chkAlerta);
        fila2.add(minutos);
        fila2.add(lblMin);

        p.add(fila1, BorderLayout.NORTH);
        p.add(fila2, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarTarea());
        campoTitulo.addActionListener(e -> agregarTarea()); // Enter también agrega
        return p;
    }

    private JPanel crearPanelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        p.setBackground(FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

        JButton btnCompletar = crearBoton("Marcar como completada", VERDE);
        p.add(btnCompletar);

        btnCompletar.addActionListener(e -> completarSeleccionada());
        return p;
    }

    /** Crea un botón de color con letras blancas. */
    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        return b;
    }

    // ----------------- Acciones -----------------

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
        int indice = lista.getSelectedIndex();
        modelo.clear();
        for (Tarea t : gestor.getTareas()) {
            modelo.addElement(t);
        }
        if (indice >= 0 && indice < modelo.size()) {
            lista.setSelectedIndex(indice);
        }
        actualizarResumen();
    }

    private void actualizarResumen() {
        int total = gestor.getTareas().size();
        int pendientes = 0;
        for (Tarea t : gestor.getTareas()) {
            if (!t.isCompletada()) {
                pendientes++;
            }
        }
        if (total == 0) {
            lblResumen.setText("Aún no tienes tareas. ¡Agrega la primera!");
        } else if (pendientes == 0) {
            lblResumen.setText("¡Todo al día! Completaste todas tus tareas.");
        } else {
            lblResumen.setText(pendientes + (pendientes == 1 ? " tarea pendiente" : " tareas pendientes"));
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

    private static String escapar(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // ----------------- Clases internas -----------------

    /** Campo de texto que muestra una pista gris cuando está vacío. */
    private static class CampoConHint extends JTextField {
        private final String hint;

        CampoConHint(String hint, int columnas) {
            super(columnas);
            this.hint = hint;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(GRIS);
                g2.setFont(getFont());
                Insets in = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(hint, in.left, y);
                g2.dispose();
            }
        }
    }

    /** Dibuja cada tarea como una fila: icono, título y una línea pequeña de detalle. */
    private class RenderizadorTarea implements ListCellRenderer<Tarea> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Tarea> list, Tarea tarea,
                int index, boolean seleccionada, boolean conFoco) {

            JPanel fila = new JPanel(new BorderLayout(14, 0));
            fila.setBackground(seleccionada ? new Color(0xE8EDFF) : Color.WHITE);
            fila.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xECEFF4)),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)));

            boolean hecha = tarea.isCompletada();

            JLabel icono = new JLabel(hecha ? "✔" : "○");
            icono.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));
            icono.setForeground(hecha ? VERDE : GRIS);

            String titulo = escapar(tarea.getTitulo());
            JLabel lblTitulo = new JLabel(hecha ? "<html><strike>" + titulo + "</strike></html>" : titulo);
            lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblTitulo.setForeground(hecha ? GRIS : TEXTO);

            String detalle = (tarea instanceof TareaConAlerta) ? "⏰ Con alerta" : "Sin alerta";
            JLabel lblDetalle = new JLabel(detalle);
            lblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblDetalle.setForeground(GRIS);

            JPanel textos = new JPanel(new GridLayout(2, 1));
            textos.setOpaque(false);
            textos.add(lblTitulo);
            textos.add(lblDetalle);

            fila.add(icono, BorderLayout.WEST);
            fila.add(textos, BorderLayout.CENTER);
            return fila;
        }
    }
}
