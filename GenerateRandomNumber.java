import java.util.Random;
public class GenerateRandomNumber {
	private static  Random rnd = null;
	public static int getRandomNumber(int min, int max) {
		if(rnd == null) {
			rnd = new Random();
		}
		return rnd.nextInt(max - min + 1) + min; 
	}
	public static int generateNewNumber() {
		int percent = getRandomNumber(1, 100);
		if(percent >= 1 && percent <= 75) {
			return getRandomNumber(1, 3);
		}
		else if(percent >= 76 && percent <= 95) {
			return getRandomNumber(4, 6);
		}
		else {
			return getRandomNumber(7, 9);
		}
	}
}
