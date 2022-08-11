import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SchedulingMethods{
    List<String> lines;
    TimeComparator comparator;
    Function<List<String>, Integer> sum;
    Function<String,Integer> timer= i->i.split(" ")[i.split(" ").length - 1]
            .equals("lightning") ? 5:
            Integer.parseInt(i.split(" ")[i.split(" ").length - 1].substring(0, 2));
    HashMap<String, Integer> eventsAndTimes;
    List<List<String>> times;
    List<String> part1;
    List<String> part2;
    List<String> part3;
    List<String> part4;
    boolean fileCreated;
    File output;
    //the key represents the corresponding index of lines while the second serves as a boolean for
    // if the key-th element in lines was viewed in the function that makes tracks.
    //
    public SchedulingMethods() throws IOException {
        sum = i ->i.isEmpty()?0:i.stream().map(j->timer.apply(j)).reduce(0, Integer::sum);
        this.lines = Files.readAllLines(Paths.get("src/main/resources/input.text"), StandardCharsets.UTF_8);
        eventsAndTimes = new HashMap<>();
        lines.forEach(i->eventsAndTimes
                .put(i,timer.apply(i))
                );
        comparator=new TimeComparator();
        part1=new ArrayList<>();
        part2=new ArrayList<>();
        part3=new ArrayList<>();
        part4=new ArrayList<>();
        times=times();
        output=new File("src/main/resources/output.text");
        fileCreated= output.createNewFile();
    }

    public List<List<String>> times() {
        //sorting lines by the times of the events in reverse order
        List<String> times= lines.stream()
                .sorted(comparator)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        int[] uniques=distinctTimes();
        //then storing all the lines in sublists of events with the same duration
       List<List<String>> splitByTimes=new ArrayList<>();
       Arrays.stream(uniques)
                .forEach(i->splitByTimes
                        .add(times.stream().filter(j->timer.apply(j)==i).collect(Collectors.toList())));
                return splitByTimes;
    }
    public int[] distinctTimes() {
        return lines.stream()
                .map(i -> i.split(" ")[i.split(" ").length - 1])
                .map(i -> i.equals("lightning") ? 5: Integer.parseInt(i.substring(0, 2)))
                .sorted(Comparator.reverseOrder())
                .mapToInt(i->i)
                .distinct()
                .toArray();
    }
    public void listsToFile(){
       int[] indeces= fillList(part1,0,0,0,0,180);
        indeces= fillList(part2,indeces[0],indeces[1],indeces[2],indeces[3],240);
        indeces= fillList(part3,indeces[0],indeces[1],indeces[2],indeces[3],180);
        indeces= fillList(part4,indeces[0],indeces[1],indeces[2],indeces[3],180);
        part4.add(times.get(times.size()-1).get(0));
        //just used to make sure everything except the lightning panel is in a track
        System.out.println(Arrays.toString(indeces));

    }
    //fills a list with events based on the desired total duration in an exact change type of way
    public int[] fillList(List<String> list,int sixties
            ,int fortyfives, int thirties,int fives,int duration){

        while (sixties < times.get(0).size() && sum.apply(list) <= duration-60) {
                list.add(times.get(0).get(sixties));
                sixties++;
            }
        while (fortyfives < times.get(1).size() && sum.apply(list) <= duration-90){
                list.add(times.get(1).get(fortyfives));
                list.add(times.get(1).get(fortyfives+1));
                fortyfives+=2;
            }
        while (thirties < times.get(2).size() && sum.apply(list) <= duration-30){
                list.add(times.get(2).get(thirties));
                thirties++;
            }
            return new int[]{sixties,fortyfives,thirties,fives};
    }
    //adds the start times to each line before adding it to the output file and printing it in the console.
    public void printLists() {
        try {
            FileWriter writer=new FileWriter(output);
            LocalTime localTime = LocalTime.of(9, 0);
            System.out.println("Track 1:");
            writer.write("Track 1:\n");
            for (String talk : part1) {
                System.out.println(localTime + " " + talk);
                writer.write(localTime + " " + talk+"\n");
                localTime = localTime.plusMinutes(timer.apply(talk));
            }
            System.out.println(localTime + " Lunch");
            writer.write(localTime + " Lunch\n");
            localTime = localTime.plusHours(1);
            for (String talk : part2) {
                System.out.println(localTime + " " + talk);
                writer.write(localTime + " " + talk+"\n");
                localTime = localTime.plusMinutes(timer.apply(talk));
            }
            System.out.println(localTime + " Networking Event");
            writer.write(localTime + " Networking Event\n");
            localTime = LocalTime.of(9, 0);
            System.out.println();
            System.out.println("Track 2:");
            writer.write("Track 2:\n");
            for (String talk : part3) {
                System.out.println(localTime + " " + talk);
                writer.write(localTime + " " + talk+"\n");
                localTime = localTime.plusMinutes(timer.apply(talk));
            }
            System.out.println(localTime + " Lunch");
            writer.write(localTime + " Lunch\n");
            localTime = localTime.plusHours(1);
            for (String talk : part4) {
                System.out.println(localTime + " " + talk);
                writer.write(localTime + " " + talk+"\n");
                localTime = localTime.plusMinutes(timer.apply(talk));
            }
            localTime = LocalTime.of(17, 0);
            System.out.println(localTime + " Networking Event\n");
            writer.write(localTime + " Networking Event\n");
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}