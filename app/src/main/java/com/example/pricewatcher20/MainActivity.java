
package com.example.pricewatcher20;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import java.util.ArrayList;


import static android.content.Intent.EXTRA_TEXT;

public class MainActivity extends AppCompatActivity {
    private ListView listview;
    Adapter adapter;
    EditText itemName, itemPrice, itemUrl;
    String url;
    Item product;
    ArrayList<Item> items = new ArrayList<>();
    int reqVer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            items = savedInstanceState.getParcelableArrayList("items");
        }

        listview = (ListView) findViewById(R.id.listview);
        adapter = new Adapter(this, items);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popup(view, position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            addDialog();
            return true;
        }
        if (id == R.id.refresh) {
            adapter.setPercent();
            listview.setAdapter(adapter);
            return true;
        }
        if (id == R.id.exit) {
            exit();
            return true;
        }
        if(id == R.id.about){
            about();
            return true;
        }
        if (id == R.id.browse) {
            url = "https://www.google.com";
            browse(url);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void passLine() {
        String action = getIntent().getAction();
        String type = getIntent().getType();
        if (Intent.ACTION_SEND.equalsIgnoreCase(action) && type != null && ("text/plain".equals(type))) {
            url = getIntent().getStringExtra(EXTRA_TEXT);
        }
    }

    private void addDialog() {
        AlertDialog.Builder itemBuilder = new AlertDialog.Builder(MainActivity.this);
        View itemView = getLayoutInflater().inflate(R.layout.layout_dialog, null);
        itemBuilder.setTitle("Add Item");
        itemName = (EditText) itemView.findViewById(R.id.diaItemName);
        itemPrice = (EditText) itemView.findViewById(R.id.diaStartingPrice);
        itemUrl = (EditText) itemView.findViewById(R.id.diaUrl);
        itemBuilder.setCancelable(false)
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = itemName.getText().toString();
                        String startPrice = itemPrice.getText().toString();
                        float inputStartPrice = Float.parseFloat(startPrice);
                        String url = itemUrl.getText().toString();
                        items.add(new Item(name, inputStartPrice, url));
                        //  adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        itemBuilder.setView(itemView);
        AlertDialog dialog = itemBuilder.create();
        dialog.show();
    }

    private void popup(View v, int position) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.popup);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit) {
                    editDialog(position);
                }
                if (item.getItemId() == R.id.browse) {
                    Item itemUrl = items.get(position);
                    browse(itemUrl.getUrl());
                    return true;
                }
                if (item.getItemId() == R.id.remove) {
                    removeItem(position);
                    return true;
                }

                return false;
            }
        });
    }

    private void removeItem(int position) {
        Item item = items.get(position);
        items.remove(item);
        listview.setAdapter(adapter);
    }

    private void editDialog(int position) {
        Item item = items.get(position);
        AlertDialog.Builder itemBuilder = new AlertDialog.Builder(MainActivity.this);
        View itemView = getLayoutInflater().inflate(R.layout.layout_dialog, null);
        itemBuilder.setTitle("Edit the Item");
        itemName = (EditText) itemView.findViewById(R.id.diaItemName);
        itemPrice = (EditText) itemView.findViewById(R.id.diaStartingPrice);
        itemUrl = (EditText) itemView.findViewById(R.id.diaUrl);

        itemName.setText(item.getItem());
        String startPrice = Float.toString((float) item.getStartPrice());
        itemPrice.setText(startPrice);
        itemUrl.setText(item.getUrl());

        itemBuilder.setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = itemName.getText().toString();
                        String startPrice = itemPrice.getText().toString();
                        float inputStartPrice = Float.parseFloat(startPrice);
                        String url = itemUrl.getText().toString();
                        item.setItem(name);
                        item.setStartPrice(inputStartPrice);
                        item.setUrl(url);
                        listview.setAdapter(adapter);
                    }
                }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        itemBuilder.setView(itemView);
        AlertDialog dialog = itemBuilder.create();
        dialog.show();
    }

    public void browse(String link) {
        Intent sendLink = new Intent(getApplicationContext(), Browser.class);
        sendLink.putExtra("link", link);
        startActivityForResult(sendLink, reqVer);
    }


    @Override
    public void onActivityResult(int reqVer, int resultCode, Intent data) {
        super.onActivityResult(reqVer, resultCode, data);
        String returnedUrl = data.getData().toString();
        if (resultCode == 0) {
            Toast.makeText(this, returnedUrl, Toast.LENGTH_SHORT).show();
            addDialog();
            itemUrl.setText(returnedUrl);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("items", items);
    }

    public void exit() {
        Intent i = new Intent();
        i.setData(Uri.parse(""));
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void about() {
        Intent j = new Intent(this, About.class);
        this.startActivity(j);
    }
}