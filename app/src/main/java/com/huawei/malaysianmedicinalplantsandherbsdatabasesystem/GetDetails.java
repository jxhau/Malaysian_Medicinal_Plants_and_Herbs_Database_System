package com.huawei.malaysianmedicinalplantsandherbsdatabasesystem;

public class GetDetails {
    private int id;
    private byte[] image;
    private String name;
    private String definition;
    private String vernacular;
    private String color;
    private String odour;
    private String taste;
    private String morphology;
    private String medicinal;
    private String safety;
    private String storage;

    public GetDetails(byte[] image_input, String name_input, String definition_input, String vernacular_input, String color_input,
                      String odour_input, String taste_input, String morphology_input, String medicinal_input, String safety_input,
                      String storage_input, int id){
        this.image = image_input;
        this.name = name_input;
        this.definition = definition_input;
        this.vernacular = vernacular_input;
        this.color = color_input;
        this.odour = odour_input;
        this.taste = taste_input;
        this.morphology = morphology_input;
        this.medicinal = medicinal_input;
        this.safety = safety_input;
        this.storage = storage_input;
        this.id = id;
    }


    public int getId(){
        return id;
    }
    public void setId(int set_id){
        this.id = set_id;
    }

    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] set_image){
        this.image = set_image;
    }
    public String getName(){
        return name;
    }
    public void setName(String set_name){
        this.name = set_name;
    }
    public String getDefinition(){
        return definition;
    }
    public void setDefinition(String set_definition){
        this.definition = set_definition;
    }
    public String getVernacular(){
        return vernacular;
    }
    public void setVernacular(String set_vernacular){
        this.vernacular = set_vernacular;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String set_color){
        this.color = set_color;
    }
    public String getOdour(){
        return odour;
    }
    public void setOdour(String set_odour){
        this.odour = set_odour;
    }
    public String getTaste(){
        return taste;
    }
    public void setTaste(String set_taste){
        this.taste = set_taste;
    }
    public String getMorphology(){
        return morphology;
    }
    public void setMorphology(String set_morphology){
        this.morphology = set_morphology;
    }
    public String getMedicinal(){
        return medicinal;
    }
    public void setMedicinal(String set_medicinal){
        this.medicinal = set_medicinal;
    }
    public String getSafety(){
        return safety;
    }
    public void setSafety(String set_safety){
        this.safety = set_safety;
    }
    public String getStorage(){
        return storage;
    }
    public void setStorage(String set_storage){
        this.storage = set_storage;
    }
}
