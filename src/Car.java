public class Car implements Entity {
    public int id;
    public String brand;
    public String model;
    public String category;
    public int year;
    public String registration_number;
    public int mileage;
    public String status;
    public double daily_fee;

    Car(String brand,String model,String category,int year,String registration_number,int mileage,double daily_fee){
        this.brand=brand;
        this.model=model;
        this.category=category;
        this.year=year;
        this.registration_number=registration_number;
        this.mileage=mileage;
        this.daily_fee=daily_fee;
    }
    Car(String brand,String model,String category,int year,String registration_number,int mileage,String status,double daily_fee,int id){
        this.brand=brand;
        this.model=model;
        this.category=category;
        this.year=year;
        this.registration_number=registration_number;
        this.mileage=mileage;
        this.status=status;
        this.daily_fee=daily_fee;
        this.id=id;
    }
    Car(){}
    public String insertColumns(){
        return "marka, model, kategoria, rok, numer_rejestracyjny, przebieg, dzienna_oplata";
    }
    public String readColumns(){
        return "marka, model, kategoria, rok, numer_rejestracyjny, przebieg, status, dzienna_oplata, id";
    }
    public String insertTable(){
        return "projekt.samochod";
    }
    public String readTable(){
        return insertTable();
    }
    public Object[] getData(){
        Object[] data={brand,model,category,year,registration_number,mileage,daily_fee};
        return data;
    }
    public String toString(){
        return brand+" "+model+" "+year;
    }
    public String condition(){
        return "id = "+id;
    }

}
