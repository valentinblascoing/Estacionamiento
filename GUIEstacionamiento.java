import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;

import javax.swing.text.MaskFormatter;
import java.time.LocalTime;

public class GUIEstacionamiento extends JFrame {

    // Atributos de la aplicación
    private Estacionamiento estacionamiento;
    private int numeroCocheraSeleccionada;
    private Tarifa tarifa;

    // Objetos Gráficos

    // PANEL COCHERA
    private JPanel panelEstacionamiento;
    private JButton[] cocheras;

    // PANEL INFORMACION COCHERA SELECCIONADA
    private JPanel panelCochera;
    private JLabel etiqueCocheraSeleccionada;
    private JButton botonFinalizar; // guarda el botón para finalizar la reserva
    private JButton botonIngresar; // guarda el botón para registrar un ingreso

    // PANEL PRINCIPAL
    private JPanel panelPrincipal;
    private JLabel etiquetaNuevaReserva;
    private JLabel etiquetaHoraIngreso;
    private JFormattedTextField horaIngreso;
    private JLabel etiquetaPatente;
    private JTextField patente;
    private JLabel etiquetaNroCochera;
    private JLabel nroCochera;
    private JLabel etiquetaHoraEgreso;
    private JLabel horaEgreso;

    public GUIEstacionamiento(Estacionamiento e) {
        super("Bienvenido a IPOO-Parking");
        estacionamiento = e;
        numeroCocheraSeleccionada = -1;
        tarifa = new Tarifa(1000, 1800, 3500, 20000);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setVisible(true);

        inicializarpanelEstacionamiento();
        inicializarpanelCochera();
        inicializarpanelPrincipal();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Agrego cada panel al panel de contenido
        getContentPane().add(panelEstacionamiento, BorderLayout.NORTH);
        getContentPane().add(panelCochera, BorderLayout.EAST);
        getContentPane().add(panelPrincipal, BorderLayout.WEST);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "cerrar");

        getRootPane().getActionMap().put("cerrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.setVisible(true);
        this.setResizable(false);
    }

    private void inicializarpanelEstacionamiento() {

        // Crea el panel y setea el layout
        panelEstacionamiento = new JPanel();
        panelEstacionamiento.setLayout(new GridLayout(2, estacionamiento.cantCocheras() / 2));

        // Crea el arreglo de botones
        cocheras = new JButton[estacionamiento.cantCocheras()];

        OyenteCochera[] oyentesCocheras = new OyenteCochera[estacionamiento.cantCocheras()];

        for (int i = 0; i < estacionamiento.cantCocheras(); i++) {
            /* getContentPane().setLayout(new FlowLayout()); */
            /* cocheras[i] = new JButton(""+(i+1) , new ImageIcon("imagenes/parking-libre.png")); */
            cocheras[i] = new JButton(""+(i+1) , escalarIcono("imagenes/parking-libre.png", 80, 80));
            cocheras[i].setBackground(Color.WHITE);
            oyentesCocheras[i] = new OyenteCochera();
            cocheras[i].addActionListener(oyentesCocheras[i]);
            cocheras[i].setPreferredSize(new Dimension(90, 130));
            cocheras[i].setHorizontalAlignment(JLabel.CENTER);
            cocheras[i].setVerticalAlignment(JLabel.CENTER);
            cocheras[i].setHorizontalTextPosition(JLabel.CENTER);
            cocheras[i].setVerticalTextPosition(JLabel.BOTTOM);
            panelEstacionamiento.add(cocheras[i]);
            this.setVisible(true);
        }
        
        /*
         * - Se crea cada uno de los botones correspondientes a la cochera
         * - Se modifica el texto, la posicion del texto (centro-abajo), el color
         * y el icono (escalarIcono("imagenes/parking-libre.png", 80, 80))
         * - se crea y registra el oyente
         * - Se modifica el identificador de la cochera.
         * - Se agrega al panel correspondiente.
         */

    }

