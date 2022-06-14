package top.dreamstartcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.dreamstartcloud.bo.ErrorCode;
import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.entity.Match;
import top.dreamstartcloud.entity.Problems;
import top.dreamstartcloud.mapper.MatchMapper;
import top.dreamstartcloud.mapper.ProblemsMapper;
import top.dreamstartcloud.service.MatchService;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {
    @Resource
    private MatchMapper matchMapper;
    @Override
    public Result getMatchSets(Integer page) {
        Page<Match> matchs = new Page<>(page,30);
        LambdaQueryWrapper<Match> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Match::getMatchId);
        queryWrapper.select(Match::getMatchId,Match::getUserId,Match::getStartTime,Match::getEndTime,Match::getProblemsId,Match::getMatchName);
        Page<Match> problemsPage = matchMapper.selectPage(matchs, queryWrapper);
        List<Match> records = problemsPage.getRecords();
        return Result.success(records);
    }

    @Override
    public Result findMatch(Integer problemId) {
        LambdaQueryWrapper<Match> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Match::getMatchId,problemId);
        queryWrapper.last("limit 1");
        Match match = matchMapper.selectOne(queryWrapper);
        if(match==null){
            return Result.fail(ErrorCode.NO_PROBLEM.getCode(),ErrorCode.NO_PROBLEM.getMsg());
        }
        return Result.success(match);
    }
}
