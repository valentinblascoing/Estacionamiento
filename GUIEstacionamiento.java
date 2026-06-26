import javax.swing.*; //importa todas las clases de Swing (JFrame, JButton, JPanel, etc.)
import java.awt.*; //importa las clases de AWT (Color, Dimension, layouts, etc.)
import java.awt.event.*; //importa las clases de eventos (ActionListener, ActionEvent)
import javax.swing.border.EmptyBorder; //importa la clase para crear bordes vacíos
import java.awt.image.BufferedImage; //importa la clase para manipular imágenes en memoria
import java.awt.Graphics2D; //importa la clase para dibujar sobre una imagen
import java.awt.RenderingHints; //importa la clase con las opciones de calidad de renderizado
import javax.swing.text.MaskFormatter; //importa la clase para aplicar máscaras de formato a campos de texto
import java.time.LocalTime; //importa la clase para obtener la hora actual del sistema

public class GUIEstacionamiento extends JFrame{ //define la clase de la ventana principal, hereda de JFrame
    
    //Atributos de la aplicación
    private Estacionamiento estacionamiento; //referencia al estacionamiento que administra la GUI
    private int numeroCocheraSeleccionada; //guarda el número de la cochera seleccionada actualmente
    private Tarifa tarifa; //guarda los montos de la tarifa por tramos
    
    //Objetos Gráficos
    
    // PANEL COCHERA
    private JPanel panelEstacionamiento; //panel que contiene los botones de todas las cocheras
    private JButton [] cocheras; //arreglo con los botones de cada cochera
    
    //PANEL INFORMACION COCHERA SELECCIONADA
    private JPanel panelCochera; //panel que muestra la info de la cochera seleccionada
    private JLabel etiqueCocheraSeleccionada; //etiqueta que muestra el estado de la cochera seleccionada
    private JButton botonFinalizar; //botón para finalizar el estacionamiento de la cochera seleccionada

    //PANEL PRINCIPAL
    private JPanel panelPrincipal; //panel con el formulario de nuevo ingreso
    private JLabel etiquetaNuevaReserva; //título del formulario de ingreso
    private JLabel etiquetaHoraIngreso; //etiqueta del campo de hora de ingreso
    private JFormattedTextField horaIngreso; //campo de texto con máscara para la hora de ingreso
    private JLabel etiquetaPatente; //etiqueta del campo de patente
    private JTextField patente; //campo de texto para ingresar la patente
    private JLabel etiquetaNroCochera; //etiqueta del número de cochera seleccionada
    private JLabel nroCochera; //muestra el número de cochera seleccionada
    private JLabel etiquetaHoraEgreso; //etiqueta de hora de egreso (informativa)
    private JLabel horaEgreso; //muestra la hora de egreso (informativa)
    private JButton botonIngresar; //botón para confirmar el ingreso de un vehículo
    
    
    public GUIEstacionamiento(Estacionamiento e){ //constructor: recibe el estacionamiento a administrar
        super("Bienvenido a IPOO-Parking"); //setea el título de la ventana
        estacionamiento = e; //guarda la referencia al estacionamiento recibido
        numeroCocheraSeleccionada = -1; //inicializa sin ninguna cochera seleccionada
        tarifa = new Tarifa(1000,1800,3500,20000); //crea la tarifa con los montos por tramo
        
        setDefaultCloseOperation(EXIT_ON_CLOSE); //cierra la aplicación al cerrar la ventana
        setSize(new Dimension(800, 600)); //define el tamaño de la ventana
        setVisible(true); //hace visible la ventana
        
        inicializarpanelEstacionamiento(); //crea y configura el panel de cocheras
        inicializarpanelCochera(); //crea y configura el panel de la cochera seleccionada
        inicializarpanelPrincipal(); //crea y configura el panel del formulario de ingreso

        getContentPane().setLayout(new BorderLayout()); //define el layout del panel de contenido
        getContentPane().setBackground(Color.WHITE); //define el color de fondo del panel de contenido

        //Agrego cada panel al panel de contenido
        getContentPane().add(panelEstacionamiento, BorderLayout.NORTH); //ubica el panel de cocheras arriba
        getContentPane().add(panelCochera, BorderLayout.EAST); //ubica el panel de info a la derecha
        getContentPane().add(panelPrincipal, BorderLayout.WEST); //ubica el panel principal a la izquierda
        
        this.setVisible(true); //vuelve a asegurar que la ventana sea visible
        this.setResizable(false); //impide que el usuario redimensione la ventana
    }

    
    private void inicializarpanelEstacionamiento(){ //crea el panel con los botones de las cocheras

        //Crea el panel y setea el layout
        panelEstacionamiento = new JPanel(); //crea el panel de cocheras
        panelEstacionamiento.setLayout(new GridLayout(2, estacionamiento.cantCocheras()/2)); //define la grilla de 2 filas
    
        //Crea el arreglo de botones
        cocheras = new JButton[estacionamiento.cantCocheras()]; //crea el arreglo con un botón por cochera
        
        for(int i = 0; i < cocheras.length; i++){ //recorre cada cochera del estacionamiento
            int numeroCochera = i + 1; // las cocheras son 1-indexadas en Estacionamiento

            JButton boton = new JButton("Cochera " + numeroCochera); //crea el botón con el número de cochera
            boton.setVerticalTextPosition(SwingConstants.BOTTOM); //ubica el texto debajo del ícono
            boton.setHorizontalTextPosition(SwingConstants.CENTER); //centra el texto horizontalmente
            boton.setBackground(Color.WHITE); //define el color de fondo del botón
            boton.setIcon(escalarIcono("imagenes/parking-libre.png", 80, 80)); //le pone el ícono de cochera libre

            boton.addActionListener(new OyenteCochera()); //registra el oyente de clic para este botón

            // Usamos el ActionCommand para poder identificar la cochera dentro del oyente
            boton.setActionCommand(String.valueOf(numeroCochera)); //guarda el número de cochera en el botón

            cocheras[i] = boton; //guarda el botón en el arreglo de cocheras
            panelEstacionamiento.add(boton); //agrega el botón al panel
        }
    }
    
