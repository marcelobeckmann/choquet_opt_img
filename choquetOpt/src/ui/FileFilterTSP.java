package ui;
import java.io.File;

import javax.swing.filechooser.FileFilter;
/**
 * Classe utilizada para filtrar arquivos obj e tsp
 * @author marcelo
 *
 */
public class FileFilterTSP extends FileFilter {

	    public boolean accept(File f) {
	        if (f.isDirectory()) {
	            return true;
	        }

	        String extension = getExtension(f);
	        if (extension != null) {
	            if (extension.equals("obj") ||
	                extension.equals("tsp") ) {
	                    return true;
	            } else {
	                return false;
	            }
	        }

	        return false;
	    }

	    //The description of this filter
	    public String getDescription() {
	        return "Arquivos TSP (*.obj,*.tsp)";
	    }

	    public static String getExtension(File f) {
	        String ext = null;
	        String s = f.getName();
	        int i = s.lastIndexOf('.');

	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
	    }
}
