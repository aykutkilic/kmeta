package deneme.editor

import java.util.HashMap
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.widgets.Display

class ColorManager {
	var colorTable = new HashMap<RGB, Color>() 
	
	def dispose() {
		colorTable.values.forEach[ it.dispose ]
	}
	
	def getColor(RGB rgb) {
		if(!colorTable.containsKey(rgb))
			colorTable.put(rgb, new Color(Display.getCurrent, rgb))
		
		colorTable.get(rgb)
	}
}