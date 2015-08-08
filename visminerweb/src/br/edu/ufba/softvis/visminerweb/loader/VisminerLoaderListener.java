package br.edu.ufba.softvis.visminerweb.loader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.edu.ufba.softvis.visminer.main.VisMiner;

/**
 * 
 * @author Luis Paulo
 */
public class VisminerLoaderListener implements ServletContextListener {

	public static VisMiner visminer;
	
	private static final String CONFIG_PATH = "/WEB-INF";
	private static final String DBCONFIG_FILE = "/dbconfig.properties";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Configuring VISMINER...");

		String path = sce.getServletContext().getRealPath(CONFIG_PATH);
		// setup visminer's DBCONFIG
		VisminerLoaderListener.visminer = new VisMiner();
		if (visminer.setDBConfig(path + DBCONFIG_FILE)) {
			System.out.println("Configuration OK!");
		} else {
			System.err.println("Configuration FAILED!");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// VisminerLoaderListener.visminer.close();
	}
}
