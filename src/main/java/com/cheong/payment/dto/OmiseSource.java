package com.cheong.payment.dto;

import co.omise.models.Item;
import co.omise.models.Source;
import co.omise.requests.ResponseType;

import java.util.ArrayList;
import java.util.List;

public class OmiseSource extends Source {

    public static class OmiseCreateRequestBuilder extends  CreateRequestBuilder{

        public OmiseCreateRequestBuilder addItems(List<Item> items){
           for(Item item : items){
               super.addItem(item);
           }
           return this;
        }
    }


}
