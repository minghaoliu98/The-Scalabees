package model;

/**
 * Model for a user's swipe info that includes the direction
 */
public class Swipe {

  private boolean swipeDirection = false;
  private int swiper;
  private int swipee;
  private int time;


  public Swipe(String swipeDirection, String swiper, String swipee, String comment) {
    if (swipeDirection.equals("right")) {
      this.swipeDirection = true;
    }
    this.swiper = Integer.parseInt(swiper);
    this.swipee = Integer.parseInt(swipee);
  }

  public void setTime(int t) {
    this.time = t;
  }

  public int getTime(){
    return time;
  }

  public boolean getSwipeDirection() {
    return swipeDirection;
  }

  public int getSwiper() {
    return swiper;
  }

  public int getSwipee() {
    return swipee;
  }

}
