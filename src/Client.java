public class Client implements Entity {
    public String name;
    public String surname;
    public String phone_number;
    public String email;

    Client(String name,String surname,String phone_number,String email){
        this.name=name;
        this.surname=surname;
        this.phone_number=phone_number;
        this.email=email;
    }
    public String insertColumns(){
        return "imie, nazwisko, numer_telefonu, email";
    }
    public String readColumns(){
        return insertColumns();
    }
    public String getTable(){
        return "projekt.klient";
    }
    public Object[] getData(){
        Object[] data={name,surname,phone_number,email};
        return data;
    }

}
