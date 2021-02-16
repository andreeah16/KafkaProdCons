package com.simpleexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping("/api/kafka")
public class KafkaSimpleController {
	
	//KafkaTemplate<String, SimpleModel> kafkaTemplate;
	private KafkaTemplate<String, String> kafkaTemplate;// gson
	private Gson jsonConverter;
	
	@Autowired
	//public KafkaSimpleController(KafkaTemplate<String, SimpleModel> kafkaTemplate) {
	public KafkaSimpleController(KafkaTemplate<String, String> kafkaTemplate, Gson jsonConverter) {
		this.kafkaTemplate = kafkaTemplate;
		this.jsonConverter = jsonConverter;
	}
	
	@PostMapping
	public void post(@RequestBody SimpleModel simpleModel) {
		//kafkaTemplate.send("myTopic", simpleModel);
		kafkaTemplate.send("myTopic", jsonConverter.toJson(simpleModel));// gson
	}
	
	@PostMapping("/v2")
	public void post(@RequestBody MoreSimpleModel moreSimpleModel) {
		kafkaTemplate.send("myTopic2", jsonConverter.toJson(moreSimpleModel));
	}
	
	@KafkaListener(topics = "myTopic")
	//public void getFromKafka(SimpleModel simpleModel) {
	public void getFromKafka(String simpleModel) {// gson
		//System.out.println(simpleModel.toString());
		System.out.println(simpleModel);// gson
		
		SimpleModel simpleModel1 = (SimpleModel) jsonConverter.fromJson(simpleModel, SimpleModel.class);
		
		System.out.println(simpleModel1.toString());
	}
	
	@KafkaListener(topics = "myTopic2")
	public void getFromKafka2(String moreSimpleModel) {
		System.out.println(moreSimpleModel);
		
		MoreSimpleModel moreSimpleModel1 = (MoreSimpleModel) jsonConverter.fromJson(moreSimpleModel, MoreSimpleModel.class);
		
		System.out.println(moreSimpleModel1.toString());
	}

}
