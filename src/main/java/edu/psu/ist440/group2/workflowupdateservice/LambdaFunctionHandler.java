package edu.psu.ist440.group2.workflowupdateservice;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * The workflow update service is used to update job data by acting as a bridge
 * between the Step Functions workflow and Dynamo. This service uses the
 * DynamoDBMapper API to interact with Dynamo.
 * 
 * This service is an AWS Lambda function.
 * 
 * @see https://aws.amazon.com/lambda/
 * @see https://docs.aws.amazon.com/lambda/latest/dg/java-programming-model-handler-types.html
 * 
 * @author j6r
 *
 */
public class LambdaFunctionHandler implements RequestHandler<JobItem, JobItem> {

	/**
	 * Updates Dynamo with the specified job information
	 * 
	 * @param input
	 *            a JobItem (matching the data structure from Step Functions)
	 * @param context
	 *            execution metadata supplied by Lambda
	 * @return the updated job item, retrieved from the database
	 */
	@Override
	public JobItem handleRequest(JobItem input, Context context) {

		JobItem item = null;

		// if the input has the failed flag set then set the status to failed
		// the item should be updated in the database then an exception thrown
		// later to stop the workflow
		if (input.isFailed()) {
			input.setStatus("FAILED");
		}

		try {
			// Create Dynamo client
			AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
			DynamoDBMapper mapper = new DynamoDBMapper(client);

			// Save data
			if (input.getUserId() == null) {
				throw new WorkflowUpdateException("Input data missing partition key (userId)");
			}
			if (input.getJobId() == null) {
				throw new WorkflowUpdateException("Input data missing sort key (jobId)");
			}
			String userId = input.getUserId();
			String jobId = input.getJobId();

			mapper.save(input);
			context.getLogger().log("Input: " + input);

			// Fetch the resulting object from Dynamo and return it
			item = mapper.load(JobItem.class, userId, jobId);

			if (item == null || input.isFailed()) {
				throw new WorkflowUpdateException(String.format("%s;%s", input.getUserId(), input.getJobId()));
			}

		} catch (WorkflowUpdateException e) {
			context.getLogger().log(e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		}

		return item;

	}

	/**
	 * Custom exception class for workflow update errors
	 * 
	 * @author j6r
	 *
	 */
	public class WorkflowUpdateException extends Exception {

		public WorkflowUpdateException(String message, Throwable cause) {
			super(message, cause);

		}

		public WorkflowUpdateException(String message) {
			super(message);
		}

	}

}
