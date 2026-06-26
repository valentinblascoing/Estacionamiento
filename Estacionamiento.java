public class Estacionamiento{ //clase que administra el conjunto de cocheras del estacionamiento
    
    //Atributos de instancia
    private Vehiculo[] vehiculos; //arreglo de vehículos, una posición por cochera
    
    //Constructor
    public Estacionamiento(int cantCocheras){ //crea el estacionamiento con la cantidad de cocheras indicada
        vehiculos = new Vehiculo[cantCocheras]; //inicializa el arreglo de cocheras, todas vacías (null)
    }
    
    //Consultas
    
    public Vehiculo obtenerVehiculo(int cochera){ //devuelve el vehículo de la cochera indicada
        return vehiculos[cochera-1]; //accede a la posición del arreglo (cochera 1-indexada)
    }
    
    public String consultarVehiculo(int cochera){ //devuelve el texto con los datos del vehículo de esa cochera
        return vehiculos[cochera-1].toString(); //llama al toString() del vehículo
    }
    
    public int cantCocheras(){ //devuelve la cantidad total de cocheras
        return vehiculos.length; //devuelve el tamaño del arreglo
    }  
    
    //comandos
    public boolean ingresarVehiculo(Vehiculo v, int cochera){ //ingresa un vehículo a la cochera indicada
        boolean pudo = false; //asume que no se pudo ingresar
        if(vehiculos[cochera-1]==null){ //verifica que la cochera esté libre
            vehiculos[cochera-1] = v; //asigna el vehículo a la cochera
            pudo = true; //marca que el ingreso fue exitoso
        }
        return pudo; //devuelve si se pudo ingresar o no
    }
    
    public boolean egresarVehiculo(int cochera){ //libera la cochera indicada
        boolean pudo = false; //asume que no se pudo egresar
        if(vehiculos[cochera-1]!=null){ //verifica que la cochera tenga un vehículo
            vehiculos[cochera-1] = null; //libera la cochera
            pudo = true; //marca que el egreso fue exitoso
        }
        return pudo; //devuelve si se pudo egresar o no
    }
}


