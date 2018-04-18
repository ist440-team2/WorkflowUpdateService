package edu.psu.ist440.group2.workflowupdateservice;

/**
 * WorkflowUpdateException is a custom exception thrown when errors occur in
 * WorkflowUpdateService
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
