package br.edu.ufba.softvis.visminerweb.beans;

import br.edu.ufba.softvis.visminer.model.business.File;
import br.edu.ufba.softvis.visminer.model.business.FileState;

public class AffectedFile {

	private int id;
	private String name;
	private String state;

	public AffectedFile(File file) {
		id = file.getId();

		java.io.File f = new java.io.File(file.getPath());
		name = f.getName();

		FileState fstate = file.getFileState();
		if (fstate.isDeleted()) {
			state = "deleted";
		} else {
			state = "";
			if (fstate.getLinesAdded() > 0) {
				state += fstate.getLinesAdded() + " lines added";
			}
			if (fstate.getLinesRemoved() > 0) {
				state += (state.equals("") ? "" : " and ")
						+ fstate.getLinesAdded() + " lines removed";
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
