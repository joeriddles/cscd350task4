package acg.architecture.view.glyph.loader;

import java.io.IOException;

public class GlyphLoaderTester {

	public static void main(String[] args) {
		GlyphLoader loader;
		LayoutBundle bundle = null;
		try {
			loader = new GlyphLoader("in.txt");
			bundle = loader.load();
		} catch (IOException | InvalidLayoutException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < bundle.getEdgeLists().size(); i++)
		{
			System.out.println(bundle.getEdgeLists().get(i));
		}
		for (int i = 0; i < bundle.getCircles().size(); i++)
		{
			System.out.println(bundle.getCircles().get(i));
		}
	}

}