module cs3500.pa05 {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires org.controlsfx.controls;
  requires javafx.media;
  requires java.desktop;
  requires java.base;

  opens cs3500.pa05 to javafx.fxml, javafx.media;
  exports cs3500.pa05;
  exports cs3500.pa05.controller;
  exports cs3500.pa05.model;
  exports cs3500.pa05.view;
  opens cs3500.pa05.controller to javafx.fxml;
  opens cs3500.pa05.model to com.fasterxml.jackson.databind;
  opens cs3500.pa05.controller.screenctrls to javafx.fxml;

}