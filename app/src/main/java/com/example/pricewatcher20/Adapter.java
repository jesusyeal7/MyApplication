package com.example.pricewatcher20;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter<Item>  {

    private Context inpContext;
    private List<Item> itemList = new ArrayList<>();
    TextView itemName, currentPrice, priceChange;
    TextView startingPrice;
    Item item;

    public Adapter( Context context, List<Item> items){
        super(context, 0, items);
        inpContext = context;
        itemList = items;
    }

    public void setPercent(){
        item.setCurrentPrice();
        currentPrice.setText(Float.toString((float) item.getCurrentPrice()));
        priceChange.setText(Float.toString((float) item.getPercent()));
    }

    public View getView(int position,View convertView,ViewGroup parent){
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(inpContext).inflate(R.layout.listview_row,parent,false);
            itemName = (TextView) listItem.findViewById(R.id.itemName);
            startingPrice = (TextView) listItem.findViewById(R.id.startingPrice);
            currentPrice = (TextView) listItem.findViewById(R.id.currentPrice);
            priceChange = (TextView) listItem.findViewById(R.id.percent);
        }

        item = itemList.get(position);
        item.setCurrentPrice();
        itemName.setText(item.getItem());
        String start = Double.toString(item.getStartPrice());
        startingPrice.setText(start);
        setPercent();
        return listItem;
    }
}
