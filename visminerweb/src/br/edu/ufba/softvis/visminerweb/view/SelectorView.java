package br.edu.ufba.softvis.visminerweb.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.retriever.RepositoryRetriever;
import br.edu.ufba.softvis.visminer.retriever.RetrieverFactory;

@ManagedBean(name = "selector")
@ViewScoped
public class SelectorView {

	private Repository repository;
	private List<Repository> repositories = new ArrayList<Repository>();

	private TreeNode treeNodes;
	private Tree currentTree;

	@PostConstruct
	private void initRepositories() {
		RepositoryRetriever retriever = RetrieverFactory.create(RepositoryRetriever.class);
		
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

	public void setCurrentTree(Tree tree) {
		this.currentTree = tree;
	}

	public Tree getCurrentTree() {
		return currentTree;
	}

}
