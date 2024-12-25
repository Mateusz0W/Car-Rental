public class Car implements Entity {
    private String brand;
    private String model;
    private String category;
    private int year;
    private String registration_number;
    private int mileage;
    private String status;
    private int daily_fee;

    Car(String brand,String model,String category,int year,String registration_number,int mileage,String status,int daily_fee){
        this.brand=brand;
        this.model=model;
        this.category=category;
        this.year=year;
        this.registration_number=registration_number;
        this.mileage=mileage;
        this.status=status;
        this.daily_fee=daily_fee;
    }
    public String getColumns(){
        return "marka, model, kategoria, rok, numer_rejestracyjny, przebieg, status, dzienna_oplata";
    }
    public String getTable(){
        return "projekt.samochod";
    }
    public Object[] getData(){
        Object[] data={brand,model,category,year,registration_number,mileage,status,daily_fee};
        return data;
    }

}
