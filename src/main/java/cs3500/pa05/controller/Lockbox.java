package cs3500.pa05.controller;

/**
 * Implementation of a lockbox to transfer objects out of screen controllers
 *
 * @param <T> type of object to be stored in the lockbox
 */
public class Lockbox<T> {
  private T item = null;

  /**
   * Stores a provided item of type T in the lockbox
   *
   * @param item is an instance of type T to be stored in the lockbox
   */
  public void putItemInLockbox(T item) {
    this.item = item;
  }

  /**
   * Gets an item currently stored in the lockbox if one is placed there
   *
   * @return the currently stored item in the lockbox
   * @throws IllegalStateException if the item wasn't placed in the lockbox yet
   */
  public T getItemInLockbox() {
    if (item == null) {
      throw new IllegalStateException("No item is yet placed in the lockbox");
    }

    return item;
  }
}
