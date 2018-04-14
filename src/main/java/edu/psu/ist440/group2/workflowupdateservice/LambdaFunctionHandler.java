package edu.psu.ist440.group2.workflowupdateservice;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.psu.ist440.group2.mappings.JobItem;

public class LambdaFunctionHandler implements RequestHandler<JobItem, JobItem> {

	@Override
	public JobItem handleRequest(JobItem input, Context context) {

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		String userId = input.getUserId();
		String jobId = input.getJobId();
		
		mapper.save(input);		
		context.getLogger().log("Input: " + input);
		
		JobItem item = mapper.load(JobItem.class, userId, jobId);

		// TODO: implement your handler
		return item;
	}

}
