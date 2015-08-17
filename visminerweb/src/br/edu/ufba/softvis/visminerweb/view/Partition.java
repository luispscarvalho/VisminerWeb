package br.edu.ufba.softvis.visminerweb.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminerweb.beans.partition.PartitionBranch;
import br.edu.ufba.softvis.visminerweb.beans.partition.PartitionItem;
import br.edu.ufba.softvis.visminerweb.util.FileUtils;

import com.google.gson.Gson;

@ManagedBean(name = "partition")
@ViewScoped
public class Partition {

	@ManagedProperty(value = "#{selector}")
	private Selector selector;

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	private Map<Integer, Map<Integer, Map<Integer, Integer>>> getPartitionTree(
			List<Commit> commits) {
		// Map of years includes a map of months that includes a map of days (in
		// each day a certain number of commits)
		Map<Integer, Map<Integer, Map<Integer, Integer>>> partition = new TreeMap<Integer, Map<Integer, Map<Integer, Integer>>>();
		for (Commit commit : commits) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(commit.getDate());
			// year already inserted in the map?
			int year = calendar.get(Calendar.YEAR);
			if (!partition.containsKey(year)) {
				// if not, create a new slot to insert the months
				partition.put(year,
						new TreeMap<Integer, Map<Integer, Integer>>());
			}
			Map<Integer, Map<Integer, Integer>> yearPart = partition.get(year);
			// month already inserted in the sub-map?
			int month = calendar.get(Calendar.MONTH);
			if (!yearPart.containsKey(month)) {
				yearPart.put(month, new TreeMap<Integer, Integer>());
			}
			Map<Integer, Integer> dayPart = yearPart.get(month);
			// day already inserted int the sub-sub-map?
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			if (!dayPart.containsKey(day)) {
				dayPart.put(day, 1);
			} else {
				dayPart.put(day, dayPart.get(day) + 1);
			}
		}

		return partition;
	}

	private String monthToString(int month) {
		String str = "";

		if (month == 0) {
			str = "January";
		} else if (month == 1) {
			str = "February";
		} else if (month == 2) {
			str = "March";
		} else if (month == 3) {
			str = "April";
		} else if (month == 4) {
			str = "May";
		} else if (month == 5) {
			str = "June";
		} else if (month == 6) {
			str = "July";
		} else if (month == 7) {
			str = "August";
		} else if (month == 8) {
			str = "September";
		} else if (month == 9) {
			str = "October";
		} else if (month == 10) {
			str = "November";
		} else if (month == 11) {
			str = "December";
		}

		return str;
	}

	private PartitionBranch<?> createPartition(List<Commit> commits) {
		// creates the root element of the partition table
		PartitionBranch<PartitionBranch<?>> root = new PartitionBranch<PartitionBranch<?>>();
		root.setName("commits");
		root.setChildren(new ArrayList<PartitionBranch<?>>());
		// creates partition tree (YEAR->MONTH->DAY->#COMMITS)
		Map<Integer, Map<Integer, Map<Integer, Integer>>> years = getPartitionTree(commits);
		for (Integer year : years.keySet()) {
			PartitionBranch<PartitionBranch<?>> yearBranch = new PartitionBranch<PartitionBranch<?>>();
			yearBranch.setName(year + "");
			yearBranch.setChildren(new ArrayList<PartitionBranch<?>>());
			root.getChildren().add(yearBranch);

			// creates partition tree (MONTH->DAY->#COMMITS)
			Map<Integer, Map<Integer, Integer>> months = years.get(year);
			for (Integer month : months.keySet()) {
				PartitionBranch<PartitionItem> monthBranch = new PartitionBranch<PartitionItem>();
				monthBranch.setName(monthToString(month));
				monthBranch.setChildren(new ArrayList<PartitionItem>());
				yearBranch.getChildren().add(monthBranch);

				// creates partition tree (DAY->#COMMITS)
				Map<Integer, Integer> days = months.get(month);
				for (Integer day : days.keySet()) {
					PartitionItem item = new PartitionItem();
					item.setSize(days.get(day));
					item.setName(day + " (" + item.getSize() + " commit(s))");
					monthBranch.getChildren().add(item);
				}
			}
		}

		return root;
	}

	public String getPartitionJSON() {
		// creates a new JSON file in the TEMP directory
		String jsonFile = FileUtils.generateJSONFileName();
		String jsonPath = FileUtils.getTempPath() + "/" + jsonFile;
		// creates partition branches
		PartitionBranch<?> partition = createPartition(selector.getCommits());
		try {
			Gson converter = new Gson();
			String jsonContent = converter.toJson(partition);

			FileUtils.writeFile(jsonPath, jsonContent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String url = "\"" + FileUtils.getContextPath() + FileUtils.TMP_PATH
				+ "/" + jsonFile + "\"";

		return url;
	}
}
