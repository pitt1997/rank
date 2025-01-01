package com.lijs.rank.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.RecordInterceptor;

import java.util.Map;

/**
 * 通过 @EnableKafka 开启 Kafka 消费功能
 * 为了使 @KafkaListener 生效，通常需要在 Spring 配置类上添加 @EnableKafka 注解。这是一个关键的注解，用来启用 Kafka 消费者功能，并触发切面初始化。
 * <p>
 * KafkaListenerAnnotationBeanPostProcessor 进行注解解析
 * KafkaListenerAnnotationBeanPostProcessor 是 @KafkaListener 注解的处理核心。它是一个 Spring Bean 后处理器，负责扫描应用上下文中的所有 @KafkaListener 注解，并为这些监听器创建代理。
 * <p>
 * 该处理器会扫描 Spring 容器中所有的 @KafkaListener 注解，并为它们创建消息监听器容器。
 * 它会初始化 MessageListenerContainer，并将每个监听器的 @KafkaListener 注解和消费者的配置绑定。
 *
 * public class KafkaListenerAnnotationBeanPostProcessor implements BeanPostProcessor {
 *     @Override
 *     public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
 *         if (bean instanceof KafkaListener) {
 *             // 解析 @KafkaListener 注解并创建对应的监听容器
 *             registerListenerContainer(bean);
 *         }
 *         return bean;
 *     }
 * }
 *
 * @author ljs
 * @date 2024-12-30
 * @description
 */
//@EnableKafka
//@Configuration
public class KafkaConfig {
    // 这里可以放其他 Kafka 配置，如消费者、生产者的配置
    // 启用 Kafka 相关的配置，Spring 会扫描并初始化 @KafkaListener 的相关配置。
    // @EnableKafka 会启用 KafkaListenerAnnotationBeanPostProcessor，该处理器负责在 Spring 容器中初始化 Kafka 消费者。

    private final KafkaProperties properties;

    private final ObjectMapper jacksonObjectMapper;

    private RecordInterceptor tenantConsumerInterceptor;

    public KafkaConfig(KafkaProperties properties, ObjectMapper jacksonObjectMapper) {
        this.properties = properties;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    /**
     * 批处理 KafkaListenerContainerFactory
     */
    public static final String BATCH_KAFKA_LISTENER_CONTAINER_FACTORY = "batchKafkaListenerContainerFactory";

    //@Bean(BATCH_KAFKA_LISTENER_CONTAINER_FACTORY)
    //@ConditionalOnMissingBean(name = BATCH_KAFKA_LISTENER_CONTAINER_FACTORY)
    public KafkaListenerContainerFactory<?> batchKafkaListenerContainerFactory(
            ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
        ConcurrentKafkaListenerContainerFactory<Integer, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> map = this.properties.buildConsumerProperties();
        map.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        map.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);
        map.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);

        // 设置Kafka生产者的acks参数设置为all（或-1）
        // acks=0：生产者发送消息后，不需要等待任何Broker的确认即可继续发送下一条消息。这种配置提供了最高的吞吐量，但牺牲了消息的可靠性。
        // acks=1：生产者发送消息后，等待leader副本的确认。这是Kafka的默认配置，它提供了吞吐量和可靠性之间的一个折中方案。
        // acks=all（或-1）：生产者发送消息后，等待所有副本（包括leader和follower）的确认。这种配置提供了最高的消息可靠性，但可能会降低吞吐量。
        // map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        map.put(ProducerConfig.ACKS_CONFIG, "all"); // 设置acks为all

        // 设置成5个小时
        map.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 5 * 60 * 60 * 1000);
        factory.setRecordInterceptor(tenantConsumerInterceptor);
        factory.setConsumerFactory(customConsumerFactory(customizers, map));
        factory.setBatchListener(true);

        return factory;
    }

    private ConsumerFactory<? super Integer, Object> customConsumerFactory(
            ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers, Map<String, Object> consumerProperties) {
        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>(
                consumerProperties);
        ObjectMapper objectMapper = jacksonObjectMapper.copy();
        // 省略
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }
}
