class Vehiculo{ //clase que representa un vehículo estacionado
    //atributos de instancia
    private Hora ingreso; //hora en la que el vehículo ingresó
    private Hora egreso; //hora en la que el vehículo egresó (null si sigue estacionado)
    private int numero; //número de cochera donde está estacionado
    private String patente; //patente del vehículo

    //constructor
    public Vehiculo (Hora i, int n, String p){ //crea el vehículo con su hora de ingreso, cochera y patente
        ingreso = i; //asigna la hora de ingreso
        egreso = null; //inicializa la hora de egreso como null (todavía no egresó)
        numero = n; //asigna el número de cochera
        patente = p; //asigna la patente
    }
    
    //comandos
    public void egresaVehiculo(Hora c){ //establece la hora de egreso del vehículo
        egreso = c; //asigna la hora de egreso recibida
    }
    
    //consultas
    public Hora obtenerIngreso(){ //devuelve la hora de ingreso
        return ingreso; //retorna el atributo ingreso
    }
    
    public Hora obtenerEgreso(){ //devuelve la hora de egreso
        return egreso; //retorna el atributo egreso
    }
    
    public int obtenerNumero(){ //devuelve el número de cochera
        return numero; //retorna el atributo numero
    }
    
    public String obtenerPatente(){ //devuelve la patente
        return patente; //retorna el atributo patente
    }
    
    public String toString(){ //representa los datos del vehículo como texto HTML
        return "<html>Cochera: " + numero + "<br/><br/>Patente: " + patente + "<br/><br/>Hora ingreso: " + ingreso.toString() + "</html>"; //construye el texto con los datos
    }
    
    public int obteneraCobrar(Tarifa t){ //calcula el monto a cobrar según el tiempo estacionado
        int diferenciaMinutos = egreso.diferenciaMinutos(ingreso); //calcula los minutos transcurridos entre ingreso y egreso
        int aCobrar = 0; //inicializa el monto a cobrar en 0
        
        if(egreso!=null){ //verifica que el vehículo ya haya egresado
            if(diferenciaMinutos<=15){
            aCobrar = t.obtenerT15(); //asigna el monto del tramo de 15 minutos
            }
            else{
                if(diferenciaMinutos<=30){
                    aCobrar = t.obtenerT30(); //asigna el monto del tramo de 30 minutos
                }
                else{
                    if(diferenciaMinutos<=60){
                        aCobrar = t.obtenerT60(); //asigna el monto del tramo de 60 minutos
                    }
                    else{
                        aCobrar = t.obtenerTFija(); //asigna el monto fijo para estadías largas
                    }
                }
            }
        }
        
        return aCobrar; //devuelve el monto calculado
    }
}