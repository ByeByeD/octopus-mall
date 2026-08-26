package com.octopusmall.business.controller;

import com.octopusmall.business.dto.AddProductReqDto;
import com.octopusmall.business.dto.DeleteProductReqDto;
import com.octopusmall.business.dto.ProductRespDto;
import com.octopusmall.business.dto.QueryProductReqDto;
import com.octopusmall.business.dto.UpdateProductReqDto;
import com.octopusmall.business.service.OtpsProductService;
import com.octopusmall.common.global.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/business/product")
@RequiredArgsConstructor
public class ProductController {

    private final OtpsProductService otpsProductService;

    /**
     * 新增商品
     */
    @PostMapping("/add")
    public ResponseDto addProduct(@Valid @RequestBody AddProductReqDto req) {
        otpsProductService.addProduct(req);
        return new ResponseDto("0", "success");
    }

    /**
     * 修改商品
     */
    @PostMapping("/update")
    public ResponseDto updateProduct(@Valid @RequestBody UpdateProductReqDto req) {
        otpsProductService.updateProduct(req);
        return new ResponseDto("0", "success");
    }

    /**
     * 删除商品
     */
    @PostMapping("/delete")
    public ResponseDto deleteProduct(@Valid @RequestBody DeleteProductReqDto req) {
        otpsProductService.deleteProduct(req.getId(), req.getShopId());
        return new ResponseDto("0", "success");
    }

    /**
     * 查询商品详情
     */
    @GetMapping("/get")
    public ResponseDto getProduct(String id) {
        ProductRespDto product = otpsProductService.getProductById(id);
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(product);
        return responseDto;
    }

    /**
     * 条件查询商品列表
     * 批量查询商品列表时一定要带上shop_id，这样才能走索引
     */
    @PostMapping("/list")
    public ResponseDto listProduct(@RequestBody QueryProductReqDto query) {
        List<ProductRespDto> productList = otpsProductService.listProduct(query);
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(productList);
        return responseDto;
    }
}
