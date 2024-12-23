public class Clinet implements Entity {
    public String name;
    public String surname;
    public String phone_number;
    public String email;

    Clinet(String name,String surname,String phone_number,String email){
        this.name=name;
        this.surname=surname;
        this.phone_number=phone_number;
        this.email=email;
    }
    public String getColumns(){
        return "imie, nazwisko, numer_telefonu, email";
    }
    public String getTable(){
        return "projekt.klient";
    }
    public Object[] getData(){
        Object[] data={name,surname,phone_number,email};
        return data;
    }
    
}
