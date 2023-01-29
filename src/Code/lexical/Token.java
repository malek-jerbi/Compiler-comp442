package Code.lexical;
import Code.lexical.enums.TokenType;

public class Token {
    private TokenType type;
    /**
     * Line
     */
    private int location;
    private String lexeme;

    public Token(TokenType type, int location, String lexeme) {
        this.type = type;
        this.location = location;
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public int getLocation() {
        return location;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return "[" + type + ", " + lexeme + ", " + location + "]";
    }
}
