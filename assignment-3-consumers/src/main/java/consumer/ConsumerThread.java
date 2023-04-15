package consumer;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import dao.SwipeDao;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import model.Swipe;

/**
 * Thread class for the consumer that takes the message from RabbitMQ and sends
 * it to the database
 */
public class ConsumerThread implements Runnable {

  private static final String QUEUE_NAME = "Queue";
  private static final String EXCHANGE_NAME = "SwipeExchange";
  private static long SERVER_START_TIME;
  private Connection connection;
  private SwipeDao swipeDao;

  private static final Gson gson = new Gson();

  public ConsumerThread(Connection connection, SwipeDao swipeDao, long time) {
    this.connection = connection;
    this.swipeDao = swipeDao;
    this.SERVER_START_TIME = time;
  }

  @Override
  public void run() {
    try {
      consume();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void consume() throws IOException {
    final Channel channel = connection.createChannel();
    boolean durable = true;
    List<Swipe> swipes = Collections.synchronizedList(new ArrayList<>());

    channel.basicQos(100);
    channel.exchangeDeclare(EXCHANGE_NAME, "direct", durable);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(message);
      Swipe swipe = gson.fromJson(message, Swipe.class);
      swipe.setTime((int) (System.currentTimeMillis() -  SERVER_START_TIME));
      swipes.add(swipe);
      if (swipes.size() >= 100) {
        addListToSwipeData(swipes);
        swipes.clear();
      }
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    };
    channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
  }

  /**
   * Adds a list of swipes to the database
   * @param swipes the swipe list
   */
  private void addListToSwipeData(List<Swipe> swipes) {
    swipeDao.createSwipes(swipes);
  }
}
