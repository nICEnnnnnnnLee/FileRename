package nicelee.file;

import javax.swing.JTextArea;

public class Printer {

	static JTextArea consoleArea;
	
	public static void init(JTextArea consoleArea) {
		Printer.consoleArea = consoleArea;
	}
	public static void clear() {
		if(consoleArea != null) {
			consoleArea.setText("");
		}
	}
	
	public static void println(String str) {
		System.out.println(str);
		if (consoleArea != null) {
			consoleArea.append(str);
			consoleArea.append("\r\n");
		}
	}
}
