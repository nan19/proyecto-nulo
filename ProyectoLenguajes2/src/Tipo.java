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

enum TipoB{
    ENTERO,
    REAL,
    BOOLEANO
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

