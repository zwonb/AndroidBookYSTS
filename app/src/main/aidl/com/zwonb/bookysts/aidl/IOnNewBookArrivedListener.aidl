// IOnNewBookArrivedListener.aidl
package com.zwonb.bookysts.aidl;

// Declare any non-default types here with import statements
import com.zwonb.bookysts.aidl.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book book);
}
