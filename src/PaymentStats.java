public class PaymentStats extends Payment {
    /*
    Double income
    public String month
    */

    PaymentStats(){}

    @Override
    public String readColumns(){
        return "miesiac, calkowite_przychody";
    }
    @Override
    public String readTable(){
        return "projekt.Przychody_Miesieczne";
    }
}
