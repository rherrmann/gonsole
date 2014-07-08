package com.codeaffine.console.core.history;

public interface HistoryStore {

  void setItems( String... concat );
  String[] getItems();
  void clearItems();
}