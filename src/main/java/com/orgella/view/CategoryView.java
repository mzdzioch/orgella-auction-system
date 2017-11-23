package com.orgella.view;

import com.orgella.helpers.CategoryBuilder;
import com.orgella.helpers.DatabaseAccess;
import com.orgella.model.Category;
import com.orgella.model.Node;

import java.util.List;

public class CategoryView {

    public void display(Node<Category> root) {
        int index = 0;
        List<Node<Category>> nodeList = root.getChildren();
        displayCategory(nodeList, index);
    }

    private void displayCategory(List<Node<Category>> lists, int index) {

        if(lists.isEmpty()) {
            index--;
        }

        if (!lists.isEmpty()) {
            index++;

            for (Node<Category> categoryNode : lists) {

                printTab(index);
                System.out.println(categoryNode.getItem().toString());
                displayCategory(categoryNode.getChildren(), index);
            }

            index--;
        }
    }

    private void printTab(int number) {
        String tab = "";
        char x = '└';
        for(int i = 1; i < number; i++){
            tab += "|--";
        }
        tab += x + "--";
        System.out.print(tab);
    }

//    private Node<Category> getBuilder(){
//        return (new CategoryBuilder(new DatabaseAccess())).getBuilder();
//    }
}
