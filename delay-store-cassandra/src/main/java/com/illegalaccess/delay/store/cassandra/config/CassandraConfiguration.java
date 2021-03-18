package com.illegalaccess.delay.store.cassandra.config;

//@Configuration
//@EnableCassandraRepositories(basePackages = { "com.illegalaccess.delay.store.cassandra" })
public class CassandraConfiguration {

//    @Value("${delay.cassandra.endpoint}")
//    private String endpoint;
//
//    @Value("${delay.cassandra.keyspace}")
//    private String keyspace;
//
//    @Bean
//    public CqlSessionFactoryBean session() {
//        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
//        session.setContactPoints(endpoint);
//        session.setKeyspaceName(keyspace);
//        return session;
//    }
//
//    @Bean
//    public SessionFactoryFactoryBean sessionFactory(CqlSession session, CassandraConverter converter) {
//
//        SessionFactoryFactoryBean sessionFactory = new SessionFactoryFactoryBean();
//        sessionFactory.setSession(session);
//        sessionFactory.setConverter(converter);
//        sessionFactory.setSchemaAction(SchemaAction.NONE);
//
//        return sessionFactory;
//    }
//
//    @Bean
//    public CassandraMappingContext mappingContext(CqlSession cqlSession) {
//
//        CassandraMappingContext mappingContext = new CassandraMappingContext();
//        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cqlSession));
//
//        return mappingContext;
//    }
//
//    @Bean
//    public CassandraConverter converter(CassandraMappingContext mappingContext) {
//        return new MappingCassandraConverter(mappingContext);
//    }
//
//    @Bean
//    public CassandraOperations cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
//        return new CassandraTemplate(sessionFactory, converter);
//    }
}
