
package kulon.publicapps.environmenttest.components;

public class TestSetup {
	private Boolean amq;
	private Boolean elastic;
	private Boolean kafka;
	private Boolean redis;
	private Boolean sql;
	
	public TestSetup(
			Boolean amq,
			Boolean elastic,
			Boolean kafka,
			Boolean redis,
			Boolean sql
			) {
		
		this.amq = amq;
		this.elastic = elastic;
		this.kafka = kafka;
		this.redis = redis;
		this.sql = sql;
	}
	
	public Boolean shouldTestAmq() {
		return amq;
	}

	public Boolean shouldTestElastic() {
		return elastic;
	}

	public Boolean shouldTestKafka() {
		return kafka;
	}

	public Boolean shouldTestRedis() {
		return redis;
	}

	public Boolean shouldTestSql() {
		return sql;
	}
}
