package de.ludwig.objstreamer;

public class ObjectChunkProcessingRtException extends RuntimeException {
	private static final long serialVersionUID = 5241347792936396214L;

	private ObjectChunk exceptionOccuredAtThis;
	
	public ObjectChunkProcessingRtException(String message, Throwable cause, ObjectChunk exceptionOccuredAtThis) {
		super(message, cause);
		this.exceptionOccuredAtThis = exceptionOccuredAtThis;
	}

	public ObjectChunkProcessingRtException(String message, ObjectChunk exceptionOccuredAtThis) {
		super(message);
		this.exceptionOccuredAtThis = exceptionOccuredAtThis;
	}

	public ObjectChunkProcessingRtException(Throwable cause, ObjectChunk exceptionOccuredAtThis) {
		super(cause);
		this.exceptionOccuredAtThis = exceptionOccuredAtThis;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " " + exceptionOccuredAtThis.toString();
	}

}
