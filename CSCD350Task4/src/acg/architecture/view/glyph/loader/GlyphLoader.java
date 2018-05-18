package acg.architecture.view.glyph.loader;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class GlyphLoader {
	
	private String filename;
	private Scanner fin;
	private int edgeIndex;
	
	/**
	 * GlyphLoader explicit value constructor
	 * @param filename fully qualified filename of the definition file
	 */
	public GlyphLoader(String filename){
		this.filename = filename;
		this.edgeIndex = 0;
	}
	
	/**
	 * Loads and processes the layout file
	 * @return layout bundle containing the edges and circles
	 * @throws IOException
	 * @throws InvalidLayoutException
	 */
	public LayoutBundle load() throws IOException, InvalidLayoutException{
		openInputFile();
		
		EntryMap<EntryColor> colors = new EntryMap<EntryColor>();
		EntryMap<EntryVertex> vertices = new EntryMap<EntryVertex>();
		EntryMap<EntryCircle> circles = new EntryMap<EntryCircle>();
		List<EntryMap<EntryEdge>> edges = new ArrayList<EntryMap<EntryEdge>>();    //Need to make a list of edge lists
		
		int lineNumber = 0;
		while(fin.hasNextLine()) {
			String[] cur = fin.nextLine().split("[\\s,;\\t]+");
			
			//trailing comma case
			if(cur.length == 0 && fin.hasNextLine()) {
				lineNumber++;
				continue;
			}
			
			if(cur[0].equals("c")) {
				//create color
				try {
					colors.addEntry(new EntryColor(Integer.parseInt(cur[1]), Color.decode(cur[2]))); //FIX
				}
				catch(Exception e) {
					throw new InvalidLayoutException("Invalid color format", lineNumber);
				}
			}
			else if(cur[0].equals("v")) {
				//create vertex
				try {
					vertices.addEntry(new EntryVertex(Integer.parseInt(cur[1]), Double.parseDouble(cur[2]), 
							Double.parseDouble(cur[3]), Double.parseDouble(cur[4])));
				}
				catch(Exception e) {
					throw new InvalidLayoutException("Invalid vertex format", lineNumber);
				}
			}
			else if(cur[0].equals("o")) {
				//create circle
				try {
					circles.addEntry(new EntryCircle(Integer.parseInt(cur[1]), vertices.getEntry(Integer.parseInt(cur[1])), 
							Double.parseDouble(cur[3]), colors.getEntry(Integer.parseInt(cur[2]))));
				}
				catch(Exception e) {
					throw new InvalidLayoutException("Invalid circle format", lineNumber);
				}
			}
			else if(cur[0].equals("e")) {
				//create edge
				EntryVertex pastVertex;
				EntryMap<EntryEdge> currentEdgeList = new EntryMap<EntryEdge>();
				while(fin.hasNextLine() && cur[0] == "e") {
					pastVertex = vertices.getEntry(Integer.parseInt(cur[1]));
					cur = fin.nextLine().split(",");
					currentEdgeList.addEntry(new EntryEdge(edgeIndex, pastVertex, 
							vertices.getEntry(Integer.parseInt(cur[1])), colors.getEntry(Integer.parseInt(cur[2]))));
				}
				edgeIndex++;
			}
			else if(cur[0].equals(",") || cur[0].equals(";") || cur[0].equals(" ") || cur[0].equals("")) {
				//skip
			}
			else {
				System.out.println(lineNumber);
				throw new InvalidLayoutException("Invalid argument", lineNumber);
			}
			lineNumber++;
		}
		
		List<EntryCircle> circleList = circles.getEntries(true);
		List<List<EntryEdge>> edgeList = new ArrayList<List<EntryEdge>>();
		Iterator<EntryMap<EntryEdge>> itr = edges.iterator();
		while(itr.hasNext()) {
			edgeList.add(itr.next().getEntries(true));
		}
		
		return new LayoutBundle(edgeList, circleList);
	}
	
	/**
	 * Opens input file
	 * @param filename the name of the file to be opened
	 * @return
	 */
	private void openInputFile() throws IOException{
		this.fin = new Scanner(new File(this.filename));
	}
}
