package org.kilic.kmeta;

import org.antlr.v4.runtime.ANTLRFileStream;

import java.io.IOException;

public class KMetaCLI {
    public static void main(String[] args) throws IOException {
        KMetaLexer lexer = new KMetaLexer(new ANTLRFileStream(args[0]));

    }
}
