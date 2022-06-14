package top.dreamstartcloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.ibo.PageParamsIBO;
import top.dreamstartcloud.service.MatchService;
import top.dreamstartcloud.service.ProblemsService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("match")
@Api(tags = "比赛模块", value = "比赛模块")
public class MatchController {
    @Resource
    private MatchService matchService;
    @GetMapping("getmatch")
    @ApiOperation(value = "比赛接口", notes = "比赛")
    public Result getMatchSets(@RequestParam Integer page){
        return matchService.getMatchSets(page);
    }
    @GetMapping("contest/{id}")
    @ApiOperation(value = "题目接口", notes = "题目")
    public Result findMatch(@PathVariable("id") Integer problemId){
        return matchService.findMatch(problemId);
    }
}
