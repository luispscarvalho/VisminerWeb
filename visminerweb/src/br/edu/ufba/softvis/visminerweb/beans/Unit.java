package br.edu.ufba.softvis.visminerweb.beans;

import java.util.Map;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;

public class Unit {

	private String uid;
	private String name;
	private String metrics;

	public Unit(String uid, String name, String metrics) {
		this.uid = uid;
		this.name = name;
		this.metrics = metrics;
	}

	public Unit(SoftwareUnit softwareUnit) {
		uid = softwareUnit.getUid();
		name = softwareUnit.getName();

		metrics = "";
		Map<MetricUid, String> ms = softwareUnit.getMetricValues();

			for (MetricUid uid : ms.keySet()) {
				if (uid == null) {
					continue;
				}
				metrics += (metrics.equals("") ? "" : ", ") + uid.name()
						+ " = " + ms.get(uid);
			}
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((metrics == null) ? 0 : metrics.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit other = (Unit) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (metrics == null) {
			if (other.metrics != null)
				return false;
		} else if (!metrics.equals(other.metrics))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	public int compareTo(Unit unit) {
		return this.getUid().compareTo(unit.getUid());
	}
}
