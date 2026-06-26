class Hora{ //clase que representa una hora del día (horas y minutos)
    //atributos de instancia
    private int hor; //hora (entre 7 y 22)
    private int min; //minutos (entre 0 y 59)
    
    //constructor
    //hor, es un entero entre 7 y 22; 
    //min, es un entero entre 0 y 59. 
    //La clase cliente es responsable de garantizar que 
    //los parámetros del constructor estén dentro del rango especificado.

    public Hora(int h, int m){ //crea una Hora con los valores indicados
        hor = h; //asigna la hora
        min = m; //asigna los minutos
    }
    
    //comandos
    public void establecerHora(int c){ //modifica el valor de la hora
        hor = c; //asigna el nuevo valor de hora
    }

    public void establecerMinutos(int c){ //modifica el valor de los minutos
        min = c; //asigna el nuevo valor de minutos
    }

    //consultas
    public int obtenerHora(){ //devuelve la hora
        return hor; //retorna el atributo hor
    }
    
    public int obtenerMinutos(){ //devuelve los minutos
        return min; //retorna el atributo min
    }
    
    public int diferenciaMinutos(Hora c){ //calcula la diferencia en minutos contra otra Hora
        return Math.abs((hor*60 + min) - 
        (c.obtenerHora()*60 + c.obtenerMinutos())); //resta los minutos totales de ambas horas y toma el valor absoluto
    }
    
    public String toString(){ //representa la hora como texto
    
        return hor + ":" + min; //devuelve el formato "h:m"
    }

}