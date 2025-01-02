public class ServiceStats extends Car {
    public double sumCost;
    public double maxCost;
    public String maxCostDescription;

    ServiceStats(String brand,String model,int year,int mileage,double sumCost,double maxCost,String maxCostDescription){
        this.brand=brand;
        this.model=model;
        this.year=year;
        this.mileage=mileage;
        this.sumCost=sumCost;
        this.maxCost=maxCost;
        this.maxCostDescription=maxCostDescription;
    }
    ServiceStats(){}
    @Override
    public String readTable(){
        return "projekt.koszty_serwisowe";
    }
    @Override
    public String readColumns(){
        return "model, marka, rok, przebieg, suma_kosztow_serwisowych, maksymalny_koszt_serwisowy, opis_max_koszt";
    }
}
