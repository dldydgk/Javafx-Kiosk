# Javafx-Kiosk
DB연결, javafx를 활용하여 키오스크 만들기
## 화면 구현
``` java
Parent root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("혼자 만드는 키오스크");
			primaryStage.show();
``` 
## 사용할 변수와 액션들
``` java
 @FXML private Button CalButton, CancleButton;
    @FXML private Button M1pButton, M2pButton, M3pButton;
    @FXML private Button M1mButton, M2mButton, M3mButton;
    @FXML private TextArea ListTextArea;
    @FXML private Label sumLabel;
    @FXML private Button AdminButton, OrderButton;
    
    private int sum=0;
    private String[] menu_name = {"아메리카노", "카푸치노", "카페라떼"};  
    private int countm[] = new int[3];
    
    Kiosksum kiosksum = new Kiosksum();
```
### 메소드를 생성하여 + 버튼을 눌렀을 시 값이 하나씩 증가
``` java
 @FXML
    public void M1pButtonAction(ActionEvent event) {
    	countm[0]=countm[0]+1;
    	menu_append();
    }
	private void menu_append() {		
		ListTextArea.setText("");
		for(int i=0; i<3;i++) {
			ListTextArea.appendText(menu_name[i] + " : " + countm[i] + "잔"+"\n");
		}	
	}
	@FXML
	public void M2pButtonAction(ActionEvent event) {
    	countm[1]++;    	
    	menu_append();
    }
    @FXML
    public void M3pButtonAction(ActionEvent event) {
    	countm[2]++;    	
    	menu_append();
    }

```
![image](https://github.com/dldydgk/Javafx-Kiosk/assets/126844590/6f0f0013-d367-4217-87f2-dce8c4c64c9b)
### - 버튼을 눌렀을 시 값이 하나씩 감소
``` java
 @FXML
    public void M1mButtonAction(ActionEvent event) {
    	if(countm[0]>0) countm[0]--;
    	menu_append();
    }

    @FXML
    public void M2mButtonAction(ActionEvent event) {
    	if(countm[1]>0) countm[1]--;
    	menu_append();
    }
    
    @FXML
    public  void M3mButtonAction(ActionEvent event) {
    	if(countm[2]>0) countm[2]--;
    	menu_append();
    }
```
![image](https://github.com/dldydgk/Javafx-Kiosk/assets/126844590/38b60737-d041-4f0d-a620-8cf5541c45b9)

### 취소 버튼을 눌렀을 시 변수들에 있는 값 초기화
``` java
 @FXML
    public void CancleButtonAction(ActionEvent event) {
    	sumLabel.setText("0");
    	ListTextArea.setText("");
    	for(int i=0;i<3; i++) {
    		countm[i]=0;
    	}	
    }
```
![image](https://github.com/dldydgk/Javafx-Kiosk/assets/126844590/4da50e6e-e958-4e25-b18f-cdfb3e64d3c7)

### 계산 버튼을 눌렀을 시 값 계산
``` java
    @FXML
    public void SumButtonAction(ActionEvent event) {
    	sum = kiosksum.ksum(countm);
    	sumLabel.setText(sum + "");
    }
```
![image](https://github.com/dldydgk/Javafx-Kiosk/assets/126844590/f6d80435-481b-4e06-bcbe-903d4c07486a)

# 관리자 로그인 화면 구현
#### 이제부터는 관리자 로그인 버튼을 눌렀을 시 DB에 접속한다
##### 먼저 관리자 로그인에 성공했을 시 화면에 나타나는 관리자 페이지를 가져오기 위한 코드이다
``` java
 @FXML
    public void AdminButtonAction(ActionEvent event) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("adminlogin.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("관리자 로그인 화면-5월");
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    
    }
```
## 주문하기 버튼을 눌렀을 시 DB에 연결하고 주문내역을 저장한다
#### 그러기 위해서 먼저 DB연결을 위한 클래스를 생성한다
``` java
package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnect3 {
	
	public Connection conn;
	
	public Connection getconn() {
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "cafe3";
		String password = "1234";
		
		try {
			Class.forName(driver);
			System.out.println("디비 접속 성공-20230516");

			conn = DriverManager.getConnection(url, id, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("디비 접속 실패");
		}
		return conn;
	}
}
```
## 합계가 0이 아니라면 DB접속하는 클래스를 호출한 후 SQL문 실행
``` java
if(sum != 0) {
	  DBconnect3 conn = new DBconnect3();
 		Connection conn2 = conn.getconn();
    		
		String sql = " insert into orderlist_accounts"
	  + " (idx, order_time, menu1, count1, menu2, count2, menu3, count3, sum)"
		+ " values(orderlist_idx_pk.nextval, current_timestamp, '아메리카노', ?, '카푸치노', ?, '카페라떼', ?, ?)";
  
	  PreparedStatement ps = conn2.prepareStatement(sql);
```
### SQL문에 있는 ? 자리에는 각 메뉴와 합계를 rs에 저장해 쿼리문을 실행한 후 나온 결과값을 저장한다
``` java
ps.setInt(1, countm[0]);
				ps.setInt(2, countm[1]);
				ps.setInt(3, countm[2]);
				ps.setInt(4, sum);
				
				ResultSet rs = ps.executeQuery();
```
### 만약 rs에 값이 남아있다면 sql문이 실행된 것이므로 계산성공의 알림창을 띄어준 후 변수안에 들어있는 값들을 초기화한다
``` java
if(rs.next()) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("주문이 완료되었습니다.");
					alert.show();
					
					ListTextArea.setText("");
					sumLabel.setText("");
					
					countm[0]=0;
					countm[1]=0;
					countm[2]=0;
					sum=0;
				}else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("다시 확인해보세요");
					alert.show();
				}
```
### 관리자 로그인 화면 창
![image](https://github.com/dldydgk/Javafx-Kiosk/assets/126844590/9c88101a-15a1-4149-b391-d0dfe365fba1)

### 관리자 페이지를 닫거나 아이디 비번 자리에 들어있는 모든 값을 초기화한다
``` java
	@FXML
	private void ClearButttonAction(ActionEvent event) {
		IdTextField.setText("");
		PwPasswordField.setText("");
	}
	@FXML
	private void CloseButtonAction(ActionEvent event) {
		Stage stage = (Stage)CloseButton.getScene().getWindow();
				stage.close();
	}
```
### 관리자 로그인 성공 시 화면에 나오는 창
![image](https://github.com/dldydgk/Javafx-Kiosk/assets/126844590/9d89d0f6-06be-4ad9-9725-ccedb3fb5f87)

### Orderlist라는 클래스 선언 후 Getter/Setter 생성<br>
바인딩 : 두 요소를 서로 연결하여 값이 동기화되도록 하는 개념이다. JavaFX에서 속성 바인딩은 한 객체의 속성이 다른 객체의 속성에 종속되어 변경되면 자동으로 동기화되는 매커니즘이다.<br>

이 코드는 주문리스트 창을 초기화하고, 테이블 열과 모델 클래스의 속성을 바인딩하여 데이터를 표시하는 작업을 수행한다

``` java
@FXML TableColumn<Orderlist, String>idxTableColumn;
	@FXML TableColumn<Orderlist, String>dateTableColumn;
	@FXML TableColumn<Orderlist, String>count1TableColumn;
	@FXML TableColumn<Orderlist, String>count2TableColumn;
	@FXML TableColumn<Orderlist, String>count3TableColumn;
	@FXML TableColumn<Orderlist, String>sumTableColumn;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		System.out.println("주문리스트 창이 열리고 초기화를 하려고 함");
		
		idxTableColumn.set CellValueFactory(new PropertyValueFactory<>("idx"));
		dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		count1TableColumn.setCellValueFactory(new PropertyValueFactory<>("count1"));
		count2TableColumn.setCellValueFactory(new PropertyValueFactory<>("count2"));
		count3TableColumn.setCellValueFactory(new PropertyValueFactory<>("count3"));
		sumTableColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
		
	}
```
### 전체 조회 버튼 클릭시 변수 초기화하고 db접속 후 쿼리 실행
#### to_char 를 사용하여 정확한 시간과 날짜를 몇일, 몇분, 몇초로 하였다
``` java
@FXML
	private void searchButtonAction(ActionEvent event) {
		//변수 초기화
		resultTextArea.setText("");
		mcount1=0;
		mcount2=0;
		mcount3=0;
		msum=0;
		//디비 접속
		//sql을 이용해서 데이터 검색하기
		DBconnect3 conn = new DBconnect3();
		Connection conn3 = conn.getconn();

		//주문리스트 테이블에 있는 자료 검색하기
		String sql = "select idx, to_char(order_time, 'yyyy-mm-dd hh24:mi:ss'), count1, count2, count3, sum "
				+ " from ORDERLIST_ACCOUNTS"
				+ " order by idx";
		
			try {
				PreparedStatement ps = conn3.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
```
### 주문리스트 데이터를 가져와서 테이블에 표시하고, 총 주문 수량과 판매 금액을 계산하여 텍스트 영역에 표시한다
``` java
ObservableList<Orderlist>datelist = FXCollections.observableArrayList();
				while(rs.next()) {
					datelist.add(
							new Orderlist(
									rs.getString(1),
									rs.getString(2), 
									rs.getString(3),
									rs.getString(4),
									rs.getString(5),
									rs.getString(6)
									)
					);
					mcount1=mcount1 + Integer.parseInt(rs.getString(3));
					mcount2=mcount2 +Integer.parseInt(rs.getString(4));
					mcount3=mcount3 +Integer.parseInt(rs.getString(5));
					msum=msum +Integer.parseInt(rs.getString(6));
				}
				
				//while문을 사용하여 주문한  칼럼의 수를 더하여 반복한다
				orderlistTableView.setItems(datelist);
				
				resultTextArea.appendText("아메키라노 : " + mcount1 + "잔\n");
				resultTextArea.appendText("카푸치노 : " + mcount2 + "잔\n");
				resultTextArea.appendText("카페라떼 : " + mcount3 + "잔\n");
				resultTextArea.appendText("총 판매금액 : " + msum + "원\n");
					
				ps.close();
				rs.close();
				conn3.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
```
### 시작 날짜 또는 종료 날짜가 비어 있으면 경고메세지를 띄운다
``` java
@FXML
	private void datesearch2Button(ActionEvent event) {
		//변수랑 textarea 초기화
		if(startDatePicker.getValue() == null||endDatePicker.getValue()==null) {
			//만약 시작날짜 또는 종료날짜가 비어 있으면 ==> 경고메세지
			//그 외에는 
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("경고메세지");
			alert.setContentText("시작날짜와 종료날짜를 모두 선택하세요");
			alert.show();
```
### 그 외에는 DB에 접속하고 쿼리문을 실행한다 
``` java
	DBconnect3 conn3 = new DBconnect3();
			Connection conn = conn3.getconn();
			
			String sql = "select idx, order_time, count1, count2, count3, sum "
					+ " from ORDERLIST_ACCOUNTS"
					+ " where order_time >= ? and order_time <=?"
					//+ " where order_time  between ? and ?"
					+ " order by idx";
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, startDatePicker.getValue().toString());
				ps.setString(2, endDatePicker.getValue().plusDays(1).toString());
				
				ResultSet rs = ps.executeQuery();
```

### 주문리스트 데이터를 처리하여 ObservableList에 추가하고, 각 주문 항목의 수량을 계산하고, 텍스트 영역에 결과를 표시한다. 마지막으로, 데이터베이스 관련 객체들을 닫는다
``` java
ObservableList<Orderlist> datelist = FXCollections.observableArrayList();
				resultTextArea.setText("");
				mcount1=0;
				mcount2=0;
				mcount3=0;
				msum=0;
				while(rs.next()) {
					datelist.add(
							new Orderlist(
									rs.getString(1),
									rs.getString(2), 
									rs.getString(3),
									rs.getString(4),
									rs.getString(5),
									rs.getString(6)			
									)
					);
					mcount1=mcount1 + Integer.parseInt(rs.getString(3));
					mcount2=mcount2 +Integer.parseInt(rs.getString(4));
					mcount3=mcount3 +Integer.parseInt(rs.getString(5));
					msum=msum +Integer.parseInt(rs.getString(6));
				}
				
				orderlistTableView.setItems(datelist);
				resultTextArea.appendText("아메키라노 : " + mcount1 + "잔\n");
				resultTextArea.appendText("카푸치노 : " + mcount2 + "잔\n");
				resultTextArea.appendText("카페라떼 : " + mcount3 + "잔\n");
				resultTextArea.appendText("총 판매금액 : " + msum + "원\n");

				ps.close();
				rs.close();
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
```
### 판매수량 그래프 버튼을 눌렀을 시  
``` java
@FXML
	private void countButtonAction(ActionEvent evnet) {
		rsPieChart.setTitle("메뉴별 판매수량 그래프");
		rsPieChart.setData(FXCollections.observableArrayList(
				new PieChart.Data("아메리카노 " + mcount1, mcount1),
				new PieChart.Data("카푸치노" + mcount2, mcount2),
				new PieChart.Data("카페라떼" + mcount3, mcount3)
				));
```
![image](https://github.com/dldydgk/Javafx-Kiosk/assets/126844590/35f36324-f459-499a-9242-d6593b7aebc0)

### 판매 금액 그래프 버튼을 눌렀을 시 
``` java
@FXML
	private void sumButtonAction(ActionEvent evnet) {
		rsPieChart.setTitle("메뉴별 판매금액 그래프");
		rsPieChart.setData(FXCollections.observableArrayList(
				new PieChart.Data("아메리카노" + mcount1, mcount1*1000),
				new PieChart.Data("카푸치노" + mcount2, mcount2*2000),
				new PieChart.Data("카페라떼" + mcount3, mcount3*3000)
				));
	}
```
![image](https://github.com/dldydgk/Javafx-Kiosk/assets/126844590/e5153eca-00a4-4527-8060-10f7f4258ae8)

	


 

