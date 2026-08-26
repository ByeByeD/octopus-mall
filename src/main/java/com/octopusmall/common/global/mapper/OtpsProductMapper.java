package com.octopusmall.common.global.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.octopusmall.business.dto.ProductRespDto;
import com.octopusmall.business.dto.QueryProductReqDto;
import com.octopusmall.business.entity.OtpsProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OtpsProductMapper extends BaseMapper<OtpsProduct> {

    int insertProduct(@Param("product") OtpsProduct product);

    int updateProduct(@Param("product") OtpsProduct product);

    int deleteProduct(@Param("id") String id, @Param("shopId") String shopId);

    ProductRespDto getProductById(@Param("id") String id);

    ProductRespDto getProductByIdAndShopId(@Param("id") String id, @Param("shopId") String shopId);

    List<ProductRespDto> listProduct(@Param("query") QueryProductReqDto query, @Param("shopId") String shopId);
}
