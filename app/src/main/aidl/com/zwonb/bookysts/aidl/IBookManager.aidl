package com.zwonb.bookysts.aidl;

import com.zwonb.bookysts.aidl.Book;
import com.zwonb.bookysts.aidl.IOnNewBookArrivedListener;

interface IBookManager {

    List<Book> getBookList();

    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);

    void unRegisterListener(IOnNewBookArrivedListener listener);
}