    private void inicializarpanelCochera(){ //crea el panel de información de la cochera seleccionada
        //Crea el panel y setea el layout
        panelCochera = new JPanel(); //crea el panel de info de cochera
        panelCochera.setPreferredSize(new Dimension(300, 0)); //define el ancho preferido del panel
        panelCochera.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10)); //define el margen interno del panel
        panelCochera.setLayout(new BorderLayout()); //define el layout del panel
        panelCochera.setBackground(Color.WHITE); //define el color de fondo del panel
        
        etiqueCocheraSeleccionada = new JLabel("Seleccione una cochera", SwingConstants.CENTER); //crea la etiqueta de estado, centrada
        etiqueCocheraSeleccionada.setVerticalAlignment(SwingConstants.TOP); //alinea el texto arriba del panel

        botonFinalizar = new JButton("Finalizar estacionamiento"); //crea el botón de finalizar
        botonFinalizar.setEnabled(false); //lo deshabilita hasta que se seleccione una cochera ocupada
        botonFinalizar.addActionListener(new OyenteFinalizarReserva()); //registra el oyente de clic del botón

        panelCochera.add(etiqueCocheraSeleccionada, BorderLayout.CENTER); //agrega la etiqueta al centro del panel
        panelCochera.add(botonFinalizar, BorderLayout.SOUTH); //agrega el botón abajo del panel
    }
    
    private void inicializarpanelPrincipal(){ //crea el panel con el formulario de nuevo ingreso
        //Crea el panel y setea el layout
        panelPrincipal = new JPanel(); //crea el panel principal
        panelPrincipal.setPreferredSize(new Dimension(400, 0)); //define el ancho preferido del panel
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10)); //define el margen interno del panel
        panelPrincipal.setLayout(new GridLayout(6, 2)); //define la grilla de 6 filas y 2 columnas
        panelPrincipal.setBackground(Color.WHITE); //define el color de fondo del panel
     
        // Crea un campo de texto con formato para ingresar una hora.
        // La máscara "##:##" obliga a que el usuario escriba: dos caracteres numéricos,
        // luego :, y luego otros dos caracteres numéricos.
        try {
            MaskFormatter mascaraHora = new MaskFormatter("##:##"); //crea la máscara hh:mm
            mascaraHora.setPlaceholderCharacter('_'); //define el carácter de relleno de la máscara
        
            horaIngreso = new JFormattedTextField(mascaraHora); //crea el campo de texto con esa máscara
        } catch (java.text.ParseException e) {
            e.printStackTrace(); //imprime el error si la máscara es inválida
        }
        
        etiquetaNuevaReserva = new JLabel("Nuevo acceso a cocheras"); //crea el título del formulario
        panelPrincipal.add(etiquetaNuevaReserva); //agrega el título al panel

        JPanel panelNuloTitulo = new JPanel(); //crea un panel vacío para completar la fila del título
        panelNuloTitulo.setBackground(Color.WHITE); //define el color de fondo del panel vacío
        panelPrincipal.add(panelNuloTitulo); //agrega el panel vacío junto al título
        
        // Fila: hora de ingreso
        etiquetaHoraIngreso = new JLabel("Hora de ingreso (hh:mm):"); //crea la etiqueta de hora de ingreso
        panelPrincipal.add(etiquetaHoraIngreso); //agrega la etiqueta al panel
        panelPrincipal.add(horaIngreso); //agrega el campo de hora al panel

        // Fila: patente
        etiquetaPatente = new JLabel("Patente:"); //crea la etiqueta de patente
        patente = new JTextField(); //crea el campo de texto de patente
        panelPrincipal.add(etiquetaPatente); //agrega la etiqueta al panel
        panelPrincipal.add(patente); //agrega el campo de patente al panel

        // Fila: número de cochera seleccionada
        etiquetaNroCochera = new JLabel("Cochera seleccionada:"); //crea la etiqueta de cochera seleccionada
        nroCochera = new JLabel("-"); //crea la etiqueta que muestra el número de cochera
        panelPrincipal.add(etiquetaNroCochera); //agrega la etiqueta al panel
        panelPrincipal.add(nroCochera); //agrega el valor de la cochera al panel

        // Estas etiquetas se inicializan por completitud, pero la hora de egreso
        // y el monto a abonar se muestran en el diálogo de OyenteFinalizarReserva,
        // no como campos fijos en el formulario de ingreso.
        etiquetaHoraEgreso = new JLabel("Hora de egreso:"); //crea la etiqueta de hora de egreso (no se agrega al panel)
        horaEgreso = new JLabel("-"); //crea el valor de hora de egreso (no se agrega al panel)

        // Fila: botón ingresar
        botonIngresar = new JButton("Ingresar"); //crea el botón de ingresar vehículo
        botonIngresar.addActionListener(new OyenteIngresar()); //registra el oyente de clic del botón

        JPanel panelNulo = new JPanel(); //crea un panel vacío para completar la fila del botón
        panelNulo.setBackground(Color.WHITE); //define el color de fondo del panel vacío

        panelPrincipal.add(panelNulo); //agrega el panel vacío junto al botón
        panelPrincipal.add(botonIngresar); //agrega el botón de ingresar al panel
    }


    private ImageIcon escalarIcono(String ruta, int ancho, int alto) { //escala una imagen al tamaño indicado
        ImageIcon iconoOriginal = new ImageIcon(ruta); //carga la imagen original desde la ruta
        Image imagenOriginal = iconoOriginal.getImage(); //obtiene la imagen sin escalar
    
        // Crear una imagen compatible con la pantalla
        BufferedImage imagenEscalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB); //crea el lienzo destino
        Graphics2D g2d = imagenEscalada.createGraphics(); //obtiene el contexto gráfico del lienzo
    
        // Dibujar imagen escalada
        g2d.drawImage(imagenOriginal, 0, 0, ancho, alto, null); //dibuja la imagen original con el nuevo tamaño
        g2d.dispose(); //libera los recursos del contexto gráfico
 
        // Activar interpolación de alta calidad
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); //mejora la interpolación
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); //prioriza calidad de renderizado
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //activa el antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY); //mejora el renderizado de color
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); //mejora la interpolación de transparencia
    
        return new ImageIcon(imagenEscalada); //devuelve el ícono ya escalado
    }
    

    
    private class OyenteCochera implements ActionListener{ //oyente de clic para los botones de cochera
        public void actionPerformed(ActionEvent e){
            JButton botonPresionado = (JButton) e.getSource(); //obtiene el botón que fue presionado
            int numero = Integer.parseInt(botonPresionado.getActionCommand()); //obtiene el número de cochera del botón

            numeroCocheraSeleccionada = numero; //guarda la cochera seleccionada

            Vehiculo v = estacionamiento.obtenerVehiculo(numero); //busca si hay un vehículo en esa cochera

            if(v != null){
                // Cochera ocupada: mostramos la info del vehículo y habilitamos "Finalizar"
                etiqueCocheraSeleccionada.setText(estacionamiento.consultarVehiculo(numero)); //muestra los datos del vehículo
                botonFinalizar.setEnabled(true); //habilita el botón de finalizar

                // Deshabilitamos el ingreso, ya que esta cochera está ocupada
                botonIngresar.setEnabled(false); //deshabilita el botón de ingresar
                nroCochera.setText("-"); //limpia el número de cochera del formulario de ingreso
            }
            else{
                // Cochera libre: mostramos mensaje y habilitamos el ingreso a esa cochera
                etiqueCocheraSeleccionada.setText("<html>Cochera: " + numero + "<br/><br/>Libre</html>"); //muestra que está libre
                botonFinalizar.setEnabled(false); //deshabilita el botón de finalizar

                botonIngresar.setEnabled(true); //habilita el botón de ingresar
                nroCochera.setText(String.valueOf(numero)); //muestra el número de cochera en el formulario
            }
        }
    }
    
    
    private class OyenteIngresar implements ActionListener{ //oyente de clic del botón "Ingresar"
        public void actionPerformed(ActionEvent e){
            String hora_min_ingreso = horaIngreso.getText(); //obtiene el texto del campo de hora
            String pat = patente.getText(); //obtiene el texto del campo de patente
            
            if (hora_min_ingreso.equals("__:__") || pat.isEmpty() || numeroCocheraSeleccionada == -1) {
                JOptionPane.showMessageDialog(null,"Debe ingresar una hora de ingreso y patente valida.","Error",JOptionPane.ERROR_MESSAGE); //avisa que los datos son inválidos
            }
            else{
                String[] partes = hora_min_ingreso.split(":"); //separa la hora y los minutos del texto ingresado
                int hor = Integer.parseInt(partes[0]); //convierte la hora a entero
                int min = Integer.parseInt(partes[1]); //convierte los minutos a entero

                Hora ingreso = new Hora(hor, min); //crea el objeto Hora de ingreso
                Vehiculo nuevoVehiculo = new Vehiculo(ingreso, numeroCocheraSeleccionada, pat); //crea el vehículo a ingresar

                boolean pudo = estacionamiento.ingresarVehiculo(nuevoVehiculo, numeroCocheraSeleccionada); //intenta ingresarlo en la cochera

                if(pudo){
                    // Actualizamos el ícono del botón de la cochera correspondiente
                    cocheras[numeroCocheraSeleccionada - 1].setIcon(escalarIcono("imagenes/parking-ocupado.png", 80, 80)); //cambia el ícono a ocupado

                    JOptionPane.showMessageDialog(null,
                        "Vehículo ingresado correctamente.\nCochera: " + numeroCocheraSeleccionada
                        + "\nPatente: " + pat
                        + "\nHora de ingreso: " + ingreso.toString(),
                        "Ingreso registrado", JOptionPane.INFORMATION_MESSAGE); //muestra el mensaje de confirmación
                }
                else{
                    JOptionPane.showMessageDialog(null,"La cochera seleccionada ya se encuentra ocupada.","Error",JOptionPane.ERROR_MESSAGE); //avisa que la cochera ya estaba ocupada
                }

                // Reiniciamos las etiquetas y campos al estado inicial
                horaIngreso.setValue(null); //limpia el campo de hora de ingreso
                patente.setText(""); //limpia el campo de patente
                nroCochera.setText("-"); //limpia el número de cochera mostrado
                botonIngresar.setEnabled(false); //deshabilita el botón de ingresar
                numeroCocheraSeleccionada = -1; //quita la selección de cochera
                etiqueCocheraSeleccionada.setText("Seleccione una cochera"); //restablece el mensaje del panel de cochera
            }
        } 
    }
    

    private class OyenteFinalizarReserva implements ActionListener{ //oyente de clic del botón "Finalizar estacionamiento"
        public void actionPerformed(ActionEvent e){
            Vehiculo v = estacionamiento.obtenerVehiculo(numeroCocheraSeleccionada); //busca el vehículo de la cochera seleccionada

            if(v != null){
                LocalTime ahora = LocalTime.now(); //obtiene la hora actual del sistema
                Hora horaActual = new Hora(ahora.getHour(), ahora.getMinute()); //crea el objeto Hora con la hora actual

                // Se debe establecer la hora de egreso ANTES de pedir el monto a cobrar,
                // ya que obteneraCobrar() usa egreso.diferenciaMinutos(...) internamente.
                v.egresaVehiculo(horaActual); //establece la hora de egreso del vehículo

                int aCobrar = v.obteneraCobrar(tarifa); //calcula el monto a cobrar según el tiempo estacionado

                JOptionPane.showMessageDialog(null,
                    "Cochera: " + v.obtenerNumero()
                    + "\nPatente: " + v.obtenerPatente()
                    + "\nHora de ingreso: " + v.obtenerIngreso().toString()
                    + "\nHora de egreso: " + horaActual.toString()
                    + "\nTotal a abonar: $" + aCobrar,
                    "Finalizar estacionamiento", JOptionPane.INFORMATION_MESSAGE); //muestra el resumen y el monto a pagar

                estacionamiento.egresarVehiculo(numeroCocheraSeleccionada); //libera la cochera en el estacionamiento

                cocheras[numeroCocheraSeleccionada - 1].setIcon(escalarIcono("imagenes/parking-libre.png", 80, 80)); //cambia el ícono a libre
            }

            // Reiniciamos las etiquetas y botones al estado inicial
            etiqueCocheraSeleccionada.setText("Seleccione una cochera"); //restablece el mensaje del panel de cochera
            botonFinalizar.setEnabled(false); //deshabilita el botón de finalizar
            numeroCocheraSeleccionada = -1; //quita la selección de cochera
        } 
    }
    
}