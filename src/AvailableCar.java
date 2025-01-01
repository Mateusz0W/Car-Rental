import java.time.LocalDate;

public class AvailableCar extends Car{
    private LocalDate startDate;
    private LocalDate endDate;

    AvailableCar(LocalDate startDate, LocalDate endDate){
        this.startDate=startDate;
        this.endDate=endDate;
    }
    AvailableCar(String brand,String model,String category,int year,double daily_fee,int id){
        this.brand=brand;
        this.model=model;
        this.category=category;
        this.year=year;
        this.daily_fee=daily_fee;
        this.id=id;
    }
    
    @Override
    public String readColumns(){
        return "marka, model, kategoria, rok, dzienna_oplata, id";
    }
    @Override
    public String readTable(){
        return "projekt.dostepne_samochody('" + startDate + "'::DATE, '" + endDate + "'::DATE)";
    }
    @Override
    public String toString(){
        return brand+" "+model+" "+category+" "+year+" "+daily_fee+" zł";
    }

}
