package tesi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import tesi.model.ArcoStazione;
import tesi.model.AutoElettriche;
import tesi.model.City;
import tesi.model.StazioniRicarica;

public class ColonnineDAO {

	//AutoDAO
	public List<String> getListMarche() {

		String sql = "SELECT DISTINCT Marca FROM AutoElettriche ORDER BY Marca";
		List<String> marche = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String s=res.getString("Marca");
				marche.add(s);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return marche;
	}
	
	public List<String> getListModelli() {

		String sql = "SELECT DISTINCT Modello FROM AutoElettriche ORDER BY Modello";
		List<String> modelli = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String s=res.getString("Modello");
				modelli.add(s);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return modelli;
	}

	public List<Integer> getListNumPosti() {
		String sql = "SELECT DISTINCT NumeroPosti FROM AutoElettriche";
		List<Integer> numposti = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				int i=res.getInt("NumeroPosti");
				numposti.add(i);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return numposti;
	}

	public List<String> getListSegmenti() {
		String sql = "SELECT DISTINCT Segmento FROM AutoElettriche";
		List<String> segmenti = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String s=res.getString("Segmento");
				segmenti.add(s);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return segmenti;
	}

	public List<String> getListModelliByMarca(String marca) {
		String sql = "SELECT DISTINCT Modello FROM AutoElettriche WHERE Marca=?";
		List<String> modelli = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, marca);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String s=res.getString("Modello");
				modelli.add(s);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return modelli;
	}

	public String getMarcaByModello(String value) {
		String sql = "SELECT DISTINCT Marca FROM AutoElettriche WHERE Modello=?";
		List<String> marche = new ArrayList<>();

		String s="";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, value);
			ResultSet res = st.executeQuery();

			if (res.next()) {
				 s=res.getString("Marca");
				
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return s;
	}
	
	public List<AutoElettriche> getListAuto() {

		String sql = "SELECT * FROM AutoElettriche ORDER BY Marca";
		List<AutoElettriche> auto = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				AutoElettriche a=new AutoElettriche();
				a.setAutonomia(res.getInt("Autonomia"));
				a.setEfficienza(res.getInt("Efficienza"));
				a.setMarca(res.getString("Marca"));
				a.setModello(res.getString("Modello"));
				a.setNumeroPosti(res.getInt("NumeroPosti"));
				a.setPrestazioni(Float.parseFloat(res.getString("Prestazioni")));
				if(res.getInt("PrezzoNoleggio")!=0)
					a.setPrezzoNoleggio(res.getInt("PrezzoNoleggio"));
				a.setPrezzoVendita(res.getInt("PrezzoVendita"));
				if(res.getInt("RicaricaRapida")==1)
					a.setRicaricaRapida(true);
				else
					a.setRicaricaRapida(false);
				a.setSegmento(res.getString("Segmento"));
				if(res.getInt("TrazioneIntegrale")==1)
					a.setTrazioneIntegrale(true);
				else
					a.setTrazioneIntegrale(false);
				a.setVelocitaMax(res.getInt("VelocitaMax"));
				a.setVelocitaRicarica(res.getInt("VelocitaRicarica"));
				
				auto.add(a);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return auto;
	}
	
	public AutoElettriche getAutoByModello(String modello) {
		String sql = "SELECT * FROM AutoElettriche WHERE Modello=?";
		AutoElettriche a=new AutoElettriche();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, modello);
			ResultSet res = st.executeQuery();

			if(res.next()) {
				
				a.setAutonomia(res.getInt("Autonomia"));
				a.setEfficienza(res.getInt("Efficienza"));
				a.setMarca(res.getString("Marca"));
				a.setModello(res.getString("Modello"));
				a.setNumeroPosti(res.getInt("NumeroPosti"));
				a.setPrestazioni(Float.parseFloat(res.getString("Prestazioni")));
				if(res.getInt("PrezzoNoleggio")!=0)
					a.setPrezzoNoleggio(res.getInt("PrezzoNoleggio"));
				a.setPrezzoVendita(res.getInt("PrezzoVendita"));
				if(res.getInt("RicaricaRapida")==1)
					a.setRicaricaRapida(true);
				else
					a.setRicaricaRapida(false);
				a.setSegmento(res.getString("Segmento"));
				if(res.getInt("TrazioneIntegrale")==1)
					a.setTrazioneIntegrale(true);
				else
					a.setTrazioneIntegrale(false);
				a.setVelocitaMax(res.getInt("VelocitaMax"));
				a.setVelocitaRicarica(res.getInt("VelocitaRicarica"));
				
				
			}
			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return a;
	}
	
	//CityDAO
	
	public List<City> getAllCities(Map<String, City> cityMap){
		String sql = "SELECT * FROM CaliforniaCities ORDER BY Citta ASC";
		List<City> citta = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(cityMap.get(res.getString("Citta"))==null) {
					City c=new City(res.getString("Citta"),new LatLng(Float.parseFloat(res.getString("Latitude")),Float.parseFloat(res.getString("Longitude"))));
					cityMap.put(c.getNome(), c);
					citta.add(c);
				}
				else
					citta.add(cityMap.get(res.getString("Citta")));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return citta;
	}
	
	
	
	public List<String> getNameOfCities(){
		String sql = "SELECT Citta FROM CaliforniaCities ORDER BY Citta ASC";
		List<String> citta = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				citta.add(res.getString("Citta"));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return citta;
	}
	
	public City getCityByName(String name, Map <String, City> cityMap) {
		List<City> citta=this.getAllCities(cityMap);
		for(City c:citta)
			if(c.getNome().toLowerCase().trim().equals(name.toLowerCase().trim()))
				return c;
		return null;
	}
	

	//StazioniRicaricaDAO
	
	public List<StazioniRicarica> getListStazioni(Map <Integer, StazioniRicarica> stationMap, Map<String, City> cityMap, Double latcenter, Double longcenter, Double radius) {

		String sql = "SELECT * FROM StazioniRicarica ";
		List<StazioniRicarica> stazioni = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				double lat=Double.parseDouble(res.getString("Latitude"));
				double lon=Double.parseDouble(res.getString("Longitude"));

				
				if(LatLngTool.distance(new LatLng(lat, lon), new LatLng(latcenter, longcenter), LengthUnit.KILOMETER)<=radius){
				if(stationMap.get(res.getInt("ID"))==null) {
				StazioniRicarica s=new StazioniRicarica(res.getInt("ID"),res.getString("Station Name"), res.getString("Street Address"), this.getCityByName(res.getString("City"), cityMap), res.getInt("ZIP"), res.getInt("EV Level1 EVSE Num"), res.getInt("EV Level2 EVSE Num"), res.getInt("EV DC Fast Count"), new LatLng(Float.parseFloat(res.getString("Latitude")), Float.parseFloat(res.getString("Longitude"))));
				stationMap.put(s.getId(), s);
				stazioni.add(s);
				}
				else {
					stazioni.add(stationMap.get(res.getInt("ID")));
				}
				}
				
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return stazioni;
	}
	
	public List<StazioniRicarica> getListStazioniSlow(Map <Integer, StazioniRicarica> stationMap, Map<String, City> cityMap, Double latcenter, Double longcenter, Double radius) {

		String sql = "SELECT * FROM StazioniRicarica s WHERE s.`EV Level1 EVSE Num`!=0 OR s.`EV Level2 EVSE Num`!=0 ";
		List<StazioniRicarica> stazioni = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				double lat=Double.parseDouble(res.getString("Latitude"));
				double lon=Double.parseDouble(res.getString("Longitude"));

				
				if(LatLngTool.distance(new LatLng(lat, lon), new LatLng(latcenter, longcenter), LengthUnit.KILOMETER)<=radius){
				if(stationMap.get(res.getInt("ID"))==null) {
				StazioniRicarica s=new StazioniRicarica(res.getInt("ID"),res.getString("Station Name"), res.getString("Street Address"), this.getCityByName(res.getString("City"), cityMap), res.getInt("ZIP"), res.getInt("EV Level1 EVSE Num"), res.getInt("EV Level2 EVSE Num"), res.getInt("EV DC Fast Count"), new LatLng(Float.parseFloat(res.getString("Latitude")), Float.parseFloat(res.getString("Longitude"))));
				stationMap.put(s.getId(), s);
				stazioni.add(s);
				}
				else
					stazioni.add(stationMap.get(res.getInt("ID")));
				}
				
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return stazioni;
	}
	
	public List<StazioniRicarica> getListStazioniFast(Map <Integer, StazioniRicarica> stationMap, Map<String, City> cityMap, Double latcenter, Double longcenter, Double radius) {

		String sql = "SELECT * FROM StazioniRicarica WHERE `EV DC Fast Count`!=0 ";
		List<StazioniRicarica> stazioni = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				double lat=Double.parseDouble(res.getString("Latitude"));
				double lon=Double.parseDouble(res.getString("Longitude"));

				
				if(LatLngTool.distance(new LatLng(lat, lon), new LatLng(latcenter, longcenter), LengthUnit.KILOMETER)<=radius){
				if(stationMap.get(res.getInt("ID"))==null) {
				StazioniRicarica s=new StazioniRicarica(res.getInt("ID"),res.getString("Station Name"), res.getString("Street Address"), this.getCityByName(res.getString("City"), cityMap), res.getInt("ZIP"), res.getInt("EV Level1 EVSE Num"), res.getInt("EV Level2 EVSE Num"), res.getInt("EV DC Fast Count"), new LatLng(Float.parseFloat(res.getString("Latitude")), Float.parseFloat(res.getString("Longitude"))));
				stationMap.put(s.getId(), s);
				stazioni.add(s);
				}
				else
					stazioni.add(stationMap.get(res.getInt("ID")));
				}
			}
			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return stazioni;
	}
	
	
	/*public List<StazioniRicarica> getStazioniRaggiungibili( StazioniRicarica partenza, ){
		String sql = "SELECT * FROM StazioniRicarica WHERE Latitude!=? AND Longitude!=?";
		List<StazioniRicarica> stazioni = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ""+partenza.getCoords().getLatitude());
			st.setString(2, ""+partenza.getCoords().getLongitude());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				StazioniRicarica s=new StazioniRicarica(res.getInt("ID"),res.getString("Station Name"), res.getString("Street Address"), this.getCityByName(res.getString("City"), cityMap), res.getInt("ZIP"), res.getInt("EV Level1 EVSE Num"), res.getInt("EV Level2 EVSE Num"), res.getInt("EV DC Fast Count"), new LatLng(Float.parseFloat(res.getString("Latitude")), Float.parseFloat(res.getString("Longitude"))));
				stazioni.add(s);
			} 

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return stazioni;
	}*/
	
	
	
	
	
	
}
