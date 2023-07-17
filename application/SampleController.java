package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class SampleController {

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
    
    
    @FXML
    public void CancleButtonAction(ActionEvent event) {
    	sumLabel.setText("0");
    	ListTextArea.setText("");
    	for(int i=0;i<3; i++) {
    		countm[i]=0;
    	}	
    }


    @FXML
    public void SumButtonAction(ActionEvent event) {
    	sum = kiosksum.ksum(countm);
    	sumLabel.setText(sum + "");
    }
    
    
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
    
    
    @FXML
    public void OrderButtonAction(ActionEvent event) {
    	if(sum != 0) {
    		DBconnect3 conn = new DBconnect3();
    		Connection conn2 = conn.getconn();
    		
    		String sql = " insert into orderlist_accounts"
    				+ " (idx, order_time, menu1, count1, menu2, count2, menu3, count3, sum)"
    				+ " values(orderlist_idx_pk.nextval, current_timestamp, '아메리카노', ?, '카푸치노', ?, '카페라떼', ?, ?)";
    		
    		try {
				PreparedStatement ps = conn2.prepareStatement(sql);
				ps.setInt(1, countm[0]);
				ps.setInt(2, countm[1]);
				ps.setInt(3, countm[2]);
				ps.setInt(4, sum);
				
				ResultSet rs = ps.executeQuery();
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
				
				
				
				//rs값이 있으면 
					//주문 성공 메시지 띄우기
					//화면에 보이는 내용 초기화, 실제 변수값도 초기화
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		
    		
    		//만약 sum이 0이면 - 경고 메시지 띄우기
    	//sum이 0이 아니면 - 
    		//디비 접속
    		//sql문을 작성해서 실행(레코드 삽입)

    	}else {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setContentText("메뉴 선택하고, 계산하기 버튼 클릭 후 주문하세요");
    		alert.show();
    		
    		//경고메시지
    	}
    	
    	
    	
    
    
    }
    
    
}
