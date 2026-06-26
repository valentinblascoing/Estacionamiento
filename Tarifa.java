class Tarifa{ //clase que representa los montos de la tarifa por tramos de tiempo
    //atributos de instancia
    private int t15; //monto para estadías de hasta 15 minutos
    private int t30; //monto para estadías de hasta 30 minutos
    private int t60; //monto para estadías de hasta 60 minutos
    private int tFija; //monto fijo para estadías de más de 60 minutos

    //constructor
    public Tarifa(int t1, int t2, int t3, int tfija){ //crea la tarifa con los montos indicados
        t15 = t1; //asigna el monto del tramo de 15 minutos
        t30 = t2; //asigna el monto del tramo de 30 minutos
        t60 = t3; //asigna el monto del tramo de 60 minutos
        tFija = tfija; //asigna el monto fijo
    }
    
    //consultas
    public int obtenerT15(){ //devuelve el monto del tramo de 15 minutos
        return t15; //retorna el atributo t15
    }
    
    public int obtenerT30(){ //devuelve el monto del tramo de 30 minutos
        return t30; //retorna el atributo t30
    }
    
    public int obtenerT60(){ //devuelve el monto del tramo de 60 minutos
        return t60; //retorna el atributo t60
    }
    
    public int obtenerTFija(){ //devuelve el monto fijo
        return tFija; //retorna el atributo tFija
    }

}