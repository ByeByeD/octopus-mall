package com.octopusmall.business.service;

import com.octopusmall.business.dto.AddProductReqDto;
import com.octopusmall.business.dto.ProductRespDto;
import com.octopusmall.business.dto.QueryProductReqDto;
import com.octopusmall.business.dto.UpdateProductReqDto;
import java.util.List;

public interface OtpsProductService {

    void addProduct(AddProductReqDto req);

    void updateProduct(UpdateProductReqDto req);

    void deleteProduct(String id, String shopId);

    ProductRespDto getProductById(String id);

    List<ProductRespDto> listProduct(QueryProductReqDto query);
}
