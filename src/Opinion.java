import java.time.LocalDate;

public class Opinion implements Entity {
    public int clientId;
    public int carId;
    public String opinion;
    public int grade;
    public LocalDate date;

    public String brand;
    public String model;
    public String category;
    public String name;
    public String surname;

    Opinion(int clientId,int carId,String opinion,int grade,LocalDate date){
        this.clientId=clientId;
        this.carId=carId;
        this.opinion=opinion;
        this.grade=grade;
        this.date=date;
    }
    Opinion(String brand,String model,String category,String name,String surname,int grade,String opinion,LocalDate date){
        this.brand=brand;
        this.model=model;
        this.category=category;
        this.name=name;
        this.surname=surname;
        this.grade=grade;
        this.opinion=opinion;
        this.date=date;
    }
    Opinion(){}

    public String insertColumns(){
        return "id_klienta, id_samochodu, opinia, ocena, data_opinii";
    }
    public String readColumns(){
        return "marka, model, kategoria, imie, nazwisko, ocena, opinia, data_opinii";
    }
    public String insertTable(){
        return "projekt.opinie";
    }
    public String readTable(){
        return "projekt.opinie_samochodow";
    }
    public Object []getData(){
        Object []data={clientId,carId,opinion,grade,date};
        return data;
    }
    public String condition(){
        return "";
    }
}
