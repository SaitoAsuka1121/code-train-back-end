package top.dreamstartcloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.ibo.PageParamsIBO;
import top.dreamstartcloud.service.ProblemsService;
import top.dreamstartcloud.service.SubmitService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("submitlog")
@Api(tags = "记录模块", value = "记录模块")
public class SubmitController {
    @Resource
    private SubmitService submitService;
    @GetMapping("sub")
    @ApiOperation(value = "记录接口", notes = "记录")
    public Result findProblemList(@RequestParam Integer page){
        return submitService.findtLog(page);
    }
}
