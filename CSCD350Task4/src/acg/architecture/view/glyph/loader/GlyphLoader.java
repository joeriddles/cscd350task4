package acg.architecture.view.glyph.loader;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GlyphLoader {

	private String filename;
	
	public GlyphLoader(String filename)
	{
		this.filename = filename;
	}
	
	public LayoutBundle load() throws IOException, InvalidLayoutException
	{
		File file = new File(filename);
		Scanner scan = new Scanner(file);
		
		EntryMap<EntryCircle> circles = new EntryMap<EntryCircle>();
		EntryMap<EntryColor> colors = new EntryMap<EntryColor>();
		EntryMap<EntryEdge> edges = new EntryMap<EntryEdge>();
		EntryMap<EntryVertex> vertices = new EntryMap<EntryVertex>();
		ArrayList<EntryMap<EntryEdge>> listofedges = new ArrayList<EntryMap<EntryEdge>>();
		
		int con = -1, line = 1, circle_index = 1, vert_index = 1, edge_index = 1;
		
		while (scan.hasNextLine())
		{
			try
			{
				String temp = "";
				String[] ar = scan.nextLine().split(";");	//split on comments
				temp = ar[0]; 								//string equals everything before comments
				ar = temp.split(",");						//split on commas
				System.out.print(line);
				for (int j = 0; j < ar.length; j++)
					System.out.print(": " + ar[j] + " ");
				System.out.print("\n");
				
				if (ar == null || ar.length < 1 || ar[0] == null || ar[0].isEmpty())
					con = -1;
				else if (ar[0].equals("c"))
				{
					colors.addEntry(new EntryColor(Integer.parseInt(ar[1]), Color.decode(ar[2])));
					con = -1;
				}
				else if (ar[0].equals("v"))
				{
					vertices.addEntry(new EntryVertex(Integer.parseInt(ar[1]), Double.parseDouble(ar[2]), Double.parseDouble(ar[3]), Double.parseDouble(ar[4])));
					con = -1;
				}
				else if (ar[0].equals("o"))
				{
					circles.addEntry(new EntryCircle(circle_index, vertices.getEntry(Integer.parseInt(ar[1])), Double.parseDouble(ar[3]), colors.getEntry(Integer.parseInt(ar[2]))));
					circle_index++;
					con = -1;
				}
				else if (ar[0].equals("e"))
				{
					if (con != -1)
					{
						edges.addEntry(new EntryEdge(edge_index, vertices.getEntry(vert_index), vertices.getEntry(Integer.parseInt(ar[1])), colors.getEntry(Integer.parseInt(ar[2]))));
						vert_index = Integer.parseInt(ar[1]);
						edge_index++;
					}
					else
					{
						if (edges.getEntries(false).size() > 0)
							listofedges.add(edges);				//add edge to list of edges
						edges = new EntryMap<EntryEdge>();		//create new edge list
						vert_index = Integer.parseInt(ar[1]);
					}
					con = Integer.parseInt(ar[1]);	
				}
				else
					con = -1;
				
				line++;
			}
			catch(Exception e)
			{
				scan.close();
				e.printStackTrace(System.out);
				throw new InvalidLayoutException(line);
			}
		}
		scan.close();
		
		List<List<EntryEdge>> mirror_edges = new ArrayList<List<EntryEdge>>();
		List<EntryCircle> mirror_circles = new ArrayList<EntryCircle>();
		
		for (EntryMap<EntryEdge> map : listofedges)
			mirror_edges.add(map.getEntries(false));
		mirror_circles = circles.getEntries(false);
		
		return new LayoutBundle(mirror_edges, mirror_circles);
	}
}