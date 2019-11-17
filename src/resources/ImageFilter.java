package resources;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter {

	//Accept all directories and all jpg files.
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = Utils.getExtension(f);
		if (extension != null) {
			//if (extension.equals(Utils.jpeg) || extension.equals(Utils.jpg) || extension.equals(Utils.png)) {
			if (extension.equals(Utils.jpg))
				return true;
			else
				return false;
		}

		return false;
	}

	//The description of this filter
	public String getDescription() {
		//return "Images (*.jpeg, *.jpg, *.png)";
		return "JPG Images (*.jpg)";
	}
	
	public static class Utils {
		public final static String jpg = "jpg";

		public static String getExtension(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 &&  i < s.length() - 1) {
				ext = s.substring(i+1).toLowerCase();
			}
			return ext;
		}

		/** Returns an ImageIcon, or null if the path was invalid. */
		protected static ImageIcon createImageIcon(String path) {
			java.net.URL imgURL = Utils.class.getResource(path);
			if (imgURL != null) {
				return new ImageIcon(imgURL);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		}
	}
	
}




