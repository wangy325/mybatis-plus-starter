package com.wangy.spel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

/**
 * @author wangy
 * @date 2021-2-20 11:20
 */
@Slf4j
public class SpelIntroTest {

    static SimpleEvaluationContext simpleEvaluationContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();

    static SpelExpressionParser parser = new SpelExpressionParser();

    public static void main(String[] args) {
        log.info("length of 'hello world' is {}", parser.parseExpression("'hello world'.length()").getValue());
    }
}
