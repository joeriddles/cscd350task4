package acg.architecture.view.glyph.loader;

import java.io.IOException;

public class Tester {
	public static void main(String[] args) {
		GlyphLoader glyph = new GlyphLoader("C:\\Users\\csfaculty\\Documents\\Spring 2018\\CSCD 350\\carrier.txt");
		try {
			LayoutBundle lb = glyph.load();
		} catch (IOException | InvalidLayoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
