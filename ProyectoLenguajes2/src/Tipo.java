
import java.util.HashMap;

/**
 * Universidad Simon Bolivar
 * Lenguajes de Programacion II
 * Entrega final 7/04/2008
 * 
 * Maria Sol Ferrer 04-36975
 * Jamil Navarro 04-37334
 *
 * Proyecto: Procesador de lenguaje imperativo.
 * 
 * Tipo.java: clase Tipo
 * 
 */

/**
 *
 * @author jamil
 */
public abstract class Tipo {
    
    
}

class TBasico extends Tipo{
    private TipoB tipo;
    
    public TBasico(TipoB t){
        this.tipo = t;
    }
}

class TArreglo extends Tipo{
    private Tipo tipo;
    
    public TArreglo(Tipo t){
        this.tipo = t;
    }
}


class TRegistro extends Tipo{
    private TablaSim tabla;
    
    public TRegistro(TablaSim ts){
        this.tabla = ts;
    }
}

class TVariante extends Tipo{
    private TablaSim tabla;
    private String disc;
    private HashMap<Object,TablaSim> discr;
    
    public TVariante(TablaSim ts, String d, HashMap<Object,TablaSim> dr){
        this.tabla = ts;
        this.discr = dr;
        this.disc = d;
    }
    
}
enum TipoB{
    ENTERO,
    REAL,
    BOOLEANO,
    ERROR
}

enum TipoES{
    IN,
    OUT,
    IN_OUT
}

class TParam extends Tipo{
    private Tipo tipo;
    private TipoES es;
    
    public TParam(Tipo t, TipoES e){
        this.tipo = t;
        this.es = e;
    }
}

