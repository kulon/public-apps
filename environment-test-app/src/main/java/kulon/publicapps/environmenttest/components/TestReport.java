
package kulon.publicapps.environmenttest.components;

import java.time.LocalDateTime;

public class TestReport {
	private String requestNumber;
	private LocalDateTime requestReceivedDateTime;

	private String amqResult = Constants.NOT_TESTED;
	private String elasticResult = Constants.NOT_TESTED;
	private String redisResult = Constants.NOT_TESTED;
	private String kafkaResult = Constants.NOT_TESTED;
	private String sqlResult = Constants.NOT_TESTED;
	
	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public LocalDateTime getRequestReceivedDateTime() {
		return requestReceivedDateTime;
	}

	public void setRequestReceivedDateTime(LocalDateTime messageReceivedDateTime) {
		this.requestReceivedDateTime = messageReceivedDateTime;
	}

	public String getAmqResult() {
		return amqResult;
	}

	public void setAmqResult(String amqResult) {
		this.amqResult = amqResult;
	}

	public String getElasticResult() {
		return elasticResult;
	}

	public void setElasticResult(String elasticResult) {
		this.elasticResult = elasticResult;
	}

	public String getRedisResult() {
		return redisResult;
	}

	public void setRedisResult(String redisResult) {
		this.redisResult = redisResult;
	}

	public String getKafkaResult() {
		return kafkaResult;
	}

	public void setKafkaResult(String kafkaResult) {
		this.kafkaResult = kafkaResult;
	}

	public String getSqlResult() {
		return sqlResult;
	}

	public void setSqlResult(String sqlResult) {
		this.sqlResult = sqlResult;
	}

	@Override
	public String toString() {
		return 	"Test Report:" +
				"\n Request Number: " + getRequestNumber() + 
				"\n Request Received: " + getRequestReceivedDateTime() +
				"\n AMQ Result: " + getAmqResult() +
				"\n Elastic Result: " + getElasticResult() +
				"\n Kafka Result: " + getKafkaResult() +
				"\n Redis Result: " + getRedisResult() +
				"\n SQL Result: " + getSqlResult() +"\n"
				;
	}
}
