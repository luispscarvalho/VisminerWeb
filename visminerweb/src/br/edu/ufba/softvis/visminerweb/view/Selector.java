package br.edu.ufba.softvis.visminerweb.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.retriever.CommitRetriever;
import br.edu.ufba.softvis.visminer.retriever.RepositoryRetriever;
import br.edu.ufba.softvis.visminerweb.factory.RetrieverFactory;

@ManagedBean(name = "selector")
@ViewScoped
public class Selector {

	private Repository repository;
	private List<Repository> repositories = new ArrayList<Repository>();
	private Map<Integer, Commit> commitsMap = new TreeMap<Integer, Commit>();

	private int commitId = -1;

	private TreeNode treeNodes;
	private Tree selectedTree;

	@PostConstruct
	private void initRepositories() {
		RepositoryRetriever retriever = RetrieverFactory
				.create(RepositoryRetriever.class);

		repositories.clear();
		repositories.addAll(retriever.retrieveAll());
	}

	private void buildTree() {
		if (repository != null) {
			List<Tree> trees = repository.getTrees();

			treeNodes = new DefaultTreeNode(repository.getName(), null);

			for (Tree t : trees) {
				new DefaultTreeNode(t, treeNodes);
			}
		} else {
			treeNodes = new DefaultTreeNode("No items to show", null);
		}
	}

	public List<Repository> getRepositories() {
		return repositories;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;

		buildTree();
	}

	public TreeNode getTreeNodes() {
		return treeNodes;
	}

	public void setSelectedTree(Tree tree) {
		this.selectedTree = tree;

		retrieveCommits();
	}

	public Tree getSelectedTree() {
		return selectedTree;
	}

	private void retrieveCommits() {
		List<Commit> commits = new ArrayList<Commit>();
		commitsMap.clear();

		if (selectedTree != null) {
			CommitRetriever retriever = RetrieverFactory
					.create(CommitRetriever.class);

			commits.addAll(retriever.retrieveByTree(selectedTree));
			for (Commit commit : commits) {
				commitsMap.put(commit.getId(), commit);
			}
		}
	}

	public List<Commit> getCommits() {
		return new ArrayList<Commit>(commitsMap.values());
	}

	public Map<Integer, Commit> getCommitsMap() {
		return commitsMap;
	}

	public void setCommitId(int id) {
		commitId = id;
	}

	public int getCommitId() {
		return commitId;
	}

	public Commit getSelectedCommit() {
		Commit commit = null;

		if (commitId != -1) {
			commit = commitsMap.get(commitId);
		}

		return commit;
	}

}
