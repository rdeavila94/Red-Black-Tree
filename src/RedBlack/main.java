package RedBlack; 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class main {

	private static Scanner sc;
	private static Scanner input = new Scanner (System.in);
	private static StringTokenizer st;
	public static void main(String[] args) throws FileNotFoundException{
		// TODO Auto-generated method stub
		File file = new File("players_homeruns.csv");
		sc = new Scanner (file);
		RedBlackTreeMap <String, Integer> t = new RedBlackTreeMap<String, Integer>();
		String player;
		int runs;
		while (sc.hasNext()) {
			st = new StringTokenizer (sc.nextLine(), ",");
			player = st.nextToken();
			runs = Integer.parseInt(st.nextToken());
			t.insert(player, runs);
		}
		sc.close();
		while (true) {
			System.out.println("Enter a player name");
			player = input.nextLine();
			if (player.equalsIgnoreCase("exit"))
				break;
			if (player.equalsIgnoreCase("all")) {
				List <String> a = t.keySet();
				for (int i = 0; i < a.size(); i++)
					System.out.println(a.get(i));
				continue;
			}
			if (t.containsKey(player))
				System.out.println(t.find(player));
		}
		input.close();
	}
	
}