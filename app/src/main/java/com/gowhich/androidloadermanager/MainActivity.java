package com.gowhich.androidloadermanager;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LoaderManager loaderManager;
    private ListView listView;
    private MyAdapter myAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAdapter = new MyAdapter(MainActivity.this);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(1000, null, callbacks);

        listView = (ListView) this.findViewById(R.id.listView);
        registerForContextMenu(listView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.add:
                final Dialog dialog = createDialog(MainActivity.this);

                Button button = (Button) dialog.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) dialog.findViewById(R.id.editText);
                        String name = editText.getText().toString().trim();
                        ContentResolver contentResolver = getContentResolver();
                        Uri uri = Uri.parse("content://com.gowhich.androidloadermanager.service.PersonContentProvider/person");
                        ContentValues values = new ContentValues();
                        values.put("name", name);
                        values.put("age", 10);
                        Uri resultUri = contentResolver.insert(uri, values);
                        if (resultUri != null) {
                            loaderManager.restartLoader(1000, null, callbacks);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
//                Toast.makeText(MainActivity.this, "添加", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                int position = info.position;
                String name = myAdapter.getItem(position).toString();
                Uri uri = Uri.parse("content://com.gowhich.androidloadermanager.service.PersonContentProvider/person");
                ContentResolver contentResolver = getContentResolver();
                int count = contentResolver.delete(uri," name = ? ", new String[]{ name });
                if(count > 0){
                    loaderManager.restartLoader(1000, null, callbacks);
                }
//                Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onContextItemSelected(item);
    }

    public Dialog createDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add);
        return dialog;
    }

    public class MyAdapter extends BaseAdapter {

        private Context context;
        private List<String> list = null;

        public MyAdapter(Context context) {
            this.context = context;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = null;
            if (convertView == null) {
                view = new TextView(context);
            } else {
                view = (TextView) convertView;
            }
            view.setText(list.get(position).toString());
            return view;
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader loader = new CursorLoader(MainActivity.this);

            Uri uri = Uri.parse("content://com.gowhich.androidloadermanager.service.PersonContentProvider/person");
            loader.setUri(uri);

            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<String> list = new ArrayList<>();

            while (data.moveToNext()) {
                String name = data.getString(data.getColumnIndex("name"));
                list.add(name);
            }

            myAdapter.setList(list);
            listView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {


        }
    };
}
