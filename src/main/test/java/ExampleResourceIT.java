
import org.junit.jupiter.api.Test;

public class ExampleResourceIT{

    @Test
    public void timesTest(){
        try{
            SchedulingMethods methods=new SchedulingMethods();
           System.out.println(methods.times());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void listsTest() {
        try {
            SchedulingMethods methods = new SchedulingMethods();
            methods.listsToFile();
            methods.printLists();
        } catch (Exception ignored) {
        }
    }
}
