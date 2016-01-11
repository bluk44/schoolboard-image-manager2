package imagemanager.gui.category;

import imagemanager.controller.CategoryController;
import imagemanager.model.Category;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class CategoryViewPanel extends JPanel {

	private JTree categoryTree;

	private Map<String, DefaultMutableTreeNode> categoryNodes = new HashMap<String, DefaultMutableTreeNode>();
	
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem chooseCategoriesMenuItem = new JMenuItem("Wybież zaznaczone kategorie");
	private JMenuItem assignImagesMenuItem = new JMenuItem("Dołącz zdjęcia do kategorii");
	
	public SelectedCategoriesPanel selectedCategoriesPanel;
	public CategoryController categoryController;
	
	{
		popupMenu.add(chooseCategoriesMenuItem);
		chooseCategoriesMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Collection<CategoryViewObject> catObjs = getSelectedCategories();
				for (CategoryViewObject categoryViewObject : catObjs) {
					selectedCategoriesPanel.addCategory(categoryViewObject);
				}
			}
		});
		popupMenu.add(assignImagesMenuItem);
		assignImagesMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				categoryController.assignImagesToCategories();
			}
		});
	}
	
	public CategoryViewPanel(){
		System.out.println("categoryViewPanel constructor called");
		//categoryTree = new JTree();
		//this.setLayout(new GridLayout());
		//this.add(categoryTree);

	}
	
	public void initView(Collection<Category> rootCategories) {
		System.out.println("CategoryViewPanel initView called");
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new CategoryViewObject(-1l, ""));
		categoryTree = new JTree(root);
		categoryTree.setRootVisible(false);
		for (Category category : rootCategories) {
			DefaultMutableTreeNode catNode = createTreeNode(category);
			addChildNodes(catNode, category.getSubCategories());
			insertChildInOrder(root, catNode);
		}

		this.setLayout(new GridLayout());
		this.add(categoryTree);
		setMinimumSize(new Dimension(200, 100));
		categoryTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				//System.out.println(e.getNewLeadSelectionPath().getLastPathComponent().getClass());				
			}
		});
		
		((DefaultTreeModel)categoryTree.getModel()).reload();
		categoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		
		for (String str : categoryNodes.keySet()) {
			System.out.println(str);
		}
		
		categoryTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON3){
					popupMenu.show(categoryTree, e.getX(), e.getY());
				}
			}
		});
	}
	
	public CategoryViewObject getSelectedCategory(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) categoryTree.getLastSelectedPathComponent();
		if(node == null) return null;
		
		return (CategoryViewObject) node.getUserObject();
	}
	
	public Collection<CategoryViewObject> getSelectedCategories(){
		TreePath[] paths = categoryTree.getSelectionPaths();
		ArrayList<CategoryViewObject> catObjs = new ArrayList<CategoryViewObject>();
		if(paths == null) return catObjs;

		for (TreePath treePath : paths) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			catObjs.add((CategoryViewObject) node.getUserObject());
		}
		return catObjs;
	}
	
	public void createCategory(String parentTitle, Category category) {
		DefaultMutableTreeNode parent = categoryNodes.get(parentTitle);
		DefaultMutableTreeNode child = createTreeNode(category);
		insertChildInOrder(parent, child);
	}

	public void createCategory(Category category) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) categoryTree.getModel()
				.getRoot();
		DefaultMutableTreeNode child = createTreeNode(category);
		insertChildInOrder(root, child);
	}
	
	public void removeCategory(String title){
		DefaultMutableTreeNode node = categoryNodes.get(title);
		DefaultTreeModel treeModel = (DefaultTreeModel) categoryTree.getModel();
		treeModel.removeNodeFromParent(node);
		categoryNodes.remove(title);
		removeNode(node);
	}
	
	public void modifyCategory(String title, Category category){
		
		DefaultMutableTreeNode node = categoryNodes.get(title);
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
		DefaultTreeModel treeModel = (DefaultTreeModel) categoryTree.getModel();
		treeModel.removeNodeFromParent(node);
		categoryNodes.remove(title);
		node.setUserObject(new CategoryViewObject(category.getId(), category.getTitle()));
		System.out.println(node.getUserObject());
		insertChildInOrder(parent, node);
		categoryNodes.put(category.getTitle(), node);
	}

	private DefaultMutableTreeNode createTreeNode(Category category) {
		return new DefaultMutableTreeNode(new CategoryViewObject(
				category.getId(), category.getTitle()));
	}

	private void addChildNodes(DefaultMutableTreeNode parent,
			Collection<Category> subCategories) {
		for (Category category : subCategories) {
			DefaultMutableTreeNode newChild = createTreeNode(category);
			if (category.getSubCategories().size() != 0) {
				addChildNodes(newChild, category.getSubCategories());
			}
			insertChildInOrder(parent, newChild);
			categoryNodes.put(category.getTitle(), newChild);
		}
	}

	private void insertChildInOrder(DefaultMutableTreeNode parentNode,
			DefaultMutableTreeNode childNode) {
		int childCount = parentNode.getChildCount();

		// Insert in alphabetical order
		int i = 0;

		while (i < childCount
				&& (((CategoryViewObject) ((DefaultMutableTreeNode) parentNode
						.getChildAt(i)).getUserObject())
						.compareTo((CategoryViewObject) childNode
								.getUserObject()) < 0)) {
			i++;
		}

		DefaultTreeModel treeModel = (DefaultTreeModel) categoryTree.getModel();
		treeModel.insertNodeInto(childNode, parentNode, i);
		categoryNodes.put(((CategoryViewObject)childNode.getUserObject()).getTitle(), childNode);		

	}
	
	private void removeNode(DefaultMutableTreeNode node){
		for (Enumeration e = node.children(); e.hasMoreElements();) {
			DefaultMutableTreeNode obj = (DefaultMutableTreeNode) e.nextElement();
			removeNode(obj);
		}
		categoryNodes.remove(((CategoryViewObject)node.getUserObject()).getTitle());
		
	}

	public CategoryController getCategoryController() {
		return categoryController;
	}

	public void setCategoryController(CategoryController categoryController) {
		this.categoryController = categoryController;
	}

	public SelectedCategoriesPanel getSelectedCategoriesPanel() {
		return selectedCategoriesPanel;
	}

	public void setSelectedCategoriesPanel(SelectedCategoriesPanel selectedCategoriesPanel) {
		this.selectedCategoriesPanel = selectedCategoriesPanel;
	}
	
}
