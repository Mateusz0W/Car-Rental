import java.time.LocalDate;

public class Booking implements Entity {
    public int clinetId;
    public int carId;
    public LocalDate bookingDate;
    public LocalDate startDate;
    public LocalDate endDate;
    public String status;
    public double cost;

    public String brand;
    public String model;
    public String category;
    public String name;
    public String surname;
    public String email;


    Booking(int clinetId, int carId, LocalDate bookingDate, LocalDate startDate, LocalDate endDate, double cost){
        this.clinetId=clinetId;
        this.carId=carId;
        this.bookingDate=bookingDate;
        this.startDate=startDate;
        this.endDate=endDate;
        this.cost=cost;
    }
    Booking(String brand,String model, String category, String name, String surname, String email, LocalDate bookingDate, LocalDate startDate, LocalDate endDate, String status, double cost){
        this.brand=brand;
        this.model=model;
        this.category=category;
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.bookingDate=bookingDate;
        this.startDate=startDate;
        this.endDate=endDate;
        this.status=status;
        this.cost=cost;
    }
    Booking(){}
    public String insertColumns(){
        return "id_klienta, id_samochodu, data_rezerwacji, data_rozpoczecia, data_zakonczenia, całkowity_koszt";
    }
    public String readColumns(){
        return "marka, model, kategoria, imie, nazwisko, email, data_rezerwacji, data_rozpoczecia, data_zakonczenia, status, całkowity_koszt";
    }
    public String insertTable(){
        return "projekt.rezerwacja";
    }
    public String readTable(){
        return "projekt.rezerwacje_samochodow";
    }
    public Object []getData(){
        Object[] data={clinetId,carId,bookingDate,startDate,endDate,cost};
        return data;
    }
    public String condition(){
        return "";
    }
}
