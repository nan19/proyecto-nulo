/*
 * Tipo.java
 *
 * Created on April 6, 2008, 4:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author jamil
 */
public abstract class Tipo {
    
    
}
/*
class Arreglo extends Tipo{
    private int size;
    private Tipo tipo;
    
    public Arreglo(int n, Tipo t){
        this.size = n;
        this.tipo = t;
    }
}
*/
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

