package tesi.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import tesi.db.ColonnineDAO;

public class TesiModel {
	private ColonnineDAO dao;
	private Graph<Vertex, DefaultWeightedEdge> reteStazioni;
	private Map <String, City> cities; //LatLng non supporta comparable
	private Map <Integer, StazioniRicarica> stations;
	private Map<Vertex, Vertex> backVisit;
	
	private class EdgeTraversedGraphListener implements TraversalListener<Vertex,DefaultWeightedEdge>{

		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {			
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {	
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {
			Vertex sourceVertex=reteStazioni.getEdgeSource(ev.getEdge());
			Vertex targetVertex=reteStazioni.getEdgeTarget(ev.getEdge());
			if(!backVisit.containsKey(targetVertex) && backVisit.containsKey(sourceVertex))
				backVisit.put(targetVertex, sourceVertex);
			else if(!backVisit.containsKey(sourceVertex) && backVisit.containsKey(targetVertex))
				backVisit.put(sourceVertex, targetVertex); 
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<Vertex> arg0) {
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<Vertex> arg0) {			
		} 

		
	}
	
	
	public TesiModel() {
		this.dao=new ColonnineDAO();
		reteStazioni=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.cities=new TreeMap<>();
		this.stations=new TreeMap<>();
	}

	
	public List<String> getMarche() {
		// TODO Auto-generated method stub
		return dao.getListMarche();
	}


	public List<String> getModelli() {
		// TODO Auto-generated method stub
		return this.dao.getListModelli();
	}


	public List<Integer> getNumPosti() {
		// TODO Auto-generated method stub
		return dao.getListNumPosti();
	}


	public List<String> getSegmenti() {
		// TODO Auto-generated method stub
		return dao.getListSegmenti();
	}


	public List<String> getModelloByMarca(String value) {
		// TODO Auto-generated method stub
		return dao.getListModelliByMarca(value);
	}


	public String getMarcaByModello(String value) {
		// TODO Auto-generated method stub
		return dao.getMarcaByModello(value);
	}


	public List<AutoElettriche> getAllAuto() {
		// TODO Auto-generated method stub
		return dao.getListAuto();
	}


	public AutoElettriche getAutoByModello(String modello) {
		// TODO Auto-generated method stub
		return dao.getAutoByModello(modello);
	}


	public String getLatitudine(String partenza) {
		List<City> citta=this.dao.getAllCities(this.cities);
		for(City c:citta)
			if(c.getNome().equals(partenza))
				return ""+c.getCoords().getLatitude();
		return "";
	}


	public String getLongitudine(String partenza) {
		List<City> citta=this.dao.getAllCities(this.cities);
		for(City c:citta)
			if(c.getNome().equals(partenza))
				return ""+c.getCoords().getLongitude();
		return "";
	}


	public List<String> getCitta() {
		// TODO Auto-generated method stub
		return dao.getNameOfCities();
	}
	
	public City getCityByName(String name) {
		return dao.getCityByName(name, cities);
	}
	//Creazione grafo per Autoveicoli che supportano RicaricaRapida ma che vogliono anche RicaricaLenta
	public void creaGrafo(String partenza, String arrivo, int autonomia) {
		City part=this.cities.get(partenza);
		City arr=this.cities.get(arrivo);
		
		Double latpartenza=part.getCoords().getLatitude();
		Double latarrivo=arr.getCoords().getLatitude();
		Double longpartenza=part.getCoords().getLongitude();
		Double longarrivo=arr.getCoords().getLongitude();
		//Creazione della fascia
		/*Double min;
		Double max;
		if(latpartenza>latarrivo) {
			max=latpartenza;
			min=latarrivo;
		}
		else  {
			min=latpartenza;
			max=latarrivo;
		}*/
		
		Double latcenter=(latpartenza+latarrivo)/2;
		Double longcenter=(longpartenza+longarrivo)/2;
		Double radius=this.distanzaPunti(part.getCoords(), arr.getCoords())/2;
		
		Graphs.addAllVertices(this.reteStazioni, dao.getListStazioni(this.stations,this.cities, latcenter, longcenter,radius));
		this.reteStazioni.addVertex(part);
		this.reteStazioni.addVertex(arr);
		
		for(Vertex s: this.reteStazioni.vertexSet()) {
			if(s instanceof StazioniRicarica)
			for(Vertex t: this.reteStazioni.vertexSet()) {
				if(t instanceof StazioniRicarica)
				if(!((StazioniRicarica)s).equals((StazioniRicarica)t) && this.distanzaPunti(((StazioniRicarica)s).getCoords(), ((StazioniRicarica)t).getCoords())<autonomia ) {
					//System.out.println(s.toString()+","+t.toString());
					ArcoStazione a=new ArcoStazione(s,t,this.distanzaPunti(((StazioniRicarica)s).getCoords(), ((StazioniRicarica)t).getCoords()));
					Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
				}
			}
		}
		
		for(Vertex s:this.reteStazioni.vertexSet())
			if(s instanceof City)
				for(Vertex t:this.reteStazioni.vertexSet()) {
					if(t instanceof City) {
						if(!((City)s).equals((City)t) && this.distanzaPunti(((City)s).getCoords(), ((City)t).getCoords())<autonomia) {
							ArcoStazione a=new ArcoStazione(s,t, this.distanzaPunti(((City)s).getCoords(), ((City)t).getCoords()));
							Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
				
						}
					}
					else if(this.distanzaPunti(((City)s).getCoords(), ((StazioniRicarica)t).getCoords())<autonomia) {
						ArcoStazione a=new ArcoStazione(s,t, this.distanzaPunti(((City)s).getCoords(), ((StazioniRicarica)t).getCoords()));
						Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
					}
				}
		System.out.format("Grafo creato con %d vertici e %d archi", this.reteStazioni.vertexSet().size(),this.reteStazioni.edgeSet().size());

	}
	
	
	//Creazione Grafo per Autoveicoli che supportano solo RicaricaLenta
	public void creaGrafoSlow(String partenza, String arrivo, int autonomia) {
		City part=this.cities.get(partenza);
		City arr=this.cities.get(arrivo);
		
		Double latpartenza=part.getCoords().getLatitude();
		Double latarrivo=arr.getCoords().getLatitude();
		Double longpartenza=part.getCoords().getLongitude();
		Double longarrivo=arr.getCoords().getLongitude();
		//Creazione della fascia
		/*Double min;
		Double max;
		if(latpartenza>latarrivo) {
			max=latpartenza;
			min=latarrivo;
		}
		else  {
			min=latpartenza;
			max=latarrivo;
		}*/
		Double latcenter=(latpartenza+latarrivo)/2;
		Double longcenter=(longpartenza+longarrivo)/2;
		Double radius=this.distanzaPunti(part.getCoords(), arr.getCoords())/2;
		
		Graphs.addAllVertices(this.reteStazioni, dao.getListStazioniSlow(this.stations,this.cities, latcenter, longcenter, radius));
		this.reteStazioni.addVertex(part);
		this.reteStazioni.addVertex(arr);
		
		for(Vertex s: this.reteStazioni.vertexSet()) {
			if(s instanceof StazioniRicarica)
			for(Vertex t: this.reteStazioni.vertexSet()) {
				if(t instanceof StazioniRicarica)
				if(!((StazioniRicarica)s).equals((StazioniRicarica)t) && this.distanzaPunti(((StazioniRicarica)s).getCoords(), ((StazioniRicarica)t).getCoords())<autonomia ) {
					//System.out.println(s.toString()+","+t.toString());
					ArcoStazione a=new ArcoStazione(s,t,this.distanzaPunti(((StazioniRicarica)s).getCoords(), ((StazioniRicarica)t).getCoords()));
					Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
				}
			}
		}
		
		for(Vertex s:this.reteStazioni.vertexSet())
			if(s instanceof City)
				for(Vertex t:this.reteStazioni.vertexSet()) {
					if(t instanceof City) {
						if(!((City)s).equals((City)t) && this.distanzaPunti(((City)s).getCoords(), ((City)t).getCoords())<autonomia) {
							ArcoStazione a=new ArcoStazione(s,t, this.distanzaPunti(((City)s).getCoords(), ((City)t).getCoords()));
							Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
				
						}
					}
					else if(this.distanzaPunti(((City)s).getCoords(), ((StazioniRicarica)t).getCoords())<autonomia) {
						ArcoStazione a=new ArcoStazione(s,t, this.distanzaPunti(((City)s).getCoords(), ((StazioniRicarica)t).getCoords()));
						Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
					}
				}
		System.out.format("Grafo creato con %d vertici e %d archi", this.reteStazioni.vertexSet().size(),this.reteStazioni.edgeSet().size());

	}
	//Creazione grafo per Autoveicoli che supportano RicaricaRapida ma che vogliono solo RicaricaRapida

	public void creaGrafoFast(String partenza, String arrivo, int autonomia) {
		City part=this.cities.get(partenza);
		City arr=this.cities.get(arrivo);
		
		Double latpartenza=part.getCoords().getLatitude();
		Double latarrivo=arr.getCoords().getLatitude();
		Double longpartenza=part.getCoords().getLongitude();
		Double longarrivo=arr.getCoords().getLongitude();
		//Creazione della fascia
		/*Double min;
		Double max;
		if(latpartenza>latarrivo) {
			max=latpartenza;
			min=latarrivo;
		}
		else  {
			min=latpartenza;
			max=latarrivo;
		}*/
		
		Double latcenter=(latpartenza+latarrivo)/2;
		Double longcenter=(longpartenza+longarrivo)/2;
		Double radius=this.distanzaPunti(part.getCoords(), arr.getCoords())/2;
			
		Graphs.addAllVertices(this.reteStazioni, dao.getListStazioniFast(this.stations,this.cities, latcenter, longcenter, radius));
		
		this.reteStazioni.addVertex(part);
		this.reteStazioni.addVertex(arr);
		
		
		
		for(Vertex s: this.reteStazioni.vertexSet()) {
			if(s instanceof StazioniRicarica)
			for(Vertex t: this.reteStazioni.vertexSet()) {
				if(t instanceof StazioniRicarica)
				if(!((StazioniRicarica)s).equals((StazioniRicarica)t) && this.distanzaPunti(((StazioniRicarica)s).getCoords(), ((StazioniRicarica)t).getCoords())<autonomia ) {
					//System.out.println(s.toString()+","+t.toString());
					ArcoStazione a=new ArcoStazione(s,t,this.distanzaPunti(((StazioniRicarica)s).getCoords(), ((StazioniRicarica)t).getCoords()));
					Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
				}
			}
		}
		
		for(Vertex s:this.reteStazioni.vertexSet())
			if(s instanceof City)
				for(Vertex t:this.reteStazioni.vertexSet()) {
					if(t instanceof City) {
						if(!((City)s).equals((City)t) && this.distanzaPunti(((City)s).getCoords(), ((City)t).getCoords())<autonomia) {
							ArcoStazione a=new ArcoStazione(s,t, this.distanzaPunti(((City)s).getCoords(), ((City)t).getCoords()));
							Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
				
						}
					}
					else if(this.distanzaPunti(((City)s).getCoords(), ((StazioniRicarica)t).getCoords())<autonomia) {
						ArcoStazione a=new ArcoStazione(s,t, this.distanzaPunti(((City)s).getCoords(), ((StazioniRicarica)t).getCoords()));
						Graphs.addEdge(this.reteStazioni, a.getS1(), a.getS2(), a.getDistance());
					}
				}
		System.out.format("Grafo creato con %d vertici e %d archi", this.reteStazioni.vertexSet().size(),this.reteStazioni.edgeSet().size());

	}
	
	
	public List<Vertex> verticiRaggiungibili(Vertex source){
		backVisit=new HashMap<>();
		List<Vertex> result=new ArrayList<>();
		
		GraphIterator<Vertex, DefaultWeightedEdge> it= new BreadthFirstIterator<>(this.reteStazioni, source);
		it.addTraversalListener(new TesiModel.EdgeTraversedGraphListener());

		backVisit.put(source, null); 
		
		while(it.hasNext()) {
			result.add(it.next());
		}
		
		return result;
	}
	
	public List<Vertex> spanningTreeFinoA(Vertex target){
		List<Vertex> percorso=new LinkedList<>();
		
		if(!backVisit.containsKey(target)) {
			return null;
		}
		
		Vertex v=target;
		
		while(v!=null) { 
			percorso.add(0,v); 
			v=backVisit.get(v);
			}
			
		return percorso;
	}
	
	public List<Vertex> calcolaCamminoMinimo(String partenza, String arrivo){
		City source=this.cities.get(partenza);
		City target=this.cities.get(arrivo);
		DijkstraShortestPath<Vertex, DefaultWeightedEdge> dijkstra=new DijkstraShortestPath<>(this.reteStazioni);
		GraphPath<Vertex, DefaultWeightedEdge> path=dijkstra.getPath(source, target);
		return path.getVertexList();
	}
	
	public double distanzaPunti(LatLng coords1, LatLng coords2) {
		return LatLngTool.distance(coords1, coords2, LengthUnit.KILOMETER);
	}


	public Graph<Vertex, DefaultWeightedEdge> getReteStazioni() {
		return reteStazioni;
	}
	
	

	

}
