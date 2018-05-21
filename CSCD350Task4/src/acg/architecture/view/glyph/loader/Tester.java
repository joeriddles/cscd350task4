package acg.architecture.view.glyph.loader;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Tester {
	public static void main(String[] args) {
		LayoutBundle lb = null;
		GlyphLoader glyph = new GlyphLoader("C:\\Users\\csfaculty\\Documents\\Spring 2018\\CSCD 350\\carrier.txt");
		try {
			lb = glyph.load();
		} catch (IOException | InvalidLayoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<List<EntryEdge>> edgeList = lb.getEdgeLists();
		List<EntryCircle> circleList = lb.getCircles();
		
		for(EntryCircle c: circleList) {
			System.out.println("o," + c.getVertex().getIndex() + "," + c.getColor().getIndex() + "," + c.getRadius());
		}
		
		System.out.println();
		
		for(List<EntryEdge> l: edgeList) {
			Iterator<EntryEdge> itr = l.iterator();
			while(itr.hasNext()) {
				EntryEdge e = itr.next();
				System.out.println("e," + e.getVertexEnd().getIndex() + "," + e.getVertexStart().getIndex() + "," + e.getColor().getIndex());
			}
			System.out.println();
		}
	}
}
