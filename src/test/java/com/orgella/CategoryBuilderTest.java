//package com.orgella;
//
//import com.orgella.helpers.CategoryBuilder;
//import com.orgella.model.Category;
//import com.orgella.model.Node;
//import org.junit.Test;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//
//
//public class CategoryBuilderTest {
//
//    CategoryBuilder testeObject = new CategoryBuilder();
//
//    @Test
//    public void shouldReturnTrueIfRootIsCreated(){
//        assertThat(testeObject.getBuilder().isRoot()).isTrue();
//    }
//
//    @Test
//    public void shouldReturnFalseIfListIsNotEmpty(){
//        assertThat(testeObject.getBuilder().getChildren().isEmpty()).isFalse();
//    }
//
//    @Test
//    public void shouldBeEqualIfElectronicCategoryExist(){
//        List<Node<Category>> testedList = testeObject.getBuilder().getChildren();
//
//        assertThat(testedList.get(0).getItem().getName()).isEqualTo("Electronics");
//    }
//
//    @Test
//    public void shouldBeEqualIfMotorsCategoryExist(){
//        List<Node<Category>> testedList = testeObject.getBuilder().getChildren();
//
//        assertThat(testedList.get(1).getItem().getName()).isEqualTo("Motors");
//    }
//}
