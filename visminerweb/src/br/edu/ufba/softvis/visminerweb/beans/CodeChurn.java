package br.edu.ufba.softvis.visminerweb.beans;

import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.File;

public class CodeChurn {

	private int linesAdded;
	private int linesRemoved;

	public CodeChurn(Commit commit) {
		for (File file : commit.getCommitedFiles()) {
			linesAdded = file.getFileState().getLinesAdded();
			linesRemoved = file.getFileState().getLinesRemoved();
		}
	}

	public int getLinesAdded() {
		return linesAdded;
	}

	public void setLinesAdded(int linesAdded) {
		this.linesAdded = linesAdded;
	}

	public int getLinesRemoved() {
		return linesRemoved;
	}

	public void setLinesRemoved(int linesRemoved) {
		this.linesRemoved = linesRemoved;
	}
	
	public void incrementBy(CodeChurn other) {
		this.linesAdded += other.getLinesAdded();
		this.linesRemoved += other.getLinesRemoved();
	}

}
