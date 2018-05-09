package com.gsd.idreamsky.weplay.thirdpart.wheelView.data;

public class DistrictModel {

    public String name;
    public String zipcode;

    public DistrictModel() {
        super();
    }

    public DistrictModel(String name, String zipcode) {
        super();
        this.name = name;
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
    }
}
