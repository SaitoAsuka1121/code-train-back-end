package top.dreamstartcloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.dreamstartcloud.bo.ErrorCode;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.entity.Problems;
import top.dreamstartcloud.entity.Submit;
import top.dreamstartcloud.ibo.DetailsIBO;
import top.dreamstartcloud.ibo.PageParamsIBO;
import top.dreamstartcloud.mapper.ProblemsMapper;
import top.dreamstartcloud.mapper.SubmitMapper;
import top.dreamstartcloud.service.ProblemsService;
import top.dreamstartcloud.utils.PythonUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Slf4j
@Service
public class ProblemsServiceImpl implements ProblemsService {
    @Resource
    private ProblemsMapper problemsMapper;
    @Resource
    private SubmitMapper submitMapper;
    @Override
    public Result findProblemList(PageParamsIBO pageParams) {
        Page<Problems> page = new Page<>(pageParams.getPage(),100);
        LambdaQueryWrapper<Problems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Problems::getId);
        queryWrapper.select(Problems::getId,Problems::getTitle, Problems::getSubmits,Problems::getAccepts);
        Page<Problems> problemsPage = problemsMapper.selectPage(page, queryWrapper);
        List<Problems> records = problemsPage.getRecords();
        return Result.success(records);
    }

    @Override
    public Result findProblem(Integer problemId) {
        LambdaQueryWrapper<Problems> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Problems::getId,problemId);
        queryWrapper.last("limit 1");
        Problems problem = problemsMapper.selectOne(queryWrapper);
        if(problem==null){
            return Result.fail(ErrorCode.NO_PROBLEM.getCode(),ErrorCode.NO_PROBLEM.getMsg());
        }
        return Result.success(problem);
    }

    @Override
    public Result submit(DetailsIBO detailsParam) {
        String problemId= detailsParam.getProblemId();
        String compiler = detailsParam.getCompiler();
        String program = detailsParam.getProgram();
        if(StringUtils.isBlank(problemId) || StringUtils.isBlank(compiler)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        String res = null;
        try {
            res = PythonUtils.runPy(problemId,program,compiler);
            log.info("res:"+res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.warn("py end....");
        JSONObject json = JSONObject.parseObject(res);
        Submit submit = new Submit();
        submit.setProblemId("1425090373964926977");
        submit.setUserId(problemId);
        String type = "";
        boolean flag = false;
        for (int i = res.indexOf("status"); i<res.length(); i++){
            if (res.charAt(i)==':'){
                i+=2;
                for (int j=i;j<res.length();j++){
                    if (res.charAt(j)=='"'){
                        flag = true;
                        break;
                    }
                    type += res.charAt(j);
                }
                if (flag){
                    break;
                }
            }
        }
        submit.setType(type);
        submit.setLanguage(compiler);
        submit.setCreatedTime(new Date());
        submitMapper.insert(submit);
        System.out.println(json);
        return Result.success(json);
    }
}
