package edu.harvard.h2ms.exception;

public class ResourceNotFoundException extends Exception {
	private String resourceId;
	
	public ResourceNotFoundException(String entityIdentifier, String message) {
		super(message);
		this.resourceId = entityIdentifier;
	}
}
