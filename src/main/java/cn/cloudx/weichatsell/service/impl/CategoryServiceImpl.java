package cn.cloudx.weichatsell.service.impl;

import cn.cloudx.weichatsell.dataobject.ProductCategory;
import cn.cloudx.weichatsell.repository.ProductCategoryRepository;
import cn.cloudx.weichatsell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhanghao
 * @date 2018/04/19
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private ProductCategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(ProductCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return repository.findById(categoryId).isPresent() ? repository.findById(categoryId).get() : null;
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
