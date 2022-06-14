package top.dreamstartcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.entity.Match;
import top.dreamstartcloud.entity.Submit;
import top.dreamstartcloud.mapper.SubmitMapper;
import top.dreamstartcloud.service.SubmitService;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SubmitServiceImpl implements SubmitService {
    @Resource
    private SubmitMapper submitMapper;
    @Override
    public Result findtLog(Integer page) {
        Page<Submit> matchs = new Page<>(page,30);
        LambdaQueryWrapper<Submit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Submit::getId);
        queryWrapper.select(Submit::getId,Submit::getUserId,Submit::getProblemId,Submit::getLanguage,Submit::getType,Submit::getCreatedTime);
        Page<Submit> problemsPage = submitMapper.selectPage(matchs, queryWrapper);
        List<Submit> records = problemsPage.getRecords();
        return Result.success(records);
    }
}
