package com.yp.nowapi.config;

import com.yp.nowapi.state.OrderState;
import com.yp.nowapi.state.OrderStateChangeAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

import javax.annotation.Resource;
import java.util.EnumSet;

/**
 * @author yp
 * @date: 2023/11/17
 * Spring 状态机的配置
 */
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderStateChangeAction> {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean(name = "stateMachineRedisPersister")
    public RedisStateMachinePersister<OrderState, OrderStateChangeAction> getRedisPersister() {
        RedisStateMachineContextRepository<OrderState, OrderStateChangeAction> repository
                = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        RepositoryStateMachinePersist<OrderState, OrderStateChangeAction> p
                = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(p);
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderStateChangeAction> states) throws Exception {
        // 设置订单创建成功后的初始状态为待支付，并将订单所有状态设置到状态机中
        states.withStates().initial(OrderState.ORDER_WAIT_PAY)
                .states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderStateChangeAction> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderState.ORDER_WAIT_PAY)
                .target(OrderState.ORDER_FINISH_PAY)
                .event(OrderStateChangeAction.PAY_ORDER);
    }
}
