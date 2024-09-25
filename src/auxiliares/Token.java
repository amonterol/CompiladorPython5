/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auxiliares;

/**
 *
 * @author abmon
 */
public class Token {

    private String lexema; //private TokenType type;
    private TipoDeToken tipoDeToken;
    private String literal; //private Object literal;
    private int numeroLinea;

    public Token() {
    }

    public Token(String lexema, TipoDeToken tipo, String literal, int linea) {
        this.lexema = lexema;
        this.tipoDeToken = tipo;
        this.literal = literal;
        this.numeroLinea = linea;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public TipoDeToken getTipoDeToken() {
        return tipoDeToken;
    }

    public void setTipoDeToken(TipoDeToken tipoDeToken) {
        this.tipoDeToken = tipoDeToken;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    @Override
    public String toString() {
        return "Token{" + "lexema=" + lexema + ", tipoDeToken=" + tipoDeToken + ", literal=" + literal + ", numeroLinea=" + numeroLinea + '}';
    }

 
}
