package top.dreamstartcloud.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.dto.DetailsDTO;
import top.dreamstartcloud.ibo.DetailsIBO;
import top.dreamstartcloud.ibo.LoginIBO;
import top.dreamstartcloud.ibo.PageParamsIBO;
import top.dreamstartcloud.service.ProblemsService;

/**
 * @author liu
 */
@Slf4j
@RestController
@RequestMapping("problemset")
@Api(tags = "题目模块", value = "题目模块")
public class ProblemsController {
    @Autowired
    private ProblemsService problemsService;
    @GetMapping("problemspage")
    @ApiOperation(value = "题集接口", notes = "题集")
    public Result findProblemList(@RequestParam Integer page){
        PageParamsIBO pageParams = new PageParamsIBO();
        pageParams.setPage(page);
        return problemsService.findProblemList(pageParams);
    }
    @GetMapping("problems/{id}")
    @ApiOperation(value = "题目接口", notes = "题目")
    public Result findProblem(@PathVariable("id") Integer problemId){
        return problemsService.findProblem(problemId);
    }
    @PostMapping("problems/submits")
    @ApiOperation(value = "提交接口", notes = "提交")
    public Result submits(@RequestBody DetailsDTO detailsDTO){
        DetailsIBO detailsParam = new DetailsIBO();
        BeanUtils.copyProperties(detailsDTO,detailsParam);
        return problemsService.submit(detailsParam);
    }
}
