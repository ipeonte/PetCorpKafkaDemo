package com.example.demo.petcorp.ui.web.test.model;

/**
 * Client model
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class Client {

  private final int id;

  private final String name;

  public Client(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
