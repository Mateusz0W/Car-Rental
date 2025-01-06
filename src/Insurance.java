import java.time.LocalDate;



public class Insurance implements Entity {
    public int id;
    public int car_id;
    public InsuranceType type;
    public LocalDate start_date;
    public LocalDate end_date;
    public double cost;
    public String company;

    public String brand;
    public String model;
    public String registration_number;

    Insurance(int car_id, InsuranceType type, LocalDate starDate , LocalDate endDate ,double cost,String company){
        this.car_id=car_id;
        this.type=type;
        this.start_date=starDate;
        this.end_date=endDate;
        this.cost=cost;
        this.company=company;
    }
    Insurance(String brand,String model,String registration_number,LocalDate starDate,LocalDate endDate,InsuranceType type,double cost,String company,int id){
        this.brand=brand;
        this.model=model;
        this.registration_number=registration_number;
        this.type=type;
        this.start_date=starDate;
        this.end_date=endDate;
        this.cost=cost;
        this.company=company;
        this.id=id;
    }
    Insurance(){}
    
    public String insertColumns(){
        return "id_samochodu, rodzaj, data_rozpoczecia, data_zakonczenia, koszt, nazwa_ubezpieczyciela";
    }
    public String readColumns(){
        return "Marka, Model, numer_rejestracyjny, data_rozpoczecia, data_zakonczenia, rodzaj, koszt, nazwa_ubezpieczyciela, id";
    }
    public String insertTable(){
        return "projekt.ubezpieczenia";
    }
    public String readTable(){
        return "projekt.ubezpieczone_samochody";
    }
    public Object []getData(){
        Object []data={car_id,type,start_date,end_date,cost,company};
        return data;
    }
    public String condition(){
        return "id = "+id;
    }
    public String toString(){
        return company+" "+brand+" "+model;
    }
}
