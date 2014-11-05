package deneme.editor

import org.eclipse.ui.editors.text.FileDocumentProvider
import org.eclipse.core.runtime.CoreException
import org.eclipse.jface.text.rules.FastPartitioner

class DenemeDocumentProvider extends FileDocumentProvider {

	override protected createDocument(Object element) throws CoreException {
		var document = super.createDocument(element)

		if (document != null) {
			var partitioner = new FastPartitioner(
				new DenemePartitionScanner,
				#[
					DenemePartitionScanner.DNM_STRING
				]
			);
			partitioner.connect(document)
			document.documentPartitioner = partitioner
		}

		document
	}
}
