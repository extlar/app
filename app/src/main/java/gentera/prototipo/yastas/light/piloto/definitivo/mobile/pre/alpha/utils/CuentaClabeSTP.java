package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils;

import org.apache.commons.lang3.StringUtils;

public class CuentaClabeSTP {
	
	/**************************************************************************
	 *  ******* Constantes ***************************************************
	 **************************************************************************/
	
	
	private static  String S_VACIO = "";
	private static final String S_NUM_0 = "0";
	private static final Integer NUM_1 = 1;
	private static final Integer NUM_3 = 3;
	private static final Integer NUM_7 = 7;
	private static final Integer NUM_10 = 10;
	private static final Integer MODULO = 10; 
	private static final String STP = "646";
	private static final String PLAZA = "180";
	private static final String PREFIJO_CLIENTE = "1022"; 
	
	
	private static final Integer LONGITUD_CUENTA = 17;
	private static final Integer[] PONDERACION  = new Integer[]{NUM_3,NUM_7,NUM_1,
														   NUM_3,NUM_7,NUM_1,
														   NUM_3,NUM_7,NUM_1,
														   NUM_3,NUM_7,NUM_1,
														   NUM_3,NUM_7,NUM_1,
														   NUM_3,NUM_7};
	
	
	/**************************************************************************
	 ********** Metodos ***************************************************
	 **************************************************************************/
	
	/**
	 * A partir del idCob, regresa la cuenta clabe STP
	 * @param cuenta Cuenta a 17 digitos
	 * @return idCob Comisionista ID
	 * @throws Exception
	 */
	public static String generarCuentaClabeSTP (String idCob) throws Exception{
		if (StringUtils.isEmpty(idCob)){
			throw new Exception ("IdCob nulo o vacio");
		}
		
		idCob = rellenaCerosIzquierda(idCob, NUM_7);
		
		StringBuilder sb = new StringBuilder (S_VACIO);
		sb.append(STP);
		sb.append(PLAZA);
		sb.append(PREFIJO_CLIENTE);
		sb.append(idCob);
		sb.append(generarDigitoVerificador(sb.toString()));
		return sb.toString();
	}
	
	/**
	 * Genera el codigo verificador de una cuenta clabe, segun la especificacion STP
	 * @param cuenta Cuenta de 17 digitos
	 * @return Digito verificador
	 * @throws Exception
	 */
	private static Integer generarDigitoVerificador (String cuenta) throws Exception{
		if (StringUtils.isEmpty(cuenta)){
			throw new Exception ("Cuenta nula o vacia");
		}
		cuenta = cuenta.trim();
		
		if (cuenta.length() != LONGITUD_CUENTA )
			throw new Exception ("Longitud de cuenta no valida.");
		
		//Algoritmo
		Integer [] cuentaClabe = convertToIntegerArray(cuenta);
		Integer [] multiplica =  multiplicaVector(cuentaClabe, PONDERACION);
		Integer [] modulo = moduloVector(multiplica, MODULO);
		Integer sumaMod = sumaVector(modulo);
		Integer A = sumaMod % MODULO;
		Integer B = NUM_10 - A;
		return B % MODULO;
		
	}
	
	/**
	 * Convierte una cadena a su respectivo arreglo de enteros
	 * @param cuenta cadena de enteros
	 * @return Arreglo de enteros
	 * @throws Exception Si la cadena no contiene puros caracteres numericos
	 */
	private static Integer[] convertToIntegerArray (String cuenta) throws Exception{
		Integer [] regreso = null;
		
		char [] desmembrar = cuenta.toCharArray();
		regreso = new Integer[desmembrar.length];
		try{
			for (int i = 0; i < regreso.length; i++){
				regreso[i] = Integer.parseInt(new String(new char[]{desmembrar[i]}));
			}
		}catch(NumberFormatException e){
			throw new Exception("La cuenta debe de tener solo caracteres numericos.");
		}
		return regreso;
	}
	
	/**
	 * Multiplicacion vecorial
	 * @param a Vector a
	 * @param b Vector b
	 * @return
	 */
	private static Integer[] multiplicaVector (Integer[] a , Integer[] b) throws Exception{
		if (a.length != b.length)
			throw new Exception("Las longitudes de los vectores no es igual.");
		
		Integer[] multiplica = new Integer[a.length];
		for (int i=0; i< b.length; i++){
			multiplica[i] = a[i] * b[i];
		}
		return multiplica;
	}
	
	/**
	 * Calcula el modulo de cada elemento del vector
	 * @param a vector
	 * @param modulo modulo
	 */
	private static Integer[] moduloVector (Integer[] a, int modulo){
		Integer[] regreso = new Integer[a.length];
		for (int i=0; i< regreso.length ; i++){
			regreso [i] = a[i] % modulo;
		}
		return regreso;
	}
	
	/**
	 * Suma todos los componentes del vecor
	 * @param a
	 * @return
	 */
	private static Integer sumaVector (Integer[] a){
		Integer regreso = 0;
		for (int i=0; i< a.length ; i++){
			regreso  += a[i];
		}
		return regreso;
	}
	
	/**
	 * Rellena la cadena con los ceros faltantes segun la longitud especifica
	 * @param idCob cadena
	 * @param len longitud final
	 * @return
	 * @throws Exception
	 */
	private static String rellenaCerosIzquierda (String idCob, int len) throws Exception{
		idCob = idCob.trim();
		if (idCob.length() > len){
			throw new Exception ("La longitud  del idCob no puede ser mayor a "+ len + ".");
		}
		
		int faltante = len - idCob.length();
		StringBuilder sb = new StringBuilder (S_VACIO);
		
		for (int i=0; i < faltante; i++){
			sb.append(S_NUM_0);
		}
		sb.append(idCob);
		return sb.toString();
	}
	
	
	public static void main (String [] args) throws Exception{
		String cuenta = "1 ";
		System.out.println (CuentaClabeSTP.generarCuentaClabeSTP(cuenta));
	}
	

}
