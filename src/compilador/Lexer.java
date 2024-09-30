/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import auxiliares.Token;
import auxiliares.Error;
import auxiliares.PalabraReservada;
import auxiliares.TipoDeToken;
import java.util.Arrays;

/**
 *
 * @author abmon
 */
public class Lexer {

    private static List<String> programaEnPythonOriginal; //Código que se analiza en forma de lista
    private static List<Token> listaDeTokens; //Almacena los tokens que se identifican
    private static List<String> programaEnPythonRevisado; //Almacena los tokens que se identifican
    private static Error erroresEnProgamaEnPythonOriginal;
    private int cantidadComentarios; // almacena la cantidad de comentarios en el código
    private int numeroLineaActual;   //Almacena el número de linea que se está analizando
    private int lineaActual; // Almancena en número de la línea que se esta leyento actualmente
    private int indiceCaracterActual;   //Almacena la posición del cáracter que el analizador esta leyendo actualmente     

    public Lexer(List<String> content) {
        this.programaEnPythonOriginal = content;
        this.listaDeTokens = new ArrayList<>();
        this.programaEnPythonRevisado = new ArrayList<>();
        this.erroresEnProgamaEnPythonOriginal = new Error();
        this.cantidadComentarios = 0;
        this.numeroLineaActual = 0;// Almancena en número de la línea que se esta leyento actualmente
        this.indiceCaracterActual = 0;   //Almacena el cáracter que el analizador esta leyendo actualmente   
    }

