import java.io.*;
import java.util.*;

/**
 * Universidad Simon Bolivar
 * Lenguajes de Programacion II
 * Entrega final 7/04/2008
 * 
 * Maria Sol Ferrer 04-36975
 * Jamil Navarro 04-37334
 *
 * Proyecto: Procesador de lenguaje imperativo. 
 * Instruccion.java: clase Instruccion.
 * 
 */



/**
 * Clase abstracta que sirve de Superclase a todas las clases que representan
 * instrucciones del Lenguaje
 */
public abstract class Inst {
    /**
	* Metodo desarrollado por conveniencia. Imprime la representacion en
	* <b>String</b> del objeto.
	*/
    public abstract String imprimir(int i);    
    public abstract boolean esCorrecta(Bloque c, Informacion info, int linea);
    public abstract String toCode(String start, String next, int registro);
    public abstract void setShift(int acum);
    /*private int registro;

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }
     */
}

/**
 * Clase que representa una Declaracion de variable
 */
class Decl extends Inst {
        
    //Nombre del tipo de la variable declarada
    private Tipo Tipo;    
    //Nombre de la variable declarada
    private String Var;
    
    /**
	* @param t Tipo de la variable declarada
	* @param v Nombre de la variable declarada
	*/
    public Decl(Tipo t, String v) {
        this.Tipo = t;
        this.Var = v;
    }
	public String imprimir(int i) {
        return "";
    }	
    public String toString() {
        return Tipo +" "+ Var+";";
    }
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {  		
		if( (c.getTS()).isDefinedLocally(this.Var) && 
				((((c.getTS()).getLocally(this.Var)).getStatus()) == 0) ) {
			System.out.println("ERROR (linea "+linea+") Variable '"+this.Var
				+"' ya esta definida.");			
			info.setStatus(1);
			//c.getTS().add(this.Var ,info);
			return false;
		} else 
			return true;
    }
    public void setParent(Bloque c) {
    }

    @Override
    public String toCode(String start, String next, int registro) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return "";//start+":\n#Declaracion no ha sido implementada todavia\n";
    }

    
    @Override
    public void setShift(int acum) {
        return;
    }
}

/**
 * Clase que representa una instruccion de asignacion
 */
class InstAsig extends Inst {
    
    //Nombre de la variable a la que se realiza la asignacion
    private LValue Variable;    
    //Expresion que se le asigna a la variable
    private Expresion E;
    
    /**
	* Constructor de una Instruccion de Asignacion
	* @param v identificador de la variable a ser asignada
	* @param e expresion a asignar a la variable
	*/
    public InstAsig (LValue v, Expresion e) {
        this.Variable = v;
        this.E = e;
    }
    public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Asignacion\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + Variable + "\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + E.imprimir(i+1);
		return s;
    }
    public String toString(){
        return Variable + " := " + E.toString() + ";";
    }		
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {
       	boolean ok = true;
		//Chequeo de variable definida.
		String var = this.Variable.obtenerId();
        if(!c.estaDefinida(var)){
			System.out.println("ERROR (linea "+linea+") Variable '"+var +"' no ha sido definida.");
			info.setTipo(TipoF.NODEF);
			c.getTS().add(var,info);
            ok = false;
        }
		Tipo tipoE = this.E.getTipo(c);		
		//System.out.println("Inst.java: tipo expr "+tipoE);
		Tipo tipoV = c.getTS().get(var).tipo;
		//chequeo de la expresion.
		if (tipoE.equals(TipoF.ERROR)) {
			System.out.println("ERROR (linea "+linea+") Error de tipo en la expresion "+this.E+".");
			ok = false;
		}
		//chequeo de compatibilidad entre la expresion y la variable
		Class c1 = tipoE.getClass();
		Class c2 = tipoV.getClass();
		if (!c1.equals(c2)) {
			//System.out.println("Inst.java 1: "+tipoE);
			System.out.println("1ERROR (linea "+linea+") La Asignacion a la variable '"+this.Variable+
						"' no es posible. Los tipos no son compatibles.");
			ok = ok && false;
		} else {
			if (!(tipoE.comparar(tipoV))) {
				//System.out.println("Inst.java 2 E: "+tipoE);
				//System.out.println("Inst.java 2 V: "+tipoV);
				System.out.println("2ERROR (linea "+linea+") La Asignacion a la variable '"+this.Variable+
						"' no es posible. Los tipos no son compatibles.");
				ok = ok && false;
			}
		}		
		return ok;  	
    }

    @Override
    public String toCode(String start, String next, int registro) {
        
        int sh = this.Variable.getValue().getShift();
        String auxy = Misc.newLabel();
        String auxn = Misc.newLabel();
        String reg = Misc.getRegister(registro);
        String code = "#Comienzo Asignacion("+start+","+next+")\n";
        code += start+": "+this.E.toCode(auxy,auxn,registro);
        code += auxy+": mv "+reg+" 1\n";
        code += "mv (r0+"+sh+ ") "+reg+"\n";
        code += "goto "+next+"\n";
        code += auxn+": mv "+reg+" 0\n";
        code += "mv (r0+"+sh+") "+reg+"\n";
        code += "#fin asignacion("+start+","+next+")\n";
        return code;
    }
    @Override
    public void setShift(int acum) {
        return;
    }
    
    
}
/**
 * Clase que representa una instruccion condicional simple
 */
class InstIf extends Inst {
    
