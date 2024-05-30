package cc.sika.bookkeeping.controller;

import cc.sika.bookkeeping.pojo.dto.BaseQuery;
import cc.sika.bookkeeping.pojo.dto.LedgeQueryDTO;
import cc.sika.bookkeeping.pojo.po.SikaLedger;
import cc.sika.bookkeeping.pojo.vo.PageVO;
import cc.sika.bookkeeping.pojo.vo.Result;
import cc.sika.bookkeeping.service.SikaLedgerService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("ledger")
@SaCheckLogin
@RequiredArgsConstructor
public class LedgerController {


    @Operation(summary = "获取用户账本接口")
    @ApiResponse(content = @Content(schema = @Schema(implementation = SikaLedger.class)))
    @GetMapping("list")
    @SaCheckRole({"admin"})
    public Result<PageVO<SikaLedger>> getUserLedger(BaseQuery<LedgeQueryDTO> statusQuery) {
        return Result.success(sikaLedgerService.getLedgersPage(statusQuery));
    }




    private final SikaLedgerService sikaLedgerService;
}
