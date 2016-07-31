package trajectory;

import java.io.*;
import java.util.ArrayList;

public class Serialize {
  public static void serializeArray(Segment[] segments, String filePath) throws IOException {
    PrintWriter out = new PrintWriter(filePath, "UTF-8");
    for (Segment seg : segments) out.println(seg.toString());
    out.close();
  }

  public static Segment[] toArray(String filePath) throws IOException {
    ArrayList<Segment> segsList = new ArrayList<>();

    BufferedReader br = new BufferedReader(new FileReader(filePath));
    String line = null;
    int index = 0;
    while ((line = br.readLine()) != null) {
      String[] params = line.split(" ");
      segsList.add(new Segment(params[0], params[1], params[2]));
    }

    Segment[] segsArray = new Segment[segsList.size()];
    segsArray = segsList.toArray(segsArray);
    return segsArray;
  }
}
