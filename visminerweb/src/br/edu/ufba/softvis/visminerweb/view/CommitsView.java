package br.edu.ufba.softvis.visminerweb.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.File;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.retriever.CommitRetriever;
import br.edu.ufba.softvis.visminer.retriever.RetrieverFactory;
import br.edu.ufba.softvis.visminer.retriever.SoftwareUnitRetriever;
import br.edu.ufba.softvis.visminerweb.beans.AffectedFile;
import br.edu.ufba.softvis.visminerweb.beans.Unit;

@ManagedBean(name = "commits")
@ViewScoped
public class CommitsView {

	@ManagedProperty(value = "#{selector}")
	private SelectorView selectorView;

	private int commitsCount = 0, commitId = -1;
	private Map<Integer, Commit> commitsMap = new TreeMap<Integer, Commit>();
	private List<AffectedFile> affectedFiles = new ArrayList<AffectedFile>();

	private Date initialDate = null, finalDate = null;

	private AffectedFile selectedFile;

	public SelectorView getSelectorView() {
		return selectorView;
	}

	public void setSelectorView(SelectorView selectorView) {
		this.selectorView = selectorView;
	}

	private List<Commit> getCommits(Tree tree) {
		CommitRetriever retriever = RetrieverFactory
				.create(CommitRetriever.class);

		commitsMap.clear();
		List<Commit> commits = retriever.retrieveByTree(tree);
		for (Commit commit : commits) {
			commitsMap.put(commit.getId(), commit);
		}
		return commits;
	}

	public Map<Integer, Commit> getCommitsMap() {
		return commitsMap;
	}

	public String getTimelineData() {
		String data = "var items = new vis.DataSet({type: { start: 'ISODate', end: 'ISODate' }});";
		data += "items.add([";

		commitsCount = 1;
		List<Commit> commits = getCommits(selectorView.getCurrentTree());
		for (Commit commit : commits) {
			Date commitDate = commit.getDate();

			if ((initialDate != null) && (finalDate != null)) {
				if (commitDate.before(initialDate)
						|| commitDate.after(finalDate)) {
					continue;
				}
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(commitDate);
			sdf = new SimpleDateFormat("hh:mm");
			String time = sdf.format(commit.getDate());

			if (commitsCount > 1) {
				data += ",";
			}

			data += "{id: " + commitsCount
					+ ", content: '<a href=\"#\" onclick=\"setCommitId("
					+ commit.getId() + "); return true;\">" + time
					+ "</a>', start: '" + date + "'}";

			commitsCount++;
		}
		data += "]);";

		return data;
	}

	public void resetFilter() {
		initialDate = finalDate = null;
	}
	
	public void setCommitId(int id) {
		commitId = id;
	}

	public int getCommitId() {
		return commitId;
	}

	public int getCommitsCount() {
		return commitsCount;
	}

	public void submmitChoice() {
		affectedFiles.clear();

		Commit commit = getSelectedCommit();
		if (commit != null) {
			for (File file : commit.getCommitedFiles()) {
				affectedFiles.add(new AffectedFile(file));
			}
		}
	}

	public Commit getSelectedCommit() {
		Commit commit = null;

		if (commitId != -1) {
			commit = commitsMap.get(commitId);
		}

		return commit;
	}

	public List<Unit> getSoftwareUnits() {
		List<Unit> units = new ArrayList<Unit>();

		if ((commitId != -1) && (selectedFile != null)) {
			SoftwareUnitRetriever retriever = RetrieverFactory
					.create(SoftwareUnitRetriever.class);

			List<SoftwareUnit> softwareUnits = retriever.findByFile(
					selectedFile.getId(), commitId);
			for (SoftwareUnit swu : softwareUnits) {
				units.add(new Unit(swu));

				if ((swu.getChildren() != null)
						&& (!swu.getChildren().isEmpty())) {
					for (SoftwareUnit cswu : swu.getChildren()) {
						units.add(new Unit(cswu));
					}
				}
			}
		}

		return units;
	}

	public List<AffectedFile> getAffectedFiles() {
		return affectedFiles;
	}

	public void setSelectedFile(AffectedFile file) {
		selectedFile = file;
	}

	public AffectedFile getSelectedFile() {
		return selectedFile;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

}
