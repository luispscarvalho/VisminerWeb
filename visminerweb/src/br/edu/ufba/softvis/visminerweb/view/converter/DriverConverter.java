package br.edu.ufba.softvis.visminerweb.view.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.edu.ufba.softvis.visminer.config.DBConfig;

@FacesConverter("driverConverter")
public class DriverConverter implements Converter {

	private static Map<String, String> drivers = new TreeMap<String, String>();
	private static DBConfig dbConfig = new DBConfig();

	static {
		drivers.put("MySQL", "com.mysql.jdbc.Driver");
		drivers.put("PostgreSQL", "org.postgresql.Driver");
	}

	public static List<String> getDrivers() {
		return new ArrayList<String>(drivers.keySet());
	}
	
	public static DBConfig getDbConfig() {
		return dbConfig;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (!value.isEmpty()) {
			dbConfig.setDriver(DriverConverter.drivers.get(value));
			dbConfig.setUrl("jdbc:" + value.toLowerCase() + "://");
		}

		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (String) value;
	}

}
