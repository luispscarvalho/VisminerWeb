package br.edu.ufba.softvis.visminerweb.beans.partition;

import java.util.List;

public class PartitionBranch<C> {

	private String name;

	private List<C> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<C> getChildren() {
		return children;
	}

	public void setChildren(List<C> children) {
		this.children = children;
	}

}
