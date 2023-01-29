package Code.lexical;
import Code.lexical.enums.TokenType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

public class LexicalAnalyzer {
    private PushbackReader input;
    private int lineNum;
    public LexicalAnalyzer(File inputFile) {
        try {
            input = new PushbackReader(new FileReader(inputFile));
            lineNum = 1;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + inputFile.getName());
            System.exit(1);
        }
    }


    public Token nextToken() {
        try {
            int currentChar = input.read();
            while (Character.isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    lineNum++;
                }
                currentChar = input.read();
            }
            if (currentChar == -1) {
                return new Token(TokenType.EOF, lineNum, "");
            }
            // handle digits and decimal numbers
            if (Character.isDigit(currentChar)) {
                StringBuilder lexeme = new StringBuilder();
                lexeme.append((char) currentChar);
                currentChar = input.read();
                while (Character.isDigit(currentChar) || currentChar == '.') {
                    lexeme.append((char) currentChar);
                    currentChar = input.read();
                }
                input.unread(currentChar);
                String lexemeString = lexeme.toString();
                if (lexemeString.contains(".")) {
                    return new Token(TokenType.FLOAT, lineNum, lexemeString);
                } else {
                    return new Token(TokenType.INTEGER, lineNum, lexemeString);
                }
            }
            // handle identifier
            if (Character.isLetter(currentChar)) {
                StringBuilder lexeme = new StringBuilder();
                lexeme.append((char) currentChar);
                currentChar = input.read();
                while (Character.isLetterOrDigit(currentChar)) {
                    lexeme.append((char) currentChar);
                    currentChar = input.read();
                }
                input.unread(currentChar);
                String lexemeString = lexeme.toString();
                switch (lexemeString) {
                    case "while":
                        return new Token(TokenType.WHILE, lineNum, lexemeString);
                    case "if":
                        return new Token(TokenType.IF, lineNum, lexemeString);
                    case "then":
                        return new Token(TokenType.THEN, lineNum, lexemeString);
                    case "else":
                        return new Token(TokenType.ELSE, lineNum, lexemeString);
                    case "function":
                        return new Token(TokenType.FUNCTION, lineNum, lexemeString);
                    case "constructor":
                        return new Token(TokenType.CONSTRUCTOR, lineNum, lexemeString);
                    case "void":
                        return new Token(TokenType.VOID, lineNum, lexemeString);
                    case "class":
                        return new Token(TokenType.CLASS, lineNum, lexemeString);
                    case "integer":
                        return new Token(TokenType.INTEGER, lineNum, lexemeString);
                    case "localvar":
                        return new Token(TokenType.LOCALVAR, lineNum, lexemeString);
                    case "float":
                        return new Token(TokenType.FLOAT, lineNum, lexemeString);
                    case "attribute":
                        return new Token(TokenType.ATTRIBUTE, lineNum, lexemeString);
                    case "self":
                        return new Token(TokenType.SELF, lineNum, lexemeString);
                    case "read":
                        return new Token(TokenType.READ, lineNum, lexemeString);
                    case "public":
                        return new Token(TokenType.PUBLIC, lineNum, lexemeString);
                    case "isa":
                        return new Token(TokenType.ISA, lineNum, lexemeString);
                    case "write":
                        return new Token(TokenType.WRITE, lineNum, lexemeString);
                    case "private":
                        return new Token(TokenType.PRIVATE, lineNum, lexemeString);
                    case "return":
                        return new Token(TokenType.RETURN, lineNum, lexemeString);
                    case "or":
                        return new Token(TokenType.OR, lineNum, lexemeString);
                    case "and":
                        return new Token(TokenType.AND, lineNum, lexemeString);
                    case "not":
                        return new Token(TokenType.NOT, lineNum, lexemeString);
                    default:
                        return new Token(TokenType.ID, lineNum, lexemeString);
                }

            }



// handle operators and punctuation
            switch (currentChar) {
                case '+':
                    return new Token(TokenType.PLUS, lineNum, "+");
                case '-':
                    return new Token(TokenType.MINUS, lineNum, "-");
                case '*':
                    return new Token(TokenType.STAR, lineNum, "*");
                case '/':
                    currentChar = input.read();
                    if (currentChar == '*') {
                        currentChar = input.read();
                        while (currentChar != -1 && !(currentChar == '*' && input.read() == '/')) {
                            currentChar = input.read();
                        }
                        return new Token(TokenType.MULTIPLELINECOMMENT, lineNum, "");
                    } else if (currentChar == '/') {
                        String comment = "";
                        currentChar = input.read();
                        while (currentChar != -1 && currentChar != '\n') {
                            comment += (char)currentChar;
                            currentChar = input.read();
                        }
                        return new Token(TokenType.ONELINECOMMENT, lineNum, comment);
                    } else {
                        input.unread(currentChar);
                        return new Token(TokenType.SLASH, lineNum, "/");
                    }
                case '(':
                    return new Token(TokenType.OPENPAR, lineNum, "(");
                case ')':
                    return new Token(TokenType.CLOSEDPAR, lineNum, ")");
                case '{':
                    return new Token(TokenType.OPENCURLYBRACE, lineNum, "{");
                case '}':
                    return new Token(TokenType.CLOSEDCURLYBRACE, lineNum, "}");
                case '[':
                    return new Token(TokenType.OPENBRACKET, lineNum, "[");
                case ']':
                    return new Token(TokenType.CLOSEDBRACKET, lineNum, "]");
                case ';':
                    return new Token(TokenType.SEMICOLON, lineNum, ";");
                case ':':
                    currentChar = input.read();
                    if (currentChar == ':') {
                        return new Token(TokenType.DOUBLECOLON, lineNum, "::");
                    } else {
                        input.unread(currentChar);
                        return new Token(TokenType.COLON, lineNum, ":");
                    }
                case '.':
                    return new Token(TokenType.DOT, lineNum, ".");
                case ',':
                    return new Token(TokenType.COMMA, lineNum, ",");
                case '=':
                    currentChar = input.read();
                    if (currentChar == '=') {
                        return new Token(TokenType.EQEQ, lineNum, "==");
                    } else if (currentChar == '>') {
                        return new Token(TokenType.ARROW, lineNum, "=>");
                    } else {
                        input.unread(currentChar);
                        return new Token(TokenType.EQ, lineNum, "=");
                    }

                case '>':
                    currentChar = input.read();
                    if (currentChar == '=') {
                        return new Token(TokenType.GREATEROREQUAL, lineNum, ">=");
                    } else {
                        input.unread(currentChar);
                        return new Token(TokenType.GREATER, lineNum, ">");
                    }
                case '<':
                    currentChar = input.read();
                    if (currentChar == '=') {
                        return new Token(TokenType.LESSEROREQUAL, lineNum, "<=");
                    } else if (currentChar == '>') {
                        return new Token(TokenType.NOTEQ, lineNum, "<>");
                    } else {
                        input.unread(currentChar);
                        return new Token(TokenType.LESSER, lineNum, "<");
                    }
                default:
                    return new Token(TokenType.UNKNOWN, lineNum, Character.toString((char) currentChar));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }

    }
}




