                                                               
public class Estacionamiento{
    
    //Atributos de instancia
    private Vehiculo[] vehiculos;
    
    //Constructor
    public Estacionamiento(int cantCocheras){
        vehiculos = new Vehiculo[cantCocheras];
    }
    
    //Consultas
    
    public Vehiculo obtenerVehiculo(int cochera){
        return vehiculos[cochera-1];
    }
    
    public String consultarVehiculo(int cochera){
        return vehiculos[cochera-1].toString();
    }
    
    public int cantCocheras(){
        return vehiculos.length;
    }  
    
    //comandos
    public boolean ingresarVehiculo(Vehiculo v, int cochera){
        boolean pudo = false;
        if(vehiculos[cochera-1]==null){
            vehiculos[cochera-1] = v;
            pudo = true;
        }
        return pudo;
    }
    
    public boolean egresarVehiculo(int cochera){
        boolean pudo = false;
        if(vehiculos[cochera-1]!=null){
            vehiculos[cochera-1] = null;
            pudo = true;
        }
        return pudo;
    }
}
