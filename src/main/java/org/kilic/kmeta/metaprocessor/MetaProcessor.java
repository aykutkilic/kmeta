package org.kilic.kmeta.metaprocessor;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.kilic.kmeta.KMetaLexer;
import org.kilic.kmeta.KMetaParser;

/**
 * Created with IntelliJ IDEA.
 * User: Aykut
 * Date: 09/09/13
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
public class MetaProcessor {
    public void process(ANTLRFileStream fileStream) {
        KMetaLexer lexer = new KMetaLexer(fileStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        KMetaParser parser = new KMetaParser(tokenStream);

        parser.setBuildParseTree(true);
        ParseTree tree = parser.executionUnit();
        ConceptVisitor visitor = new ConceptVisitor(this);

        visitor.visit(tree);
    }
}
