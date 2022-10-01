@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class EmbeddedKafkaIntegrationTest {
    private BankModel event = new BankModel(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "7703", 111, "12/05/2021", "Abid", "Khan", 10000d);

    @SpyBean
    private KafkaConsumerService consumer;

    @Autowired
    private KafkaProducer producer;

    @Captor
    ArgumentCaptor<List<BankModel>> bankModelArgumentCaptor;

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    @Captor
    ArgumentCaptor<String> topicArgumentCaptor;

    @Test
    public void embeddedKafka_whenSendingToSimpleProducer_thenMessageReceived() {

        //Producer
        producer.send(event);

        //consumer
        verify(consumer, timeout(1000).times(1)).listen(BankModelArgumentCaptor.capture(),
                topicArgumentCaptor.capture());
        List<BankModel> batchPayload = BankModelArgumentCaptor.getValue();
        assertNotNull(batchPayload);
        assertThat(batchPayload.size(), equalTo(01));
        assertTrue(TOPIC_NAME.contains(topicArgumentCaptor.getValue()));
        testEvents(batchPayload);
    }

    private void testEvents(List<BankAccount> eventsPayload) {
        eventsPayload.forEach(record -> {
            assertNotNull(record);
            assertEquals(event.getAccountNumber(), record.getAccountNumber());
            assertEquals(event.getTransactionId(), record.getTransactionId());
            assertEquals(event.getIdentificationNumber(), record.getIdentificationNumber());
            assertEquals(event.getSecurityCode(), record.getSecurityCode());
            assertEquals(event.getDateOfBirth(), record.getDateOfBirth());
            assertEquals(event.getFirstName(), record.getFirstName());
            assertEquals(event.getLastName(), record.getLastName());
            assertEquals(event.getBalance(), record.getBalance());
        });
    }
