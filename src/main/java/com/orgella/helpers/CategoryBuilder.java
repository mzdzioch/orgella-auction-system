package com.orgella.helpers;

import com.orgella.model.Node;
import com.orgella.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;

public class CategoryBuilder {

    private Node<Category> rootCategory;
    private List<Integer> idList;
    private Map<Integer, Node<Category>> categoryMap = new HashMap<>();
    private DatabaseAccess databaseAccess;

    public CategoryBuilder(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
        this.rootCategory = createCategory();
    }

    private Node<Category> createCategory() {

        rootCategory = new Node<Category>(null, new Category());
        rootCategory = getRootCategories();

        for (Node<Category> categoryNode : rootCategory.getChildren()) {
            categoryNode.addChildren(getChildrenByParentId(categoryNode.getItem().getIdCategory()));
        }

        //rootCategory.getChildren().stream()
        //        .forEach(n -> n.addChildren(getChildrenByParentId(n.getItem().getIdCategory())));

        return rootCategory;
    }

    public Node<Category> getBuilder() {
        return rootCategory;
    }

    public void addRootCategory(int id, String name) {

        if (rootCategory.isRoot())
            rootCategory.addChild(new Category(id, name));
        else {
            rootCategory = new Node<Category>(null, new Category());
            rootCategory.addChild(new Category(id, name));
        }
    }

    public boolean addSubCategory(String parentCategory, String name) {
        categoryMap = copyCategoryToMap();

        if (findParentByName(parentCategory)) {
            for (Node<Category> categoryNode : categoryMap.values()) {
                if (categoryNode.getItem().getName().equals(parentCategory)) {
                    categoryNode.addChild(new Node<Category>(new Category(nextCategoryId(categoryNode), name)));
                    return true;
                }
            }
        }

        return false;
    }

    public List<Integer> getCategoryAndSubcategoriesListId(int categoryId){

        if(isParentExist(categoryId)){
            Node<Category> nodeTemp = findParentById(categoryId);

            idList = new ArrayList<>();
            idList.add(nodeTemp.getItem().getIdCategory());

            for (Node<Category> categoryNode : nodeTemp.getChildren()) {
                idList.add(categoryNode.getItem().getIdCategory());
            }
        }

        return idList;
    }

    private Node<Category> findParentById(Integer parentId) {
        categoryMap = copyCategoryToMap();

        return categoryMap.get(parentId);
    }

    public boolean isParentExist(Integer parentId) {
        categoryMap = copyCategoryToMap();

        if(categoryMap.get(parentId) == null)
            return false;
        else
            return true;
    }

    private boolean findParentByName(String parentName) {

        categoryMap = copyCategoryToMap();

        for (Node<Category> categoryNode : categoryMap.values()) {
            if (categoryNode.getItem().getName().equals(parentName))
                return true;
        }

        return false;
    }

    private Map<Integer, Node<Category>> copyCategoryToMap() {

        List<Node<Category>> nodeList = rootCategory.getChildren();

        writeToMap(nodeList);

        return categoryMap;
    }

    private void writeToMap(List<Node<Category>> lists) {

        if (!lists.isEmpty()) {
            for (Node<Category> categoryNode : lists) {
                categoryMap.put(categoryNode.getItem().getIdCategory(), categoryNode);

                writeToMap(categoryNode.getChildren());
            }
        }
    }

    private int nextCategoryId(Node<Category> parentNode) {
        return parentNode.getChildren().get(returnLastChildOfParent(parentNode)).getItem().getIdCategory() + 1;
    }

    private int returnLastChildOfParent(Node<Category> parentNode) {
        return parentNode.getChildren().size()-1;
    }

    public Node<Category> getRootCategories(){

        rootCategory = new Node<Category>(null, new Category());

        String categoryQuery = "SELECT * FROM category WHERE parent_id = '0';";

        ResultSet resultSet;
        resultSet = databaseAccess.readData(categoryQuery);

        try {
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String category_name = resultSet.getString("category_name");
                int parentId = resultSet.getInt("parent_id");
                int childId = resultSet.getInt("next_element_id");
                rootCategory.addChild(new Category(id, category_name));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rootCategory;
    }

    private List<Node> getChildrenByParentId(int parentId) {

        String categoryQuery = "SELECT * FROM category WHERE parent_id = '" + parentId + "';";
        List<Node> nodeList = new ArrayList<>();

        ResultSet resultSet;
        resultSet = databaseAccess.readData(categoryQuery);

        try {
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String category_name = resultSet.getString("category_name");
                int parent_Id = resultSet.getInt("parent_id");
                int childId = resultSet.getInt("next_element_id");

                nodeList.add(new Node<Category>(new Category(id, category_name)));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nodeList;
    }
}
