package top.dreamstartcloud.service;

import top.dreamstartcloud.bo.Result;
import top.dreamstartcloud.ibo.DetailsIBO;
import top.dreamstartcloud.ibo.PageParamsIBO;

public interface ProblemsService {
    Result findProblemList(PageParamsIBO pageParams);

    Result findProblem(Integer problemId);

    Result submit(DetailsIBO detailsParam);
}
