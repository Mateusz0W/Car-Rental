public class Client implements Entity {
    public String name;
    public String surname;
    public String phone_number;
    public String email;
    public int id;

    Client(String name,String surname,String phone_number,String email){
        this.name=name;
        this.surname=surname;
        this.phone_number=phone_number;
        this.email=email;
    }
    Client(String name,String surname,String phone_number,String email,int id){
        this.name=name;
        this.surname=surname;
        this.phone_number=phone_number;
        this.email=email;
        this.id=id;
    }
    Client(){}
    public String insertColumns(){
        return "imie, nazwisko, numer_telefonu, email";
    }
    public String readColumns(){
        return "imie, nazwisko, numer_telefonu, email, id";
    }
    public String insertTable(){
        return "projekt.klient";
    }
    public String readTable(){
        return insertTable();
    }
    public Object[] getData(){
        Object[] data={name,surname,phone_number,email};
        return data;
    }
    public String condition(){
        return "id = " + id;
    }
    public String toString(){
        return id+" "+name+" "+surname;
    }

}
