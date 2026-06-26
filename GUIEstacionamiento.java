import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;

import javax.swing.text.MaskFormatter;
import java.time.LocalTime;

public class GUIEstacionamiento extends JFrame{
    
    //Atributos de la aplicación
    private Estacionamiento estacionamiento;
    private int numeroCocheraSeleccionada;
    private Tarifa tarifa;
    
    //Objetos Gráficos
    
    // PANEL COCHERA
    private JPanel panelEstacionamiento;    
    private JButton [] cocheras;
    
    //PANEL INFORMACION COCHERA SELECCIONADA
    private JPanel panelCochera;    
    private JLabel etiqueCocheraSeleccionada;

    //PANEL PRINCIPAL
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
    
    
    public GUIEstacionamiento(Estacionamiento e){
        super("Bienvenido a IPOO-Parking");
        estacionamiento = e;
        numeroCocheraSeleccionada = -1;
        tarifa = new Tarifa(1000,1800,3500,20000);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setVisible(true);
        
        inicializarpanelEstacionamiento();
        inicializarpanelCochera();
        inicializarpanelPrincipal();

        getContentPane().setLayout(new BorderLayout()); 
        getContentPane().setBackground(Color.WHITE);

        //Agrego cada panel al panel de contenido
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

    
    private void inicializarpanelEstacionamiento(){

        //Crea el panel y setea el layout
        panelEstacionamiento = new JPanel();
        panelEstacionamiento.setLayout(new GridLayout(2, estacionamiento.cantCocheras()/2)); 
    
        //Crea el arreglo de botones
        cocheras = new JButton[estacionamiento.cantCocheras()];
        
        for(int i = 0; i < estacionamiento.cantCocheras(); i++){
            int numeroCochera = i + 1;

            JButton boton = new JButton("Cochera " + numeroCochera);

            boton.setVerticalAlignment(SwingConstants.BOTTOM);
            boton.setHorizontalAlignment(SwingConstants.CENTER);
            boton.setIcon(escalarIcono("imagenes/parking-libre.png", 80, 80));
            boton.setBackground(Color.WHITE);

            boton.addActionListener(new OyenteCochera());
            boton.setActionCommand(String.valueOf(numeroCochera));

            cocheras[i] = boton;
            panelEstacionamiento.add(boton);
        }
        /*
           - Se crea cada uno de los botones correspondientes a la cochera
           - Se modifica el texto, la posicion del texto (centro-abajo), el color 
           y el icono (escalarIcono("imagenes/parking-libre.png", 80, 80))
           - se crea y registra el oyente
           - Se modifica el identificador de la cochera.
           - Se agrega al panel correspondiente.
           */
        
    }
    
    private void inicializarpanelCochera(){
        //Crea el panel y setea el layout
        panelCochera = new JPanel();
        panelCochera.setPreferredSize(new Dimension(300, 0));
        panelCochera.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));
        panelCochera.setLayout(new BorderLayout()); 
        panelCochera.setBackground(Color.WHITE);

        etiqueCocheraSeleccionada = new JLabel("Seleccione una cochera", SwingConstants.CENTER);
        etiqueCocheraSeleccionada.setVerticalAlignment(SwingConstants.TOP);

        JButton botonFinalizar = new JButton("Finalizar estacionamiento");
        botonFinalizar.setEnabled(false);
        botonFinalizar.addActionListener(new OyenteFinalizarReserva());

        panelCochera.add(etiqueCocheraSeleccionada, BorderLayout.CENTER);
        panelCochera.add(botonFinalizar, BorderLayout.SOUTH);

        
        /*
           - Crea la etiqueta de la cochera seleccionada
           - Crea el boton para finalizar un estacionamiento (crea y registra su oyente)
           - Inserta ambos objetos graficos al panel
           */
    }
    
    private void inicializarpanelPrincipal(){
        //Crea el panel y setea el layout
        panelPrincipal = new JPanel();
        panelPrincipal.setPreferredSize(new Dimension(400, 0));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));
        panelPrincipal.setLayout(new GridLayout(6, 1)); 
        panelPrincipal.setBackground(Color.WHITE);
     
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
        /*
           - Se crean las restantes etiquetas, campos de texto y botones
           correspondientes al panel principal.
           - Cada uno de estos objetos graficos se insertar en el panel
           principal hasta obtener el mismo disenio que se muestra en el
           enunciado.
           - Tener en cuenta que, si es necesario dejar una celda del 
           GridLayout sin contenido, deberá crearse un panel vacío e insertarse en el contenedor correspondiente. 
           JPanel panelNulo = new JPanel();
           panelNulo.setBackground(Color.WHITE);
           */
        
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
    

    
    private class OyenteCochera implements ActionListener{
        public void actionPerformed(ActionEvent e){
            /*  - Obtenga el numero de la cochera seleccionada
                - Verifique si la cochera se encuentra ocupada en el estacionamiento
                - Si la cochera esta ocupada, se habilita el boton 
                correspondiente, y se debe modificar la etiqueta de la cochera seleccionada.
                - Si la cochera esta libre, se habilita el boton,
                y las etiquetas correspondientes (VER IMAGENES DEL ENUNCIADO).
                */  
        }
    }
    
    
    private class OyenteIngresar implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String hora_min_ingreso = horaIngreso.getText();
            String pat = patente.getText();
            
            /* Si alguno de los datos ingresados es incorrecto, se 
             * muestra un mensaje de dialogo
               */
            if (hora_min_ingreso.equals("__:__") || pat.isEmpty()) {
                JOptionPane.showMessageDialog(null,"Debe ingresar una hora de ingreso y patente valida.","Error",JOptionPane.ERROR_MESSAGE);   
            }
            else{
                /* - Si los datos son correctos, se debe crear un nuevo
                   vehiculo para ingresar en el estacionamiento.
                   - Este vehiculo se crea con la hora de ingreso, numero
                   de cochera seleccionada y patente.
                   - Se modifica el icono del boton de la cochera correspondiente 
                   a "parking-ocupado.png"
                   - Se muestra un mensaje de dialogo tal como se observa en las imagenes del enunciad
                   - Se reinician todas las etiquetas al valor inicial (hora ingreso, nroCochera y patente) asi como el boton ingresar.
                   */
            }
        } 
    }
    

    private class OyenteFinalizarReserva implements ActionListener{
        public void actionPerformed(ActionEvent e){
            
            /* - Se obtiene el vehiculo seleccionado y la hora actual local
               (ver LocalTime).
               - Se muestra el mensaje de dialogo con el total a abonar
               - Se establece la hora de egreso del vehiculo
               - Se modifica el icono del boton de la cochera correspondiente 
               a "parking-libre.png"
               - Se reinician las etiquetas y botones al estado inicial.
               */
        } 
    }
    
}
