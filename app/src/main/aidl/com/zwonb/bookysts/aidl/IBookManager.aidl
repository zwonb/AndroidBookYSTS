package com.zwonb.bookysts.aidl;

import com.zwonb.bookysts.aidl.Book;

interface IBookManager {

    List<Book> getBookList();

    void addBook(in Book book);
}