package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdmindbController implements Initializable{
	
	int  mcount1=0;
	int  mcount2=0;
	int  mcount3=0;
	int  msum=0;
	
	@FXML private Button searchButton, datesearchButton, datesearch2Button, countButton, sumButton;
	@FXML private DatePicker dateDatePicker, startDatePicker, endDatePicker;
	@FXML private TextArea resultTextArea;
	@FXML private TableView<Orderlist> orderlistTableView;
	@FXML private PieChart rsPieChart;
	
	//TableView<s> s는각칼럼과 데이터형식이 일치하는 자료구조를 가진 클래스 파일명
	@FXML TableColumn<Orderlist, String>idxTableColumn;
	@FXML TableColumn<Orderlist, String>dateTableColumn;
	@FXML TableColumn<Orderlist, String>count1TableColumn;
	@FXML TableColumn<Orderlist, String>count2TableColumn;
	@FXML TableColumn<Orderlist, String>count3TableColumn;
	@FXML TableColumn<Orderlist, String>sumTableColumn;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		System.out.println("주문리스트 창이 열리고 초기화를 하려고 함");
		
		idxTableColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
		dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		count1TableColumn.setCellValueFactory(new PropertyValueFactory<>("count1"));
		count2TableColumn.setCellValueFactory(new PropertyValueFactory<>("count2"));
		count3TableColumn.setCellValueFactory(new PropertyValueFactory<>("count3"));
		sumTableColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
		
	}
	
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
				//쿼리 실행
				
				ObservableList<Orderlist>datelist = FXCollections.observableArrayList();
				//
				
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
	}
	@FXML
	private void datesearchButton(ActionEvent event) {
		
		//만약에 날짜가 비어있으면 ==> 경고메세지
		//그 외에는
			//디비 접속 ==> 해당 날짜의 데이터를 검색
		if(dateDatePicker.getValue()==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("알림 메세지");
			alert.setContentText("날짜를 먼저 선택하고 조회하세요");
			alert.show();
		}else {
			DBconnect3 conn= new DBconnect3();
			Connection conn3 = conn.getconn();
		//데이트피커에 있는 날짜에 해당하는 데이터를 검색
		String sql = "select  idx, order_time, count1, count2, count3, sum"
				+ " from ORDERLIST_ACCOUNTS"
				+ " where to_char(order_time, 'yyyy-mm-dd') = ?";
		
	try {
		PreparedStatement ps = conn3.prepareStatement(sql);
		
		ps.setString(1, (dateDatePicker.getValue()).toString());
		ResultSet rs = ps.executeQuery();
		
		ObservableList<Orderlist>datelist = FXCollections.observableArrayList();
		//TableColumn<s, t>
		//t는 s파일에서 선언한 변수의 데이터형
		
		//변수초기화 
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
		
	} catch (SQLException e) {
		e.printStackTrace();
	}	
		}
	}
	@FXML
	private void datesearch2Button(ActionEvent event) {
		
		//변수랑 textarea 초기화
		if(startDatePicker.getValue() == null||endDatePicker.getValue()==null) {
			//만약 시작날짜 또는 종료날짜가 비어 있으면 ==> 경고메세지
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("경고메세지");
			alert.setContentText("시작날짜와 종료날짜를 모두 선택하세요");
			alert.show();
		}else {
			//그 외에는 
			
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
			
			
		}
			//DB접속 ==> 해당 기간에 일치하는 데이터 검색 sql 작성
			//쿼리 세팅, 실행 ==> Resultset에 저장
			//어레이리스트 생성
		
			//while문으로 어레이리스트에 값을 추가(add)
			//변수값도 계산
		
			//테이블뷰에 표시하기 (setItems)
			//통계도 표시
		
		
		
	}
	@FXML
	private void countButtonAction(ActionEvent evnet) {
		rsPieChart.setTitle("메뉴별 판매수량 그래프");
		rsPieChart.setData(FXCollections.observableArrayList(
				new PieChart.Data("아메리카노 " + mcount1, mcount1),
				new PieChart.Data("카푸치노" + mcount2, mcount2),
				new PieChart.Data("카페라떼" + mcount3, mcount3)
				));
		
	}
	@FXML
	private void sumButtonAction(ActionEvent evnet) {
		rsPieChart.setTitle("메뉴별 판매금액 그래프");
		rsPieChart.setData(FXCollections.observableArrayList(
				new PieChart.Data("아메리카노" + mcount1, mcount1*1000),
				new PieChart.Data("카푸치노" + mcount2, mcount2*2000),
				new PieChart.Data("카페라떼" + mcount3, mcount3*3000)
				));
		
	}
	
}

