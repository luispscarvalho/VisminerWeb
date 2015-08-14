package br.edu.ufba.softvis.visminerweb.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.ufba.softvis.visminer.config.DBConfig;
import br.edu.ufba.softvis.visminerweb.loader.VisminerLoaderListener;
import br.edu.ufba.softvis.visminerweb.view.converter.DriverConverter;

@ManagedBean(name = "configurator")
@ViewScoped
public class Configurator {

	private static final String CONFIG_PATH = "/WEB-INF";

	public List<String> getDrivers() {
		return DriverConverter.getDrivers();
	}

	public String getDriver() {
		return DriverConverter.getDbConfig().getDriver();
	}

	public void setDriver(String driver) {
		//
	}

	public String getUrl() {
		return DriverConverter.getDbConfig().getUrl();
	}

	public void setUrl(String url) {
		DriverConverter.getDbConfig().setUrl(url);
	}

	public String getUser() {
		return DriverConverter.getDbConfig().getUser();
	}

	public void setUser(String user) {
		DriverConverter.getDbConfig().setUser(user);
	}

	public String getPassword() {
		return DriverConverter.getDbConfig().getPassword();
	}

	public void setPassword(String password) {
		DriverConverter.getDbConfig().setPassword(password);
	}

	public void testConnection() {
		DBConfig dbConfig = DriverConverter.getDbConfig();
		Connection conn = null;
		try {
			Class.forName(dbConfig.getDriver()).newInstance();
			Properties connectionProps = new Properties();
			connectionProps.put("user", dbConfig.getUser());
			connectionProps.put("password", dbConfig.getPassword());
			conn = DriverManager.getConnection(dbConfig.getUrl(),
					connectionProps);

			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Success",
									"Connection Ok. You may save the configuration now!"));
		} catch (Exception e) {
			e.printStackTrace();

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"Connection failed!"));
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private Properties getProperties(DBConfig dbConfig) {
		Properties properties = new Properties();

		properties.put(DBConfig.PROP_JDBC_DRIVER, dbConfig.getDriver());
		properties.put(DBConfig.PROP_JDBC_URL, dbConfig.getUrl());
		properties.put(DBConfig.PROP_JDBC_USER, dbConfig.getUser());
		properties.put(DBConfig.PROP_JDBC_PASSWORD, dbConfig.getPassword());
		properties.put(DBConfig.PROP_JDBC_GENERATION, dbConfig.getGeneration());

		return properties;
	}

	public void saveConfiguration() {
		DBConfig dbConfig = DriverConverter.getDbConfig();
		dbConfig.setGeneration("create-tables");

		String path = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath(CONFIG_PATH)
				+ "/dbconfig.properties";

		try {
			OutputStream fileOut = new FileOutputStream(path);
			getProperties(dbConfig).store(fileOut, "database configuration");
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		VisminerLoaderListener.configure(path);
		if (VisminerLoaderListener.configOK()) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
							"Configuration saved!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"Failed to save configuration!"));
		}
	}
	
	public void gotoVisminer() {
		
	}
	
}
