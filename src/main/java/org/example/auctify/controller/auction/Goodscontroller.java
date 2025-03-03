package org.example.auctify.controller.auction;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.auctify.dto.Goods.GoodsRequestDTO;
import org.example.auctify.dto.Goods.GoodsResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;

/**
 * worker : 박예빈
 * work : 요구사항 2.경매기능에 대한 (Action) Controller 중 Goods 에 관한 반환정보에 관련된 Controller 작업중
 * date    : 2025/02/18~
 */

@RestController
@RequestMapping("/api/auction") //검색,물품정보,물품 등록
public class Goodscontroller{

    @Operation(summary = "물품 정보 반환", description = "경매 물품 정보 페이지 입니다.")
    @GetMapping("/{goodsId}")
    public ResponseEntity<GoodsResponseDTO> getGoods(
            @Parameter(description = "조회할 물품 ID", example = "1")
            @PathVariable("goodsId") Long goodsId){
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

    //Pageable 로 반환, 페이지 사이즈는 요청에 따라 변경 가능
    @Operation(summary = "물품 검색 결과창", description = "사용자가 물품을 검색하고, 페이지네이션으로 반환합니다.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "검색 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoodsResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "해당 검색어에 해당 하는 제품이 없습니다.")
    })
    @PostMapping("/search")
    public ResponseEntity <Page<GoodsResponseDTO>> searchGoods(
            @RequestParam(required = true) String keyword, //단어 필수 입력
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        //TODO
        return ResponseEntity.ok(null);
    }

    //사용자가 물품에 찜을 누를때
    @Operation(summary = "사용자가 물품 찜 등록", description = "사용자가 해당 goodsId 를 찜에 저장합니다.")
    @PostMapping("/like/{goodsId}")
    public ResponseEntity<GoodsResponseDTO> likeGoods(
            @Parameter(description = "좋아요 누를 물품 ID", example = "1")
            @PathVariable("goodsId") Long goodsId){
        return ResponseEntity.ok(null);

        //likeService.likeGoods(goodsId);
    }

}
