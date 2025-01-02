public class AvgOpinion extends Opinion{
    public double avgGrade;
    public int numOfOpinions;

    AvgOpinion(String brand,String model,String category,double avgGrade,int numOfOpinions){
        this.brand=brand;
        this.model=model;
        this.category=category;
        this.avgGrade=avgGrade;
        this.numOfOpinions=numOfOpinions;
    }
    AvgOpinion(){}
    @Override
    public String readTable(){
        return "projekt.srednie_opinie";
    }
    @Override
    public String readColumns(){
        return "marka, model, kategoria, sr, l";
    }

}
