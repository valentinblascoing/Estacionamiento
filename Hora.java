class Hora{
    //atributos de instancia
    private int hor;
    private int min;
    
    //constructor
    //hor, es un entero entre 7 y 22; 
    //min, es un entero entre 0 y 59. 
    //La clase cliente es responsable de garantizar que 
    //los parámetros del constructor estén dentro del rango especificado.

    public Hora(int h, int m){
        hor = h;
        min = m;
    }
    
    //comandos
    public void establecerHora(int c){
        hor = c;
    }

    public void establecerMinutos(int c){
        min = c;
    }

    //consultas
    public int obtenerHora(){
        return hor;
    }
    
    public int obtenerMinutos(){
        return min;
    }
    
    public int diferenciaMinutos(Hora c){
        return Math.abs((hor*60 + min) - 
        (c.obtenerHora()*60 + c.obtenerMinutos()));
    }
    
    public String toString(){
    
        return hor + ":" + min;
    }

}