    public void analizadorLexico(List<String> contenido) throws IOException {

        // System.out.println("\n3 LEXER > INICIO LINEA DE CODIGO CONVERTIDA A CARACTERES  " + lineaDeCodigoActual);
        char caracterActual = ' '; //Almacena el caracter que actualmente se lee en la línea de código
        char caracterSiguiente = ' '; //Almacena el caracter siguiente al que actualmente se lee en la línea de código

        boolean posibleIdentificador = false;
        boolean posibleNumero = false;

        String identificador = "";
        String string = "";
        String comentario = "";
        String numero = ""; //Almacena transitoriamente un cadena constituido por digitos

        listaDeTokens = new ArrayList<>();

        //Itera sobre cada línea del archivo con el código fuente en Python
        //for (String lineaDeCodigoActual : programaEnPythonOriginal) {
        for (lineaActual = 0; lineaActual < programaEnPythonOriginal.size(); lineaActual++) {
            String contenidoDeLaLineaActual = programaEnPythonOriginal.get(lineaActual).trim();
            //Verifica si la linea esta en blanco
            if (contenidoDeLaLineaActual.isBlank() || contenidoDeLaLineaActual.isEmpty()) {
                numeroLineaActual++;
                continue;
            }

            //Verifica si la linea es un comentario o si hay comentarios al final de una linea de código
            if (existeComentario(contenidoDeLaLineaActual)) {
                cantidadComentarios++;
                contenidoDeLaLineaActual = contenidoDeLaLineaActual.split("#")[0].trim(); // Elimina la parte del comentario en la línea
            }

            //Agrega la linea que actualmente se analiza al archivo de salida 
            registrarLineaAnalizadaEnProgramaEnPythonRevisado(contenidoDeLaLineaActual, numeroLineaActual);

            //Convierte cada línea de código en un arreglo de caracteres para recorrer caracter por caracter e identificar su tipo
            char[] arregloCaracteres = contenidoDeLaLineaActual.toCharArray();

            //Recorre la línea de código actuas
            //for (int i = 0; i < arregloCaracteres.length; ++i) {
            //for (indiceCaracterActual = 0; indiceCaracterActual < arregloCaracteres.length; ++indiceCaracterActual) {
            for (indiceCaracterActual = 0; indiceCaracterActual < contenidoDeLaLineaActual.length(); ++indiceCaracterActual) {
                boolean existenComillas = false;

                caracterActual = contenidoDeLaLineaActual.charAt(indiceCaracterActual);
                System.out.println(indiceCaracterActual + " EL CARACTER ACTUAL ES: " + caracterActual);
                switch (caracterActual) {

                    // Se ignoran los caracteres en blanco
                    case ' ':
                    case '\r':
                    case '\t':
                        System.out.println("5 caracter en blanco BORRAR " + string.trim() + "  "
                                + caracterActual + " posicion " + indiceCaracterActual + " size " + arregloCaracteres.length);
                        break;

                    //Identifica los operadores aritméticos    
                    case '+':
                        agregarNuevoToken(null, TipoDeToken.SUMA, String.valueOf(caracterActual), numeroLineaActual);
                        break;
                    case '-':
                        agregarNuevoToken(null, TipoDeToken.RESTA, String.valueOf(caracterActual), numeroLineaActual);
                        break;
                    case '*':
                        if (verificarCaracterSiguiente('*')) {
                            agregarNuevoToken(null, TipoDeToken.POTENCIA, String.valueOf(caracterActual), numeroLineaActual);
                        } else {
                            agregarNuevoToken(null, TipoDeToken.MULTIPLICACION, String.valueOf(caracterActual), numeroLineaActual);
                        }
                        break;
                    case '/':
                        if (verificarCaracterSiguiente('/')) {
                            agregarNuevoToken(null, TipoDeToken.DIVISION_ENTERA, String.valueOf(caracterActual), numeroLineaActual);
                        } else {
                            agregarNuevoToken(null, TipoDeToken.DIVISION, String.valueOf(caracterActual), numeroLineaActual);
                        }
                        break;
                    case '%':
                        agregarNuevoToken(null, TipoDeToken.MODULO, String.valueOf(caracterActual), numeroLineaActual);
                        break;

                    //Identifica los operadores relacionales
                    case '=':
                        if (verificarCaracterSiguiente('=')) {
                            agregarNuevoToken(null, TipoDeToken.IGUAL_QUE, String.valueOf(caracterActual), numeroLineaActual);
                        } else {
                            agregarNuevoToken(null, TipoDeToken.ASIGNACION, String.valueOf(caracterActual), numeroLineaActual);
                        }
                        break;
                    case '!':
                        if (verificarCaracterSiguiente('=')) {
                            agregarNuevoToken(null, TipoDeToken.DIFERENTE_QUE, String.valueOf(caracterActual), numeroLineaActual);
                        } else {
                            //Caracter no contemplado en Python
                        }
                        break;
                    case '>':
                        if (verificarCaracterSiguiente('=')) {
                            agregarNuevoToken(null, TipoDeToken.MAYOR_O_IGUAL_QUE, String.valueOf(caracterActual), numeroLineaActual);
                        } else {
                            agregarNuevoToken(null, TipoDeToken.MAYOR_QUE, String.valueOf(caracterActual), numeroLineaActual);
                        }
                        break;
                    case '<':
                        if (verificarCaracterSiguiente('=')) {
                            agregarNuevoToken(null, TipoDeToken.MENOR_O_IGUAL_QUE, String.valueOf(caracterActual), numeroLineaActual);
                        } else {
                            agregarNuevoToken(null, TipoDeToken.MENOR_QUE, String.valueOf(caracterActual), numeroLineaActual);
                        }
                        break;

                    //Identifica los operadores dos puntos ->  de sublista, subcadena o subarreglos y tipo de retorno de una función    
                    case ':':
                        agregarNuevoToken(null, TipoDeToken.DOS_PUNTOS, String.valueOf(caracterActual), numeroLineaActual);
                        break;

                    //Identifica los operadores de agrupación
                    case '(':
                        agregarNuevoToken(null, TipoDeToken.PARENTESIS_IZQUIERDO, String.valueOf(caracterActual), numeroLineaActual);
                        break;
                    case ')':
                        agregarNuevoToken(null, TipoDeToken.PARENTESIS_DERECHO, String.valueOf(caracterActual), numeroLineaActual);
                        break;
                    case '[':
                        agregarNuevoToken(null, TipoDeToken.CORCHETE_IZQUIERDO, String.valueOf(caracterActual), numeroLineaActual);
                        break;
                    case ']':
                        agregarNuevoToken(null, TipoDeToken.CORCHETE_DERECHO, String.valueOf(caracterActual), numeroLineaActual);
                        break;

                    //Identifica los operadores de string 
                    case '"':
                        
                        System.out.println("\n\n<<< 175 Lexico> ENCONTRAMOS COMILLAS>>> " + String.valueOf(caracterActual));
                        agregarNuevoToken(null, TipoDeToken.COMILLAS, String.valueOf(caracterActual), numeroLineaActual);

                        //Recolentamos el texto entre comillas
                        String textoDelString = new String();
                        textoDelString = leyendoUnString(contenidoDeLaLineaActual.substring(indiceCaracterActual + 1)); //Evitamos las comillas
                        if(textoDelString.length() !=0){
                              agregarNuevoToken(null, TipoDeToken.TEXTO_ENTRE_COMILLAS, textoDelString, numeroLineaActual);
                        }
                      


                        break;

                    // Identifica números enteros, decimales, identificadores y palabras reservadas    
                    default: {

                        PalabraReservada palabraReservada = new PalabraReservada();

                       
                        char[] caracteres = {' ', '=', '(', ')', '[', ']', '+', '-', '*', '/', '%', '<', '>'};

                        string = string + caracterActual;

                        if (i < arregloCaracteres.length - 1) {
                            caracterSiguiente = arregloCaracteres[i + 1];

                            while ((i < arregloCaracteres.length - 1)) {
                                boolean encontrado = false;
                                for (char ch : caracteres) {
                                    if (caracterSiguiente == ch) {
                                        encontrado = true;
                                        break;
                                    }
                                }

                                if (encontrado) {
                                    break;
                                } else {
                                    ++i;
                                    caracterActual = arregloCaracteres[i];
                                    string = string + caracterActual;
                                    if (i < (arregloCaracteres.length - 1)) {
                                        caracterSiguiente = arregloCaracteres[i + 1];
                                    } // fin if
                                }

                            } // fin while
                        } // fin if

                         
                        
                        /*
                        if (esCaracterAlfabetico(caracterActual)) {
                            String identifier = leyendoCadenaDeCaracteres(arregloCaracteres);
                            if (palabraReservada.esPalabraReservada(identifier)) {
                                agregarNuevoToken(string, TipoDeToken.PALABRA_RESERVADA, identifier.trim(), this.numeroLineaActual);
                            } else {
                                agregarNuevoToken(string, TipoDeToken.IDENTIFICADOR, identifier.trim(), this.numeroLineaActual);
                            }
                        } else if (caracterActual == '"') {
                            //agregarNuevoToken(string, TipoDeToken.TEXTO_ENTRE_COMILLAS, leyendoUnString(arregloCaracteres), this.numeroLineaActual);
                        } else {
                            // Manejar otros caracteres
                        }

                        */
                        System.out.println(" 273 Este es el TOKEN  BORRAR:   " + string.trim() + " en linea " + numeroLineaActual
                                + "  valor de i " + indiceCaracterActual + "\n");

                        /*
                        if (esNumeroEntero(string.trim())) {
                            agregarNuevoToken(string, TipoDeToken.NUMERO_ENTERO, string.trim(), this.numeroLineaActual);
                        } else if (esNumeroDecimal(string.trim())) {
                            agregarNuevoToken(string, TipoDeToken.NUMERO_DECIMAL, string.trim(), this.numeroLineaActual);
                        } else if (palabraReservada.esPalabraReservada(string.trim())) {
                            agregarNuevoToken(string, TipoDeToken.PALABRA_RESERVADA, string.trim(), this.numeroLineaActual);
                        } else if (verificarPrimerCaracterDeUnIdentificador(string.trim(), numeroLineaActual)
                                && verificarSecuenciaDeCaracteresDeUnIdentificador(string.trim(), numeroLineaActual)) {
                            agregarNuevoToken(string, TipoDeToken.IDENTIFICADOR, string.trim(), this.numeroLineaActual);
                        } else {
                            agregarNuevoToken(string, TipoDeToken.DESCONOCIDO, string.trim(), this.numeroLineaActual);
                        }

                         */
                        break;

                    }//fin default
                    } //Fin switch

            } //Fin del for
            numeroLineaActual++; //Aumenta con cada linea que es analizada
        }

        System.out.println("\n\n<<< 124 Lexico> CANTIDAD DE TOKENS EN EL LEXICO>>> " + listaDeTokens.size());
        System.out.println("\n\n<<<CANTIDAD DE TOKENS>>> " + listaDeTokens.size());
        listaDeTokens.forEach((item) -> {
            System.out.println(item.getLexema() + " " + item.getTipoDeToken() + " " + item.getLiteral() + " " + item.getNumeroLinea());
        });

        System.out.println("\n\n<<< 124 Lexico> LISTA DE TOKENS REVISADO>>> " + programaEnPythonRevisado.size());
        programaEnPythonRevisado.forEach((item) -> {
            System.out.println(item);
        });

        System.out.println("\n\n<<<NUMERO DE COMENTARIOS>>> " + cantidadComentarios);
    }//Fin metodo analizadorLexico

