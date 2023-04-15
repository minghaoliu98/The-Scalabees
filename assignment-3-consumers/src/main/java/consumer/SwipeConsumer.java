package consumer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dao.SwipeDao;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Consumer class for swipes
 */
public class SwipeConsumer {

  private static final String AWS_PRIVATE = "35.164.66.34";
  private final static int NUM_THREADS = 100;

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(AWS_PRIVATE);
    factory.setVirtualHost("mark");
    Connection connection = factory.newConnection();
    SwipeDao swipeDao = new SwipeDao();
    ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
    long server_start_time = System.currentTimeMillis();
    for (int j = 0; j < NUM_THREADS; j++) {
      threadPool.execute(new ConsumerThread(connection, swipeDao, server_start_time));
    }
    System.out.println("it's work");
  }
}
