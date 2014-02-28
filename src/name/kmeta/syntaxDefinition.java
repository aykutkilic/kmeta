package name.kmeta;

import org.kilic.kmeta.meta.impl.ConceptImpl;

public class SyntaxDefinition {
    private boolean ifLeftRecursive;
    private String  EBNFSyntax;
    private ConceptImpl container;

    public SyntaxDefinition(ConceptImpl container) {
        this.container = container;
    }

    public boolean isIfLeftRecursive() {
        return ifLeftRecursive;
    }

    public void setIfLeftRecursive(boolean ifLeftRecursive) {
        this.ifLeftRecursive = ifLeftRecursive;
    }

    public String getEBNF() {
        return EBNFSyntax;
    }

    public void setEBNF(String EBNFSyntax) {
        this.EBNFSyntax = EBNFSyntax;
    }

    public ConceptImpl getContainer() {
        return container;
    }
}
