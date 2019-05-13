package cn.cloudx.weichatsell.service.impl;


import cn.cloudx.weichatsell.dataobject.ProductInfo;
import cn.cloudx.weichatsell.dto.CartDTO;
import cn.cloudx.weichatsell.enums.ProductStatusEnum;
import cn.cloudx.weichatsell.enums.ResultEnum;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.repository.ProductInfoRepository;
import cn.cloudx.weichatsell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    private ProductInfoRepository repository;

    @Autowired
    public ProductServiceImpl(ProductInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).isPresent() ? repository.findById(productId).get() : null;
    }


    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }


    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }


    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    /**
     * 加库存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            Optional<ProductInfo> productInfo = repository.findById(cartDTO.getProductId());
            if (!productInfo.isPresent()) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            Integer stock = productInfo.get().getProductStock() + cartDTO.getProductQuantity();
            productInfo.get().setProductStock(stock);
            repository.save(productInfo.get());
        }
    }

    /**
     * 减库存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            Optional<ProductInfo> productInfoOptional = repository.findById(cartDTO.getProductId());
            if (!productInfoOptional.isPresent()) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            Integer stock = productInfoOptional.get().getProductStock() - cartDTO.getProductQuantity();
            if (stock < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROT);
            }
            productInfoOptional.get().setProductStock(stock);
            repository.save(productInfoOptional.get());

        }
    }

    /**
     * 上架
     */
    @Override
    public ProductInfo onSale(String productId) {
        Optional<ProductInfo> optionalProductInfo = repository.findById(productId);
        if (!optionalProductInfo.isPresent()) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        ProductInfo productInfo = optionalProductInfo.get();
        if (productInfo.getProductStatus().equals(ProductStatusEnum.UP.getCode())) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(productInfo);
    }

    /**
     * 下架
     */
    @Override
    public ProductInfo offSale(String productId) {
        Optional<ProductInfo> optionalProductInfo = repository.findById(productId);
        if (!optionalProductInfo.isPresent()) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        ProductInfo productInfo = optionalProductInfo.get();
        if (productInfo.getProductStatus().equals(ProductStatusEnum.DOWN.getCode())) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo);
    }
}
