package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminloginController {

	@FXML Button LoginButtion, ClearButton, CloseButton;
	@FXML TextField IdTextField;
	@FXML PasswordField PwPasswordField;
	
	@FXML
	private void LoginButtonAction(ActionEvent event) {
		if(IdTextField.getText().isEmpty()||PwPasswordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("알림메시지");
			alert.setHeaderText("경고");
			alert.setContentText("아이디 비번 확인 바람");
			alert.show();
			
		}else {
			DBconnect3 conn = new DBconnect3();
			Connection conn3 = conn.getconn();
			String sql = "select adminid, adminpw"
				+ " from admin_accounts"
				+ " where adminid = ?"
				+ " and adminpw = ?";
		
			try {
				PreparedStatement ps = conn3.prepareStatement(sql);
				ps.setString(1, IdTextField.getText());
				ps.setString(2, PwPasswordField.getText());
				
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
						System.out.println("로그인 성공");
						
						CloseButtonAction(event);
					
						Parent root = FXMLLoader.load(getClass().getResource("Admindb.fxml"));
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.show();
						//로그인 창 닫기
//						Stage stage = (Stage)CloseButton.getScene().getWindow();
//						stage.close();
						//관리자 페이지 띄우기
						
//					Alert alert = new Alert(AlertType.INFORMATION);
//					alert.setTitle("알림창");
//					alert.setHeaderText("성공");
//					alert.setContentText("로그인 성공");
//					alert.show();
				}else {
//					Alert alert = new Alert(AlertType.INFORMATION);
//					alert.setTitle("알림창");
//					alert.setHeaderText("실패");
//					alert.setContentText("로그인 실패");
//					alert.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
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
}