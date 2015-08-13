package br.edu.ufba.softvis.visminerweb.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.File;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.retriever.SoftwareUnitRetriever;
import br.edu.ufba.softvis.visminerweb.beans.AffectedFile;
import br.edu.ufba.softvis.visminerweb.beans.Unit;
import br.edu.ufba.softvis.visminerweb.factory.RetrieverFactory;

@ManagedBean(name = "timeline")
@ViewScoped
public class Timeline {

	@ManagedProperty(value = "#{selector}")
	private Selector selector;

	private List<AffectedFile> affectedFiles = new ArrayList<AffectedFile>();

	private Date initialDate = null, finalDate = null;

	private AffectedFile selectedFile;

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public String getTimelineData() {
		String data = "var items = new vis.DataSet({type: { start: 'ISODate', end: 'ISODate' }});";
		data += "items.add([";

		int count = 1;
		List<Commit> commits = selector.getCommits();
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

			if (count > 1) {
				data += ",";
			}

			data += "{id: " + count
					+ ", content: '<a href=\"#\" onclick=\"setCommitId("
					+ commit.getId() + "); return true;\">" + time
					+ "</a>', start: '" + date + "'}";

			count++;
		}
		data += "]);";

		return data;
	}

	public void resetFilter() {
		initialDate = finalDate = null;
	}

	public void submmitChoice() {
		affectedFiles.clear();

		Commit commit = selector.getSelectedCommit();
		if (commit != null) {
			for (File file : commit.getCommitedFiles()) {
				affectedFiles.add(new AffectedFile(file));
			}
		}
	}

	public List<Unit> getSoftwareUnits() {
		List<Unit> units = new ArrayList<Unit>();

		if ((selector.getCommitId() != -1) && (selectedFile != null)) {
			SoftwareUnitRetriever retriever = RetrieverFactory
					.create(SoftwareUnitRetriever.class);

			List<SoftwareUnit> softwareUnits = retriever.findByFile(
					selectedFile.getId(), selector.getCommitId());
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
