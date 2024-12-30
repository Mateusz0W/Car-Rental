public class Service implements Entity{
    public int car_id;
    public String brand;
    public String model;
    public int year;
    public String description;
    public double cost;

    Service(int car_id,String description,double cost){
        this.car_id=car_id;
        this.description=description;
        this.cost=cost;
    }
    Service(String brand,String model,int year,String description,double cost,int car_id){
        this.brand=brand;
        this.model=model;
        this.year=year;
        this.description=description;
        this.cost=cost;
        this.car_id=car_id;
    }
    Service(){}
    public String insertColumns(){
        return "id_samochodu, opis, koszt";
    }
    public String readColumns(){
        return "marka, model, rok, opis, koszt, id_samochodu";
    }
    public String insertTable(){
        return "projekt.serwis";
    }
    public String readTable(){
        return "projekt.samochody_w_serwisie";
    }
    public Object[] getData(){
        Object[] data={car_id,description,cost};
        return data;
    }
    public String condition(){
        return "id_samochodu = " + car_id;
    }
    public String toString(){
        return brand+" "+model+" "+year+" "+description;
    }
}
