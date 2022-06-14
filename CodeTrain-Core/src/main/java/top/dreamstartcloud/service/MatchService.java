package top.dreamstartcloud.service;

import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.ibo.PageParamsIBO;

public interface MatchService {
    Result findMatch(Integer problemId) ;

    Result getMatchSets(Integer page);
}
