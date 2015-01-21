package org.eclipse.cdt.internal.core.dom.rewrite.astwriter;

/*******************************************************************************
 * Copyright (c) 2008, 2014 Institute for Software, HSR Hochschule fuer Technik
 * Rapperswil, University of applied sciences and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Institute for Software - initial API and implementation
 *     Markus Schorn (Wind River Systems)
 *     Sergey Prigogin (Google)
 *     Thomas Corbat (IFS)
 *******************************************************************************/
import org.eclipse.cdt.core.dom.ast.IASTStatement;

public class MyASTWriterVisitor extends ASTWriterVisitor {
	
	@Override
	public int visit(IASTStatement statement) {
		try {
			return statementWriter.writeStatement(statement, true);
		} finally {
		}
	}
}

