
public class SchedulingMain {
    public static void main(String[] args) {
        try{
            SchedulingMethods methods=new SchedulingMethods();
            methods.listsToFile();
            methods.printLists();
         }
        catch(Exception ignored){
        }
    }
}