    public List<Token> getListaDeTokens() {
        return listaDeTokens;
    }

    public int getCantidadDeComentarios() {
        return cantidadComentarios;
    }

    //FUNCIONES AUXILIARES
    public static void agregarNuevoToken(String nombreToken, TipoDeToken tipoDeToken, String valor, int numeroLinea) {
        Token nuevoToken = new Token(nombreToken, tipoDeToken, valor, numeroLinea);

        listaDeTokens.add(nuevoToken);
    }

    public static void registrarLineaCodigoAnalizado() {

    }

    public static void registrarError() {

    }

    //Verifica si la linea de codigo que se esta leyendo contiene un comentario
    public boolean existeComentario(String lineaActual) {
        Character caracterDeComentario = '#';

        System.out.println(" Existe el caracter de comentarios " + lineaActual.contains(String.valueOf(caracterDeComentario)));
        return lineaActual.contains(String.valueOf(caracterDeComentario));

    }

    public static boolean esFinalLinea(char[] arregloCaracteres, int contador) {
        return contador >= arregloCaracteres.length;
    }

    private static boolean esCaracterAlfabetico(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || c == '_';
    }

    private static boolean esCaracterNumerico(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean esCaracterAlfaNumerico(char c) {
        return esCaracterAlfabetico(c) || esCaracterNumerico(c);
    }

    //Valida si el token corresponde a un identificador valido
    public static boolean verificarPrimerCaracterDeUnIdentificador(String string, int numero) {

        if (string == null || string.isEmpty()) {
            return false;
        }

        if (string.matches("^[a-zA-Z_].*")) {
            return true;
        } else {
            System.out.println("191 Borrar " + Error.obtenerDescripcionDeError(200));
            registrarMensajeDeErrorEnProgramaEnPythonRevisado(200, numero);
            return false;
        }

    }

    //Verifica que el identificador sea una secuencia de letras y
    //numeros sin caracteres especiales a partir del segundo caracter
    public boolean verificarSecuenciaDeCaracteresDeUnIdentificador(String string, int numero) {
        // Verificar los caracteres restantes
        if (string == null || string.isEmpty()) {
            return false;
        }

        if (string.matches("[a-zA-Z0-9_]*$")) {
            return true;
        } else {
            System.out.println("201 Borrar " + Error.obtenerDescripcionDeError(201));
            registrarMensajeDeErrorEnProgramaEnPythonRevisado(201, numero);
            return false;
        }

    }

    //Valida si el token corresponde a una palabra reservada
    public static boolean esNumeroEntero(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Integer.valueOf(str);
            return true; // El token es un número entero
        } catch (NumberFormatException e) {
            return false; // El token no es entero 
        }
    }

