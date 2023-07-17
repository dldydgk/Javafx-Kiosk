package application;

public class Kiosksum {

	public int ksum(int[] countm) {
		
		int sum2=0;
		int price[] = {1000,2000,3000};
			
		for(int i=0 ; i<3 ; i++) {
			sum2 = sum2 + countm[i] * price[i];
		}
		
		return sum2;
		
	}

}
