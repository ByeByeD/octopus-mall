package com.octopusmall.business.service.impl;

import com.octopusmall.business.dto.AddProductReqDto;
import com.octopusmall.business.dto.ProductRespDto;
import com.octopusmall.business.dto.QueryProductReqDto;
import com.octopusmall.business.dto.QueryShopReqDto;
import com.octopusmall.business.dto.ShopRespDto;
import com.octopusmall.business.dto.UpdateProductReqDto;
import com.octopusmall.business.entity.OtpsProduct;
import com.octopusmall.business.service.OtpsProductService;
import com.octopusmall.business.service.OtpsShopService;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.business.mapper.OtpsProductMapper;
import com.octopusmall.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OtpsProductServiceImpl implements OtpsProductService {

    private final OtpsProductMapper otpsProductMapper;
    private final OtpsShopService otpsShopService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProduct(AddProductReqDto req) {
        QueryShopReqDto queryShopReqDto = new QueryShopReqDto();
        queryShopReqDto.setId(req.getShopId());
        ShopRespDto shop = otpsShopService.getShopByCondition(queryShopReqDto);
        if (shop == null) {
            throw new OtpsBaseException("您还没有创建店铺，无法添加商品");
        }
        // 审核未通过也可以添加商品，为商家考虑，只有商家通过审核后，且店铺状态为开启才会在顾客页面看到店铺信息
        // 此外店铺是不能删除的，店铺ID不会变化，因此允许新增商品不影响业务
//        if (!"pass".equals(shop.getAuditStatus())) {
//            throw new OtpsBaseException("店铺尚未通过审核，无法添加商品");
//        }
//        if (!"normal".equals(shop.getShopStatus())) {
//            throw new OtpsBaseException("店铺状态异常，无法添加商品");
//        }

        OtpsProduct product = new OtpsProduct();
        product.setId(CommonUtil.getSnowID());
        product.setShopId(shop.getId());
        product.setName(req.getName());
        product.setCategory(req.getCategory());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setImageUrl(req.getImageUrl());
        product.setRemark(req.getRemark());
        product.setStatus("onsale");
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());

        otpsProductMapper.insertProduct(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(UpdateProductReqDto req) {
        ProductRespDto product = otpsProductMapper.getProductByIdAndShopId(req.getId(), req.getShopId());
        if (product == null) {
            throw new OtpsBaseException("商品不存在或不属于您的店铺");
        }

        OtpsProduct updateProduct = new OtpsProduct();
        updateProduct.setId(req.getId());
        updateProduct.setShopId(req.getShopId());
        updateProduct.setName(req.getName());
        updateProduct.setCategory(req.getCategory());
        updateProduct.setPrice(req.getPrice());
        updateProduct.setStock(req.getStock());
        updateProduct.setImageUrl(req.getImageUrl());
        updateProduct.setRemark(req.getRemark());
        updateProduct.setStatus(req.getStatus());
        updateProduct.setUpdateTime(LocalDateTime.now());

        otpsProductMapper.updateProduct(updateProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(String id, String shopId) {
        ProductRespDto product = otpsProductMapper.getProductByIdAndShopId(id, shopId);
        if (product == null) {
            throw new OtpsBaseException("商品不存在");
        }

        otpsProductMapper.deleteProduct(id, shopId);
    }

    @Override
    public ProductRespDto getProductById(String id) {
        ProductRespDto product = otpsProductMapper.getProductById(id);
        if (product == null) {
            throw new OtpsBaseException("商品不存在或不属于您的店铺");
        }
        return product;
    }

    @Override
    public List<ProductRespDto> listProduct(QueryProductReqDto query) {
        return otpsProductMapper.listProduct(query, query.getShopId());
    }
}
