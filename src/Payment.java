import java.time.LocalDate;

public class Payment implements Entity{
    public int  bookingId;
    public LocalDate paymentDate;
    public double cost;
    public PaymentMethod paymentMethod;

    public String name;
    public String surname;
    
    Payment(int bookingId,LocalDate paymenDate, double cost,PaymentMethod paymentMethod){
        this.bookingId=bookingId;
        this.paymentDate=paymenDate;
        this.cost=cost;
        this.paymentMethod=paymentMethod;
    }
    Payment(String name,String surname,double cost,PaymentMethod paymentMethod,LocalDate paymentDate){
        this.name=name;
        this.surname=surname;
        this.cost=cost;
        this.paymentMethod=paymentMethod;
        this.paymentDate=paymentDate;
    }
    Payment(){}

    public String insertColumns(){
        return "id_rezerwacji, data_platnosci, kwota, metoda_platnosci";
    }
    public String readColumns(){
        return "imie, nazwisko, kwota, metoda_platnosci, data_platnosci";
    }
    public String insertTable(){
        return "projekt.platnosc";
    }
    public String readTable(){
        return "projekt.platnosci_widok";
    }
    public Object []getData(){
        Object []data={bookingId,paymentDate,cost,paymentMethod};
        return data;
    }
    public String condition(){
        return " ";
    }
}
