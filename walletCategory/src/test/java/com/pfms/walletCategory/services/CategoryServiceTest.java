package com.pfms.walletCategory.services;

import com.pfms.walletCategory.dto.CategoryNameDTO;
import com.pfms.walletCategory.dto.GoalDTO;
import com.pfms.walletCategory.exception.WalletCategoryException;
import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.repository.CategoryRepository;
import com.pfms.walletCategory.repository.GoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService underTest;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GoalRepository goalRepository;

    @Test
    void getAll() {
        underTest.getAll();
        verify(categoryRepository).findAll();
    }

    @Test
    void canAddCategory() {
        Category c1=new Category("food","anand@gmail.com");
        when(categoryRepository.save(c1)).thenReturn(c1);

        assertEquals(c1,underTest.addCategory(c1));

    }

    @Test
    void shouldThrowExceptionUpdateCategoryWhenCategoryIsNull() {

        String userEmail="abc@gmail.com";
        String categoryName="food";


        List<String> listCategory=Arrays.asList("food","shopping");

        GoalDTO goalDTO=new GoalDTO("Goal 1",userEmail,233D,23D,listCategory);

        when(categoryRepository.findByUserEmailAndCategoryName(userEmail,categoryName)).thenReturn(null);
        assertThrows(Exception.class, () -> underTest.updateCategory(listCategory,userEmail,goalDTO));

    }

    /*
    @Test
    void canGetCategory() {
        Category c1=new Category("food","anand@gmail.com");
        Long id =1L;
        when(categoryRepository.findById(id).get()).thenReturn(c1);
        assertEquals(c1,underTest.getCategory(id));
    }

     */

    @Test
    void creditTransactionWhenCategoryIsNull() {

        String userEmail="abc@gmail.com";
        String categoryName="food";
        when(categoryRepository.findByUserEmailAndCategoryName(userEmail,categoryName)).thenReturn(null);

        assertThrows(WalletCategoryException.class, () -> underTest.creditTransaction(userEmail,categoryName,234));
    }

    /*
    @Test
    void canGetCategoryListObject() {

        String userEmail="anand@gmail.com";

        CategoryNameDTO cDTO1=new CategoryNameDTO(1L,"food",256D);
        CategoryNameDTO cDTO2=new CategoryNameDTO(2L,"wedding",257D);
        CategoryNameDTO cDTO3=new CategoryNameDTO(3L,"travel",5336D);
        List<CategoryNameDTO> listCategoryDTO=Arrays.asList(cDTO1,cDTO2,cDTO3);

        Category c1=new Category("food",userEmail);
        Category c2=new Category("wedding",userEmail);
        Category c3=new Category("travel",userEmail);
        List<Category> listCategory=Arrays.asList(c1,c2,c3);

        when(categoryRepository.findAllByUserEmail(userEmail)).thenReturn(listCategory);

        assertThat(listCategoryDTO).hasSameElementsAs(underTest.getcategoryName(userEmail));
       // assertIterableEquals(listCategoryDTO,underTest.getcategoryName(userEmail));
        //assertArrayEquals(listCategoryDTO.toArray(),underTest.getcategoryName(userEmail).toArray(),"Expected"+listCategoryDTO.get(0).getCategoryName()+"Actual"+underTest.getcategoryName(userEmail).get(0).getCategoryName());
    }
    */
    @Test
    void canGetCategoryNamesListString() {
        String category[]
                = new String[] { "food", "travel", "wedding" };
        List<String>  categoryNameList= Arrays.asList(category);

        when(categoryRepository.listAllCategories("anand@gmail.com")).thenReturn(categoryNameList);

        assertEquals(categoryNameList,underTest.getNames("anand@gmail.com"));
    }
}

