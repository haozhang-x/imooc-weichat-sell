package cn.cloudx.weichatsell.controller;

import cn.cloudx.weichatsell.dataobject.ProductCategory;
import cn.cloudx.weichatsell.dataobject.ProductInfo;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.service.CategoryService;
import cn.cloudx.weichatsell.service.ProductService;
import cn.cloudx.weichatsell.dataobject.ProductCategory;
import cn.cloudx.weichatsell.dataobject.ProductInfo;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.form.ProductForm;
import cn.cloudx.weichatsell.service.CategoryService;
import cn.cloudx.weichatsell.service.ProductService;
import cn.cloudx.weichatsell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
 * 卖家端商品
 *
 * @author zhanghao
 * @date 2018/05/03
 */
@Controller
@RequestMapping("/seller/product")
@Slf4j
public class SellerProductController {


    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public SellerProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /**
     * 列表
     *
     * @param page
     * @param size
     * @param modelMap
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size, ModelMap modelMap) {
        Page<ProductInfo> productInfoPage = productService.findAll(PageRequest.of(page - 1, size));
        modelMap.put("productInfoPage", productInfoPage);
        modelMap.put("currentPage", page);
        modelMap.put("size", size);
        return new ModelAndView("product/list", modelMap);
    }


    /**
     * 上架
     */
    @GetMapping("/on_sale")
    public ModelAndView onSale(@RequestParam("productId") String productId, ModelMap modelMap) {
        modelMap.put("url", "/sell/seller/product/list");
        try {
            productService.onSale(productId);
        } catch (SellException e) {
            log.error("[商品上架]发生异常{}", e);
            modelMap.put("msg", e.getMessage());
            return new ModelAndView("/common/error", modelMap);
        }
        return new ModelAndView("/common/success", modelMap);
    }


    /**
     * 下架
     *
     * @return
     */
    @GetMapping("/off_sale")
    public ModelAndView offSale(@RequestParam("productId") String productId, ModelMap modelMap) {
        modelMap.put("url", "/sell/seller/product/list");
        try {
            productService.offSale(productId);
        } catch (SellException e) {
            log.error("[商品下架]发生异常{}", e);
            modelMap.put("msg", e.getMessage());
            return new ModelAndView("/common/error", modelMap);
        }
        return new ModelAndView("/common/success", modelMap);
    }


    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId", required = false) String productId, ModelMap modelMap) {

        if (!StringUtils.isEmpty(productId)) {
            ProductInfo productInfo = productService.findOne(productId);
            modelMap.put("productInfo", productInfo);
        }
        //查询所有的类目
        List<ProductCategory> productCategoryList = categoryService.findAll();
        modelMap.put("categoryList", productCategoryList);
        return new ModelAndView("product/index", modelMap);

    }


    @PostMapping("/save")
//    @CachePut(value = "product",key = "123")
    @CacheEvict(value = "product", key = "123")
    public ModelAndView save(@Valid ProductForm productForm, BindingResult bindingResult, ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            modelMap.put("msg", bindingResult.getFieldError().getDefaultMessage());
            modelMap.put("url", "/sell/seller/product/index");
            return new ModelAndView("common/error", modelMap);
        }
        ProductInfo productInfo = new ProductInfo();
        try {
            //如果productId为空 则是新增
            if (!StringUtils.isEmpty(productForm.getProductId())) {
                productInfo = productService.findOne(productForm.getProductId());
            } else {
                productForm.setProductId(KeyUtil.genUniqueKey());
            }
            BeanUtils.copyProperties(productForm, productInfo);
            productService.save(productInfo);
        } catch (Exception e) {
            modelMap.put("msg", e.getMessage());
            modelMap.put("url", "/sell/seller/product/index");
            return new ModelAndView("common/error", modelMap);
        }
        modelMap.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", modelMap);
    }


}
