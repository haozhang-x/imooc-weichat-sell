package cn.cloudx.weichatsell.controller;

import cn.cloudx.weichatsell.dataobject.ProductCategory;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.service.CategoryService;
import cn.cloudx.weichatsell.dataobject.ProductCategory;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.form.CategoryForm;
import cn.cloudx.weichatsell.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * 买家类目
 *
 * @author zhanghao
 * @date 2018/05/05
 */
@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {

    private CategoryService categoryService;

    @Autowired
    public SellerCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 类目列表
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(ModelMap modelMap) {
        List<ProductCategory> categoryList = categoryService.findAll();
        modelMap.put("categoryList", categoryList);
        return new ModelAndView("category/list", modelMap);
    }

    /**
     * @param categoryId
     * @param modelMap
     * @return
     */

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId", required = false) Integer categoryId, ModelMap modelMap) {
        if (categoryId != null) {
            ProductCategory category = categoryService.findOne(categoryId);
            modelMap.put("category", category);
        }
        return new ModelAndView("category/index", modelMap);
    }


    /**
     * 保存
     *
     * @param form
     * @param bindingResult
     * @param modelMap
     * @return
     */
    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm form, BindingResult bindingResult, ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            modelMap.put("url", "/sell/seller/category/index");
            modelMap.put("msg", bindingResult.getFieldError().getDefaultMessage());
            return new ModelAndView("common/error", modelMap);
        }
        ProductCategory category = new ProductCategory();
        try {
            if (!StringUtils.isEmpty(form.getCategoryId())) {
                category = categoryService.findOne(form.getCategoryId());
            }
            BeanUtils.copyProperties(form, category);
            categoryService.save(category);
        } catch (SellException e) {
            modelMap.put("msg", e.getMessage());
            modelMap.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", modelMap);
        }
        modelMap.put("url", "/sell/seller/category/list");
        return new ModelAndView("common/success", modelMap);
    }

}
