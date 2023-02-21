package Code.parser;
import Code.lexical.LexicalAnalyzer;
import Code.lexical.Token;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TopDownParser {
    private Token lookahead;
    private LexicalAnalyzer lexicalAnalyzer;

    public TopDownParser() {
        File inputFile = new File("src/Code/example-bubblesort.src");
        lexicalAnalyzer = new LexicalAnalyzer(inputFile);
        lookahead = lexicalAnalyzer.nextToken();
    }

    public boolean parse() {
        return reptSTART0() && match("$");
    }

    private boolean reptSTART0() {
        switch (lookahead.getLexeme()) {
            case "class":
            case "function":
                if (classDeclOrFuncDef()) {
                    return reptSTART0();
                }
                return false;
            default:
                return true;
        }
    }

    private boolean classDeclOrFuncDef() {
        switch (lookahead.getLexeme()) {
            case "class":
                return classDecl();
            case "function":
                return funcDef();
            default:
                return false;
        }
    }

    private boolean classDecl() {
        if (match("class") && match("id") && optClassDecl2() && match("{")
                && reptClassDecl4() && match("}") && match(";")) {
            return true;
        }
        return false;
    }

    private boolean optClassDecl2() {
        switch (lookahead.getLexeme()) {
            case "isa": //first
                if (match("isa") && match("id") && reptOptClassDecl22()) {
                    return true;
                }
                return false;
            case "{": //follow
                return true;
            default:
                return false;
        }
    }

    private boolean reptOptClassDecl22() {
        switch (lookahead.getLexeme()) {
            case ",":
                if (match(",") && match("id") && reptOptClassDecl22()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean reptClassDecl4() {
        switch (lookahead.getLexeme()) {
            case "private":
            case "public":
                if (visibility() && memberDecl() && reptClassDecl4()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean visibility() {
        switch (lookahead.getLexeme()) {
            case "private":
            case "public":
                if (match(lookahead.getLexeme())) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean memberDecl() {
        switch (lookahead.getLexeme()) {
            case "attribute":
                return memberVarDecl();
            case "function":
                return memberFuncDecl();
            default:
                return false;
        }
    }

    private boolean memberVarDecl() {
        if (match("attribute") && match("id") && match(":") && match("type")
                && reptMemberVarDecl4() && match(";")) {
            return true;
        }
        return false;
    }

    private boolean reptMemberVarDecl4() {
        switch (lookahead.getLexeme()) {
            case "[":
                if (arraySize() && reptMemberVarDecl4()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean memberFuncDecl() {
        switch (lookahead.getLexeme()) {
            case "function":
                if (match("function") && match("id") && match(":")
                        && match("(") && fParams() && match(")") && arrow()
                        && returnType() && match(";")) {
                    return true;
                }
                return false;
            case "constructor":
                if (match("constructor") && match(":") && match("(")
                        && fParams() && match(")") && match(";")) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean funcDef() {
        if (funcHead() && funcBody()) {
            return true;
        }
        return false;
    }

    private boolean funcHead() {
        switch (lookahead.getLexeme()) {
            case "function":
                if (match("function") && match("id") && funcHead2()) {
                    return true;
                }
                return false;
            case "constructor":
                if (match("constructor") && match("(") && fParams()
                        && match(")") && arrow() && returnType()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean funcHead2() {
        switch (lookahead.getLexeme()) {
            case "(":
                if (match("(") && fParams() && match(")") && arrow()
                        && returnType()) {
                    return true;
                }
                return false;
            case "sr":
                if (match("sr") && funcHead1()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean funcHead1() {
        if (match("id") && match("(") && fParams() && match(")") && arrow()
                && returnType()) {
            return true;
        }
        return false;
    }

    private boolean arrow() {
        switch (lookahead.getLexeme()) {
            case "->":
                if (match("->")) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean returnType() {
        switch (lookahead.getLexeme()) {
            case "void":
                return match("void");
            case "type":
                return match("type");
            default:
                return false;
        }
    }

    private boolean fParams() {
        switch (lookahead.getLexeme()) {
            case "id":
            case ")":
                if (aParams() && reptFParams3() && reptFParams4()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean reptFParams3() {
        switch (lookahead.getLexeme()) {
            case "[":
                if (arraySize() && reptFParams3()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean reptFParams4() {
        switch (lookahead.getLexeme()) {
            case ",":
                if (fParamsTail() && reptFParams4()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean fParamsTail() {
        if (match("id") && match(":") && match("type") && reptFParamsTail4()) {
            return true;
        }
        return false;
    }

    private boolean reptFParamsTail4() {
        switch (lookahead.getLexeme()) {
            case "[":
                if (arraySize() && reptFParamsTail4()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean arraySize() {
        switch (lookahead.getLexeme()) {
            case "[":
                if (match("[") && arraySize1()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean arraySize1() {
        switch (lookahead.getLexeme()) {
            case "intLit":
                if (match("intLit") && match("]")) {
                    return true;
                }
                return false;
            case "]":
                return match("]");
            default:
                return false;
        }
    }
    private boolean funcBody () {
        switch (lookahead.getLexeme()) {
            case "{":
                if (match("{") && reptFuncBody1() && match("}")) {
                    return true;
                }
                return false;
            default:
                if (localVarDeclOrStmt()) {
                    return true;
                }
                return false;
        }
    }

    private boolean reptFuncBody1 () {
        switch (lookahead.getLexeme()) {
            case "id":
            case "read":
            case "return":
            case "while":
            case "write":
            case "{":
            case "}":
                if (localVarDeclOrStmt() && reptFuncBody1()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean localVarDeclOrStmt () {
        switch (lookahead.getLexeme()) {
            case "id":
                return idnestloop() && paramOrReptvar2() && match(";");
            case "type":
                return localVarDecl() && match(";");
            case "read":
                return readStmt();
            case "write":
                return writeStmt();
            case "while":
                return whileStmt();
            case "if":
                return ifStmt();
            case "return":
                return returnStmt();
            default:
                return false;
        }
    }

    private boolean localVarDecl () {
        if (match("type") && match("id") && match(":") && match("type")
                && localVarDecl1()) {
            return true;
        }
        return false;
    }

    private boolean localVarDecl1 () {
        switch (lookahead.getLexeme()) {
            case "(":
                return match("(") && aParams() && match(")") && match(";");
            case "[":
                return arraySize() && reptLocalVarDecl4() && match(";");
            default:
                return false;
        }
    }

    private boolean reptLocalVarDecl4 () {
        switch (lookahead.getLexeme()) {
            case "[":
                if (arraySize() && reptLocalVarDecl4()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean readStmt () {
        return match("read") && match("(") && variable() && match(")") && match(";");
    }

    private boolean writeStmt () {
        return match("write") && match("(") && expr() && match(")") && match(";");
    }

    private boolean whileStmt () {
        return match("while") && match("(") && relExpr() && match(")") && statBlock();
    }

    private boolean ifStmt () {
        return match("if") && match("(") && relExpr() && match(")") && match("then")
                && statBlock() && match("else") && statBlock() && match(";");
    }

    private boolean returnStmt () {
        return match("return") && match("(") && expr() && match(")") && match(";");
    }

    private boolean paramOrReptvar2 () {
        switch (lookahead.getLexeme()) {
            case "(":
                return match("(") && aParams() && match(")");
            case "[":
                return reptVariable2() && match("=") && expr();
            case "=":
                return match("=") && expr();
            default:
                return false;
        }
    }

    private boolean reptVariable2 () {
        switch (lookahead.getLexeme()) {
            case "[":
                return indice() && reptVariable2();
            default:
                return true;
        }
    }

    private boolean idnestloop () {
        if (match("id") && idnest1()) {
            return true;
        }
        return false;
    }

    private boolean idnest1 () {
        switch (lookahead.getLexeme()) {
            case "(":
                return match("(") && aParams() && match(")") && match(".");
            case ".":
                return reptIdnest1() && match(".");
            default:
                return false;
        }
    }

    private boolean idnest() {
        switch (lookahead.getLexeme()) {
            case "id":
                return match("id") && idnest();
            default:
                return false;
        }
    }
    private boolean reptIdnest1 () {
        switch (lookahead.getLexeme()) {
            case "[":
                return indice() && reptIdnest1();
            case ".":
                return match(".") && idnest();
            default:
                return false;
        }
    }

    private boolean indice () {
        return match("[") && arithExpr() && match("]");
    }

    private boolean statement () {
        switch (lookahead.getLexeme()) {
            case "if":
                return ifStmt();
            case "read":
                return readStmt();
            case "return":
                return returnStmt();
            case "while":
                return whileStmt();
            case "write":
                return writeStmt();
            case "id":
                return idnestloop() && paramOrReptvar2() && match(";");
            default:
                return false;
        }
    }

    private boolean statBlock () {
        switch (lookahead.getLexeme()) {
            case "{":
                return match("{") && reptStatBlock1() && match("}");
            case "if":
            case "read":
            case "return":
            case "while":
            case "write":
            case "id":
            case "}":
            case "constructor":
            case "function":
            case "private":
            case "public":
                return statement();
            default:
                return false;
        }
    }

    private boolean reptStatBlock1 () {
        switch (lookahead.getLexeme()) {
            case "if":
            case "read":
            case "return":
            case "while":
            case "write":
            case "{":
            case "}":
            case "id":
            case "constructor":
            case "function":
            case "private":
            case "public":
                if (statement() && reptStatBlock1()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean relExpr () {
        if (arithExpr() && relOp() && arithExpr()) {
            return true;
        }
        return false;
    }

    private boolean relOp () {
        switch (lookahead.getLexeme()) {
            case "eq":
            case "geq":
            case "gt":
            case "leq":
            case "lt":
            case "neq":
                return match(lookahead.getLexeme());
            default:
                return false;
        }
    }

    private boolean arithExpr () {
        if (term() && rightrecArithExpr()) {
            return true;
        }
        return false;
    }

    private boolean rightrecArithExpr () {
        switch (lookahead.getLexeme()) {
            case "+":
            case "-":
            case "or":
                return addOp() && term() && rightrecArithExpr();
            default:
                return true;
        }
    }

    private boolean term () {
        if (factor() && rightrecTerm()) {
            return true;
        }
        return false;
    }

    private boolean rightrecTerm () {
        switch (lookahead.getLexeme()) {
            case "*":
            case "/":
            case "and":
                return multOp() && factor() && rightrecTerm();
            default:
                return true;
        }
    }

    private boolean factor () {
        switch (lookahead.getLexeme()) {
            case "(":
                return match("(") && arithExpr() && match(")");
            case "floatLit":
            case "intLit":
                return match(lookahead.getLexeme());
            case "not":
                return match("not") &&
                        factor();
            case "id":
                return varFuncCall() || signFactor() || variable();
            default:
                return false;
        }
    }
    private boolean varFuncCall () {
        if (idnestloop() && varFuncCall1()) {
            return true;
        }
        return false;
    }

    private boolean varFuncCall1 () {
        switch (lookahead.getLexeme()) {
            case "(":
                return match("(") && aParams() && match(")");
            case "[":
                return reptVariable2();
            case ".":
                return match(".") && idnestloop() && varFuncCall1();
            default:
                return true;
        }
    }

    private boolean signFactor () {
        switch (lookahead.getLexeme()) {
            case "+":
            case "-":
                return sign() && factor();
            default:
                return false;
        }
    }

    private boolean sign () {
        switch (lookahead.getLexeme()) {
            case "+":
            case "-":
                return match(lookahead.getLexeme());
            default:
                return false;
        }
    }

    private boolean variable () {
        if (idnestloop() && reptVariable2()) {
            return true;
        }
        return false;
    }

    private boolean aParams () {
        switch (lookahead.getLexeme()) {
            case "(":
            case "floatLit":
            case "id":
            case "intLit":
            case "not":
            case "+":
            case "-":
                if (expr() && reptAParams1()) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean reptAParams1 () {
        switch (lookahead.getLexeme()) {
            case ",":
                return aParamsTail() && reptAParams1();
            default:
                return true;
        }
    }

    private boolean aParamsTail () {
        if (match(",") && expr()) {
            return true;
        }
        return false;
    }

    private boolean addOp () {
        switch (lookahead.getLexeme()) {
            case "+":
            case "-":
            case "or":
                return match(lookahead.getLexeme());
            default:
                return false;
        }
    }

    private boolean multOp () {
        switch (lookahead.getLexeme()) {
            case "*":
            case "/":
            case "and":
                return match(lookahead.getLexeme());
            default:
                return false;
        }
    }

    private boolean expr () {
        if (arithExpr() && relOpArithExprEpsilon()) {
            return true;
        }
        return false;
    }

    private boolean relOpArithExprEpsilon () {
        switch (lookahead.getLexeme()) {
            case "eq":
            case "geq":
            case "gt":
            case "leq":
            case "lt":
            case "neq":
                return relOp() && arithExpr();
            default:
                return true;
        }
    }


    private boolean match(String token) {
        if (lookahead.equals(token)) {
            lookahead = lexicalAnalyzer.nextToken();
            return true;
        } else {
            lookahead = lexicalAnalyzer.nextToken();
            return false;
        }
    }
}