    //Condicion del la instruccion
    protected Expresion Condicion;    
    //Instrucciones a ejecutar si la condicion se cumple
    protected Bloque inst;
    
    /**
	* Constructor de una expresion condicional
	* @param e Expresion de la Condicion
	* @Param l Instrucciones a ejecutar si el condicional se cumple
	*/
    public InstIf (Expresion e, Bloque c) {
        this.Condicion = e;
        this.inst = c;
    }
    public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Condicional\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + Condicion.imprimir(i+1);		
		s+= inst.imprimir(i);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-"+inst.getTS();
		return s;
    }    
    public String toString(){
        return "si "+Condicion.toString()+"\n"+inst+"fins;";
    }
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {
        return this.Condicion.esCorrecta(c, 0);
    }

    @Override
    public String toCode(String start, String next, int registro) {
        //this.Condicion.setYes(Misc.newLabel());
        //this.Condicion.setNo(this.getNext());
        //this.inst.setNext(this.getNext());
        String aux = Misc.newLabel();
        return start+": "+this.Condicion.toCode(aux,next,registro)+aux+": "+this.inst.toCode(next);
    }
    
    @Override
    public void setShift(int acum) {
        this.inst.setShift(acum);
    }
}

/**
 * Clase que representa un condicional con instrucciones alternativas
 */
class InstIfElse extends InstIf {
    
    //Intrucciones a ejecutar si la Condicion no es cierta
    private Bloque instElse;
	
    /**
	* Constructor de la instruccion condicional con instrucciones alternativas
	* @param e Condicion
	* @param l1 instrucciones a ejecutar si la Condicion es cierta
	* @param l2 instrucciones a ejecutar si la condicion es falsa
	*/
    public InstIfElse (Expresion e, Bloque c1, Bloque c2) {
        super(e,c1);
        this.instElse = c2;
    }
     public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Condicional\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + Condicion.imprimir(i+1);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-Inst Si\n" + inst.imprimir(i+1);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-Inst Sino\n" + instElse.imprimir(i+1);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-"+instElse.getTS();
		return s;
    }	
    public String toString() {
        return super.toString().replaceFirst("fins","sino\n"+instElse+"fins");
    }

    @Override
    public String toCode(String start, String next, int registro) {
        //this.Condicion.setYes(Misc.newLabel());
        //this.Condicion.setNo(Misc.newLabel());
        //this.inst.setNext(this.getNext());
        String auxy = Misc.newLabel();
        String auxn = Misc.newLabel();
        String code = start+": "+this.Condicion.toCode(auxy,auxn,registro);
        code += auxy+": "+this.inst.toCode(next);
        code += "goto "+next+"\n";
        code += auxn+": "+this.instElse.toCode(next);
        return code;
    }
    
    @Override 
    public void setShift(int acum) {
        this.inst.setShift(acum);
        this.instElse.setShift(acum);
    }
}

/**
 * Clase que representa una instruccion iterativa
 */
class InstDo extends Inst {
    
    //Condicion de la Iteracion
    private Expresion Condicion;    
    //Cuerpo de la iteracion
    private Bloque inst;
    
    /**
	* Constructor de la Instruccion de Iteracion
	* @param e Condicion de la iteracion
	* @param l Cuerpo de la Iteracion
	*/
    public InstDo (Expresion e, Bloque c) {
        this.Condicion = e;
        this.inst = c;
    }
    public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Iteracion\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + Condicion.imprimir(i+1);		
		s+= inst.imprimir(i);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-"+inst.getTS();
		return s;
    }    
    public String toString() {
        return "Hacer " + Condicion +"\n"+inst+"finh;";
    }
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {
        return this.Condicion.esCorrecta(c, 0);
    }

    @Override
    public String toCode(String start, String next, int registro) {
        //this.Condicion.setYes(Misc.newLabel());
        //this.Condicion.setNo(this.getNext());
        String aux = Misc.newLabel();
        String code = start+": "+this.Condicion.toCode(aux,next,registro);
        code += aux+": "+this.inst.toCode(start);
        code += "goto "+start+"\n";
        return code;
    }
    
    @Override 
    public void setShift(int acum) {
        this.inst.setShift(acum);
    }
}
/**
 * Clase que representa una instruccion de llamada a un procedimiento
 */
class InstLlamada extends Inst {
    
    //Identificador del procedimiento
    private String id;    
    //Lista de parametros
    private LinkedList<Expresion> param;
    
    /**
	* Constructor de la Instruccion de Llamada a Procedimientos
	* @param id Identificador del procedimiento
	* @param param Lista de parametros
	*/
    public InstLlamada (String id, LinkedList<Expresion> param) {
        this.id = id;
        this.param = param;
    }
    public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Llamada a procedimiento\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + "Identificador\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "--" + id +"\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-Lista de Parametros\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+="--";
		s+="(";
		for (int j=0;j<param.size();j++) {
			s+= param.get(j) +", ";
		}		
		s+= ")\n";
		return s;
    }    
    public String toString() {
        return "Llamada a procedimiento" + id +"\n"+param+";";
    }
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {
        return true;
    }
	public String getId() {
		return this.id;
	}
	public LinkedList<Expresion> getParams() {
		return this.param;
	}

    @Override
    public String toCode(String start, String next, int registro) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return start+":\nLlamadas a procedimientos aun no implementadas\n";
    }
    
    @Override
    public void setShift(int acum) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return;
    }
    
}