    private void inicializarpanelCochera() {
        // Crea el panel y setea el layout
        panelCochera = new JPanel();
        panelCochera.setPreferredSize(new Dimension(300, 0));
        panelCochera.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));
        panelCochera.setLayout(new BorderLayout());
        panelCochera.setBackground(Color.WHITE);

        etiqueCocheraSeleccionada = new JLabel("No hay cocheras seleccionadas", SwingConstants.CENTER);
        etiqueCocheraSeleccionada.setVerticalAlignment(SwingConstants.CENTER);

        botonFinalizar = new JButton("Finalizar estacionamiento"); // crea el botón de finalización
        botonFinalizar.setEnabled(false); // lo deja deshabilitado al inicio
        botonFinalizar.addActionListener(new OyenteFinalizarReserva()); // registra el oyente del botón

        panelCochera.add(etiqueCocheraSeleccionada, BorderLayout.CENTER);
        panelCochera.add(botonFinalizar, BorderLayout.SOUTH);

        /*
         * - Crea la etiqueta de la cochera seleccionada
         * - Crea el boton para finalizar un estacionamiento (crea y registra su oyente)
         * - Inserta ambos objetos graficos al panel
         */
    }

    private void inicializarpanelPrincipal() {
        // Crea el panel y setea el layout
        panelPrincipal = new JPanel();
        panelPrincipal.setPreferredSize(new Dimension(400, 0));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));
        panelPrincipal.setLayout(new GridLayout(6, 1));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setVisible(false);

        // Crea un campo de texto con formato para ingresar una hora.
        // La máscara "##:##" obliga a que el usuario escriba: dos caracteres numéricos,
        // luego :, y luego otros dos caracteres numéricos.
        try {
            MaskFormatter mascaraHora = new MaskFormatter("##:##");
            mascaraHora.setPlaceholderCharacter('_');

            horaIngreso = new JFormattedTextField(mascaraHora);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        etiquetaNuevaReserva = new JLabel("Nuevo acceso a cocheras");
        panelPrincipal.add(etiquetaNuevaReserva);

        JPanel panelNuloTitulo = new JPanel();
        panelNuloTitulo.setBackground(Color.WHITE);
        panelPrincipal.add(panelNuloTitulo);

        etiquetaHoraIngreso = new JLabel("Hora de ingreso:");
        panelPrincipal.add(etiquetaHoraIngreso);
        panelPrincipal.add(horaIngreso);

        etiquetaHoraEgreso = new JLabel("Hora egreso:");
        horaEgreso = new JLabel("Sin egreso");
        panelPrincipal.add(etiquetaHoraEgreso);
        panelPrincipal.add(horaEgreso);

        etiquetaPatente = new JLabel("Patente:");
        patente = new JTextField();
        panelPrincipal.add(etiquetaPatente);
        panelPrincipal.add(patente);

        etiquetaNroCochera = new JLabel("Cochera nro:");
        nroCochera = new JLabel("Seleccionar cochera");
        panelPrincipal.add(etiquetaNroCochera);
        panelPrincipal.add(nroCochera);

        botonIngresar = new JButton("Ingresar");
        botonIngresar.setEnabled(false);
        botonIngresar.addActionListener(new OyenteIngresar());
        panelPrincipal.add(botonIngresar);

        horaIngreso.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotonIngresar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotonIngresar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotonIngresar(); }
        });

        patente.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotonIngresar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotonIngresar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotonIngresar(); }
        });

        /*
         * - Se crean las restantes etiquetas, campos de texto y botones
         * correspondientes al panel principal.
         * - Cada uno de estos objetos graficos se insertar en el panel
         * principal hasta obtener el mismo disenio que se muestra en el
         * enunciado.
         * - Tener en cuenta que, si es necesario dejar una celda del
         * GridLayout sin contenido, deberá crearse un panel vacío e insertarse en el
         * contenedor correspondiente.
         * JPanel panelNulo = new JPanel();
         * panelNulo.setBackground(Color.WHITE);
         */

    }

    private void actualizarEstadoBotonIngresar() {
        boolean hayCocheraSeleccionada = numeroCocheraSeleccionada > 0;
        boolean horaIngresada = horaIngreso.getText() != null && !horaIngreso.getText().isEmpty();
        boolean patenteIngresada = patente.getText() != null && !patente.getText().trim().isEmpty();

        botonIngresar.setEnabled(hayCocheraSeleccionada && horaIngresada && patenteIngresada);
    }

    private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
        ImageIcon iconoOriginal = new ImageIcon(ruta);
        Image imagenOriginal = iconoOriginal.getImage();

        // Crear una imagen compatible con la pantalla
        BufferedImage imagenEscalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagenEscalada.createGraphics();

        // Dibujar imagen escalada
        g2d.drawImage(imagenOriginal, 0, 0, ancho, alto, null);
        g2d.dispose();

        // Activar interpolación de alta calidad
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        return new ImageIcon(imagenEscalada);
    }

    private class OyenteCochera implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            /*
             * - Obtenga el numero de la cochera seleccionada
             * - Verifique si la cochera se encuentra ocupada en el estacionamiento
             * - Si la cochera esta ocupada, se habilita el boton
             * correspondiente, y se debe modificar la etiqueta de la cochera seleccionada.
             * - Si la cochera esta libre, se habilita el boton,
             * y las etiquetas correspondientes (VER IMAGENES DEL ENUNCIADO).
             */
            panelPrincipal.setVisible(true); // muestra el panel principal al seleccionar una cochera
            actualizarEstadoBotonIngresar();
            etiqueCocheraSeleccionada.setText("No hay cocheras ocupadas seleccionadas"); // muestra la cochera seleccionada
            etiquetaNuevaReserva.setText("Nuevo acceso a cocheras"); // reinicia el texto de la etiqueta de nueva reserva

            JButton cocheraSeleccionada = (JButton) e.getSource(); // obtiene la cochera clickeada
            numeroCocheraSeleccionada = Integer.parseInt(cocheraSeleccionada.getText()); // guarda el número de la cochera
            nroCochera.setText(String.valueOf(numeroCocheraSeleccionada)); // muestra la cochera seleccionada

            horaEgreso.setText("Sin egreso"); // reinicia la hora de egreso al cambiar de cochera

            if (estacionamiento.obtenerVehiculo(numeroCocheraSeleccionada) != null) { // verifica si la cochera está ocupada
                botonFinalizar.setEnabled(true);
                botonIngresar.setEnabled(false); // deshabilita el botón de ingresar
                etiqueCocheraSeleccionada.setText(estacionamiento.consultarVehiculo(numeroCocheraSeleccionada));
                nroCochera.setText("Seleccionar cochera"); // muestra los datos del vehículo
            } else {
                botonIngresar.setEnabled(true);
                botonFinalizar.setEnabled(false); // deshabilita el botón si la cochera está libre
               // muestra que la cochera está libre
            }
        }
    }

    private class OyenteIngresar implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String hora_min_ingreso = horaIngreso.getText();
            String pat = patente.getText();

            /*
             * Si alguno de los datos ingresados es incorrecto, se
             * muestra un mensaje de dialogo
             */

            if (hora_min_ingreso.equals("__:__") || pat.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar una hora de ingreso y patente valida.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {

                /*
                 * - Si los datos son correctos, se debe crear un nuevo
                 * vehiculo para ingresar en el estacionamiento.
                 * - Este vehiculo se crea con la hora de ingreso, numero
                 * de cochera seleccionada y patente.
                 * - Se modifica el icono del boton de la cochera correspondiente
                 * a "parking-ocupado.png"
                 * - Se muestra un mensaje de dialogo tal como se observa en las imagenes del
                 * enunciad
                 * - Se reinician todas las etiquetas al valor inicial (hora ingreso, nroCochera
                 * y patente) asi como el boton ingresar.
                 */

                String[] partes = hora_min_ingreso.split(":"); // separa la hora ingresada en horas y minutos
                int hora = Integer.parseInt(partes[0]); // convierte la hora a entero
                int minutos = Integer.parseInt(partes[1]); // convierte los minutos a entero

                Hora horaIngresoVehiculo = new Hora(hora, minutos); // crea el objeto de hora de ingreso
                Vehiculo vehiculo = new Vehiculo(horaIngresoVehiculo, numeroCocheraSeleccionada, pat); // crea el vehículo con los datos ingresados

                // intenta asignar el vehículo a la cochera
                estacionamiento.ingresarVehiculo(vehiculo, numeroCocheraSeleccionada);
                cocheras[numeroCocheraSeleccionada - 1].setIcon(escalarIcono("imagenes/parking-ocupado.png", 80, 80)); // cambia el icono a ocupado
                JOptionPane.showMessageDialog(null, "Ingreso registrado con exito", "Exito", JOptionPane.INFORMATION_MESSAGE); // muestra confirmación de ingreso

                horaIngreso.setText(""); // limpia el campo de hora
                patente.setText(""); // limpia el campo de patente
                nroCochera.setText("Seleccionar cochera"); // reinicia la etiqueta de cochera
                numeroCocheraSeleccionada = -1; // deja sin cochera seleccionada
                etiqueCocheraSeleccionada.setText("No hay cocheras ocupadas seleccionadas"); // reinicia el texto de la cochera
                horaEgreso.setText("Sin egreso"); // reinicia la hora de egreso
            }
        }
    }

    private class OyenteFinalizarReserva implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Vehiculo vehiculo = estacionamiento.obtenerVehiculo(numeroCocheraSeleccionada); // obtiene el vehículo de la cochera seleccionada
            if (vehiculo != null) {
                LocalTime horaActual = LocalTime.now(); // toma la hora actual del sistema
                Hora horaSalida = new Hora(horaActual.getHour(), horaActual.getMinute()); // crea la hora de salida
                vehiculo.egresaVehiculo(horaSalida); // marca el egreso del vehículo

                int total = vehiculo.obteneraCobrar(tarifa); // calcula el total a pagar
                JOptionPane.showMessageDialog(null, "Abonar: $" + total, "Finalizando estacionamiento", JOptionPane.INFORMATION_MESSAGE); // muestra el importe

                estacionamiento.egresarVehiculo(numeroCocheraSeleccionada); // libera la cochera en el estacionamiento
                cocheras[numeroCocheraSeleccionada - 1].setIcon(escalarIcono("imagenes/parking-libre.png", 80, 80)); // cambia el icono a libre

                nroCochera.setText("Seleccionar cochera"); // reinicia la etiqueta de cochera
                etiqueCocheraSeleccionada.setText("Seleccione una cochera"); // reinicia la información mostrada
                numeroCocheraSeleccionada = -1; // deja sin cochera seleccionada
                botonFinalizar.setEnabled(false); // deshabilita el botón de finalizar
            }
        }
    }

}
