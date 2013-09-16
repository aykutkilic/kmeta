package org.kilic.kmeta.meta.impl;

import com.google.common.base.Joiner;
import org.kilic.kmeta.meta.factory.ContainmentImpl;
import org.kilic.kmeta.ExecutionUnit;
import org.kilic.kmeta.SyntaxDefinition;

import java.util.*;

public class ConceptImpl implements Concept {
    private final Generalization generalization = new Generalization();
    private ExecutionUnit executionUnit;
    private boolean isAbstract;
    private ConceptImpl container;
    private Map<String, ContainmentImpl> containedConcepts;
    private Map<String, Object> properties;
    private SyntaxDefinition syntax;

    public ConceptImpl(String name, ExecutionUnit container) {
        super(name, container);

        executionUnit = container;
        generalization.parentConcepts = new HashSet<>();
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

    public Map<String, ContainmentImpl> getContainedConcepts() {
        return containedConcepts;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Set<ConceptImpl> getParentConcepts() {
        return generalization.parentConcepts;
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
            for(ConceptImpl subConcept : subConcepts ) {
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

    public Set<ConceptImpl> getSubConcepts() {
        return subConcepts;
    }

    public ConceptImpl getContainer() {
        return container;
    }

    public void setContainer(ConceptImpl container) {
        this.container = container;
    }
}
