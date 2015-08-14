package br.edu.ufba.softvis.visminerweb.loader;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.edu.ufba.softvis.visminer.main.VisMiner;

/**
 * Loader for Visminer Context
 * 
 * It initiates db access
 * 
 * @author Luis Paulo
 */
public class VisminerLoaderListener implements ServletContextListener {

	public static VisMiner visminer;

	private static final String CONFIG_PATH = "/WEB-INF";
	private static final String DBCONFIG_FILE = "/dbconfig.properties";

	public static boolean configOK() {
		return (visminer != null);
	}

	public static void configure(String path) {
		VisminerLoaderListener.visminer = new VisMiner();

		if (visminer.setDBConfig(path)) {
			System.out.println("Configuration OK!");
		} else {
			System.out.println("Configuration FAILED!");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Configuring VISMINER...");

		String path = sce.getServletContext().getRealPath(CONFIG_PATH)
				+ DBCONFIG_FILE;
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			// setup visminer's DBCONFIG
			VisminerLoaderListener.configure(path);
		} else {
			System.out
					.println("No config found. Proceed to configuration wizard...");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//
	}
}
