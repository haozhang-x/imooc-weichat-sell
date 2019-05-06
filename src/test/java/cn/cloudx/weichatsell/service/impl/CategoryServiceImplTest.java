package cn.cloudx.weichatsell.service.impl;

import cn.cloudx.weichatsell.dataobject.ProductCategory;
import cn.cloudx.weichatsell.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhanghao
 * @date 2018/04/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl productCategoryService;

    @Test
    public void findOne() {
        ProductCategory one = productCategoryService.findOne(9);
        Assert.assertEquals(new Integer(9), one.getCategoryId());
    }

    @Test
    public void findAll() {
        List<ProductCategory> productCategoryList = productCategoryService.findAll();
        Assert.assertNotEquals(0, productCategoryList.size());

    }

    @Test
    public void findByCategoryTypeIn() {
        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeIn(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        Assert.assertNotEquals(0, productCategoryList.size());

    }

    @Test
    public void save() {
        ProductCategory productCategory = new ProductCategory("男生专享", 15);
        ProductCategory result = productCategoryService.save(productCategory);
        Assert.assertNotNull(result);

    }
}