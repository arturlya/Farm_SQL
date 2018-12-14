package model;

public class Resource {

    private String name;
    private int ammount;

    public Resource(String name, int ammount){
        this.name = name;
        this.ammount = ammount;
    }

    public int getAmmount() {
        return ammount;
    }

    public String getName() {
        return name;
    }

    public void addAmmount(int add){
        ammount = ammount + add;
    }
    public void removeAmmount(int add){
        if(ammount-add >= 0) {
            ammount = ammount - add;
        }else{
            System.out.println("So viel hast du von der Ressource nicht!");
        }
    }
}
