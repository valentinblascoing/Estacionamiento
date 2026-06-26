class Vehiculo{
    //atributos de instancia
    private Hora ingreso;
    private Hora egreso;
    private int numero;
    private String patente;

    //constructor
    public Vehiculo (Hora i, int n, String p){
        ingreso = i;
        egreso = null;
        numero = n;
        patente = p;
    }
    
    //comandos
    public void egresaVehiculo(Hora c){
        egreso = c;
    }
    
    //consultas
    public Hora obtenerIngreso(){
        return ingreso;
    }
    
    public Hora obtenerEgreso(){
        return egreso;
    }
    
    public int obtenerNumero(){
        return numero;
    }
    
    public String obtenerPatente(){
        return patente;
    }
    
    public String toString(){
        return "<html>Cochera: " + numero + "<br/><br/>Patente: " + patente + "<br/><br/>Hora ingreso: " + ingreso.toString() + "</html>";
    }
    
    public int obteneraCobrar(Tarifa t){
        int diferenciaMinutos = egreso.diferenciaMinutos(ingreso);
        int aCobrar = 0;
        
        if(egreso!=null){
            if(diferenciaMinutos<=15){
            aCobrar = t.obtenerT15();
            }
            else{
                if(diferenciaMinutos<=30){
                    aCobrar = t.obtenerT30();
                }
                else{
                    if(diferenciaMinutos<=60){
                        aCobrar = t.obtenerT60();
                    }
                    else{
                        aCobrar = t.obtenerTFija();
                    }
                }
            }
        }
        
        return aCobrar;
    }
}
