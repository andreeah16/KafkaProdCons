package com.simpleexample;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.google.gson.Gson;


@Configuration
@EnableKafka
public class KafkaConfig {

	@Bean
	//public ProducerFactory<String, SimpleModel> producerFactory() {
	public ProducerFactory<String, String> producerFactory() {// gson
		Map<String, Object> config = new HashMap<>();
		
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		//config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);// gson
		
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	//public KafkaTemplate<String, SimpleModel> kafkaTemplate() {
	public KafkaTemplate<String, String> kafkaTemplate() {// gson
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	//public ConsumerFactory<String, SimpleModel> consumerFactory() {
	public ConsumerFactory<String, String> consumerFactory() {// gson
		Map<String, Object> config = new HashMap<>();

		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		//config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);// change standard to our specific ser/des (gson)
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "myGroupId");//different kafka listeners (can be many)
		
		//return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(SimpleModel.class));
		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());// change standard to our specific ser/des (gson)
	}
	
	@Bean
	//public ConcurrentKafkaListenerContainerFactory<String, SimpleModel> kafkaListenerContainerFactory() {
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {// gson
		//ConcurrentKafkaListenerContainerFactory<String, SimpleModel> concurrentKafkaListenerContainerFactory =
		ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory =
				new ConcurrentKafkaListenerContainerFactory<>();
		
		concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
		return concurrentKafkaListenerContainerFactory;
	}
	
	@Bean
	public Gson jsonConverter() {// use something else for ser/deser, not the standard
		return new Gson();
	}
}
