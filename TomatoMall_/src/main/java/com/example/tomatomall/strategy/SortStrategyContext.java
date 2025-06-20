package com.example.tomatomall.strategy;

import com.example.tomatomall.exception.TomatoMallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SortStrategyContext {

    /**
     * 自动注入所有实现了 SortStrategy 接口的 Bean，
     * Spring 会将它们以 Bean 名称为 key，Bean 实例为 value，注入到 Map 中。
     *
     * 示例：如果有 @Service("comment") 的策略类，
     * 那么 strategyMap 中就会包含一对 "comment" -> CommentSortStrategy 实例。
     */

    @Autowired
    private Map<String, SortStrategy> strategyMap;

    /**
     * 根据排序类型字符串（type），动态获取对应的排序策略。
     * 这是策略模式的核心实现逻辑。
     *
     * @param type  排序类型（如 "post"、"comment"），通常由 Controller 层传入
     * @return      对应的排序策略实例（SortStrategy 的具体实现类）
     * @throws TomatoMallException 如果输入的 type 没有对应的策略实现类
     */
    public SortStrategy getStrategy(String type) {
        SortStrategy strategy = strategyMap.get(type.toLowerCase());

        // 如果没有找到对应的策略类，抛出自定义异常
        if (strategy == null) {
            throw TomatoMallException.cannotSort();
        }

        // 返回匹配的策略实例，供业务层使用
        return strategy;
    }
}
