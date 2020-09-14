package com.valle.foodieapp.models;

import java.util.ArrayList;
import java.util.List;

public class CommonDataUtils {

    public static List<RestaurantDataModel> getDataList() {
        List<RestaurantDataModel> restaurantDataModels = new ArrayList<>();
        restaurantDataModels.add(new RestaurantDataModel("https://i.ndtvimg.com/i/2016-06/chinese-625_625x350_81466064119.jpg", "Food@U", "73", "23", "FOODIEFRESH", "FOODIETOP", "Chinese,Italian,North Indian..", "3.2"));
        restaurantDataModels.add(new RestaurantDataModel("https://serafinamia.com/wp-content/uploads/2018/09/10-Italian-Fun-Facts-The-Food-Fashion-and-Culture-of-Italy.jpg", "Bit & Bites", "60", "15", "FOODIEFRESH", "FOODIETOP", "Chinese,Italian,North Indian..", "3.7"));
        restaurantDataModels.add(new RestaurantDataModel("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2012/3/22/0/FNCC_bobby-flay-salmon-brown-sugar-mustard_s4x3.jpg.rend.hgtvcom.616.462.suffix/1382541357316.jpeg", "Chicken Hut", "83", "25", "FOODIECHICK", "FOODIETOP", "American,Italian,South indian..", "4.2"));
        return restaurantDataModels;
    }

    public static List<RestaurantDataModel> getDataTopRatedList() {
        List<RestaurantDataModel> restaurantDataModels = new ArrayList<>();
        restaurantDataModels.add(new RestaurantDataModel("https://cinepolisdevcdnstr.blob.core.windows.net/sitefinity/images/default-source/food/starters.jpg?sfvrsn=80f087c2_0", "24/7 Your Choice", "73", "23", "FOODIEFRESH", "FOODIETOP", "French,American,Continental..", "3.2"));
        restaurantDataModels.add(new RestaurantDataModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTO5byh5n5tsPFKsKkVR-JOepjUhZ0VDYfuPoA-zUgTqQC3PttvWQ&s", "Pizza Bites", "73", "23", "FOODIEFRESH", "FOODIETOP", "Chinese,Italian,North Indian..", "3.2"));
        restaurantDataModels.add(new RestaurantDataModel("https://bluewater.co.uk/sites/bluewater/files/styles/image_spotlight_large/public/images/spotlights/burger-cropped.jpg?itok=SeFYMFP6", "Burger Hub", "60", "15", "FOODIEFRESH", "FOODIETOP", "Fast Food,Indina Food..", "3.7"));
        restaurantDataModels.add(new RestaurantDataModel("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2012/3/22/0/FNCC_bobby-flay-salmon-brown-sugar-mustard_s4x3.jpg.rend.hgtvcom.616.462.suffix/1382541357316.jpeg", "Chicken Hut", "83", "25", "FOODIECHICK", "FOODIETOP", "American,Italian,South indian..", "4.2"));
        return restaurantDataModels;
    }

    public static List<RestaurantDataModel> getDataTopRatedList2() {
        List<RestaurantDataModel> restaurantDataModels = new ArrayList<>();
        restaurantDataModels.add(new RestaurantDataModel("https://cdn.images.express.co.uk/img/dynamic/14/590x/Pizza-hut-piiza-817060.jpg", "Pizza Hut", "73", "23", "FOODIEFRESH", "FOODIETOP", "Pizza..", "3.2"));
        restaurantDataModels.add(new RestaurantDataModel("https://ichef.bbci.co.uk/news/660/cpsprodpb/66A2/production/_107847262_gettyimages-1152276563-594x594.jpg", "KFC", "73", "23", "FOODIEFRESH", "FOODIETOP", "Chicken..", "3.2"));
        restaurantDataModels.add(new RestaurantDataModel("https://bluewater.co.uk/sites/bluewater/files/styles/image_spotlight_large/public/images/spotlights/burger-cropped.jpg?itok=SeFYMFP6", "Burger Hub", "60", "15", "FOODIEFRESH", "FOODIETOP", "Fast Food,Indina Food..", "3.7"));
        restaurantDataModels.add(new RestaurantDataModel("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2012/3/22/0/FNCC_bobby-flay-salmon-brown-sugar-mustard_s4x3.jpg.rend.hgtvcom.616.462.suffix/1382541357316.jpeg", "Chicken Hut", "83", "25", "FOODIECHICK", "FOODIETOP", "American,Italian,South indian..", "4.2"));
        return restaurantDataModels;
    }

    public static List<SelectAddressBean> getSelectedAddress() {
        List<SelectAddressBean> selectAddressBeans = new ArrayList<>();
        selectAddressBeans.add(new SelectAddressBean("House no. #1254,Armeni Street,California,USA", "Home", "70.26412", "93.2653", true));
        selectAddressBeans.add(new SelectAddressBean("House no. #1421,Armeni Street,Maxico,USA", "Work", "70.26412", "93.2653", false));
        selectAddressBeans.add(new SelectAddressBean("House no. #1756,Armeni Street,Newyork,USA", "Other", "70.26412", "93.2653", false));
        return selectAddressBeans;
    }

    public static List<ChatBeanModel> chatListModel() {
        List<ChatBeanModel> chatBeanModels = new ArrayList<>();
        chatBeanModels.add(new ChatBeanModel("25-10-2019", "", "How can i help you?", "y"));
        chatBeanModels.add(new ChatBeanModel("25-10-2019", "My order not delivered yet...why?", "", "m"));
        chatBeanModels.add(new ChatBeanModel("25-10-2019", "", "Sorry for the inconvenience. Let me check", "y"));
        return chatBeanModels;
    }
}
