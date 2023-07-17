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
## 사용할 변수들과 액션을 위한 버튼들
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
바인딩 : 두 요소를 서로 연결하여 값이 동기화되도록 하는 개념입니다. JavaFX에서 속성 바인딩은 한 객체의 속성이 다른 객체의 속성에 종속되어 변경되면 자동으로 동기화되는 매커니즘입니다.<br>

이 코드는 주문리스트 창을 초기화하고, 테이블 열과 모델 클래스의 속성을 바인딩하여 데이터를 표시하는 작업을 수행합니다.

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

--- 