    //Valida si el token corresponde a un numero decimal o entero
    public static boolean esNumeroDecimal(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Double.valueOf(str);
            return true; // El token es un número decimal
        } catch (NumberFormatException e) {
            return false; // El token no es un número decimal
        }
    }

    private boolean verificarCaracterSiguiente(char caracterSiguiente) {
        if (indiceCaracterActual >= programaEnPythonOriginal.get(numeroLineaActual).length()
                || programaEnPythonOriginal.get(numeroLineaActual).charAt(indiceCaracterActual) != caracterSiguiente) {
            return false; // El caracter siguiente no es el esperado
        }
        indiceCaracterActual++;
        return true;
    }

    private String leyendoCadenaDeCaracteres(char[] cadena) {
        int inicioCadena = indiceCaracterActual;

        while (indiceCaracterActual < cadena.length && esCaracterAlfaNumerico(cadena[indiceCaracterActual])) {
            indiceCaracterActual++;
        }

        String string = new String(cadena, inicioCadena, indiceCaracterActual - inicioCadena);
        indiceCaracterActual--;
        return string;
    }

    private String leyendoUnString(String string) {
        System.out.println("Entramos a metodo leyendoUnString");
        System.out.println(string);
        int inicioString = 0; 
        System.out.println(inicioString);
        while (inicioString < string.length() && string.charAt(inicioString) != '"' && string.charAt(inicioString) != ')' ) {
            System.out.println("En el while el indice de caracter actual es: " + inicioString +  " "+ string.charAt(inicioString));
            inicioString++;
        }
        System.out.println("El indice de caracter actual es: " + indiceCaracterActual);
        String str = string.substring(0, inicioString); // Saltar la comilla final
        indiceCaracterActual = indiceCaracterActual + str.length();
        System.out.print("Salimos de metodo leyendoUnString:  ");
        System.out.println(str);
        return str;
    }

    public void imprimirListas(List<String> contenidoArchivo) {
        /*
        Iterator iter = lista.iterator();

        while (iter.hasNext()) {
            System.out.print(iter.next());
        }
         */
        for (String linea : contenidoArchivo) {
            System.out.println(linea);
        }
    }

    public static void registrarLineaAnalizadaEnProgramaEnPythonRevisado(String instruccion, int numeroDeLinea) {
        String formatoLinea1 = String.format("%05d", numeroDeLinea);
        programaEnPythonRevisado.add(formatoLinea1 + " " + instruccion);
    }

    public static void registrarMensajeDeErrorEnProgramaEnPythonRevisado(int numeroDeError, int numeroDeLinea) {
        programaEnPythonRevisado.add(String.format("%14s", "Error ") + numeroDeError
                + ". " + Error.obtenerDescripcionDeError(numeroDeError)
                + String.format("[%s%d]", "Linea ", numeroDeLinea));
    }
}
