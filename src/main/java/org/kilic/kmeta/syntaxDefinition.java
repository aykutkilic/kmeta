package org.kilic.kmeta;

public class SyntaxDefinition {
    private boolean ifLeftRecursive;
    private String  EBNFSyntax;
    private Concept container;

    public SyntaxDefinition(Concept container) {
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

    public Concept getContainer() {
        return container;
    }
}
