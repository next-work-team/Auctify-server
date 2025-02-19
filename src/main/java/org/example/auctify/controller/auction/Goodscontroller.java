package org.example.auctify.controller.auction;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.auctify.dto.Goods.GoodsRequestDTO;
import org.example.auctify.dto.Goods.GoodsResponseDTO;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * worker : 박예빈
 * work : 요구사항 2.경매기능에 대한 (Action) Controller 중 Goods 에 관한 반환정보에 관련된 Controller 작업중
 * date    : 2025/02/18~
 */

@RestController
@RequestMapping("/api/auction")
public class Goodscontroller {

    @Operation(summary = "물품 정보 반환", description = "경매 물품 정보 페이지 입니다.")
    @GetMapping("/{goodsId}")
    public ResponseEntity<GoodsResponseDTO> getGoods(
            @Parameter(description = "조회할 물품 ID", example = "1")
            @PathVariable("goodsId") String goodsId){
        return ResponseEntity.ok(null);
        //TODO
    }

    @Operation(summary = "물품 등록", description = "사용자가 경매 품목을 등록합니다.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoodsResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<GoodsRequestDTO> createGoods(
            @RequestBody GoodsRequestDTO goodsRequestDTO
    ){
        return ResponseEntity.ok(null);
        //TODO
    }

    //경매 아이템 목록반환 Page 로 할지?

}
