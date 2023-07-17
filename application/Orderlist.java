package application;

public class Orderlist {
	
	//실제 테이블 뷰에 들어가는 각 칼럼들을 나타랠 수 있는 자료구조를 만들어야 함
	//각 칼럼에 해당하는 변수 선언하기
	//정수형, 날짜형, 문자형이 있지만,, 화면에 보이는건 모두 문자형 ==> 모두 string으로 선언
	
	//생성자 만들기(2가지==> 매개변수 없는거 + 매개변수 있는거)
	String idx = "";
	String date = "";
	String count1 = "";
	String count2 = "";
	String count3 = "";
	String sum = "";
	
	
	//생성자 만들기
	public Orderlist() {
		super();
	}


	public Orderlist(String idx, String date, String count1, String count2, String count3, String sum) {
		super();
		this.idx = idx;
		this.date = date;
		this.count1 = count1;
		this.count2 = count2;
		this.count3 = count3;
		this.sum = sum;
	}

	//게터, 세터 만들기
	
	public String getIdx() {
		return idx;
	}


	public void setIdx(String idx) {
		this.idx = idx;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getCount1() {
		return count1;
	}


	public void setCount1(String count1) {
		this.count1 = count1;
	}


	public String getCount2() {
		return count2;
	}


	public void setCount2(String count2) {
		this.count2 = count2;
	}


	public String getCount3() {
		return count3;
	}


	public void setCount3(String count3) {
		this.count3 = count3;
	}


	public String getSum() {
		return sum;
	}


	public void setSum(String sum) {
		this.sum = sum;
	}
	
	
	
	
}
