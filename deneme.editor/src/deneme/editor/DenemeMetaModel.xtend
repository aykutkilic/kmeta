package deneme.editor

import org.eclipse.emf.ecore.EcoreFactory

class DenemeMetaModel {
	new() {
		var it = EcoreFactory.eINSTANCE;
		var class = createEClass()
		var containment = createEReference()
		containment.containment = true
	}
}