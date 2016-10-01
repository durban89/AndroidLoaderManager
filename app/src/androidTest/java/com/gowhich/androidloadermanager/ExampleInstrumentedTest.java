package com.gowhich.androidloadermanager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.gowhich.androidloadermanager", appContext.getPackageName());
    }

    @Test
    public void insert(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        ContentResolver contentResolver = appContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name","王柳1");
        values.put("age","61");
        Uri uri = Uri.parse("content://com.gowhich.androidloadermanager.service.PersonContentProvider/person");
        contentResolver.insert(uri, values);

    }

    @Test
    public void delete(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        ContentResolver contentResolver = appContext.getContentResolver();
        Uri uri = Uri.parse("content://com.gowhich.androidloadermanager.service.PersonContentProvider/person/1");
        contentResolver.delete(uri, null, null);
    }

    public void update(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        ContentResolver contentResolver = appContext.getContentResolver();
        Uri uri = Uri.parse("content://com.gowhich.androidloadermanager.service.PersonContentProvider/person/1");
        ContentValues values = new ContentValues();
        values.put("name","张三");
        values.put("age","10");
        contentResolver.update(uri, values, null, null);

    }
}
