package org.kilic.kmeta;

import com.google.common.base.Joiner;

import java.util.*;

public class Concept extends BaseScope {
    private ExecutionUnit executionUnit;
    private boolean isAbstract;
    private Set<Concept> parentConcepts;
    private Set<Concept> subConcepts;
    private Concept container;
    private Map<String, Definition> containedConcepts;
    private Map<String, Object> properties;
    private SyntaxDefinition syntax;

    public Concept(String name, ExecutionUnit container) {
        super(name, container);

        executionUnit = container;
        parentConcepts = new HashSet<>();
        subConcepts = new HashSet<>();
        containedConcepts = new HashMap<>();
        properties = new HashMap<>();
    }

    public SyntaxDefinition getSyntax() {
        return syntax;
    }

    public void setSyntax(SyntaxDefinition syntax) {
        this.syntax = syntax;
    }

    public Map<String, Definition> getContainedConcepts() {
        return containedConcepts;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Set<Concept> getParentConcepts() {
        return parentConcepts;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public ExecutionUnit getExecutionUnit() {
        return executionUnit;
    }

    public String getCurrentGrammar() {
        StringBuilder result = new StringBuilder();

        result.append(getShortName() + "Rule" + ":\n");
        if (subConcepts.size() > 0) {
            List<String> items = new ArrayList<>();
            for(Concept subConcept : subConcepts ) {
                if( subConcept.getSyntax().isIfLeftRecursive() )
                    items.add(subConcept.getSyntax().getEBNF() + " #" + subConcept.getShortName() );
                else
                    items.add(subConcept.getShortName()+"Rule" + " #" + subConcept.getShortName() );
            }

            result.append(Joiner.on("\n  | ").join(items));
        }

        if( syntax != null ) {
            if (syntax.isIfLeftRecursive())
                return null;

            if (syntax.getEBNF() != null && !syntax.getEBNF().isEmpty())
                result.append("  " + syntax.getEBNF() + "\n");
        }
        result.append(";\n\n");

        return result.toString();
    }

    public Set<Concept> getSubConcepts() {
        return subConcepts;
    }

    public Concept getContainer() {
        return container;
    }

    public void setContainer(Concept container) {
        this.container = container;
    }
}
